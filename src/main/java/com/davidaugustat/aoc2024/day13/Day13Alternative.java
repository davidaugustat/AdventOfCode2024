package com.davidaugustat.aoc2024.day13;

import com.davidaugustat.aoc2024.utils.TextFileReader;
import org.apache.commons.math3.linear.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a simpler alternative solution for day 13.
 * <p>
 * Note: Day13.java is fully correct and functional. However, it turned out I was thinking way too complicated about
 * this problem. I thought only allowing integer values for the number of key presses would be a huge limitation, and
 * I considered this problem to be Integer Linear Programming. Therefore, I used a rather complicated modular
 * arithmetic approach to calculate the solution.
 * <p>
 * However, it turns out that for the given puzzle inputs from AoC one can just "ignore" the integer value condition
 * and treat the problem like a normal linear system of equations with continuous values.
 * The reason is that there exists at most one solution for each machine. Thus, one can simply check if the
 * solution found by the linear system has only integer values. If not, no integer solution exists.
 */
public class Day13Alternative {
    private record ClawMachine(long ax, long ay, long bx, long by, long px, long py) {
    }

    public Day13Alternative() {
        List<String> lines = TextFileReader.readLinesFromFile("day13/input.txt");
        List<ClawMachine> machines = parseAllClawMachines(lines);

        part1(machines);
        part2(machines);
    }

    private void part1(List<ClawMachine> machines) {
        long totalCost = machines.stream().map(this::findCost).reduce(0L, Long::sum);
        System.out.println("Part 1: " + totalCost);
    }

    private void part2(List<ClawMachine> originalMachines) {
        long offset = 10000000000000L;
        List<ClawMachine> machines = originalMachines.stream()
                .map(m -> new ClawMachine(m.ax(), m.ay(), m.bx(), m.by(), m.px() + offset, m.py() + offset))
                .toList();

        long totalCost = machines.stream().map(this::findCost).reduce(0L, Long::sum);
        System.out.println("Part 2: " + totalCost);
    }

    /**
     * The problem can be formulated as follows:
     * Find non-negative integers s, t, such that the following system of linear equations is satisfied:<br>
     * (ax, ay) * s + (bx, by) * t = (px, py)
     * <p>
     * As stated at the top of this class, there only exists at most one solution, even when s, t are allowed to
     * be continuous. Thus:
     * <ol>
     *     <li>Solve the linear system in the continuous space to obtain floats sR, tR</li>
     *     <li>Check if sR, tR are non-negative</li>
     *     <li>Round sR, tR to the next integer values s, t</li>
     *     <li>Check if the integers s, t satisfy the system of equations</li>
     * </ol>
     */
    private long findCost(ClawMachine machine) {
        RealMatrix m = new Array2DRowRealMatrix(new double[][]{
                {machine.ax(), machine.bx()},
                {machine.ay(), machine.by()}}
        );
        RealVector v = new ArrayRealVector(new double[]{machine.px(), machine.py()});

        RealVector solution = new LUDecomposition(m).getSolver().solve(v);
        double sR = solution.getEntry(0);
        double tR = solution.getEntry(1);

        if (sR < 0 || tR < 0) {
            return 0;  // No solution exists - don't count
        }

        long s = Math.round(sR);
        long t = Math.round(tR);

        boolean isXSatisfied = machine.ax() * s + machine.bx() * t == machine.px();
        boolean isYSatisfied = machine.ay() * s + machine.by() * t == machine.py();
        if (!isXSatisfied || !isYSatisfied) {
            return 0; // No solution exists - don't count
        }
        return 3 * s + t;
    }

    private List<ClawMachine> parseAllClawMachines(List<String> lines) {
        List<ClawMachine> machines = new ArrayList<>();
        for (int i = 0; i < lines.size(); i += 4) {
            machines.add(parseClawMachine(lines.get(i), lines.get(i + 1), lines.get(i + 2)));
        }
        return machines;
    }

    private ClawMachine parseClawMachine(String buttonALine, String buttonBLine, String prizeLine) {
        String[] aStepStrings = buttonALine.split(":")[1].split(",");
        long ax = Long.parseLong(aStepStrings[0].split("\\+")[1]);
        long ay = Long.parseLong(aStepStrings[1].split("\\+")[1]);

        String[] bStepStrings = buttonBLine.split(":")[1].split(",");
        long bx = Long.parseLong(bStepStrings[0].split("\\+")[1]);
        long by = Long.parseLong(bStepStrings[1].split("\\+")[1]);

        String[] priceStrings = prizeLine.split(":")[1].split(",");
        long px = Long.parseLong(priceStrings[0].split("=")[1]);
        long py = Long.parseLong(priceStrings[1].split("=")[1]);

        return new ClawMachine(ax, ay, bx, by, px, py);
    }

    public static void main(String[] args) {
        new Day13Alternative();
    }
}
