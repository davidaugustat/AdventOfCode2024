package com.davidaugustat.aoc2024.day04;

import com.davidaugustat.aoc2024.utils.TextFileReader;

import java.util.List;

public class Day04 {
    private record Point(int i, int j) {
    }

    public Day04() {
        char[][] grid = TextFileReader.readCharGridFromFile("day04/input.txt");

        part1(grid);
        part2(grid);
    }

    private void part1(char[][] grid) {
        final String word = "XMAS";
        final List<List<Point>> indexTraces = List.of(
                // Horizontal:
                List.of(new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0)),
                List.of(new Point(0, 0), new Point(-1, 0), new Point(-2, 0), new Point(-3, 0)),

                // Vertical:
                List.of(new Point(0, 0), new Point(0, 1), new Point(0, 2), new Point(0, 3)),
                List.of(new Point(0, 0), new Point(0, -1), new Point(0, -2), new Point(0, -3)),

                // Diagonal upper left -> lower right:
                List.of(new Point(0, 0), new Point(1, 1), new Point(2, 2), new Point(3, 3)),
                List.of(new Point(0, 0), new Point(-1, -1), new Point(-2, -2), new Point(-3, -3)),

                // Diagonal upper right -> lower left:
                List.of(new Point(0, 0), new Point(1, -1), new Point(2, -2), new Point(3, -3)),
                List.of(new Point(0, 0), new Point(-1, 1), new Point(-2, 2), new Point(-3, 3))
        );

        int numXmasTotal = numOccuringTracesTotal(grid, indexTraces, word);
        System.out.println("Part 1: " + numXmasTotal);
    }

    private void part2(char[][] grid) {
        final String word = "AMSMS";
        final List<List<Point>> indexTraces = List.of(
                List.of(new Point(0, 0), new Point(-1, -1), new Point(1, 1), new Point(1, -1), new Point(-1, 1)), // MMSS
                List.of(new Point(0, 0), new Point(-1, 1), new Point(1, -1), new Point(1, 1), new Point(-1, -1)), // SSMM
                List.of(new Point(0, 0), new Point(-1, -1), new Point(1, 1), new Point(-1, 1), new Point(1, -1)), // MSMS
                List.of(new Point(0, 0), new Point(1, 1), new Point(-1, -1), new Point(1, -1), new Point(-1, 1)) // SMSM
        );

        int numXmasTotal = numOccuringTracesTotal(grid, indexTraces, word);
        System.out.println("Part 2: " + numXmasTotal);
    }

    private int numOccuringTracesTotal(char[][] grid, List<List<Point>> indexTraces, String word) {
        int j_len = grid.length; // size in j-direction (rows)
        int i_len = grid[0].length; // size in i-direction (columns)

        int numOccurences = 0;
        for (int j = 0; j < j_len; j++) {
            for (int i = 0; i < i_len; i++) {
                numOccurences += numOccurringTracesAtPosition(i, j, grid, indexTraces, word);
            }
        }
        return numOccurences;
    }

    private int numOccurringTracesAtPosition(int i, int j, char[][] grid, List<List<Point>> indexTraces, String word) {
        int j_len = grid.length; // size in j-direction (rows)
        int i_len = grid[0].length; // size in i-direction (columns)

        int foundTraces = 0;
        for (List<Point> trace : indexTraces) {

            boolean isValid = true;
            for (int k = 0; k < trace.size(); k++) {
                Point point = trace.get(k);
                int i_current = i + point.i;
                int j_current = j + point.j;
                if (i_current < 0 || j_current < 0 || i_current >= i_len || j_current >= j_len) {
                    isValid = false;
                    break;
                }
                if (grid[j_current][i_current] != word.charAt(k)) {
                    isValid = false;
                    break;
                }
            }
            if (isValid) {
                foundTraces++;
            }
        }
        return foundTraces;
    }

    public static void main(String[] args) {
        new Day04();
    }
}
