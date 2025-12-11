package amu.cvmanager.service;

import amu.cvmanager.exception.CVNotFoundException;
import amu.cvmanager.model.Activity;
import amu.cvmanager.model.CV;
import amu.cvmanager.repository.CVRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CVService {

    private final CVRepository cvRepository;


    public CVService(CVRepository cvRepository) {
        this.cvRepository = cvRepository;
    }

    // Méthode utilitaire pour trouver un CV
    public CV findCVById(long id){
        return cvRepository.findById(id)
                .orElseThrow(() -> new CVNotFoundException("CV non trouvé"));
    }

    /**
     * Vérifie si l'utilisateur connecté est le propriétaire du CV ciblé.
     * Lance une AccessDeniedException si ce n'est pas le cas.
     */
    public void checkOwnership(long cvId) {
        // 1. Récupérer l'email de l'utilisateur connecté (du JWT)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String connectedUserEmail = authentication.getName(); // Contient l'email du token

        // 2. Récupérer le CV cible et son propriétaire (avec EAGER loading si possible)
        CV cv = findCVById(cvId);
        String ownerEmail = cv.getPerson().getEmail();
        // NOTE: Ceci nécessite que la relation CV.person soit chargée (FetchType.EAGER) ou que la session soit ouverte.

        // 3. Comparaison
        if (!connectedUserEmail.equals(ownerEmail)) {
            // Lève une exception de sécurité (403 Forbidden)
            throw new AccessDeniedException("Accès refusé. Vous n'êtes pas le propriétaire de ce CV.");
        }
    }


    public List<CV> findAllCVs() {
        return cvRepository.findAll();
    }


    public CV createCV(CV cv){
        // NOTE: La création ne devrait pas vérifier l'ownership, mais l'assigner
        // L'assignation de la personne connectée devrait avoir lieu dans le contrôleur ou une méthode dédiée.
        return cvRepository.save(cv);
    }


    public CV updateCV(long id , CV cv){
        // ⬅️ VÉRIFICATION DE SÉCURITÉ
        checkOwnership(id);

        CV foundedCV = findCVById(id);

        // Mise à jour de la personne (ne devrait pas être changée ici)
        // foundedCV.setPerson(cv.getPerson()); // Retiré car on ne change pas le propriétaire

        // Mise à jour des activités
        foundedCV.getActivities().clear();
        for(Activity activity : cv.getActivities()){
            // La méthode addActivity doit gérer l'assignation de CV à l'activité
            foundedCV.addActivity(activity);
        }

        return cvRepository.save(foundedCV);
    }

    public void deleteCVById(long id){
        // ⬅️ VÉRIFICATION DE SÉCURITÉ
        checkOwnership(id);

        CV foundedCV = findCVById(id);
        cvRepository.deleteById(id);
    }


    public List<CV> searchCVs(String searchTerm){
        if (searchTerm == null || searchTerm.trim().isEmpty()){
            return cvRepository.findAll();
        }
        return cvRepository.searchCVs(searchTerm);
    }


}