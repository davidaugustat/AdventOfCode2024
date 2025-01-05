package com.davidaugustat.aoc2024.day20;

import com.davidaugustat.aoc2024.utils.BinaryHeapPriorityQueue;
import com.davidaugustat.aoc2024.utils.Helper;
import com.davidaugustat.aoc2024.utils.Point;
import com.davidaugustat.aoc2024.utils.TextFileReader;

import java.util.*;

public class Day20 {
    public Day20() {
        char[][] grid = TextFileReader.readColumnsFirstCharGridFromFile("day20/input.txt");
        Point start = Helper.findInCharGrid(grid, 'S');
        Point end = Helper.findInCharGrid(grid, 'E');

        int part1 = getNumCheatsImprovingAtLeast100(grid, start, end, 2);
        System.out.println("Part 1: " + part1);

        int part2 = getNumCheatsImprovingAtLeast100(grid, start, end, 20);
        System.out.println("Part 2: " + part2);
    }

    private int getNumCheatsImprovingAtLeast100(char[][] grid, Point start, Point end, int maxCheatLength) {
        Map<Point, Integer> startDistances = getMinDistancesDijkstra(grid, start);
        Map<Point, Integer> endDistances = getMinDistancesDijkstra(grid, end);

        int distanceWithoutCheat = startDistances.get(end);

        int numCheatsAtLeast100Improvement = 0;
        for (int i1 = 0; i1 < grid.length; i1++) {
            for (int j1 = 0; j1 < grid[0].length; j1++) {
                Point cheatStart = new Point(i1, j1);
                if (cheatStart.gridValue(grid) == '#') {
                    continue;
                }
                for (int i2 = 0; i2 < grid.length; i2++) {
                    for (int j2 = 0; j2 < grid[0].length; j2++) {
                        Point cheatEnd = new Point(i2, j2);
                        if (cheatEnd.gridValue(grid) == '#') {
                            continue;
                        }
                        Optional<Integer> distanceWithCheat = calculateDistanceWithCheat(cheatStart,
                                cheatEnd, startDistances, endDistances, maxCheatLength);
                        if (distanceWithCheat.isPresent()) {
                            int improvement = distanceWithoutCheat - distanceWithCheat.get();
                            if (improvement >= 100) {
                                numCheatsAtLeast100Improvement++;
                            }
                        }
                    }
                }
            }
        }
        return numCheatsAtLeast100Improvement;
    }

    private Optional<Integer> calculateDistanceWithCheat(Point cheatStart, Point cheatEnd, Map<Point,
            Integer> distancesToStart, Map<Point, Integer> distancesToEnd, int maxCheatLength) {
        int cheatPathLength = Math.abs(cheatStart.i() - cheatEnd.i()) + Math.abs(cheatStart.j() - cheatEnd.j());
        if (cheatPathLength > maxCheatLength) {
            return Optional.empty();
        }

        int distanceCheatPathToStart = distancesToStart.get(cheatStart);
        int distanceCheatPathToEnd = distancesToEnd.get(cheatEnd);
        int totalDistance = distanceCheatPathToStart + cheatPathLength + distanceCheatPathToEnd;
        return Optional.of(totalDistance);
    }

    private Map<Point, Integer> getMinDistancesDijkstra(char[][] grid, Point start) {
        Map<Point, Integer> distances = new HashMap<>();
        distances.put(start, 0);

        int numNodes = grid.length * grid[0].length;
        BinaryHeapPriorityQueue<Point> toProcess = new BinaryHeapPriorityQueue<>(Point.class, numNodes,
                (node1, node2) -> isNode1SmallerThanNode2(node1, node2, distances));
        toProcess.insertWithoutDuplicates(start);

        Set<Point> processed = new HashSet<>();

        while (!toProcess.isEmpty()) {
            Point currentNode = toProcess.poll();
            processed.add(currentNode);
            if (!distances.containsKey(currentNode)) {
                // Minimum distance node has infinite distance. We can abort here since no improvement is possible.
                break;
            }

            for (Point neighbor : getNeighbors(currentNode, grid)) {
                if (processed.contains(neighbor)) {
                    continue;
                }
                int currentDistance = distances.getOrDefault(neighbor, Integer.MAX_VALUE);
                int altDistance = distances.get(currentNode) + 1;
                if (altDistance < currentDistance) {
                    distances.put(neighbor, altDistance);
                    toProcess.insertWithoutDuplicates(neighbor);
                }
            }
        }
        return distances;
    }

    private Set<Point> getNeighbors(Point point, char[][] grid) {
        Set<Point> neighbors = new HashSet<>();
        for (Point neighborCandidate : point.getNeighbors()) {
            if (neighborCandidate.isInsideGrid(grid) && neighborCandidate.gridValue(grid) != '#') {
                neighbors.add(neighborCandidate);
            }
        }
        return neighbors;
    }

    private boolean isNode1SmallerThanNode2(Point node1, Point node2, Map<Point, Integer> distances) {
        int node1Distance = distances.getOrDefault(node1, Integer.MAX_VALUE);
        int node2Distance = distances.getOrDefault(node2, Integer.MAX_VALUE);
        return node1Distance < node2Distance;
    }

    public static void main(String[] args) {
        new Day20();
    }
}
