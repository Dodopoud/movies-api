package com.dimitra.movies.exception;

public class ReviewNotFoundException extends RuntimeException{
    public ReviewNotFoundException(Long id){
        super("Review with id " + id + " not found");
    }
    
}
