package amu.cvmanager.dto;

import amu.cvmanager.model.ActivityType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityDTO { // Changé de record à class

        private long id;

        @Min(value = 1930 , message = "L'année doit être supérieure à 1930")
        @Max(value = 2025 , message = "L'année doit être inférieure à 2025")
        private int year;

        @NotNull(message = "La nature de l'activité est obligatoire")
        private ActivityType type;

        @NotBlank(message = "Le titre est obligatoire")
        private String title;

        private String description;

        private String webAddress;

}