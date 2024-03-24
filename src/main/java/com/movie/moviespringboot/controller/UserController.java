package com.movie.moviespringboot.controller;

import com.movie.moviespringboot.entity.User;
import com.movie.moviespringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/users")
public class UserController {
    private final UserService userService;

    // Get All Users
    @GetMapping
    public String getHomePage(Model model){
        List<User> userList = userService.getAllUser();
        model.addAttribute("userList", userList);
        return "admin/user/index";
    }
}
