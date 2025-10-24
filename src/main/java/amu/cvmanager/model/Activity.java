package amu.cvmanager.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull(message = "L'année est obligatoire")
    @Column(nullable = false)
    private Integer year;

    @NotNull(message = "La nature de l'activité est obligatoire")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActivityType type;

    @Basic
    @NotBlank(message = "Le titre est obligatoire")
    @Column(nullable = false)
    private String title;

    private String description;

    private String webAddress;

}
