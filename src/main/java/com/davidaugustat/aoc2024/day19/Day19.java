package com.davidaugustat.aoc2024.day19;

import com.davidaugustat.aoc2024.utils.TextFileReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day19 {

    public Day19() {
        List<String> lines = TextFileReader.readLinesFromFile("day19/input.txt");
        List<String> patterns = List.of(lines.getFirst().split(", "));
        List<String> designs = lines.subList(2, lines.size());

        part1(patterns, designs);
        part2(patterns, designs);
    }

    private void part1(List<String> patterns, List<String> designs) {
        long numPossibleDesigns = designs.stream()
                .filter(design -> isDesignPossible(design, patterns, new HashMap<>()))
                .count();
        System.out.println("Part 1: " + numPossibleDesigns);
    }

    private void part2(List<String> patterns, List<String> designs) {
        long numPatternOptions = designs.stream()
                .map(design -> getNumPatternOptions(design, patterns, new HashMap<>()))
                .reduce(0L, Long::sum);
        System.out.println("Part 2: " + numPatternOptions);
    }

    boolean isDesignPossible(String design, List<String> patterns, Map<String, Boolean> cache) {
        if (cache.containsKey(design)) {
            return cache.get(design);
        }
        for (String pattern : patterns) {
            if (design.startsWith(pattern)) {
                String subDesign = design.substring(pattern.length());
                if (subDesign.isEmpty() || isDesignPossible(subDesign, patterns, cache)) {
                    cache.put(design, true);
                    return true;
                }
            }
        }
        cache.put(design, false);
        return false;
    }

    long getNumPatternOptions(String design, List<String> patterns, Map<String, Long> cache) {
        if (cache.containsKey(design)) {
            return cache.get(design);
        }
        long numPatternOptions = 0;
        for (String pattern : patterns) {
            if (design.startsWith(pattern)) {
                String subDesign = design.substring(pattern.length());
                if (subDesign.isEmpty()) {
                    numPatternOptions++;
                    continue;
                }
                numPatternOptions += getNumPatternOptions(subDesign, patterns, cache);
            }
        }
        cache.put(design, numPatternOptions);
        return numPatternOptions;
    }

    public static void main(String[] args) {
        new Day19();
    }
}
