package com.movie.moviespringboot.controller;

import com.movie.moviespringboot.entity.Genre;
import com.movie.moviespringboot.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/genres")
public class GenreController {
    private final GenreService genreService;

    // Get All Genres
    @GetMapping("/homePage")
    public String getHomePage(Model model){
        List<Genre> genreList = genreService.getAllGenres();
        model.addAttribute("genreList",genreList);
        return "admin/genre/index";
    }

    // Create Genre
    @GetMapping("/create")
    public String getCreatePage(){
        return "admin/genre/create";
    }

    // Genre Detail
    @GetMapping("/{id}/detail")
    public String getDetailPage(@PathVariable Integer id, Model model){
        // Get genre by id
        Genre genre = genreService.getGenreById(id);
        model.addAttribute("genre",genre);
        return "admin/genre/detail";
    }
}
