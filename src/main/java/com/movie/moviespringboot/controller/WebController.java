package com.movie.moviespringboot.controller;

import com.movie.moviespringboot.entity.*;
import com.movie.moviespringboot.model.enums.MovieType;
import com.movie.moviespringboot.service.EpisodeService;
import com.movie.moviespringboot.service.ReviewService;
import com.movie.moviespringboot.service.WebService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final WebService webService;
    private final ReviewService reviewService;
    private final HttpSession httpSession;
    private final EpisodeService episodeService;

    // HOME PAGE
    // http://localhost:8080
    @GetMapping("/")
    public String getHomePage(Model model) {
        Page<Movie> pageDataHotFilm = webService.getHotMovies(true,1,4);
        Page<Movie> pageDataSingleFilm = webService.getMoviesByType(MovieType.PHIM_LE,true,1,6);
        Page<Movie> pageDataSeriesFilm = webService.getMoviesByType(MovieType.PHIM_BO,true,1,6);
        Page<Movie> pageDataTheaterFilm = webService.getMoviesByType(MovieType.PHIM_CHIEU_RAP,true,1,6);

        Page<Blog> pageBlog = webService.getBlogByStatus(true,1,4);

        model.addAttribute("listHotMovies",pageDataHotFilm.getContent());
        model.addAttribute("listSingleMovies",pageDataSingleFilm.getContent());
        model.addAttribute("listSeriesMovies",pageDataSeriesFilm.getContent());
        model.addAttribute("listTheaterMovies",pageDataTheaterFilm.getContent());

        model.addAttribute("listBlogs",pageBlog.getContent());
        return "web/index";
    }

    // SINGLE FILM PAGE
    // http://localhost:8080/singleMovies
    @GetMapping("/singleMovies")
    public String getSingleFilmPage(Model model,
                                    @RequestParam(required = false, defaultValue = "1") Integer page,
                                    @RequestParam(required = false, defaultValue = "12") Integer size) {
        Page<Movie> pageData = webService.getMoviesByType(MovieType.PHIM_LE,true,page,size);
        // show information page
        model.addAttribute("pageData",pageData);
        // add current page to active
        model.addAttribute("currentPage",page);
        return "web/singleMovies";
    }

    // SERIES MOVIE PAGE
    // http://localhost:8080/seriesMovies
    @GetMapping("/seriesMovies")
    public String getSeriesMoviePage(Model model,
                                @RequestParam(required = false, defaultValue = "1") Integer page,
                                @RequestParam(required = false, defaultValue = "12") Integer size) {
        Page<Movie> pageData = webService.getMoviesByType(MovieType.PHIM_BO,true,page,size);
        // show information page
        model.addAttribute("pageData",pageData);
        // add current page to active
        model.addAttribute("currentPage",page);
        return "web/seriesMovies";
    }

    // THEATER MOVIE PAGE
    // http://localhost:8080/theaterMovies
    @GetMapping("/theaterMovies")
    public String getTheatersMoviePage(Model model,
                                       @RequestParam(required = false, defaultValue = "1") Integer page,
                                       @RequestParam(required = false, defaultValue = "12") Integer size) {
        Page<Movie> pageData = webService.getMoviesByType(MovieType.PHIM_CHIEU_RAP,true,page,size);
        // show information page
        model.addAttribute("pageData",pageData);
        // add current page to active
        model.addAttribute("currentPage",page);
        return "web/theaterMovies";
    }

    // DETAIL MOVIE PAGE
    // http://localhost:8080/phim/{id}/{slug}
    @GetMapping("/movie/{id}/{slug}")
    public String getMovieDetailPage(Model model, @PathVariable Integer id, @PathVariable String slug) {
        // get movie detail
        Movie movie = webService.getMovie(id, slug, true);
        // get movie release page
        Page<Movie> pageDataReleaseFilm = webService.getMoviesByType(movie.getType(),true,1,6);
        // get list reviews
        List<Review> reviews = reviewService.getReviewOfMovie(id);
        // get list episodes
        List<Episode> episodes = episodeService.getEpisodeOfMovie(id,true);

        model.addAttribute("movie", movie);
        model.addAttribute("listReleaseFilm",pageDataReleaseFilm.getContent());
        model.addAttribute("reviews", reviews);
        model.addAttribute("episodes",episodes);

        return "web/movieDetail";
    }

    // BLOG PAGE
    // http://localhost:8080/blog
    @GetMapping("/blogs")
    public String getBlogPage(Model model,
                              @RequestParam(required = false, defaultValue = "1") Integer page,
                              @RequestParam(required = false,defaultValue = "6") Integer size){
        Page<Blog> pageData = webService.getBlogByStatus(true,page,size);
        // show information page
        model.addAttribute("pageData",pageData);
        // add current page to active
        model.addAttribute("currentPage",page);
        return "web/blogs";
    }

    // BLOG DETAIL PAGE
    // http://localhost:8080/blog/{id}/{slug}
    @GetMapping("/blog/{id}/{slug}")
    public String getBlogDetailPage(Model model, @PathVariable Integer id, @PathVariable String slug) {
        Blog blog = webService.getBlog(id,slug,true);
        model.addAttribute("blog",blog);
        return "web/blogDetail";
    }

    // LOGIN PAGE
    // http://localhost:8080/login
    @GetMapping("/login")
    public String login() {
        // Get user information in session
        User user = (User) httpSession.getAttribute("currentUser");
        // if login then switch location to home page
        if(user != null) {
            return "redirect:/";
        }
        return "web/login";
    }

    // REGISTER
    // http://localhost:8080/register
    @GetMapping("/register")
    public String register() {
        return "web/register";
    }

    // WATCH MOVIE PAGE
    // http://localhost:8080/blog/{id}/{slug}?tap={currentEpisode.tap}
    @GetMapping("/watch-movie/{id}/{slug}")
    public String getWatchMoviePage(Model model, @PathVariable Integer id,
                                    @PathVariable String slug, @RequestParam String tap) {
        // get movie detail
        Movie movie = webService.getMovie(id, slug, true);
        // get movie release page
        Page<Movie> pageDataReleaseFilm = webService.getMoviesByType(movie.getType(),true,1,6);
        // get list reviews
        List<Review> reviews = reviewService.getReviewOfMovie(id);
        // get list episodes
        List<Episode> episodes = episodeService.getEpisodeOfMovie(id,true);
        Episode currentEpisode = episodeService.getEpisode(id,tap,true);

        model.addAttribute("movie", movie);
        model.addAttribute("listReleaseFilm",pageDataReleaseFilm.getContent());
        model.addAttribute("reviews", reviews);
        model.addAttribute("episodes",episodes);
        model.addAttribute("currentEpisode",currentEpisode);

        return "web/watch-movie";
    }
}
