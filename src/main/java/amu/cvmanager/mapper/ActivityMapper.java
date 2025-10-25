package amu.cvmanager.mapper;

import amu.cvmanager.dto.ActivityDTO;
import amu.cvmanager.model.Activity;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ActivityMapper {


    public ActivityDTO toDTO(Activity activity) {
        return new ActivityDTO(
                activity.getId(),
                activity.getYear(),
                activity.getType(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getWebAddress()
        );
    }

    public Activity fromDTO(ActivityDTO activityDTO) {
        return new Activity(
                activityDTO.id(),
                activityDTO.year(),
                activityDTO.type(),
                activityDTO.title(),
                activityDTO.description(),
                activityDTO.webAddress()
        );
    }
}
