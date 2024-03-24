package com.movie.moviespringboot.service;

import com.movie.moviespringboot.exception.BadRequestException;
import com.movie.moviespringboot.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class VideoService {
    public static final String UPLOAD_FOLDER = "video_uploads";
    public static final long CHUNK_SIZE = 100000L;

    public VideoService() {
        FileUtils.createDirectory(UPLOAD_FOLDER);
    }

    public ResourceRegion getVideoResourceRegion(String fileName, long start, long end) throws IOException {
        UrlResource videoResource = new UrlResource("file:" + UPLOAD_FOLDER + File.separator + fileName);

        if (!videoResource.exists() || !videoResource.isReadable()) {
            throw new IOException("Video not found");
        }

        Resource video = videoResource;
        long contentLength = video.contentLength();
        end = Math.min(end, contentLength - 1);

        long rangeLength = Math.min(CHUNK_SIZE, end - start + 1);
        return new ResourceRegion(video, start, rangeLength);
    }

    // Sử dụng cơ chế bất đồng bộ để tải lên video (Async)
    public String uploadVideo(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Can not upload empty file");
        }

        // TODO: Về bổ sung logic
        // Validate file size, file type, file extension, ...
        // Nén video
        // Trích xuất thông tin video: duration, format, resolution, ...

        try {
            // Tạo tên tệp duy nhất với UUID
            String fileName = UUID.randomUUID().toString();

            // Tạo đường dẫn lưu tệp (/video_uploads/fileName)
            Path path = Paths.get(UPLOAD_FOLDER + File.separator + fileName);

            // Lưu tệp
            Files.copy(file.getInputStream(), path);

            return File.separator + "api" + File.separator + "videos" + File.separator + fileName; // path = /api/videos/21341313
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can not upload file");
        }
    }

    public static void deleteVideo(String file) {
        if (file.isEmpty()) {
            throw new BadRequestException("File not found");
        }
        String fileName = file.substring(12);
        Path path = Paths.get(UPLOAD_FOLDER + File.separator + fileName);
        try{
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete this file");
        }
    }
}
