package amu.cvmanager.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer";

    // Informations de l'utilisateur connecté
    private Long id;
    private String email;
    private String firstName;
    private String lastName;

    public LoginResponseDTO(String accessToken, Long id, String email, String firstName, String lastName) {
        this.accessToken = accessToken;
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}