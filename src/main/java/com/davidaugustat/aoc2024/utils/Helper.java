package com.davidaugustat.aoc2024.utils;

public class Helper {
    public static String[] splitWhitespace(String text) {
        return text.split("\\s+");
    }

    public static void printColumnFirstGrid(char[][] grid) {
        for (int j = 0; j < grid[0].length; j++) {
            for (int i = 0; i < grid.length; i++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }

    public static int gcd(int a, int b) {
        while (b != 0) {
            int h = a % b;
            a = b;
            b = h;
        }
        return a;
    }
}
