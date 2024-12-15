package com.davidaugustat.aoc2024.day15;

import com.davidaugustat.aoc2024.utils.Helper;
import com.davidaugustat.aoc2024.utils.Point;
import com.davidaugustat.aoc2024.utils.TextFileReader;

import java.util.List;

public class Day15 {
    public Day15() {
        List<String> lines = TextFileReader.readLinesFromFile("day15/input.txt");
        int split = lines.indexOf("");
        char[][] grid = Helper.linesToColumnFirstCharGrid(lines.subList(0, split));
        String moves = String.join("", lines.subList(split + 1, lines.size()));

        part1(Helper.copyGrid(grid), moves);
        part2(Helper.copyGrid(grid), moves);
    }

    private void part1(char[][] grid, String moves) {
        Point robotPos = Helper.findInCharGrid(grid, '@');
        robotPos.gridSet(grid, '.');

        for (char move : moves.toCharArray()) {
            robotPos = performMoveNormalGrid(robotPos, move, grid);
        }

        long coordinatesSum = calculateCoordinatesSum(grid, 'O');
        System.out.println("Part 1: " + coordinatesSum);
    }

    private void part2(char[][] grid, String moves) {
        grid = createWideGrid(grid);
        Point robotPos = Helper.findInCharGrid(grid, '@');
        robotPos.gridSet(grid, '.');

        for (char move : moves.toCharArray()) {
            robotPos = performMoveWideGrid(robotPos, move, grid);
        }

        long coordinatesSum = calculateCoordinatesSum(grid, '[');
        System.out.println("Part 2: " + coordinatesSum);
    }

