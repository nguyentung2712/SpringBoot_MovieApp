package com.movie.moviespringboot.repository;

import com.movie.moviespringboot.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog,Integer> {

    // Find Page Blog by status and publishedAt DESC
    Page<Blog> findByStatus(boolean status, Pageable pageable);

    // Find Blog Detail by id and slug and status
    Blog findBlogByIdAndSlugAndStatus(int id, String slug, boolean status);

    // Find Blog List by id and sort by createdAt
    List<Blog> findByUser_Id(Integer id, Sort createdAt);
}
