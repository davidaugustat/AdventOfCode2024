package com.davidaugustat.aoc2024.day18;

import com.davidaugustat.aoc2024.utils.BinaryHeapPriorityQueue;
import com.davidaugustat.aoc2024.utils.Point;
import com.davidaugustat.aoc2024.utils.TextFileReader;

import java.util.*;

public class Day18 {
    public Day18() {
        boolean isExampleInput = false;
        String inputFile = isExampleInput ? "day18/exampleinput.txt" : "day18/input.txt";
        List<String> lines = TextFileReader.readLinesFromFile(inputFile);
        List<Point> unsafePointsList = lines.stream().map(line -> {
            String[] parts = line.split(",");
            return new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }).toList();

        part1(unsafePointsList, isExampleInput);
        part2(unsafePointsList, isExampleInput);
    }

    private void part1(List<Point> unsafePointsList, boolean isExampleInput) {
        int listTruncatePosition = isExampleInput ? 12 : 1024;
        int gridSize = isExampleInput ? 7 : 71;
        unsafePointsList = unsafePointsList.subList(0, listTruncatePosition);
        Set<Point> unsafePointsSet = new HashSet<>(unsafePointsList);
        int distance = dijkstraFindShortestPathLength(gridSize, gridSize, unsafePointsSet);

        System.out.println("Part 1: " + distance);
    }

    private void part2(List<Point> unsafePointsList, boolean isExampleInput) {
        int gridSize = isExampleInput ? 7 : 71;

        Point result = null;
        for (int i = 1; i < unsafePointsList.size(); i++) {
            List<Point> unsafePointsListTruncated = unsafePointsList.subList(0, i);
            Set<Point> unsafePointsSet = new HashSet<>(unsafePointsListTruncated);
            int distance = dijkstraFindShortestPathLength(gridSize, gridSize, unsafePointsSet);
            if (distance == -1) {
                result = unsafePointsList.get(i - 1);
                break;
            }
        }
        assert result != null;
        String resultString = result.i() + "," + result.j();
        System.out.println("Part 2: " + resultString);
    }

    private int dijkstraFindShortestPathLength(int gridSizeX, int gridSizeY, Set<Point> unsafePoints) {
        Point startNode = new Point(0, 0);
        Point endNode = new Point(gridSizeX - 1, gridSizeY - 1);

        Map<Point, Integer> distances = new HashMap<>();
        distances.put(startNode, 0);

        int numNodes = gridSizeX * gridSizeY - unsafePoints.size();
        BinaryHeapPriorityQueue<Point> toProcess = new BinaryHeapPriorityQueue<>(Point.class, numNodes,
                (node1, node2) -> isNode1SmallerThanNode2(node1, node2, distances));
        toProcess.insertWithoutDuplicates(startNode);

        Set<Point> processed = new HashSet<>();

        while (!toProcess.isEmpty()) {
            Point currentNode = toProcess.poll();
            processed.add(currentNode);
            if (!distances.containsKey(currentNode)) {
                // Minimum distance node has infinite distance. We can abort here since no improvement is possible.
                break;
            }
            if (currentNode.equals(endNode)) {
                break;
            }

            for (Point neighbor : getNeighbors(currentNode, gridSizeX, gridSizeY, unsafePoints)) {
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
        return distances.getOrDefault(endNode, -1);
    }

    private boolean isNode1SmallerThanNode2(Point node1, Point node2, Map<Point, Integer> distances) {
        int node1Distance = distances.getOrDefault(node1, Integer.MAX_VALUE);
        int node2Distance = distances.getOrDefault(node2, Integer.MAX_VALUE);
        return node1Distance < node2Distance;
    }

    private Set<Point> getNeighbors(Point point, int gridSizeX, int gridSizeY, Set<Point> unsafePositions) {
        Set<Point> neighbors = new HashSet<>();
        for (Point neighborCandidate : point.getNeighbors()) {
            if (neighborCandidate.isInsideGrid(gridSizeX, gridSizeY) && !unsafePositions.contains(neighborCandidate)) {
                neighbors.add(neighborCandidate);
            }
        }
        return neighbors;
    }

    public static void main(String[] args) {
        new Day18();
    }
}
