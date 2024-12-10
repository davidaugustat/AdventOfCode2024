package com.davidaugustat.aoc2024.day10;

import com.davidaugustat.aoc2024.utils.Point;
import com.davidaugustat.aoc2024.utils.TextFileReader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day10 {
    public Day10() {
        char[][] charGrid = TextFileReader.readColumnsFirstCharGridFromFile("day10/input.txt");
        int[][] grid = charGridToIntGrid(charGrid);
        List<Point> trailheads = getTrailheads(grid);

        part1(grid, trailheads);
        part2(grid, trailheads);
    }

    private void part1(int[][] grid, List<Point> trailheads) {
        int scoreSum = trailheads.stream()
                .map(trailhead -> findReachableTops(trailhead, grid))
                .map(Set::size)
                .reduce(0, Integer::sum);
        System.out.println("Part 1: " + scoreSum);
    }

    private void part2(int[][] grid, List<Point> trailheads) {
        int ratingSum = trailheads.stream()
                .map(trailhead -> findTrailsToTops(trailhead, grid))
                .map(Set::size)
                .reduce(0, Integer::sum);
        System.out.println("Part 2: " + ratingSum);
    }

    private Set<Point> findReachableTops(Point trailhead, int[][] grid) {
        List<Point> relativeNeighbors = List.of(new Point(0, 1), new Point(0, -1), new Point(1, 0), new Point(-1, 0));
        Set<Point> pointsOfCurrentHeight = new HashSet<>();
        pointsOfCurrentHeight.add(trailhead);

        for (int currentHeight = 0; currentHeight < 9; currentHeight++) {
            Set<Point> pointsOfNextHeight = new HashSet<>();
            for (Point point : pointsOfCurrentHeight) {
                int ownHeight = grid[point.i()][point.j()];
                for (Point relativeNeighbor : relativeNeighbors) {
                    Point neighbor = point.add(relativeNeighbor);
                    if (neighbor.isInsideGrid(grid.length, grid[0].length) && grid[neighbor.i()][neighbor.j()] == ownHeight + 1) {
                        pointsOfNextHeight.add(neighbor);
                    }
                }
            }
            pointsOfCurrentHeight = pointsOfNextHeight;
        }
        return pointsOfCurrentHeight;
    }

    private Set<List<Point>> findTrailsToTops(Point trailhead, int[][] grid) {
        List<Point> relativeNeighbors = List.of(new Point(0, 1), new Point(0, -1), new Point(1, 0), new Point(-1, 0));
        Set<List<Point>> trailsToCurrentHeight = new HashSet<>();
        trailsToCurrentHeight.add(List.of(trailhead));

        for (int currentHeight = 0; currentHeight < 9; currentHeight++) {
            Set<List<Point>> trailsToNextHeight = new HashSet<>();
            for (List<Point> trail : trailsToCurrentHeight) {
                Point point = trail.getLast();
                int ownHeight = grid[point.i()][point.j()];
                for (Point relativeNeighbor : relativeNeighbors) {
                    Point neighbor = point.add(relativeNeighbor);
                    if (neighbor.isInsideGrid(grid.length, grid[0].length) && grid[neighbor.i()][neighbor.j()] == ownHeight + 1) {
                        List<Point> trailCopy = new ArrayList<>(trail);
                        trailCopy.add(neighbor);
                        trailsToNextHeight.add(trailCopy);
                    }
                }
            }
            trailsToCurrentHeight = trailsToNextHeight;
        }
        return trailsToCurrentHeight;
    }

    private int[][] charGridToIntGrid(char[][] charGrid) {
        int[][] grid = new int[charGrid.length][charGrid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = charGrid[i][j] - '0';
            }
        }
        return grid;
    }

    private List<Point> getTrailheads(int[][] grid) {
        List<Point> trailHeads = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 0) {
                    trailHeads.add(new Point(i, j));
                }
            }
        }
        return trailHeads;
    }

    public static void main(String[] args) {
        new Day10();
    }
}