    private char[][] createWideGrid(char[][] grid) {
        char[][] wideGrid = new char[2 * grid.length][grid[0].length];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                String replacement = switch (grid[i][j]) {
                    case '#' -> "##";
                    case 'O' -> "[]";
                    case '.' -> "..";
                    case '@' -> "@.";
                    default -> "  ";
                };
                wideGrid[2 * i][j] = replacement.charAt(0);
                wideGrid[2 * i + 1][j] = replacement.charAt(1);
            }
        }
        return wideGrid;
    }

    private long calculateCoordinatesSum(char[][] grid, char boxChar) {
        long sum = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == boxChar) {
                    sum += i + j * 100L;
                }
            }
        }
        return sum;
    }

    private Point performMoveWideGrid(Point robotPos, char move, char[][] grid) {
        Point direction = moveCharToDirection(move);
        Point nextPos = robotPos.add(direction);
        char nextChar = nextPos.gridValue(grid);
        if (nextChar == '#') {
            return robotPos;
        }
        if (nextChar == '[') {
            moveWideBox(nextPos, move, grid);
        } else if (nextChar == ']') {
            Point nextBoxLeft = nextPos.add(new Point(-1, 0));
            moveWideBox(nextBoxLeft, move, grid);
        }
        // Refresh nextChar after moving boxes:
        nextChar = nextPos.gridValue(grid);
        if (nextChar == '.') {
            return nextPos;
        }
        return robotPos;
    }

    private void moveWideBox(Point boxPos1, char move, char[][] grid) {
        if (move == '<' || move == '>') {
            moveWideBoxHorizontally(boxPos1, move, grid);
        } else {
            moveWideBoxVertically(boxPos1, move, grid);
        }
    }

    private void moveWideBoxHorizontally(Point boxPos1, char move, char[][] grid) {
        // boxPos1 always indicates the left part of the box
        Point boxPos2 = boxPos1.add(new Point(1, 0));
        Point direction = moveCharToDirection(move);

        Point nextPos = move == '<' ? boxPos1.add(direction) : boxPos2.add(direction);
        char nextChar = nextPos.gridValue(grid);
        if (nextChar == '#') {
            return;
        }
        if (nextChar == '[' || nextChar == ']') {
            Point nextBoxPos1 = boxPos1.add(direction.multiply(2));
            moveWideBoxHorizontally(nextBoxPos1, move, grid);
        }
        // Reload value after next box may have been moved:
        nextChar = nextPos.gridValue(grid);
        if (nextChar == '.') {
            if (move == '<') {
                nextPos.gridSet(grid, '[');
                boxPos1.gridSet(grid, ']');
                boxPos2.gridSet(grid, '.');
            } else {
                boxPos2.gridSet(grid, '[');
                nextPos.gridSet(grid, ']');
                boxPos1.gridSet(grid, '.');
            }
        }
    }

    private void moveWideBoxVertically(Point boxPos1, char move, char[][] grid) {
        // boxPos1 always indicates the left part of the box
        Point boxPos2 = boxPos1.add(new Point(1, 0));
        Point direction = moveCharToDirection(move);

        if (!isBoxMovableVerticallyWideGrid(boxPos1, direction, grid)) {
            return;
        }

        Point nextPosLeft = boxPos1.add(direction);
        Point nextPosRight = boxPos2.add(direction);
        char nextCharLeft = nextPosLeft.gridValue(grid);
        char nextCharRight = nextPosRight.gridValue(grid);

        if (nextCharLeft == '#' || nextCharRight == '#') {
            return;
        }
        if (nextCharLeft == '[') {
            // box directly in front
            moveWideBoxVertically(nextPosLeft, move, grid);
        }
        if (nextCharLeft == ']') {
            // box half to the left:
            Point nextBox1 = nextPosLeft.add(new Point(-1, 0));
            moveWideBoxVertically(nextBox1, move, grid);
        }
        if (nextCharRight == '[') {
            // box half to the right:
            moveWideBoxVertically(nextPosRight, move, grid);
        }
        // Reload values after next box(es) may have been moved:
        nextCharLeft = nextPosLeft.gridValue(grid);
        nextCharRight = nextPosRight.gridValue(grid);

        if (nextCharLeft == '.' && nextCharRight == '.') {
            nextPosLeft.gridSet(grid, '[');
            nextPosRight.gridSet(grid, ']');
            boxPos1.gridSet(grid, '.');
            boxPos2.gridSet(grid, '.');
        }
    }

    private boolean isBoxMovableVerticallyWideGrid(Point boxPos1, Point direction, char[][] grid) {
        Point boxPos2 = boxPos1.add(new Point(1, 0));
        char nextCharLeft = boxPos1.add(direction).gridValue(grid);
        char nextCharRight = boxPos2.add(direction).gridValue(grid);
        if (nextCharLeft == '#' || nextCharRight == '#') {
            return false;
        }

        boolean leftFree = nextCharLeft == '.';
        boolean rightFree = nextCharRight == '.';

        if (!leftFree && !rightFree && nextCharLeft == '[') {
            // box directly in front
            leftFree = rightFree = isBoxMovableVerticallyWideGrid(boxPos1.add(direction), direction, grid);
        }
        if (!leftFree && nextCharLeft == ']') {
            // box half to the left:
            Point nextBox1 = boxPos1.add(direction).add(new Point(-1, 0));
            leftFree = isBoxMovableVerticallyWideGrid(nextBox1, direction, grid);
        }
        if (!rightFree && nextCharRight == '[') {
            // box half to the right:
            Point nextBox1 = boxPos2.add(direction);
            rightFree = isBoxMovableVerticallyWideGrid(nextBox1, direction, grid);
        }
        return leftFree && rightFree;
    }

    private Point moveCharToDirection(char moveChar) {
        return switch (moveChar) {
            case '<' -> new Point(-1, 0);
            case '>' -> new Point(1, 0);
            case '^' -> new Point(0, -1);
            case 'v' -> new Point(0, 1);
            default -> throw new IllegalArgumentException("Invalid move char");
        };
    }

    private Point performMoveNormalGrid(Point robotPos, char move, char[][] grid) {
        Point direction = moveCharToDirection(move);
        Point nextPos = robotPos.add(direction);

        if (nextPos.gridValue(grid) == '#') {
            // Cannot move because wall
            return robotPos;
        }

        if (nextPos.gridValue(grid) == 'O') {
            Point spaceForBox = nextPos.add(direction);
            while (spaceForBox.gridValue(grid) == 'O') {
                spaceForBox = spaceForBox.add(direction);
            }
            if (spaceForBox.gridValue(grid) == '#') {
                // Cannot move packet and thus cannot move robot
                return robotPos;
            }
            spaceForBox.gridSet(grid, 'O');
            nextPos.gridSet(grid, '.');
        }
        return nextPos;
    }

    public static void main(String[] args) {
        new Day15();
    }
}
