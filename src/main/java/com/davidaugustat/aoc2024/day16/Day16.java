package com.davidaugustat.aoc2024.day16;

import com.davidaugustat.aoc2024.utils.Helper;
import com.davidaugustat.aoc2024.utils.Point;
import com.davidaugustat.aoc2024.utils.TextFileReader;

import java.util.*;

public class Day16 {

    private enum Direction {N, S, W, E}
    private record Node(Point pos, Direction dir) { }
    private record NeighborWithEdgeWeight(Node node, long edgeWeight) { }

    public Day16() {
        char[][] grid = TextFileReader.readColumnsFirstCharGridFromFile("day16/input.txt");
        Point start = Helper.findInCharGrid(grid, 'S');
        Point end = Helper.findInCharGrid(grid, 'E');
        dijkstraFindShortestPaths(start, end, grid);
    }

    private void dijkstraFindShortestPaths(Point start, Point end, char[][] grid) {
        Node startNode = new Node(start, Direction.E);
        // Direction (N) does not matter since edges between all 4 end nodes have cost 0:
        Node endNode = new Node(end, Direction.N);

        Map<Node, Long> distances = new HashMap<>();
        distances.put(startNode, 0L);

        Map<Node, Set<Node>> predecessors = new HashMap<>();
        Set<Node> toProcess = getSetOfAllNodes(grid);

        int numEndNodesSeen = 0;
        while (!toProcess.isEmpty()) {
            Node currentNode = getNodeWithMinDistance(toProcess, distances);
            if (!distances.containsKey(currentNode)) {
                // Minimum distance node has infinite distance. We can abort here since no improvement is possible.
                break;
            }
            if (currentNode.pos().equals(end)) {
                numEndNodesSeen++;
                if (numEndNodesSeen == 4) {
                    // All directions for end nodes were visited. We can exit here.
                    break;
                }
            }

            toProcess.remove(currentNode);
            boolean isEndNode = currentNode.pos().equals(end);
            for (NeighborWithEdgeWeight neighbor : getNeighbors(currentNode, grid, isEndNode)) {
                if (!toProcess.contains(neighbor.node())) {
                    continue;
                }
                long currentDistance = distances.getOrDefault(neighbor.node(), Long.MAX_VALUE);
                long altDistance = distances.get(currentNode) + neighbor.edgeWeight();
                if (altDistance < currentDistance) {
                    distances.put(neighbor.node(), altDistance);

                    Set<Node> predecessorsOfNeighbor = new HashSet<>();
                    predecessorsOfNeighbor.add(currentNode);
                    predecessors.put(neighbor.node(), predecessorsOfNeighbor);
                }
                if (altDistance == currentDistance) {
                    predecessors.get(neighbor.node()).add(currentNode);
                }
            }
        }

        System.out.println("Part 1: " + distances.get(endNode));
        int numTilesOnAllBestPaths = getNumTilesOnAllBestPaths(predecessors, endNode);
        System.out.println("Part 2: " + numTilesOnAllBestPaths);
    }

    private Set<Node> getSetOfAllNodes(char[][] grid) {
        Set<Node> toProcess = new HashSet<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] != '#') {
                    toProcess.add(new Node(new Point(i, j), Direction.N));
                    toProcess.add(new Node(new Point(i, j), Direction.S));
                    toProcess.add(new Node(new Point(i, j), Direction.W));
                    toProcess.add(new Node(new Point(i, j), Direction.E));
                }
            }
        }
        return toProcess;
    }

    private int getNumTilesOnAllBestPaths(Map<Node, Set<Node>> predecessors, Node endNode) {
        Set<Point> tilesOnBestPaths = new HashSet<>();

        Queue<Node> toProcess = new LinkedList<>();
        toProcess.add(endNode);

        while (!toProcess.isEmpty()) {
            Node currentNode = toProcess.remove();
            tilesOnBestPaths.add(currentNode.pos());
            if (predecessors.get(currentNode) != null) {
                toProcess.addAll(predecessors.get(currentNode));
            }
        }
        return tilesOnBestPaths.size();
    }

    private Node getNodeWithMinDistance(Set<Node> toProcess, Map<Node, Long> distances) {
        // Not very efficient but sufficient for the input size. Could be improved by using a MinHeap.
        Node minNode = toProcess.iterator().next();
        long minDistance = distances.getOrDefault(minNode, Long.MAX_VALUE);
        for (Node node : toProcess) {
            long distance = distances.getOrDefault(node, Long.MAX_VALUE);
            if (distance < minDistance) {
                minNode = node;
                minDistance = distance;
            }
        }
        return minNode;
    }

    private Set<NeighborWithEdgeWeight> getNeighbors(Node node, char[][] grid, boolean isEndNode) {
        int rotateCost = isEndNode ? 0 : 1000;
        Set<NeighborWithEdgeWeight> neighbors = new HashSet<>();

        // Same position but direction that is 90° off from current direction:
        if (node.dir() == Direction.N || node.dir() == Direction.S) {
            neighbors.add(new NeighborWithEdgeWeight(new Node(node.pos(), Direction.W), rotateCost));
            neighbors.add(new NeighborWithEdgeWeight(new Node(node.pos(), Direction.E), rotateCost));
        } else {
            neighbors.add(new NeighborWithEdgeWeight(new Node(node.pos(), Direction.N), rotateCost));
            neighbors.add(new NeighborWithEdgeWeight(new Node(node.pos(), Direction.S), rotateCost));
        }

        // Different position but same direction:
        Point ownPos = node.pos();
        Point neighborPos = switch (node.dir()) {
            case N -> new Point(ownPos.i(), ownPos.j() - 1);
            case S -> new Point(ownPos.i(), ownPos.j() + 1);
            case W -> new Point(ownPos.i() - 1, ownPos.j());
            case E -> new Point(ownPos.i() + 1, ownPos.j());
        };

        if (neighborPos.isInsideGrid(grid) && neighborPos.gridValue(grid) != '#') {
            neighbors.add(new NeighborWithEdgeWeight(new Node(neighborPos, node.dir()), 1));
        }
        return neighbors;
    }

    public static void main(String[] args) {
        new Day16();
    }
}
