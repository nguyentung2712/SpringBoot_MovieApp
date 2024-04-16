package com.movie.moviespringboot.service;

import com.github.slugify.Slugify;
import com.movie.moviespringboot.entity.Blog;
import com.movie.moviespringboot.entity.User;
import com.movie.moviespringboot.exception.BadRequestException;
import com.movie.moviespringboot.exception.ResourceNotFoundException;
import com.movie.moviespringboot.repository.BlogRepository;
import com.movie.moviespringboot.model.request.UpsertBlogRequest;
import com.movie.moviespringboot.utils.StringUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {
    @Autowired
    private final BlogRepository blogRepository;
    @Autowired
    private final HttpSession httpSession;
    @Autowired
    private final Slugify slugify;

    // Get all blog sort by createdAt decrease
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll(Sort.by("createdAt").descending());
    }

    // Get all blog of user is login, sort by createdAt decrease
    public List<Blog> getAllBlogOfCurrentUser() {
        User user = (User) httpSession.getAttribute("currentUser");
        return blogRepository.findByUser_Id(user.getId(), Sort.by("createdAt").descending());
    }

    // Get blog by id
    public Blog getBlogById(Integer id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));
    }

    // Create blog
    public Blog createBlog(UpsertBlogRequest request) {
        User user = (User) httpSession.getAttribute("currentUser");

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
        User user = (User) httpSession.getAttribute("currentUser");

        // Check condition: blog want to update is existed
        Blog blog = getBlogById(id);

        // Check condition: only the blog's owner can update
        if (!blog.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("You are not the owner of this blog");
        }

        // Check condition: if blog's thumbnail still use the default thumbnail => change thumbnail while change the title
        if (blog.getThumbnail().equals(StringUtils.generateLinkImage(blog.getTitle())) || blog.getThumbnail().isEmpty()) {
            blog.setThumbnail(StringUtils.generateLinkImage(request.getTitle()));
        }

        // Update blog
        blog.setTitle(request.getTitle());
        blog.setDescription(request.getDescription());
        blog.setContent(request.getContent());
        blog.setStatus(request.getStatus());

        return blogRepository.save(blog);
    }

    // Delete blog
    public void deleteBlog(Integer id) {
        User user = (User) httpSession.getAttribute("currentUser");

        // Check condition: blog want to delete is existed
        Blog blog = getBlogById(id);

        // Check condition: only the blog's owner can delete
        if (!blog.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("You are not the owner of this blog");
        }

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
    public String uploadThumbnail(Integer id, MultipartFile file) {
        User user = (User) httpSession.getAttribute("currentUser");

        // Check condition: blog want to upload thumbnail is existed
        Blog blog = getBlogById(id);

        // Check condition: only the blog's owner can upload thumbnail
        if (!blog.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("You are not the owner of this blog");
        }

        // Upload file into server folder (image_uploads)
        String filePath = FileService.uploadFile(file);

        // Update path thumbnail for blog
        blog.setThumbnail(filePath);
        blogRepository.save(blog);

        return filePath;
    }

    // Delete thumbnail
    public void deleteThumbnail(Integer id) {
        User user = (User) httpSession.getAttribute("currentUser");

        // Check condition: blog want to delete thumbnail is existed
        Blog blog = getBlogById(id);

        // Check condition: only the blog's owner can delete thumbnail
        if (!blog.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("You are not the owner of this blog");
        }

        // Check condition: default blog's thumbnail can't be deleted
        if (blog.getThumbnail().equals(StringUtils.generateLinkImage(blog.getTitle()))) {
            throw new BadRequestException("Can not delete default blog's thumbnail");
        }

        FileService.deleteFile(blog.getThumbnail());
        blog.setThumbnail(StringUtils.generateLinkImage(blog.getTitle()));
        blogRepository.save(blog);
    }
}
