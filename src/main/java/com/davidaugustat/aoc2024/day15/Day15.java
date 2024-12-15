package com.davidaugustat.aoc2024.day15;

import com.davidaugustat.aoc2024.utils.Helper;
import com.davidaugustat.aoc2024.utils.Point;
import com.davidaugustat.aoc2024.utils.TextFileReader;

import java.util.List;

public class Day15 {
    public Day15() {
        List<String> lines = TextFileReader.readLinesFromFile("day15/input.txt");
        lines.stream().filter(line -> line.length() < 2).findFirst().get();
        int split = findEmptyLine(lines);
        char[][] grid = Helper.linesToColumnFirstCharGrid(lines.subList(0, split));
        String moves = String.join("", lines.subList(split + 1, lines.size()));

        part1(Helper.copyGrid(grid), moves);
        part2(Helper.copyGrid(grid), moves);
    }

    private int findEmptyLine(List<String> lines) {
        int split = -1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).length() < 2) {
                split = i;
                break;
            }
        }
        return split;
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
                char c1 = 0, c2 = 0;
                switch (grid[i][j]) {
                    case '#':
                        c1 = c2 = '#';
                        break;
                    case 'O':
                        c1 = '[';
                        c2 = ']';
                        break;
                    case '.':
                        c1 = c2 = '.';
                        break;
                    case '@':
                        c1 = '@';
                        c2 = '.';
                        break;
                }
                wideGrid[2 * i][j] = c1;
                wideGrid[2 * i + 1][j] = c2;
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

    private boolean moveWideBox(Point boxPos1, char move, char[][] grid) {
        // boxPos always indicates the left part of the box
        Point boxPos2 = boxPos1.add(new Point(1, 0));
        Point direction = moveCharToDirection(move);
        if (move == '<' || move == '>') {
            if (move == '<') {
                char nextChar = boxPos1.add(direction).gridValue(grid);
                if (nextChar == '#') {
                    return false;
                }
                if (nextChar == ']') {
                    Point nextBoxPos1 = boxPos1.add(direction.multiply(2));
                    moveWideBox(nextBoxPos1, move, grid);
                }
                nextChar = boxPos1.add(direction).gridValue(grid);
                if (nextChar == '.') {
                    // This gets triggered when either there already was a space in front of this box of if
                    // the previous recursive call made space in front of the box.
                    boxPos1.add(direction).gridSet(grid, '[');
                    boxPos1.gridSet(grid, ']');
                    boxPos2.gridSet(grid, '.');
                    return true;
                }
                return false;
            } else { // >
                char nextChar = boxPos2.add(direction).gridValue(grid);
                if (nextChar == '#') {
                    return false;
                }
                if (nextChar == '[') {
                    Point nextBoxPos1 = boxPos1.add(direction.multiply(2));
                    moveWideBox(nextBoxPos1, move, grid);
                }
                nextChar = boxPos2.add(direction).gridValue(grid);
                if (nextChar == '.') {
                    // This gets triggered when either there already was a space in front of this box of if
                    // the previous recursive call made space in front of the box.
                    boxPos2.gridSet(grid, '[');
                    boxPos2.add(direction).gridSet(grid, ']');
                    boxPos1.gridSet(grid, '.');
                    return true;
                }
                return false;
            }
        } else {
            // move vertically
            if (!isBoxMovableVerticallyWideGrid(boxPos1, direction, grid)) {
                return false;
            }
            char nextCharLeft = boxPos1.add(direction).gridValue(grid);
            char nextCharRight = boxPos2.add(direction).gridValue(grid);
            if (nextCharLeft == '#' || nextCharRight == '#') {
                return false;
            }
            if (nextCharLeft == '[') {
                // box directly in front
                moveWideBox(boxPos1.add(direction), move, grid);
            }
            if (nextCharLeft == ']') {
                // box half to the left:
                Point nextBox1 = boxPos1.add(direction).add(new Point(-1, 0));
                moveWideBox(nextBox1, move, grid);
            }
            if (nextCharRight == '[') {
                // box half to the right:
                Point nextBox1 = boxPos2.add(direction);
                moveWideBox(nextBox1, move, grid);
            }
            nextCharLeft = boxPos1.add(direction).gridValue(grid);
            nextCharRight = boxPos2.add(direction).gridValue(grid);
            if (nextCharLeft == '.' && nextCharRight == '.') {
                boxPos1.add(direction).gridSet(grid, '[');
                boxPos2.add(direction).gridSet(grid, ']');
                boxPos1.gridSet(grid, '.');
                boxPos2.gridSet(grid, '.');
                return true;
            }
            return false;
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
