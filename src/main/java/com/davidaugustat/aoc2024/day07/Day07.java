package com.davidaugustat.aoc2024.day07;

import com.davidaugustat.aoc2024.utils.TextFileReader;

import java.util.Arrays;
import java.util.List;

public class Day07 {
    private record Equation(long result, List<Long> numbers) { }

    public Day07() {
        List<String> lines = TextFileReader.readLinesFromFile("day07/input.txt");
        List<Equation> equations = lines.stream().map(this::parseEquation).toList();

        part1(equations);
        part2(equations);
    }

    private void part1(List<Equation> equations) {
        long sumSatisfiableEquations = equations.stream()
                .filter(this::isSatisfiableAddMult)
                .map(Equation::result)
                .reduce(0L, Long::sum);
        System.out.println("Part 1: " + sumSatisfiableEquations);
    }

    private void part2(List<Equation> equations) {
        long sumSatisfiableEquations = equations.stream()
                .filter(this::isSatisfiableAddMultConcat)
                .map(Equation::result)
                .reduce(0L, Long::sum);
        System.out.println("Part 2: " + sumSatisfiableEquations);
    }

    private Equation parseEquation(String line) {
        String[] splitByColon = line.split(":");
        long result = Long.parseLong(splitByColon[0]);
        List<Long> numbers = Arrays.stream(splitByColon[1].substring(1).split(" "))
                .map(Long::parseLong)
                .toList();
        return new Equation(result, numbers);
    }

    private boolean isSatisfiableAddMult(Equation equation) {
        return isSatisfiableAddMult(equation.result(), equation.numbers(), equation.numbers().size());
    }

    private boolean isSatisfiableAddMult(long result, List<Long> numbers, int endPosExcl) {
        if (endPosExcl == 1) {
            return numbers.getFirst() == result;
        }
        long lastNumber = numbers.get(endPosExcl - 1);

        // Use '+' for current operator:
        boolean satisfiableByAddition = isSatisfiableAddMult(result - lastNumber, numbers, endPosExcl - 1);
        if (satisfiableByAddition) {
            return true;
        }

        // Use '*' for current operator:
        if (result % lastNumber == 0) {
            boolean satisfiableByMultiplication = isSatisfiableAddMult(result / lastNumber, numbers, endPosExcl - 1);
            return satisfiableByMultiplication;
        }
        return false;
    }

    private boolean isSatisfiableAddMultConcat(Equation equation) {
        return isSatisfiableAddMultConcat(equation.result(), equation.numbers(), equation.numbers().size());
    }

    private boolean isSatisfiableAddMultConcat(long result, List<Long> numbers, int endPosExcl) {
        if (endPosExcl == 1) {
            return numbers.getFirst() == result;
        }
        long lastNumber = numbers.get(endPosExcl - 1);

        // Use '+' for current operator:
        boolean satisfiableByAddition = isSatisfiableAddMultConcat(result - lastNumber, numbers, endPosExcl - 1);
        if (satisfiableByAddition) {
            return true;
        }

        // Use '*' for current operator:
        if (result % lastNumber == 0) {
            boolean satisfiableByMultiplication = isSatisfiableAddMultConcat(result / lastNumber, numbers, endPosExcl - 1);
            if (satisfiableByMultiplication) {
                return true;
            }
        }

        // Use '||' for current operator:
        if (numberEndsWith(result, lastNumber)) {
            long resultWithoutLastNumber = removeSuffixFromNumber(result, lastNumber);
            boolean isSatisfiableByConcatenation = isSatisfiableAddMultConcat(resultWithoutLastNumber, numbers, endPosExcl - 1);
            return isSatisfiableByConcatenation;
        }
        return false;
    }

    private boolean numberEndsWith(long number, long endOfNumber) {
        String numberString = Long.toString(number);
        String suffix = Long.toString(endOfNumber);
        return numberString.length() > suffix.length() && numberString.endsWith(suffix);
    }

    /**
     * Precondition: Decimal representation of number must end in suffixNumber.
     */
    private long removeSuffixFromNumber(long number, long suffixNumber) {
        String numberString = Long.toString(number);
        String suffix = Long.toString(suffixNumber);

        String suffixRemovedString = numberString.substring(0, numberString.length() - suffix.length());
        return Long.parseLong(suffixRemovedString);
    }

    public static void main(String[] args) {
        new Day07();
    }
}
