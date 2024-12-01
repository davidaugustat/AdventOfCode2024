package com.davidaugustat.aoc2024.day01;

import com.davidaugustat.aoc2024.utils.Helper;
import com.davidaugustat.aoc2024.utils.TextFileReader;

import java.util.*;

public class Day01 {

    public Day01() {
        List<String> lines = TextFileReader.readLinesFromFile("day01/input.txt");

        List<Integer> list1 = new ArrayList<>(), list2 = new ArrayList<>();
        for (String line : lines) {
            String[] parts = Helper.splitWhitespace(line);
            list1.add(Integer.valueOf(parts[0]));
            list2.add(Integer.valueOf(parts[1]));
        }

        Collections.sort(list1);
        Collections.sort(list2);

        part1(list1, list2);
        part2(list1, list2);
    }

    /**
     * @param list1: Sorted list of ints (ascending)
     * @param list2: Sorted list of ints (ascending)
     */
    private void part1(List<Integer> list1, List<Integer> list2) {
        int sum = 0;
        for (int i = 0; i < list1.size(); i++) {
            sum += Math.abs(list1.get(i) - list2.get(i));
        }
        System.out.println("Part 1: " + sum);
    }

    /**
     * @param list1: Sorted list of ints (ascending)
     * @param list2: Sorted list of ints (ascending)
     */
    private void part2(List<Integer> list1, List<Integer> list2) {
        Map<Integer, Integer> list2FrequencyMap = createFrequencyMap(list2);

        int score = 0;
        for (int item : list1) {
            if (list2FrequencyMap.containsKey(item)) {
                score += item * list2FrequencyMap.get(item);
            }
        }
        System.out.println("Part 2: " + score);
    }

    private Map<Integer, Integer> createFrequencyMap(List<Integer> list) {
        Map<Integer, Integer> frequencyMap = new HashMap<>(list.size());
        int frequencyCurrentItem = 0;
        for (int i = 0; i < list.size(); i++) {
            frequencyCurrentItem++;
            if (i + 1 == list.size() || !list.get(i).equals(list.get(i + 1))) {
                frequencyMap.put(list.get(i), frequencyCurrentItem);
                frequencyCurrentItem = 0;
            }
        }
        return frequencyMap;
    }

    public static void main(String[] args) {
        new Day01();
    }
}
