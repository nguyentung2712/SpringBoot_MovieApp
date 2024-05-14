package com.movie.moviespringboot.controller;

import com.movie.moviespringboot.entity.User;
import com.movie.moviespringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/users")
public class UserController {
    @Autowired
    private final UserService userService;

    // Get All Users
    @GetMapping("/homePage")
    public String getHomePage(Model model){
        List<User> userList = userService.getAllUser();
        model.addAttribute("userList", userList);
        return "admin/user/index";
    }

    // Create user
    @GetMapping("/create")
    public String getCreatePage(){
        return "admin/user/create";
    }

    // Get User Detail
    @GetMapping("/{id}/detail")
    public String getDetailPage(@PathVariable Integer id, Model model){
        // Get user by id
        User user = userService.getUserById(id);
        model.addAttribute("user",user);
        return "admin/user/detail";
    }
}
