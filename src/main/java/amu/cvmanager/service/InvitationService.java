package amu.cvmanager.service;

import amu.cvmanager.model.Invitation;
import amu.cvmanager.model.Person;
import amu.cvmanager.repository.InvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class InvitationService {

    @Autowired private InvitationRepository invitationRepository;
    @Autowired private EmailService emailService;
    @Autowired private PersonService personService; // Pour vérifier si l'email existe déjà

    private static final long EXPIRATION_HOURS = 24;

    /**
     * Crée et envoie un jeton d'invitation.
     * @param email L'email de l'invité.
     */
    public void sendInvitation(String email) {
        // 1. Vérification rapide: l'utilisateur existe-t-il déjà?
        if (personService.findPersonByEmail(email).isPresent()) {
            throw new RuntimeException("L'utilisateur avec cet email existe déjà et ne peut être invité.");
        }

        // 2. Création du jeton et de l'expiration
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(EXPIRATION_HOURS);

        Invitation invitation = new Invitation(token, email, expiryDate);

        // 3. Sauvegarde de l'invitation
        invitationRepository.save(invitation);

        // 4. Envoi de l'email (vers MailHog)
        emailService.sendInvitationEmail(email, token);
    }

    /**
     * Valide le jeton et le marque comme utilisé.
     * @param token Jeton de l'invitation.
     * @return L'objet Invitation valide.
     */
    public Invitation validateToken(String token) {
        Invitation invitation = invitationRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Jeton invalide."));

        if (invitation.isUsed()) {
            throw new RuntimeException("Ce jeton a déjà été utilisé.");
        }

        if (invitation.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Ce jeton a expiré.");
        }

        return invitation;
    }

    /**
     * Marque le jeton comme utilisé après l'enregistrement.
     * @param invitation L'invitation à marquer.
     */
    public void markTokenAsUsed(Invitation invitation) {
        invitation.setUsed(true);
        invitationRepository.save(invitation);
    }
}