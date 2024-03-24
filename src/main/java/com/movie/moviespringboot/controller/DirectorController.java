package com.movie.moviespringboot.controller;

import com.movie.moviespringboot.entity.Director;
import com.movie.moviespringboot.service.DirectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/directors")
public class DirectorController {
    private final DirectorService directorService;

    // Get All Directors
    @GetMapping
    public String getHomePage(Model model){
        List<Director> directorList = directorService.getAllDirectors();
        model.addAttribute("directorList",directorList);
        return "admin/director/index";
    }

    // Create Director
    @GetMapping("/create")
    public String getCreatePage(Model model){
        return "admin/director/create";
    }

    // Director Detail
    @GetMapping("/{id}/detail")
    public String getDetailPage(@PathVariable Integer id,Model model){
        // Get director by id
        Director director = directorService.getDirectorById(id);
        model.addAttribute("director",director);
        return "admin/director/detail";
    }
}
