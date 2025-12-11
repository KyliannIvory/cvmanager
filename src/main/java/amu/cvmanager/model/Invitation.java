package amu.cvmanager.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Jeton unique envoyé par email (ex: UUID)
    @Column(unique = true, nullable = false)
    private String token;

    // Email de la personne invitée
    @Column(nullable = false)
    private String invitedEmail;

    // Date d'expiration du jeton
    @Column(nullable = false)
    private LocalDateTime expiryDate;

    // Indique si le jeton a été utilisé
    @Column(nullable = false)
    private boolean used = false;

    // Constructeur simplifié pour la création
    public Invitation(String token, String invitedEmail, LocalDateTime expiryDate) {
        this.token = token;
        this.invitedEmail = invitedEmail;
        this.expiryDate = expiryDate;
    }
}