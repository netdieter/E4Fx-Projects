package com.shoppinglist.backend.protocol;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProtokollManager {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void write(ProtokollObjekt obj, Path filePath) throws IOException {
        String json = objectMapper.writeValueAsString(obj);
        Files.writeString(filePath, json + System.lineSeparator(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public List<ProtokollObjekt> readAll(Path filePath) throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try (var lines = Files.lines(filePath)) {
            return lines
                .filter(line -> line != null && !line.isBlank())
                .map(line -> {
                    try {
                        return objectMapper.readValue(line, ProtokollObjekt.class);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to parse JSON line: " + line, e);
                    }
                }).collect(Collectors.toList());
        }
    }

    public List<ProtokollObjekt> findByKey(String key, Path filePath) throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try (var lines = Files.lines(filePath)) {
            return lines
                .filter(line -> line != null && !line.isBlank())
                .map(line -> {
                    try {
                        return objectMapper.readValue(line, ProtokollObjekt.class);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to parse JSON line: " + line, e);
                    }
                })
                .filter(obj -> key.equals(obj.getKey()))
                .collect(Collectors.toList());
        }
    }
}
