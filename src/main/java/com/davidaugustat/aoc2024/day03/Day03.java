package com.davidaugustat.aoc2024.day03;

import com.davidaugustat.aoc2024.utils.TextFileReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 {
    private final Pattern singleExpressionPattern = Pattern.compile("mul\\(([0-9]+),([0-9]+)\\)");

    public Day03() {
        String input = TextFileReader.readStringFromFile("day03/input.txt");
        // Remove linebreaks from input (interferes with the regexes):
        input = input.replace("\n", "").replace("\r", "");

        part1(input);
        part2(input);
    }

    private void part1(String input) {
        int part1Result = evaluateAndSumAllExpressions(input);
        System.out.println("Part 1: " + part1Result);
    }

    private void part2(String input) {
        String filteredInput = removeDisabledSequences(input);
        int part2Result = evaluateAndSumAllExpressions(filteredInput);
        System.out.println("Part 2: " + part2Result);
    }

    private int evaluateAndSumAllExpressions(String input) {
        Pattern multPattern = Pattern.compile("mul\\([0-9]{1,3},[0-9]{1,3}\\)");
        Matcher mulMatcher = multPattern.matcher(input);
        int sum = 0;
        while (mulMatcher.find()) {
            String expression = mulMatcher.group();
            sum += evaluateExpression(expression);
        }
        return sum;
    }

    private int evaluateExpression(String expression) {
        Matcher matcher = singleExpressionPattern.matcher(expression);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid expression");
        }
        int number1 = Integer.parseInt(matcher.group(1));
        int number2 = Integer.parseInt(matcher.group(2));
        return number1 * number2;
    }

    private String removeDisabledSequences(String input) {
        // remove all parts with "don't()...do()"
        // mind the "?" in the regex, ensuring that not the longest but the shortest matches are used.
        input = input.replaceAll("don't\\(\\).*?do\\(\\)", "");

        // remove trailing "don't()..."
        return input.split("don't\\(\\)")[0];
    }

    public static void main(String[] args) {
        new Day03();
    }
}
