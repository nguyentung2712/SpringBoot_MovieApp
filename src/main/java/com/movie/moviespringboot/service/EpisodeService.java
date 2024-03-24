package com.movie.moviespringboot.service;

import com.movie.moviespringboot.entity.Episode;
import com.movie.moviespringboot.entity.Movie;
import com.movie.moviespringboot.exception.ResourceNotFoundException;
import com.movie.moviespringboot.model.enums.MovieType;
import com.movie.moviespringboot.repository.EpisodeRepository;
import com.movie.moviespringboot.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class EpisodeService {
    private final EpisodeRepository episodeRepository;
    private final VideoService videoService;
    private final MovieRepository movieRepository;

    // Get episode list of movie by id sort by displayOrder increase
    public List<Episode> getEpisodeListOfMovie(Integer movieId) {
        return episodeRepository.findByMovie_IdOrderByDisplayOrderAsc(movieId);
    }

    // Upload video - EpisodeResource - Upload video
    public void uploadVideo(MultipartFile file, Integer id){

        // Check video upload is existed or not
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Can not find episode has id is: "+id));

        // Upload video
        String videoUrl = videoService.uploadVideo(file);

        episode.setVideoURL(videoUrl);
        episodeRepository.save(episode);
    }

    // Get episode of movie and has status is true
    public List<Episode> getEpisodeOfMovie(Integer movieId, boolean status) {
        return episodeRepository.findByMovie_IdAndStatusOrderByDisplayOrderAsc(movieId,status);
    }

    // Get episode of movie has tap and status is true
    public Episode getEpisode(Integer movieId, String tap, boolean status) {
        // if movie's episode equals full => return movie only 1 episode
        if (tap.equals("full")) {
            return episodeRepository.findByMovie_IdAndDisplayOrderAndStatus(movieId,1,status).orElse(null);
        }
        // if movie's episode unequals full => return movie episode
        else {
            return episodeRepository.findByMovie_IdAndDisplayOrderAndStatus(movieId,Integer.parseInt(tap),status).orElse(null);
        }
    }

    // Create episode for SingleMovie, TheaterMovie and first episode of SeriesMovie
    public void createFirstEpisode(Integer movieId) {

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Can not find movie has id is "+movieId));

        // If movie have type is SingleMovie or TheaterMovie already has episode will not create episode
        if( episodeRepository.findAll().stream().noneMatch(episode -> Objects.equals(episode.getMovie().getId(), movie.getId())) &&
                (movie.getType().equals(MovieType.PHIM_LE) || movie.getType().equals(MovieType.PHIM_CHIEU_RAP)) ) {
            Episode episode = Episode.builder()
                    .name("Episode full")
                    .displayOrder(1)
                    .status(true)
                    .createdAt(new Date())
                    .updatedAt(new Date())
                    .publishedAt(new Date())
                    .movie(movie)
                    .build();
            episodeRepository.save(episode);
        }

        // If movie have type is SeriesMovie do not have any episode will create episode and start at "Episode 1"
        else if( episodeRepository.findAll().stream().noneMatch(episode -> Objects.equals(episode.getMovie().getId(), movie.getId())) &&
                movie.getType().equals(MovieType.PHIM_BO) ){
            Episode episode = Episode.builder()
                    .name("Episode "+1)
                    .displayOrder(1)
                    .status(true)
                    .createdAt(new Date())
                    .updatedAt(new Date())
                    .publishedAt(new Date())
                    .movie(movie)
                    .build();
            episodeRepository.save(episode);
        }

        // If user can not create episode throw message exception
        else {
            throw new RuntimeException("Can not create episode");
        }
    }

    // Create episode starting from the 2nd episode for SeriesMovie
    public Episode createEpisodeSeriesMovie(Integer id) {
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Can not find episode has id is: "+id));

        // If movie have type is Series already has episode will create next episode is "Episode + (i + 1)"
        if(episode.getMovie().getType().equals(MovieType.PHIM_BO)){

            int indexEpisodeMostRecent = episode.getDisplayOrder();

            Episode episodeNewCreate = Episode.builder()
                    .name("Episode "+(indexEpisodeMostRecent))
                    .displayOrder(indexEpisodeMostRecent)
                    .status(true)
                    .createdAt(new Date())
                    .updatedAt(new Date())
                    .publishedAt(new Date())
                    .movie(episode.getMovie())
                    .build();
            episodeRepository.save(episodeNewCreate);

            List<Episode> episodeList = episodeRepository.findByMovie_IdOrderByDisplayOrderAsc(episode.getMovie().getId());
            int numberOfEpisode = episodeList.size();

            for (int i =  episodeNewCreate.getDisplayOrder(); i < numberOfEpisode; i++) {
                episodeList.get(i).setName("Episode "+(i+1));
                episodeList.get(i).setDisplayOrder(i+1);
                episodeRepository.save(episodeList.get(i));
            }

            return episodeNewCreate;
        }

        // If user can not create episode throw message exception
        else {
            throw new RuntimeException("Can not create episode");
        }
    }

    // Delete episode
    public void deleteEpisode(Integer id) {
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Can not find episode has id is: "+id));

        if (episode.getMovie().getType().equals(MovieType.PHIM_BO)){

            List<Episode> episodeList = episodeRepository.findByMovie_IdOrderByDisplayOrderAsc(episode.getMovie().getId());
            int numberOfEpisode = episodeList.size();
            int indexEpisodeDelete = episode.getDisplayOrder();

            if(episode.getVideoURL() != null){
                deleteVideo(id);
            }
            episodeRepository.delete(episode);

            for (int i = indexEpisodeDelete; i < numberOfEpisode; i++) {
                episodeList.get(i).setName("Episode " + (i));
                episodeList.get(i).setDisplayOrder(i);
                episodeRepository.save(episodeList.get(i));
            }
        }
        else {
            throw new RuntimeException("Can not delete episode");
        }
    }

    // Delete video
    public void deleteVideo(Integer id) {
        // Check video is existed or not
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Can not find episode has is is: "+id));

        // Delete file video in video_uploads
        VideoService.deleteVideo(episode.getVideoURL());

        // Set episode videoURL to null
        episode.setVideoURL(null);

        episodeRepository.save(episode);
    }
}
