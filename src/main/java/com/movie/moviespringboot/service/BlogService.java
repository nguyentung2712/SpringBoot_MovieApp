package com.movie.moviespringboot.service;

import com.github.slugify.Slugify;
import com.movie.moviespringboot.entity.Blog;
import com.movie.moviespringboot.entity.User;
import com.movie.moviespringboot.exception.ResourceNotFoundException;
import com.movie.moviespringboot.repository.BlogRepository;
import com.movie.moviespringboot.model.request.UpsertBlogRequest;
import com.movie.moviespringboot.utils.StringUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;
    private final HttpSession httpSession;
    private final Slugify slugify;

    // Get all blog sort by createdAt decrease
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll(Sort.by("createdAt").descending());
    }

    // Get all blog of user is login, sort by createdAt decrease
    // Get user in SecurityContextHolder
    // Get blog by userId
    public List<Blog> getAllBlogOfCurrentUser() {
        User user = (User) httpSession.getAttribute("currentUser");
        return blogRepository.findByUser_Id(user.getId(), Sort.by("createdAt").descending());
    }

    // Get blog by id
    public Blog getBlogById(Integer id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Can not find blog has id is: " + id));
    }

    // Create new blog
    public Blog createBlog(UpsertBlogRequest request) {
        User user = (User) httpSession.getAttribute("currentUser");

        // create blog
        Blog blog = Blog.builder()
                .title(request.getTitle())
                .slug(slugify.slugify(request.getTitle()))
                .thumbnail(StringUtils.generateLinkImage(request.getTitle()))
                .content(request.getContent())
                .description(request.getDescription())
                .status(request.getStatus())
                .user(user)
                .build();

        return blogRepository.save(blog);
    }

    // Update blog
    public Blog updateBlog(Integer id, UpsertBlogRequest request) {

        Blog blog = blogRepository.findById(id) // check blog existed or not
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));


        // Update blog
        blog.setTitle(request.getTitle());
        blog.setDescription(request.getDescription());
        blog.setContent(request.getContent());
        blog.setStatus(request.getStatus());

        return blogRepository.save(blog);
    }

    // Delete blog
    public void deleteBlog(Integer id) {

        Blog blog = blogRepository.findById(id) // check blog existed or not
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));

        // Check 2 conditions:
        // Blog has thumbnail
        // Blog's thumbnail isn't equal with "generateLinkImage(blog.getTitle())"
        // => delete image thumbnail in "image_uploads" file
        if (blog.getThumbnail() != null && !blog.getThumbnail().equals(StringUtils.generateLinkImage(blog.getThumbnail())) ) {
            FileService.deleteFile(blog.getThumbnail());
        }

        // Delete Blog
        blogRepository.delete(blog);

    }

    // Upload thumbnail
    // C1: Upload file into database
    // C2: Upload file into server (image_uploads)
    public String uploadThumbnail(Integer id, MultipartFile file) {
        // Check blog is existed or not
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Can not find blog has id is: " + id));

        // Upload file into server folder (image_uploads)
        String filePath = FileService.uploadFile(file);

        // Update path thumbnail for blog
        blog.setThumbnail(filePath);
        blogRepository.save(blog);

        return filePath;
    }
}
