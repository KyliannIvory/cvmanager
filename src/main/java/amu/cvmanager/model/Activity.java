package amu.cvmanager.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name="activity_year", nullable = false)
    private int year;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActivityType type;

    @Column(nullable = false)
    private String title;

    private String description;

    private String webAddress;

    @ManyToOne
    @JoinColumn(name = "cv_id")
    private CV cv;

    public Activity(int year, ActivityType type, String title, String description, String webAddress, CV cv) {
        this.year = year;
        this.type = type;
        this.title = title;
        this.description = description;
        this.webAddress = webAddress;
        this.cv = cv;
    }

    
}
