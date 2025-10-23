package amu.cvmanager.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Basic
    private int year;

    @Enumerated(EnumType.STRING)
    private ActivityType type;

    @Basic
    private String title;

    @Basic
    private String description;

    @Basic
    private String webAddress;

}
