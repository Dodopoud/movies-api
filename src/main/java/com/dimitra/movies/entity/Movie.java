package com.dimitra.movies.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name = "movie")

public class Movie {
    @Id 
    @GeneratedValue (strategy=GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String genre;
    private double rating;
    private int releaseYear;

    protected Movie(){} //τον χρειάζεται κενό το hibernate

    public Movie(String title, String genre, double rating, int releaseYear){
        this.title = title;
        this.genre = genre; 
        this.rating = rating;
        this.releaseYear = releaseYear;
    }

    public Long getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getGenre(){
        return genre;
    }

    public double getRating(){
        return rating;
    }

    public int getReleaseYear(){
        return releaseYear;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setGenre(String genre){
        this.genre = genre;
    }

    public void setRating(double rating){
        this.rating = rating;
    }

    public void setReleaseYear(int releaseYear){
        this.releaseYear = releaseYear;
    }
}
