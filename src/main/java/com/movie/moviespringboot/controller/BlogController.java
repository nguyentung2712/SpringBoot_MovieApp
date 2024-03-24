package com.movie.moviespringboot .controller;

import com.movie.moviespringboot.entity.Blog;
import com.movie.moviespringboot.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/blogs")
public class BlogController {
    private final BlogService blogService;

    // Get all blogs
    @GetMapping
    public String getHomePage(Model model) {
        // Get all blog sort by createdAt decrease
        List<Blog> blogList = blogService.getAllBlogs();
        model.addAttribute("blogList", blogList);
        return "admin/blog/index";
    }

    // Get owner blogs by userId
    @GetMapping("/own-blogs")
    public String getOwnPage(Model model) {
        List<Blog> blogList = blogService.getAllBlogOfCurrentUser();
        model.addAttribute("blogList", blogList);
        return "admin/blog/own-blog";
    }

    // Create blog
    @GetMapping("/create")
    public String getCreatePage(Model model) {
        return "admin/blog/create";
    }

    // Blog detail
    @GetMapping("/{id}/detail")
    public String getDetailPage(@PathVariable Integer id, Model model) {
        // Get blog by id
        Blog blog = blogService.getBlogById(id);
        model.addAttribute("blog", blog);
        return "admin/blog/detail";
    }
}
