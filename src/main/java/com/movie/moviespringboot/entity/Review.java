package com.movie.moviespringboot.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increase id
    Integer id;

    @Column(columnDefinition = "TEXT")
    String content;

    Integer rating;

    @Transient // not create new column in database but still become a value in REVIEW
    String ratingText;

    Date createdAt;
    Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public String getRatingText() {
        if(rating == null) {
            return "Don't have rating";
        }
        return switch (rating){
            case 1 -> "I don't wanna see this film";
            case 2 -> "This film is so bad";
            case 3 -> "Bad";
            case 4 -> "Not as my expected";
            case 5 -> "Average";
            case 6 -> "Just OK";
            case 7 -> "Good";
            case 8 -> "Great";
            case 9 -> "Awesome";
            case 10 -> "This film is the shit";
            default -> "Don't have rating";
        };
    }

    // before save to database
    @PrePersist
    public void prePersist() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    // before update to database
    @PreUpdate
    public void preUpdate() {
        updatedAt = new Date();
    }
}
