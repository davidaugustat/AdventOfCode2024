package com.davidaugustat.aoc2024.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

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

    public static int mod(int a, int b) {
        return (a % b + b) % b;
    }

    public static void createImageFromBooleanArray(boolean[][] data, String outputPath) {
        int width = data.length;
        int height = data[0].length;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Set the pixel to black for true and white for false
                int color = data[x][y] ? 0xFF000000 : 0xFFFFFFFF;
                image.setRGB(x, y, color);
            }
        }

        try {
            File outputFile = new File(outputPath);
            ImageIO.write(image, "png", outputFile);
            System.out.println("Image created at: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error writing image file: " + e.getMessage());
        }
    }

    public static char[][] linesToColumnFirstCharGrid(List<String> lines) {
        char[][] grid = new char[lines.getFirst().length()][lines.size()];
        for (int j = 0; j < grid.length; j++) {
            for (int i = 0; i < grid[0].length; i++) {
                grid[i][j] = lines.get(j).charAt(i);
            }
        }
        return grid;
    }

    public static Point findInCharGrid(char[][] grid, char target) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == target) {
                    return new Point(i, j);
                }
            }
        }
        throw new IllegalArgumentException("Target not found");
    }

    public static char[][] copyGrid(char[][] grid) {
        char[][] copy = new char[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(grid[i], 0, copy[i], 0, grid[i].length);
        }
        return copy;
    }
}
