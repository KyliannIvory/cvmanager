package amu.cvmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record PersonDTO(

        long id,

        @NotBlank(message = "Le prénom est obligatoire")
        String firstName,

        @NotBlank( message = "Le nom est obligatoire")
        String lastName,

        @Email(message = "L'email doit être valide")
        @NotBlank (message = "L'email est obligatoire")
        String email,

        String website,

        @NotBlank(message = "Le mot de passe est obligatoire")
        String password,

        LocalDate birthDate,

        CVDTO cv

) {}
