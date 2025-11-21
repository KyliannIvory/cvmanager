package amu.cvmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonDTO {

        private long id;

        @NotBlank(message = "Le prénom est obligatoire")
        private String firstName;

        @NotBlank( message = "Le nom est obligatoire")
        private String lastName;

        @Email(message = "L'email doit être valide")
        @NotBlank (message = "L'email est obligatoire")
        private String email;

        private String website;

        @NotBlank(message = "Le mot de passe est obligatoire")
        private String password;

        private LocalDate birthDate;

        private CVDTO cv; // CVDTO doit également être converti
}