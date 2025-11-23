package amu.cvmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonSearchedDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String website;
    private String birthDate;
}
