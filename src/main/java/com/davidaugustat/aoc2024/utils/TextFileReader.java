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
}
