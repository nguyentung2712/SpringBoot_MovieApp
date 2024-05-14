package com.movie.moviespringboot.controller;

import com.movie.moviespringboot.entity.Actor;
import com.movie.moviespringboot.service.ActorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/actors")
public class ActorController {
    private final ActorService actorService;

    // HomePage all actors
    @GetMapping("/homePage")
    public String getHomePage(Model model){
        List<Actor> actorList = actorService.getAllActors();
        model.addAttribute("actorList",actorList);
        return "admin/actor/index";
    }

    // Create actor
    @GetMapping("/create")
    public String getCreatePage(){
        return "admin/actor/create";
    }

    // Detail actor
    @GetMapping("/{id}/detail")
    public String getDetailPage(@PathVariable Integer id, Model model){
        // Get actor by id
        Actor actor = actorService.getActorById(id);
        model.addAttribute("actor",actor);
        return "admin/actor/detail";
    }
}
