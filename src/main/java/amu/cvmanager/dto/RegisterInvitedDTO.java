package amu.cvmanager.dto;

import amu.cvmanager.model.Activity;
import amu.cvmanager.model.CV;
import amu.cvmanager.model.Person;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;

@Data
public class RegisterInvitedDTO {
    // Infos du formulaire
    private String firstName;
    private String lastName;
    private String password;
    private String website;
    private LocalDate birthDate;

    // Jeton d'invitation
    private String token;
}