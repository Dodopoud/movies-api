package com.dimitra.movies.dto;

import jakarta.validation.constraints.*;

public record ReviewRequest(@NotNull @Min(0) @Max(10) Integer rating,
                            @Size(max = 1000) String comment) {
}
