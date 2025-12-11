package amu.cvmanager.controller;

import amu.cvmanager.model.CV;
import amu.cvmanager.model.Person;
import amu.cvmanager.dto.InvitationRequest;
import amu.cvmanager.dto.RegisterInvitedDTO;
import amu.cvmanager.service.InvitationService;
import amu.cvmanager.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import amu.cvmanager.model.Invitation;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/invitations")
public class InvitationController {

    private final InvitationService invitationService;
    private final PersonService personService;

    public InvitationController(InvitationService invitationService, PersonService personService) {
        this.invitationService = invitationService;
        this.personService = personService;
    }

    /**
     * ENDPOINT PROTÉGÉ : Permet à un utilisateur connecté d'envoyer une invitation.
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendInvitation(@RequestBody InvitationRequest request) {
        try {
            invitationService.sendInvitation(request.getEmail());
            return ResponseEntity.ok("Invitation envoyée avec succès à " + request.getEmail());
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * ENDPOINT PUBLIC : Permet à un invité de s'enregistrer avec son jeton.
     */
    @PostMapping("/register-invited")
    public ResponseEntity<String> registerInvitedUser(@RequestBody RegisterInvitedDTO dto) {
        try {
            // 1. Valider le jeton
            Invitation invitation = invitationService.validateToken(dto.getToken());

            // 2. Construire la nouvelle Personne
            Person newPerson = new Person();
            newPerson.setFirstName(dto.getFirstName());
            newPerson.setLastName(dto.getLastName());
            newPerson.setEmail(invitation.getInvitedEmail()); // L'email vient de l'invitation (sécurité)
            newPerson.setPassword(dto.getPassword());
            newPerson.setWebsite(dto.getWebsite());
            newPerson.setBirthDate(dto.getBirthDate());

            // Assigner un CV vide
            newPerson.setCv(new CV(new ArrayList<>()));

            // 3. Créer la personne (le service s'occupera du hachage du mot de passe)
            personService.createPerson(newPerson);

            // 4. Marquer le jeton comme utilisé
            invitationService.markTokenAsUsed(invitation);

            return new ResponseEntity<>("Enregistrement réussi pour " + newPerson.getEmail(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}