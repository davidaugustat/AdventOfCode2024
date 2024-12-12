package com.davidaugustat.aoc2024.day12;

import com.davidaugustat.aoc2024.utils.Point;
import com.davidaugustat.aoc2024.utils.TextFileReader;

import java.util.*;
import java.util.stream.Stream;

public class Day12 {
    public Day12() {
        char[][] grid = TextFileReader.readColumnsFirstCharGridFromFile("day12/input.txt");
        Set<Set<Point>> regions = findRegions(grid);

        part1(regions);
        part2(regions);
    }

    private void part1(Set<Set<Point>> regions) {
        int price = regions.stream()
                .map(this::calculatePriceOfRegion)
                .reduce(0, Integer::sum);
        System.out.println("Part 1: " + price);
    }

    private void part2(Set<Set<Point>> regions) {
        int discountedPrice = regions.stream()
                .map(this::calculateDiscountedPriceOfRegion)
                .reduce(0, Integer::sum);
        System.out.println("Part 2: " + discountedPrice);
    }

    private Set<Set<Point>> findRegions(char[][] grid) {
        Set<Set<Point>> regions = new HashSet<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                regions.add(discoverRegion(new Point(i, j), grid));
            }
        }
        return regions;
    }

    private Set<Point> discoverRegion(Point start, char[][] grid) {
        Set<Point> region = new HashSet<>();
        char name = grid[start.i()][start.j()];
        Queue<Point> pointsToVisit = new LinkedList<>();
        pointsToVisit.add(start);
        region.add(start);
        while (!pointsToVisit.isEmpty()) {
            Point current = pointsToVisit.remove();
            for (Point neighbor : current.getNeighbors()) {
                if (neighbor.isInsideGrid(grid) && !region.contains(neighbor) && grid[neighbor.i()][neighbor.j()] == name) {
                    region.add(neighbor);
                    pointsToVisit.add(neighbor);
                }
            }
        }
        return region;
    }

    private int calculatePerimeter(Set<Point> region) {
        int perimeter = 0;
        for (Point point : region) {
            for (Point neighbor : point.getNeighbors()) {
                if (!region.contains(neighbor)) {
                    perimeter++;
                }
            }
        }
        return perimeter;
    }

    private int calculateDiscountedPriceOfRegion(Set<Point> region) {
        return region.size() * countSides(region);
    }

    private int countSides(Set<Point> region) {
        Map<Integer, Set<Point>> upwardFacingSides = new HashMap<>(),
                downwardFacingSides = new HashMap<>(),
                leftFacingSides = new HashMap<>(),
                rightFacingSides = new HashMap<>();

        for (Point point : region) {
            int i = point.i(), j = point.j();
            if (!region.contains(new Point(i, j - 1))) {
                addToSidesMap(upwardFacingSides, j, point);
            }
            if (!region.contains(new Point(i, j + 1))) {
                addToSidesMap(downwardFacingSides, j, point);
            }
            if (!region.contains(new Point(i - 1, j))) {
                addToSidesMap(leftFacingSides, i, point);
            }
            if (!region.contains(new Point(i + 1, j))) {
                addToSidesMap(rightFacingSides, i, point);
            }
        }

        return Stream.of(upwardFacingSides, downwardFacingSides, leftFacingSides, rightFacingSides)
                .map(this::sidesMapToNumsides)
                .reduce(0, Integer::sum);
    }

    private int sidesMapToNumsides(Map<Integer, Set<Point>> sidesMap) {
        return sidesMap.values()
                .stream()
                .map(this::findNumDisjointRegions)
                .reduce(0, Integer::sum);
    }

    private int findNumDisjointRegions(Set<Point> points) {
        Set<Set<Point>> regions = new HashSet<>();
        for (Point point : points) {
            Set<Point> region = new HashSet<>();
            region.add(point);
            Queue<Point> pointsToVisit = new LinkedList<>();
            pointsToVisit.add(point);
            while (!pointsToVisit.isEmpty()) {
                Point current = pointsToVisit.remove();
                for (Point neighbor : current.getNeighbors()) {
                    if (points.contains(neighbor) && !region.contains(neighbor)) {
                        region.add(neighbor);
                        pointsToVisit.add(neighbor);
                    }
                }
            }
            regions.add(region);
        }
        return regions.size();
    }

    private void addToSidesMap(Map<Integer, Set<Point>> sidesMap, int pos, Point point) {
        if (!sidesMap.containsKey(pos)) {
            sidesMap.put(pos, new HashSet<>());
        }
        sidesMap.get(pos).add(point);
    }

    private int calculatePriceOfRegion(Set<Point> region) {
        return region.size() * calculatePerimeter(region);
    }

    public static void main(String[] args) {
        new Day12();
    }
}
