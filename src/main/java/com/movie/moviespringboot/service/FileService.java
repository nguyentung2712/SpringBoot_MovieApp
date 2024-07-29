package com.movie.moviespringboot.service;

import com.movie.moviespringboot.exception.BadRequestException;
import com.movie.moviespringboot.exception.ResourceNotFoundException;
import com.movie.moviespringboot.utils.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {
    public static final String UPLOAD_FOLDER = "image_uploads";

    public FileService() {
        FileUtils.createDirectory(UPLOAD_FOLDER);
    }

    public static String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Can not upload empty file");
        }

        // Validate file size, file type, file extension, ...
        try {
            // Create folder name only with UUID
            String fileName = UUID.randomUUID().toString();

            // Create path save file (/image_uploads/fileName)
            Path path = Paths.get(UPLOAD_FOLDER + File.separator + fileName);

            // Save file
            Files.copy(file.getInputStream(), path);

            return File.separator + UPLOAD_FOLDER + File.separator + fileName; // /image_uploads/fileName
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can not upload file");
        }
    }

    public static void deleteFile(String file){
        if (file.isEmpty()) {
            throw new BadRequestException("File not found");
        }
        String fileName = file.substring(15);
        Path path = Paths.get(UPLOAD_FOLDER + File.separator + fileName);
        try{
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new ResourceNotFoundException("Can not delete this file");
        }
    }
}
