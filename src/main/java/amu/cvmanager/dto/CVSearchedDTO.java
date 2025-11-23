package amu.cvmanager.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CVSearchedDTO {

    private long id;
    private PersonSearchedDTO person;
    private List<ActivityDTO> activities;
}
