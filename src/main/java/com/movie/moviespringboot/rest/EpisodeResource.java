package com.movie.moviespringboot.rest;

import com.movie.moviespringboot.entity.Episode;
import com.movie.moviespringboot.entity.Movie;
import com.movie.moviespringboot.service.EpisodeService;
import com.movie.moviespringboot.service.MovieService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/episodes")
@RequiredArgsConstructor
public class EpisodeResource {
    @Autowired
    private final EpisodeService episodeService;

    // upload video - POST
    @PostMapping("/{id}/upload-video")
    public ResponseEntity uploadVideo(@RequestParam("video") MultipartFile file, @PathVariable Integer id) {
        episodeService.uploadVideo(file,id);
        return ResponseEntity.ok().build(); // status code 200
    }

    // delete video - DELETE
    @DeleteMapping("/{id}/delete-video")
    public ResponseEntity deleteVideo(@PathVariable Integer id) {
        episodeService.deleteVideo(id);
        return ResponseEntity.noContent().build(); // status code 204
    }

    // create episode for SeriesMovie - POST
    @PostMapping("{id}/create-episode-series-movie")
    public ResponseEntity createEpisodeSeriesMovie(@PathVariable Integer id) {
        Episode episode = episodeService.createEpisodeSeriesMovie(id);
        return ResponseEntity.noContent().build(); // status code 200
    }

    // delete episode - DELETE
    @DeleteMapping("{id}/delete-episode")
    public ResponseEntity deleteEpisode(@PathVariable Integer id) {
        episodeService.deleteEpisode(id);
        return ResponseEntity.noContent().build(); // status code 204
    }
}