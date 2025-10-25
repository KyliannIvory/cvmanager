package amu.cvmanager.dto;


import java.util.List;

public record CVDTO(

        long id,
        List<ActivityDTO> activities

) { }
