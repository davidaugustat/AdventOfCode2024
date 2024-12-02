package com.davidaugustat.aoc2024.day02;

import com.davidaugustat.aoc2024.utils.Helper;
import com.davidaugustat.aoc2024.utils.TextFileReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day02 {
    public Day02() {
        List<String> lines = TextFileReader.readLinesFromFile("day02/input.txt");

        List<List<Integer>> reports = new ArrayList<>();
        for (String line : lines) {
            List<Integer> report = Arrays.stream(Helper.splitWhitespace(line))
                    .map(Integer::valueOf)
                    .toList();
            reports.add(report);
        }

        part1(reports);
        part2(reports);
    }

    private void part1(List<List<Integer>> reports) {
        long numSafeReports = reports.stream().filter(this::isSafeReportPart1).count();
        System.out.println("Part 1: " + numSafeReports);
    }

    private void part2(List<List<Integer>> reports) {
        long numSafeReports = reports.stream().filter(this::isSafeReportPart2).count();
        System.out.println("Part 2: " + numSafeReports);
    }

    private boolean isSafeReportPart1(List<Integer> report) {
        boolean allIncreasing = true, allDecreasing = true;
        for (int i = 0; i < report.size() - 1; i++) {
            int current = report.get(i);
            int next = report.get(i + 1);

            int difference = Math.abs(current - next);
            if (difference < 1 || difference > 3) {
                return false;
            }
            if (current >= next) {
                allIncreasing = false;
            }
            if (current <= next) {
                allDecreasing = false;
            }
        }
        return allIncreasing || allDecreasing;
    }

    private boolean isSafeReportPart2(List<Integer> report) {
        if (isSafeReportPart1(report)) {
            return true;
        }

        for (int i = 0; i < report.size(); i++) {
            List<Integer> reportCopy = new ArrayList<>(report);
            reportCopy.remove(i);
            if (isSafeReportPart1(reportCopy)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        new Day02();
    }
}
