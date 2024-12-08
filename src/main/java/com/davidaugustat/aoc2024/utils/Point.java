package com.davidaugustat.aoc2024.utils;

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

    public boolean isInsideGrid(int gridWidth, int gridHeight) {
        return this.i >= 0 && this.i < gridWidth && this.j >= 0 && this.j < gridHeight;
    }
}
