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

    @OneToOne(mappedBy = "cv")
    private Person person;

    @OneToMany(mappedBy = "cv",  cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Activity> activities;

    public CV( List<Activity> activities) {
        this.activities = activities;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
        activity.setCv(this);
    }

    public void removeActivity(Activity activity) {
        activities.remove(activity);
        activity.setCv(null);
    }

    public void assignPerson(Person person) {
        this.person = person;
        if( person.getCv() != this )
            person.setCv(this);
    }
}
