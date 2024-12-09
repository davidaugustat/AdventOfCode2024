package com.davidaugustat.aoc2024.day09;

import com.davidaugustat.aoc2024.utils.TextFileReader;

import java.util.ArrayList;
import java.util.List;

public class Day09 {
    private record Block(int id, int fileSize) {
    }

    public Day09() {
        String input = TextFileReader.readStringFromFile("day09/input.txt");
        List<Block> blocks = inputToBlockSequence(input);

        part1(new ArrayList<>(blocks));
        part2(new ArrayList<>(blocks));
    }

    private void part1(List<Block> blocks) {
        compactWithFragmentation(blocks);
        long checksum = checksum(blocks);
        System.out.println("Part 1: " + checksum);
    }

    private void part2(List<Block> blocks) {
        compactWithoutFragmentation(blocks);
        long checksum = checksum(blocks);
        System.out.println("Part 2: " + checksum);
    }

    private List<Block> inputToBlockSequence(String input) {
        List<Block> blocks = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            int size = input.charAt(i) - '0';
            for (int l = 0; l < size; l++) {
                if (i % 2 == 0) {
                    blocks.add(new Block(i / 2, size));
                } else {
                    blocks.add(null);
                }
            }
        }
        return blocks;
    }

    private void compactWithoutFragmentation(List<Block> blocks) {
        for (int i = blocks.size() - 1; i > 0; i--) {
            Block currentBlock = blocks.get(i);
            Block preceedingBlock = blocks.get(i - 1);
            if (currentBlock != null && (preceedingBlock == null || currentBlock.id() != preceedingBlock.id())) {
                // start of file found
                int newPos = findGap(blocks, currentBlock.fileSize(), i);
                if (newPos != -1) {
                    moveFile(blocks, i, newPos, currentBlock.fileSize());
                }
            }
        }
    }

    private int findGap(List<Block> blocks, int size, int maxEndExcl) {
        int gapSize = 0;
        for (int i = 0; i < maxEndExcl; i++) {
            if (blocks.get(i) == null) {
                gapSize++;
            } else {
                gapSize = 0;
            }
            if (gapSize == size) {
                return i - (gapSize - 1);
            }
        }
        return -1;
    }

    private void moveFile(List<Block> blocks, int oldFileStartPos, int newFileStartPos, int size) {
        for (int i = 0; i < size; i++) {
            blocks.set(newFileStartPos + i, blocks.get(oldFileStartPos + i));
            blocks.set(oldFileStartPos + i, null);
        }
    }

    private void compactWithFragmentation(List<Block> blocks) {
        int backwardPos = blocks.size() - 1;
        for (int i = 0; i < blocks.size(); i++) {
            Block forwardBlock = blocks.get(i);
            if (forwardBlock == null) {
                while (blocks.get(backwardPos) == null) {
                    backwardPos--;
                }
                if (backwardPos <= i) {
                    return;
                }
                blocks.set(i, blocks.get(backwardPos));
                blocks.set(backwardPos, null);
            }
        }
    }

    private long checksum(List<Block> blocks) {
        long result = 0;
        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            if (block == null) {
                continue;
            }
            result += (long) i * block.id();
        }
        return result;
    }

    public static void main(String[] args) {
        new Day09();
    }
}
