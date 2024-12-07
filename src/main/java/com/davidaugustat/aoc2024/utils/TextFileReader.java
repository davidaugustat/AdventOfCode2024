package com.davidaugustat.aoc2024.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TextFileReader {

    public static List<String> readLinesFromFile(String path){
        Path filePath = Paths.get(path);
        try {
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readStringFromFile(String path){
        Path filePath = Paths.get(path);
        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static char[][] readCharGridFromFile(String path){
        List<String> lines = readLinesFromFile(path);
        char[][] grid = new char[lines.size()][lines.getFirst().length()];
        for(int j=0; j<grid.length; j++){
            for(int i=0; i<grid[0].length; i++){
                grid[j][i] = lines.get(j).charAt(i);
            }
        }
        return grid;
    }

    /**
     * Returns a grid such that
     * <ul>
     *     <li>the first index indicates the column (x direction)</li>
     *     <li>the second index indicates the row (y direction)</li>
     * </ul>
     */
    public static char[][] readColumnsFirstCharGridFromFile(String path){
        List<String> lines = readLinesFromFile(path);
        char[][] grid = new char[lines.getFirst().length()][lines.size()];
        for(int j=0; j<grid.length; j++){
            for(int i=0; i<grid[0].length; i++){
                grid[i][j] = lines.get(j).charAt(i);
            }
        }
        return grid;
    }
}
