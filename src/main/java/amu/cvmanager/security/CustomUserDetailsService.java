package amu.cvmanager.security;

import amu.cvmanager.model.Person;
import amu.cvmanager.repository.PersonRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    public CustomUserDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Person not found with email: " + email));

        // 2. Retourner un objet UserDetails standard
        // Pour ce projet, nous n'avons qu'un seul rôle implicite: USER/AUTHENTICATED
        return new org.springframework.security.core.userdetails.User(
                person.getEmail(),
                person.getPassword(),
                Collections.emptyList() // Pas de rôles spécifiques pour l'instant (peut être étendu si nécessaire)
        );
    }
}