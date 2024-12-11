package com.davidaugustat.aoc2024.day11;

import com.davidaugustat.aoc2024.utils.Helper;
import com.davidaugustat.aoc2024.utils.TextFileReader;

import java.util.*;
import java.util.stream.Collectors;

public class Day11 {

    public Day11() {
        String input = TextFileReader.readStringFromFile("day11/input.txt");
        List<Long> stones = Arrays.stream(Helper.splitWhitespace(input)).map(Long::parseLong).toList();

        long part1Result = calcNumStonesAfterNBlinks(stones, 25);
        System.out.println("Part 1: " + part1Result);

        long part2Result = calcNumStonesAfterNBlinks(stones, 75);
        System.out.println("Part 2: " + part2Result);
    }

    private long calcNumStonesAfterNBlinks(List<Long> stones, int blinkTimes) {
        Map<Long, Long> stoneOccurrences = stones.stream().collect(Collectors.toMap(stone -> stone, stone -> 1L));
        for (int i = 0; i < blinkTimes; i++) {
            stoneOccurrences = blink(stoneOccurrences);
        }
        return stoneOccurrences.values().stream().reduce(0L, Long::sum);
    }

    /**
     * The {@code Map<Long, Long>} has  the following structure:
     * <ul>
     *     <li>Key: Number engraved in stone</li>
     *     <li>Value: Number of times this stone occurs in the current blink step</li>
     * </ul>
     */
    private Map<Long, Long> blink(Map<Long, Long> stoneOccurrences) {
        Map<Long, Long> updated = new HashMap<>();
        for (var entry : stoneOccurrences.entrySet()) {
            long value = entry.getKey();
            long occurrences = entry.getValue();
            if (value == 0) {
                insertOrIncrease(1L, updated, occurrences);
            } else if (Long.toString(value).length() % 2 == 0) {
                String stoneString = Long.toString(value);
                long stone1 = Long.parseLong(stoneString.substring(0, stoneString.length() / 2));
                long stone2 = Long.parseLong(stoneString.substring(stoneString.length() / 2));
                insertOrIncrease(stone1, updated, occurrences);
                insertOrIncrease(stone2, updated, occurrences);
            } else {
                insertOrIncrease(value * 2024L, updated, occurrences);
            }
        }
        return updated;
    }

    private void insertOrIncrease(long stone, Map<Long, Long> stoneOccurrences, long times) {
        stoneOccurrences.merge(stone, times, Long::sum);
    }

    public static void main(String[] args) {
        new Day11();
    }
}
