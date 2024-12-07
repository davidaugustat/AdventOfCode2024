package com.davidaugustat.aoc2024.day06;

import com.davidaugustat.aoc2024.utils.Point;
import com.davidaugustat.aoc2024.utils.TextFileReader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day06 {
    private enum Direction {LEFT, RIGHT, UP, DOWN}

    private record PointWithDir(Point point, Direction direction) { }

    public Day06() {
        char[][] grid = TextFileReader.readColumnsFirstCharGridFromFile("day06/input.txt");

        Point guardPosition = findGuardPosition(grid);
        part1(grid, guardPosition);
        part2(grid, guardPosition);
    }

    public void part1(char[][] grid, Point startPosition) {
        Set<Point> visitedPositions = recordGuardWalk(grid, startPosition);
        System.out.println("Part 1: " + visitedPositions.size());
    }

    public void part2(char[][] grid, Point startPosition) {
        Set<Point> visitedPositions = recordGuardWalk(grid, startPosition);
        visitedPositions.remove(startPosition);

        int cycleCausingObstacles = 0;
        for (Point position : visitedPositions) {
            grid[position.i()][position.j()] = '#';
            if (hasCycle(grid, startPosition)) {
                cycleCausingObstacles++;
            }
            grid[position.i()][position.j()] = '.';
        }

        System.out.println("Part 2: " + cycleCausingObstacles);
    }

    private Set<Point> recordGuardWalk(char[][] grid, Point startPosition) {
        PointWithDir pointDir = new PointWithDir(startPosition, getDirection(startPosition, grid));
        Set<Point> positions = new HashSet<>();
        while (isPointInsideGrid(pointDir.point(), grid)) {
            positions.add(pointDir.point());
            pointDir = nextStep(grid, pointDir);
        }
        return positions;
    }

    private boolean hasCycle(char[][] grid, Point startPosition) {
        PointWithDir pointDir = new PointWithDir(startPosition, getDirection(startPosition, grid));

        Set<PointWithDir> visitedPointDirs = new HashSet<>();
        while (isPointInsideGrid(pointDir.point(), grid)) {
            if (visitedPointDirs.contains(pointDir)) {
                return true;
            }
            visitedPointDirs.add(pointDir);
            pointDir = nextStep(grid, pointDir);
        }
        return false;
    }

    private Direction getDirection(Point position, char[][] grid) {
        return charToDirection(grid[position.i()][position.j()]);
    }

    private PointWithDir nextStep(char[][] grid, PointWithDir pointDir) {
        int i = pointDir.point().i();
        int j = pointDir.point().j();
        Direction direction = pointDir.direction();

        while (isObstacleInDirection(grid, pointDir.point(), direction)) {
            direction = switch (direction) {
                case UP -> Direction.RIGHT;
                case DOWN -> Direction.LEFT;
                case LEFT -> Direction.UP;
                case RIGHT -> Direction.DOWN;
            };
        }

        switch (direction) {
            case UP:
                j -= 1;
                break;
            case DOWN:
                j += 1;
                break;
            case LEFT:
                i -= 1;
                break;
            case RIGHT:
                i += 1;
                break;
        }

        return new PointWithDir(new Point(i, j), direction);
    }

    private boolean isObstacleInDirection(char[][] grid, Point position, Direction direction) {
        int i = position.i(), j = position.j();
        switch (direction) {
            case UP -> j -= 1;
            case DOWN -> j += 1;
            case LEFT -> i -= 1;
            case RIGHT -> i += 1;
        }
        return isPointInsideGrid(i, j, grid) && grid[i][j] == '#';
    }

    private Direction charToDirection(char guardChar) {
        return switch (guardChar) {
            case '^' -> Direction.UP;
            case 'v' -> Direction.DOWN;
            case '<' -> Direction.LEFT;
            case '>' -> Direction.RIGHT;
            default -> throw new IllegalArgumentException("Invalid guard character");
        };
    }

    private boolean isPointInsideGrid(Point point, char[][] grid) {
        return isPointInsideGrid(point.i(), point.j(), grid);
    }

    private boolean isPointInsideGrid(int i, int j, char[][] grid) {
        return i >= 0 && i < grid.length && j >= 0 && j < grid[0].length;
    }

    private Point findGuardPosition(char[][] grid) {
        List<Character> guardCharacters = List.of('^', 'v', '>', '<');
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (guardCharacters.contains(grid[i][j])) {
                    return new Point(i, j);
                }
            }
        }
        throw new IllegalArgumentException("Grid does not contain a guard");
    }

    public static void main(String[] args) {
        new Day06();
    }
}
