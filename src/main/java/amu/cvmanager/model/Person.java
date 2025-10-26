package amu.cvmanager.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.rest.core.annotation.RestResource;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false,  unique =true)
    private String email;

    private String website;

    @Column(nullable = false)
    private String password;

    @Column(name ="birth_date")
    private LocalDate birthDate;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "cv_id")
    private CV cv;

    public Person(String lastName, String firstName, String email, String website, String password, LocalDate birthDate) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.website = website;
        this.password = password;
        this.birthDate = birthDate;
    }
}
