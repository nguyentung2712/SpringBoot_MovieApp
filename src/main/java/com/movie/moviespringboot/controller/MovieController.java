package com.movie.moviespringboot.controller;

import com.movie.moviespringboot.entity.*;
import com.movie.moviespringboot.service.*;
import com.movie.moviespringboot.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/movies")
@RequiredArgsConstructor
public class MovieController {
    @Autowired
    private final MovieService movieService;
    @Autowired
    private final GenreService genreService;
    @Autowired
    private final DirectorService directorService;
    @Autowired
    private final ActorService actorService;
    @Autowired
    private final EpisodeService episodeService;

    // Get All Movies
    @GetMapping("/homePage")
    public String getHomePage(Model model){
        List<Movie> movieList = movieService.getAllMovies();
        model.addAttribute("movieList",movieList);
        return "admin/movie/index";
    }

    // Create Movie
    @GetMapping("/create")
    public String getCreatePage(Model model) {
        List<Genre> genreList = genreService.getAllGenres();
        List<Director> directorList = directorService.getAllDirectors();
        List<Actor> actorList = actorService.getAllActors();

        model.addAttribute("genreList",genreList);
        model.addAttribute("directorList",directorList);
        model.addAttribute("actorList",actorList);

        return "admin/movie/create";
    }

    // Movie Detail
    @GetMapping("/{id}/detail")
    public String getDetailPage(@PathVariable Integer id, Model model) {
        // Get movie by id
        Movie movie = movieService.getMovieById(id);
        List<Genre> genreList = genreService.getAllGenres();
        List<Director> directorList = directorService.getAllDirectors();
        List<Actor> actorList = actorService.getAllActors();
        List<Episode> episodeList = episodeService.getEpisodeListOfMovie(id);

        model.addAttribute("movie",movie);
        model.addAttribute("genreList",genreList);
        model.addAttribute("directorList",directorList);
        model.addAttribute("actorList",actorList);
        model.addAttribute("episodeList", episodeList);
        return "admin/movie/detail";
    }
}
