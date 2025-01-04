package com.davidaugustat.aoc2024.day17;

import com.davidaugustat.aoc2024.utils.TextFileReader;

import java.util.*;

public class Day17 {
    public Day17() {
        part1();
        part2();
    }

    private void part1() {
        List<String> lines = TextFileReader.readLinesFromFile("day17/input.txt");
        long regA = Integer.parseInt(lines.get(0).split(": ")[1]);
        long regB = Integer.parseInt(lines.get(1).split(": ")[1]);
        long regC = Integer.parseInt(lines.get(2).split(": ")[1]);
        List<Integer> program = Arrays.stream(lines.get(4).split(": ")[1].split(","))
                .map(Integer::parseInt)
                .toList();

        executeProgram(program, regA, regB, regC);
    }

    private void executeProgram(List<Integer> program, long regA, long regB, long regC) {
        List<Long> output = new ArrayList<>();
        int instructionPointer = 0;

        while (instructionPointer + 1 < program.size()) {
            int opcode = program.get(instructionPointer);
            long literal = program.get(instructionPointer + 1);

            boolean usesComboOperand = List.of(0, 2, 5, 6, 7).contains(opcode);
            long operand = usesComboOperand ? getComboOperand(literal, regA, regB, regC) : literal;

            switch (opcode) {
                case 0:
                    regA = regA >> operand;
                    break;
                case 1:
                    regB = regB ^ operand;
                    break;
                case 2:
                    regB = operand & 0x7L;  // modulo 8
                    break;
                case 3:
                    if (regA != 0L) {
                        instructionPointer = (int) operand - 2; // -2 to compensate for increment at end
                    }
                    break;
                case 4:
                    regB = regB ^ regC;
                    break;
                case 5:
                    output.add(operand & 0x7L);
                    break;
                case 6:
                    regB = regA >> operand;
                    break;
                case 7:
                    regC = regA >> operand;
                    break;
            }
            instructionPointer += 2;
        }
        String outputString = String.join(",", output.stream().map(String::valueOf).toList());
        System.out.println("Part 1: " + outputString);
    }

    private long getComboOperand(long literal, long regA, long regB, long regC) {
        if (0 < literal && literal <= 3) {
            return literal;
        }
        if (literal == 4) {
            return regA;
        }
        if (literal == 5) {
            return regB;
        }
        if (literal == 6) {
            return regC;
        }
        throw new IllegalArgumentException("Illegal literal for combo: " + literal);
    }

    /**
     * This method was crafted for my specific puzzle input. It probably doesn't work with any puzzle input.
     */
    private void part2() {
        long[] expectedDigits = {2, 4, 1, 5, 7, 5, 1, 6, 0, 3, 4, 6, 5, 5, 3, 0};

        List<Set<Long>> candidatesPerStep = part2FindCandidatesPerDigit(expectedDigits);
        Set<Long> candidates = part2FindOverlappingCandidates(candidatesPerStep, expectedDigits);

        long result = Collections.min(candidates);
        System.out.println("Part 2: " + result);
    }

    private Set<Long> part2FindOverlappingCandidates(List<Set<Long>> candidatesPerStep, long[] expectedDigits) {
        Set<Long> candidates = candidatesPerStep.getFirst();
        for (int i = 1; i < expectedDigits.length; i++) {
            Set<Long> candidatesCurrentStep = candidatesPerStep.get(i);
            Set<Long> candidatesNextStep = new HashSet<>();
            for (long candidateCurrent : candidatesCurrentStep) {
                for (long candidatePrev : candidates) {
                    long mask = 127L << (3L * i);
                    if (((candidateCurrent ^ candidatePrev) & mask) == 0L) {
                        candidatesNextStep.add(candidateCurrent | candidatePrev);
                    }
                }
            }
            candidates = candidatesNextStep;
        }
        return candidates;
    }

    /**
     * This method was crafted for my specific puzzle input. It probably doesn't work with any puzzle input.
     */
    private List<Set<Long>> part2FindCandidatesPerDigit(long[] expectedDigits) {
        List<Set<Long>> candidatesPerStep = new ArrayList<>(expectedDigits.length);
        for (int i = 0; i < expectedDigits.length; i++) {
            Set<Long> candidates = new HashSet<>();
            for (int regA = 0; regA < 1024; regA++) {
                // This expression represents the calculation of B in one iteration of my given program:
                int regB = ((((regA & 7) ^ 5) ^ 6) ^ (regA >> ((regA & 7) ^ 5))) & 7;
                if (regB == expectedDigits[i]) {
                    long candidate = (long) regA << (3L * i);
                    candidates.add(candidate);
                }
            }
            candidatesPerStep.add(candidates);
        }
        return candidatesPerStep;
    }

    public static void main(String[] args) {
        new Day17();
    }
}
