package com.davidaugustat.aoc2024.day14;

import com.davidaugustat.aoc2024.utils.Helper;
import com.davidaugustat.aoc2024.utils.Point;
import com.davidaugustat.aoc2024.utils.TextFileReader;

import java.util.Arrays;
import java.util.List;

public class Day14 {
    private record Robot(int px, int py, int vx, int vy) {
    }

    public Day14() {
        int xLength = 101, yLength = 103;

        List<String> lines = TextFileReader.readLinesFromFile("day14/input.txt");
        List<Robot> robots = lines.stream().map(this::parseRobot).toList();

        part1(robots, xLength, yLength);
        part2(robots, xLength, yLength);
    }

    private void part1(List<Robot> robots, int xLength, int yLength) {
        List<Point> positionsAfterSteps = getPositionsAfterSteps(robots, 100, xLength, yLength);

        int q1Points = numPointsInArea(positionsAfterSteps, 0, xLength / 2, 0, yLength / 2);
        int q2Points = numPointsInArea(positionsAfterSteps, xLength / 2 + 1, xLength, 0, yLength / 2);
        int q3Points = numPointsInArea(positionsAfterSteps, 0, xLength / 2, yLength / 2 + 1, yLength);
        int q4Points = numPointsInArea(positionsAfterSteps, xLength / 2 + 1, xLength, yLength / 2 + 1, yLength);

        int result = q1Points * q2Points * q3Points * q4Points;
        System.out.println("Part 1: " + result);
    }

    /**
     * This method creates a monochrome png image file showing the positions of the robots after each step.
     * <p>
     * The Christmas tree must then be found manually by looking at the exported images.
     * The step number in the image filename is then the answer for the puzzle.
     */
    private void part2(List<Robot> robots, int xLength, int yLength) {
        // Assuming xLength and yLength are prime numbers, the lcm is xLength * yLength:
        int maxSteps = xLength * yLength;
        for (int i = 0; i < maxSteps; i++) {
            List<Point> positions = getPositionsAfterSteps(robots, i, xLength, yLength);
            writeToImage(positions, xLength, yLength, i);
        }
    }

    private void writeToImage(List<Point> points, int xLength, int yLength, int step) {
        boolean[][] grid = new boolean[xLength][yLength];
        for (Point point : points) {
            grid[point.i()][point.j()] = true;
        }
        Helper.createImageFromBooleanArray(grid, "day14/step_" + step + ".png");
    }

    private List<Point> getPositionsAfterSteps(List<Robot> robots, int steps, int xLength, int yLength) {
        return robots.stream()
                .map(robot -> getPositionAfterSteps(robot, steps, xLength, yLength))
                .toList();
    }

    private int numPointsInArea(List<Point> points, int xStart, int xEnd, int yStart, int yEnd) {
        return (int) points.stream()
                .filter(point ->
                        point.i() >= xStart && point.i() < xEnd && point.j() >= yStart && point.j() < yEnd)
                .count();
    }

    private Robot parseRobot(String line) {
        String[] parts = Helper.splitWhitespace(line);
        List<Integer> p = Arrays.stream(parts[0].split("=")[1].split(",")).map(Integer::parseInt).toList();
        List<Integer> v = Arrays.stream(parts[1].split("=")[1].split(",")).map(Integer::parseInt).toList();
        return new Robot(p.get(0), p.get(1), v.get(0), v.get(1));
    }

    private Point getPositionAfterSteps(Robot robot, int steps, int xLength, int yLength) {
        int x = Helper.mod(robot.px() + steps * robot.vx(), xLength);
        int y = Helper.mod(robot.py() + steps * robot.vy(), yLength);
        return new Point(x, y);
    }

    public static void main(String[] args) {
        new Day14();
    }
}
