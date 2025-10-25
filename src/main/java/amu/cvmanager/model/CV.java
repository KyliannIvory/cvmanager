package amu.cvmanager.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class CV {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(mappedBy = "cv", optional = false)
    private Person person;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true)
    @JoinColumn(name = "cv_id", referencedColumnName = "id")
    private List<Activity> activities;

    public CV(long id , List<Activity> activities) {
        this.id = id;
        this.activities = activities;
    }
}
