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
public class CVDTOv2 {

    private long id;
    private PersonDTOv2 person;
    private List<ActivityDTO> activities;
}
