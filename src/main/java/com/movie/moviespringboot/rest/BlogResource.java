package com.movie.moviespringboot.rest;

import com.movie.moviespringboot.entity.Blog;
import com.movie.moviespringboot.model.request.UpsertBlogRequest;
import com.movie.moviespringboot.service.BlogService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/blogs")
@RequiredArgsConstructor
public class BlogResource {
    @Autowired
    private final BlogService blogService;

    // create blog - POST
    @PostMapping("/create-blog")
    public ResponseEntity createBlog(@RequestBody UpsertBlogRequest request) {
        Blog blog = blogService.createBlog(request);
        return new ResponseEntity(blog, HttpStatus.CREATED); // status code 201
    }

    // update blog - PUT
    @PutMapping("/{id}/update-blog")
    public ResponseEntity updateBlog(@RequestBody UpsertBlogRequest request, @PathVariable Integer id) {
        Blog blog = blogService.updateBlog(id, request);
        return ResponseEntity.ok(blog); // status code 200
    }

    // delete blog - DELETE
    @DeleteMapping("/{id}/delete-blog")
    public ResponseEntity deleteBlog(@PathVariable Integer id) {
        blogService.deleteBlog(id);
        return ResponseEntity.noContent().build(); // status code 204
    }

    // upload thumbnail - POST
    @PostMapping("/{id}/upload-thumbnail")
    public ResponseEntity uploadThumbnail(@RequestParam("file") MultipartFile file, @PathVariable Integer id) {
        String filePath = blogService.uploadThumbnail(id, file);
        return ResponseEntity.ok(filePath); // status code 200
    }

    // delete thumbnail - DELETE
    @DeleteMapping("/{id}/delete-thumbnail")
    public ResponseEntity deleteThumbnail(@PathVariable Integer id) {
        blogService.deleteThumbnail(id);
        return ResponseEntity.noContent().build(); // status code 204
    }
}
