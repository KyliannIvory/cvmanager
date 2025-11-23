package amu.cvmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonDTOv2 {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String website;
    // remettre String s'il y a un problème
    private LocalDate birthDate;
}
