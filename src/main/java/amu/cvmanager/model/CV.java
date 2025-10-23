package amu.cvmanager.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class CV {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(mappedBy = "cv")
    private Person person;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true)
    @JoinColumn(name = "cv_id", referencedColumnName = "id")
    private List<Activity> activities;
}
