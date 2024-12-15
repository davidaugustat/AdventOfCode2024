package com.davidaugustat.aoc2024.utils;

import java.util.List;

/**
 * Stores the coordinates of a 2D point
 *
 * @param i x-coordinate
 * @param j y-coordinate
 */
public record Point(int i, int j) {
    public Point add(Point other) {
        return new Point(this.i + other.i, this.j + other.j);
    }

    public Point subtract(Point other) {
        return new Point(this.i - other.i, this.j - other.j);
    }

    public Point multiply(int factor) {
        return new Point(this.i * factor, this.j * factor);
    }

    public boolean isInsideGrid(int gridWidth, int gridHeight) {
        return this.i >= 0 && this.i < gridWidth && this.j >= 0 && this.j < gridHeight;
    }

    public boolean isInsideGrid(char[][] grid) {
        return isInsideGrid(grid.length, grid[0].length);
    }

    public boolean isInsideGrid(int[][] grid) {
        return isInsideGrid(grid.length, grid[0].length);
    }

    /**
     * Returns a list of the top, bottom, left and right point.
     * It is not checked whether these points actually exist in the grid.
     */
    public List<Point> getNeighbors() {
        return List.of(new Point(i, j + 1), new Point(i, j - 1), new Point(i + 1, j), new Point(i - 1, j));
    }

    public char gridValue(char[][] grid) {
        return grid[i][j];
    }

    public void gridSet(char[][] grid, char value) {
        grid[i][j] = value;
    }
}
