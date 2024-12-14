package com.davidaugustat.aoc2024.day13;

import com.davidaugustat.aoc2024.utils.ExtendedEuclidResult;
import com.davidaugustat.aoc2024.utils.Helper;
import com.davidaugustat.aoc2024.utils.TextFileReader;

import java.util.ArrayList;
import java.util.List;

public class Day13 {
    private record ClawMachine(long ax, long ay, long bx, long by, long px, long py) { }

    public Day13() {
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
     * Note: This method uses a property of the puzzle input: As all step vectors (xa, ya), (xb, yb) are linearly
     * independent, there always only exists at most one solution (s, t) for a machine, such that the prize is
     * reached:
     * (xa, ya) * s +  (xb, yb) * t = (px, py)
     * <p>
     * This property implies that we do not need to look for a solution with minimal costs, since there is only one
     * solution in the first place.
     * <p>
     * Disclosure: While this method was written by me (David), ChatGPT was used to come up with the idea.
     */
    private long findCost(ClawMachine machine) {
        long k = machine.bx() * machine.ay() - machine.by() * machine.ax();
        long c = machine.bx() * machine.py() - machine.by() * machine.px();
        long g = Helper.gcd(k, c);

        long kPrime = k / g;
        long cPrime = c / g;
        ExtendedEuclidResult euclidResult = Helper.extendedEuclideanAlgorithm(kPrime, g);
        long s = cPrime * euclidResult.s();
        long t = (machine.px() - machine.ax() * s) / machine.bx();

        boolean isXCorrect = machine.ax() * s + machine.bx() * t == machine.px();
        boolean isYCorrect = machine.ay() * s + machine.by() * t == machine.py();
        if (s >= 0 && t >= 0 && isXCorrect && isYCorrect) {
            return 3 * s + t;
        }
        return 0; // Not solvable --> Cost does not countL
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
        new Day13();
    }
}
