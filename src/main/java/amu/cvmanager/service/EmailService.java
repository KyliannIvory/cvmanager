package amu.cvmanager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${frontend.base-url}")
    private String frontendBaseUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Construit et envoie l'email d'invitation.
     * @param recipientEmail L'adresse de l'invité.
     * @param token Le jeton unique pour l'enregistrement.
     */
    public void sendInvitationEmail(String recipientEmail, String token) {
        // Lien d'enregistrement complet
        String registrationLink = frontendBaseUrl + "/#/register-invited?token=" + token;

        String subject = "Invitation à créer votre CV Manager";
        String text = "Cher invité,\n\n"
                + "Vous avez été invité à créer votre compte CV Manager. "
                + "Veuillez cliquer sur le lien ci-dessous pour vous enregistrer :\n\n"
                + registrationLink + "\n\n"
                + "Ce lien expirera dans 24 heures.\n\n"
                + "Cordialement,\n"
                + "L'équipe CV Manager";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@cvmanager.amu.fr");
        message.setTo(recipientEmail);
        message.setSubject(subject);
        message.setText(text);

        try {
            mailSender.send(message);
            System.out.println("📧 E-mail d'invitation envoyé à : " + recipientEmail);
        } catch (Exception e) {
            System.err.println("❌ ERREUR LORS DE L'ENVOI DE L'EMAIL (MailHog) : " + e.getMessage());
            // Nous affichons l'erreur mais ne bloquons pas l'application pour MailHog
        }
    }
}