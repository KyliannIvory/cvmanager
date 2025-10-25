package amu.cvmanager.dto;

import amu.cvmanager.model.ActivityType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ActivityDTO(

        long id,

        @Min(value = 1930 , message = "L'année doit être supérieure à 1930")
        @Max(value = 2025 , message = "L'année doit être inférieure à 2025")
        int year,

        @NotNull(message = "La nature de l'activité est obligatoire")
        ActivityType type,

        @NotBlank(message = "Le titre est obligatoire")
        String title,

        String description,

        String webAddress

) {}
