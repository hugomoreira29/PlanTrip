package com.plantrip.api.place;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PlaceRequest(
        @NotBlank @Size(max = 100) String name,
        @Size(max = 150) String description
) {
}
