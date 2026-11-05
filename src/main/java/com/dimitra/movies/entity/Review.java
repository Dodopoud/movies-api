package com.dimitra.movies.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

@Entity 
@Table(name="review")

public class Review {
    @Id 
    @GeneratedValue (strategy=GenerationType.IDENTITY)
    private Long id;
    private int rating;
    private String comment;

    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @ManyToOne 
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    protected Review(){}

    public Review(int rating, String comment, Movie movie){
        this.rating = rating;
        this.comment = comment;
        this.movie = movie;
    }

    public Long getId(){
        return id;
    }

    public int getRating(){
        return rating;
    }

    public String getComment(){
        return comment;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public Movie getMovie(){
        return movie;
    }

    public void setRating(int rating){
        this.rating = rating;
    }

    public void setComment(String comment){
        this.comment = comment;
    }
}


