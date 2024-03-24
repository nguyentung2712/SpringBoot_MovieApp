package com.movie.moviespringboot.service;

import com.movie.moviespringboot.entity.Genre;
import com.movie.moviespringboot.exception.ResourceNotFoundException;
import com.movie.moviespringboot.model.request.UpsertGenreRequest;
import com.movie.moviespringboot.repository.GenreRepository;
import com.movie.moviespringboot.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;

    // Get all genres - Controller
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    // Get genre by id
    public Genre getGenreById(Integer id) {
        return genreRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Can not find genre has id is: "+id));
    }

    // Create Genre - GenreResource
    public Genre createGenre(UpsertGenreRequest request) {
        if(genreRepository.findAll().stream().noneMatch(genre -> genre.getName().equals(request.getName()))) {
            // Create genre
            Genre genre = Genre.builder()
                    .name(request.getName())
                    .build();

            return genreRepository.save(genre);
        }
        else {
            throw new RuntimeException("This genre already existed");
        }
    }

    // Update Genre
    public Genre updateGenre(UpsertGenreRequest request, Integer id) {
        // Find genre by id
        Genre genre = getGenreById(id);

        if(genreRepository.findAll().stream().noneMatch(genreMatch -> genreMatch.getName().equals(request.getName()))){
            // Update genre
            genre.setName(request.getName());

            return genreRepository.save(genre);
        }
        else {
            throw new RuntimeException("This genre already existed");
        }
    }

    // Delete Genre
    public void deleteGenre(Integer id) {
        // Find genre by id
        Genre genre = getGenreById(id);

        if(movieRepository.findAll().stream().noneMatch(movie -> movie.getGenres().stream().anyMatch(genreMatch -> genreMatch.equals(genre)))){
            genreRepository.delete(genre);
        }
        else {
            throw new RuntimeException("Can not delete this genre");
        }
    }

    // Get list genres by request - MovieService
    public List<Genre> getListGenresByRequest(List<Integer> genreIds) {
        // Create list genres were chosen by request
        List<Genre> genreList = new ArrayList<>();

        // Get genre id by request using for each
        for (int id : genreIds){
            // each every id will match with 1 id of genre in genres list in database
            genreList.addAll(genreRepository.findAll()
                    .stream()
                    // check condition then add to genres list was create
                    .filter(genre -> genre.getId() == id).toList());
        }

        return genreList;
    }
}
