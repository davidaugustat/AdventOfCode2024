package com.davidaugustat.aoc2024.day08;

import com.davidaugustat.aoc2024.utils.Helper;
import com.davidaugustat.aoc2024.utils.Point;
import com.davidaugustat.aoc2024.utils.TextFileReader;

import java.util.*;

public class Day08 {
    public Day08() {
        char[][] grid = TextFileReader.readColumnsFirstCharGridFromFile("day08/input.txt");

        Map<Character, List<Point>> antennasByFrequency = findAntennasByFrequency(grid);
        int gridWidth = grid.length;
        int gridHeight = grid[0].length;

        part1(antennasByFrequency, gridWidth, gridHeight);
        part2(antennasByFrequency, gridWidth, gridHeight);
    }

    private void part1(Map<Character, List<Point>> antennasByFrequency, int gridWidth, int gridHeight) {
        Set<Point> antinodes = findAllAntinodesPart1(antennasByFrequency, gridWidth, gridHeight);
        System.out.println("Part 1: " + antinodes.size());
    }

    private void part2(Map<Character, List<Point>> antennasByFrequency, int gridWidth, int gridHeight) {
        Set<Point> antinodes = findAllAntinodesPart2(antennasByFrequency, gridWidth, gridHeight);
        System.out.println("Part 2: " + antinodes.size());
    }

    private Map<Character, List<Point>> findAntennasByFrequency(char[][] grid) {
        Map<Character, List<Point>> antennas = new HashMap<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                char frequency = grid[i][j];
                if (frequency == '.') {
                    continue;
                }
                if (!antennas.containsKey(frequency)) {
                    antennas.put(frequency, new ArrayList<>());
                }
                antennas.get(frequency).add(new Point(i, j));
            }
        }
        return antennas;
    }

    private Set<Point> findAllAntinodesPart1(Map<Character, List<Point>> antennas, int gridWidth, int gridHeight) {
        Set<Point> antinodes = new HashSet<>();
        for (List<Point> antennasOfFrequency : antennas.values()) {
            Set<Point> antinodesOfFrequency = findAntinodesOfFrequencyPart1(antennasOfFrequency, gridWidth, gridHeight);
            antinodes.addAll(antinodesOfFrequency);
        }
        return antinodes;
    }

    private Set<Point> findAntinodesOfFrequencyPart1(List<Point> antennas, int gridWidth, int gridHeight) {
        Set<Point> antinodes = new HashSet<>();
        for (int k = 0; k < antennas.size(); k++) {
            for (int l = k + 1; l < antennas.size(); l++) {
                Point antenna1 = antennas.get(k);
                Point antenna2 = antennas.get(l);

                Point antinode1 = antenna1.add(antenna1.subtract(antenna2));
                Point antinode2 = antenna2.add(antenna2.subtract(antenna1));

                if (antinode1.isInsideGrid(gridWidth, gridHeight)) {
                    antinodes.add(antinode1);
                }
                if (antinode2.isInsideGrid(gridWidth, gridHeight)) {
                    antinodes.add(antinode2);
                }
            }
        }
        return antinodes;
    }

    private Set<Point> findAllAntinodesPart2(Map<Character, List<Point>> antennas, int gridWidth, int gridHeight) {
        Set<Point> antinodes = new HashSet<>();
        for (List<Point> antennasOfFrequency : antennas.values()) {
            Set<Point> antinodesOfFrequency = findAntinodesOfFrequencyPart2(antennasOfFrequency, gridWidth, gridHeight);
            antinodes.addAll(antinodesOfFrequency);
        }
        return antinodes;
    }

    private Set<Point> findAntinodesOfFrequencyPart2(List<Point> antennas, int gridWidth, int gridHeight) {
        Set<Point> antinodes = new HashSet<>();
        for (int k = 0; k < antennas.size(); k++) {
            for (int l = k + 1; l < antennas.size(); l++) {
                Point antenna1 = antennas.get(k);
                Point antenna2 = antennas.get(l);
                antinodes.addAll(drawLine(antenna1, antenna2, gridWidth, gridHeight));
            }
        }
        return antinodes;
    }

    private Set<Point> drawLine(Point p1, Point p2, int gridWidth, int gridHeight) {
        Set<Point> line = new HashSet<>();
        Point diff = p2.subtract(p1);
        int gcdOfDiff = Helper.gcd(Math.abs(diff.i()), Math.abs(diff.j()));
        Point step = new Point(diff.i() / gcdOfDiff, diff.j() / gcdOfDiff);

        // From p1 towards p2:
        Point current = p1;
        while (current.isInsideGrid(gridWidth, gridHeight)) {
            line.add(current);
            current = current.add(step);
        }

        // From p1 away from p2:
        current = p1.subtract(step);
        while (current.isInsideGrid(gridWidth, gridHeight)) {
            line.add(current);
            current = current.subtract(step);
        }
        return line;
    }

    public static void main(String[] args) {
        new Day08();
    }
}
