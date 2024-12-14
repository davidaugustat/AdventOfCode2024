package com.davidaugustat.aoc2024.utils;

import com.davidaugustat.aoc2024.day13.Day13;

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

    public static long gcd(long a, long b) {
        while (b != 0) {
            long h = a % b;
            a = b;
            b = h;
        }
        return a;
    }

    /**
     * Finds integers s, t such that
     * a * s + b * t = gcd(a, b)
     * <p>
     * Modified version of the algorithm from https://de.wikipedia.org/wiki/Erweiterter_euklidischer_Algorithmus
     */
    public static ExtendedEuclidResult extendedEuclideanAlgorithm(long a, long b) {
        long s = 1;
        long t = 0;

        long u = 0;
        long v = 1;
        while (b != 0) {
            long q = a / b;
            long b1 = b;
            b = a - q * b;
            a = b1;
            long u1 = u;
            u = s - q * u;
            s = u1;
            long v1 = v;
            v = t - q * v;
            t = v1;
        }
        return new ExtendedEuclidResult(a, s, t);
    }

    /**
     * Measures the execution time of a piece of code and prints the time to standard output.
     * <p>
     * Usage:
     * <pre>
     * {@code
     * Helper.measureExecutionTime(() -> {
     *     // code to measure
     * });
     * }
     * </pre>
     */
    public static void measureExecutionTime(Runnable code) {
        long start = System.nanoTime();
        code.run();
        long finish = System.nanoTime();
        double timeElapsedMillis = (finish - start) / 1_000_000.0;
        System.out.println("Execution Time: " + timeElapsedMillis + " ms");
    }
}
