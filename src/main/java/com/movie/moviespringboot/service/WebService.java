package com.movie.moviespringboot.service;

import com.movie.moviespringboot.entity.Blog;
import com.movie.moviespringboot.entity.Movie;
import com.movie.moviespringboot.model.enums.MovieType;
import com.movie.moviespringboot.repository.BlogRepository;
import com.movie.moviespringboot.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WebService {
    private final MovieRepository movieRepository;
    private final BlogRepository blogRepository;

    // Get movie by id, slug and status
    public Movie getMovie(Integer id, String slug, Boolean status) {
        return movieRepository.findMovieByIdAndSlugAndStatus(id,slug,status);
    }

    // Get page of movie by type, status, sort by publishedAt DESC
    public Page<Movie> getMoviesByType(MovieType movieType, Boolean status, Integer page, Integer size) {
        // In JPA start with 0 => page in put PageRequest = page - 1
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("publishedAt").descending());
        return movieRepository.findByTypeAndStatus(movieType,status,pageRequest);
    }

    // Get page of movie by status, sort by view DESC
    public Page<Movie> getHotMovies(Boolean status,Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page - 1,size,Sort.by("view").descending());
        return movieRepository.findByStatus(status,pageRequest);
    }

    // Get blog by status and publishedAt DESC
    public Page<Blog> getBlogByStatus(Boolean status, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("publishedAt").descending());
        return blogRepository.findByStatus(status,pageRequest);
    }

    // Get blog by id, slug, status
    public Blog getBlog(Integer id, String slug, boolean status) {
        return blogRepository.findBlogByIdAndSlugAndStatus(id,slug,status);
    }
}