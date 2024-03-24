package com.movie.moviespringboot.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileUtils {

    // create file
    public static void createDirectory(String name) {
        Path path = Paths.get(name);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                log.error("Can't create upload folder");
                log.error(e.getMessage());
                throw new RuntimeException("Could not create upload folder");
            }
        }
    }

    // delete file
    public static void deleteFile(String filePath) {
        // filePath: /image_uploads/123456789
        filePath = filePath.substring(1);
        Path path = Paths.get(filePath);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Can not delete this file");
        }
    }
}
