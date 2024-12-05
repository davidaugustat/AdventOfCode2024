package com.davidaugustat.aoc2024.day05;

import com.davidaugustat.aoc2024.utils.TextFileReader;

import java.util.*;
import java.util.stream.Collectors;

public class Day05 {
    private record OrderingRule(int first, int second) { }

    public Day05() {
        List<String> lines = TextFileReader.readLinesFromFile("day05/input.txt");
        int splitPosition = lines.indexOf("");

        // Use a set here, such that the contains(e) operation runs efficiently:
        Set<OrderingRule> orderingRules = lines.subList(0, splitPosition)
                .stream()
                .map(this::parseOrderingRule)
                .collect(Collectors.toCollection(HashSet::new));

        List<List<Integer>> updates = lines.subList(splitPosition + 1, lines.size())
                .stream()
                .map(line -> Arrays.stream(line.split(",")).map(Integer::parseInt).toList())
                .toList();

        Comparator<Integer> rulesComparator = getComparatorForRules(orderingRules);

        part1(updates, rulesComparator);
        part2(updates, rulesComparator);
    }

    private void part1(List<List<Integer>> updates, Comparator<Integer> rulesComparator) {
        int part1Result = updates.stream()
                .filter(update -> isUpdateOrdered(update, rulesComparator))
                .map(this::getMiddleElement)
                .reduce(0, Integer::sum);

        System.out.println("Part 1: " + part1Result);
    }

    private void part2(List<List<Integer>> updates, Comparator<Integer> rulesComparator) {
        int part2Result = updates.stream()
                .filter(update -> !isUpdateOrdered(update, rulesComparator))
                .map(update -> {
                    List<Integer> updateCopy = new ArrayList<>(update);
                    updateCopy.sort(rulesComparator);
                    return updateCopy;
                })
                .map(this::getMiddleElement)
                .reduce(0, Integer::sum);

        System.out.println("Part 2: " + part2Result);
    }

    private int getMiddleElement(List<Integer> update) {
        return update.get(update.size() / 2);
    }

    private boolean isUpdateOrdered(List<Integer> update, Comparator<Integer> comparator) {
        for (int i = 0; i < update.size() - 1; i++) {
            if (comparator.compare(update.get(i), update.get(i + 1)) != -1) {
                return false;
            }
        }
        return true;
    }

    private Comparator<Integer> getComparatorForRules(Set<OrderingRule> rules) {
        return (a, b) -> {
            if (rules.contains(new OrderingRule(a, b))) {
                return -1; // a is smaller than b
            }
            if (rules.contains(new OrderingRule(b, a))) {
                return 1; // a is greater than b
            }
            return 0;
        };
    }

    private OrderingRule parseOrderingRule(String ruleString) {
        String[] parts = ruleString.split("\\|");
        int first = Integer.parseInt(parts[0]);
        int second = Integer.parseInt(parts[1]);
        return new OrderingRule(first, second);
    }

    public static void main(String[] args) {
        new Day05();
    }
}
