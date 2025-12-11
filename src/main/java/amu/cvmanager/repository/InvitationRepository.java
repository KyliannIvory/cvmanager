package amu.cvmanager.repository;

import amu.cvmanager.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    /**
     * Recherche une invitation par son jeton unique.
     */
    Optional<Invitation> findByToken(String token);
}