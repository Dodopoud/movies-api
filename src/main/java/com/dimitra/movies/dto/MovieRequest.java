package com.dimitra.movies.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MovieRequest(@NotBlank @Size (max = 50)String title, 
                           @NotBlank String genre, 
                           @DecimalMin("0.0") @DecimalMax("10.0")double rating, 
                           @Min(1880) int releaseYear) {

}
