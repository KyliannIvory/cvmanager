package amu.cvmanager.mapper;

import amu.cvmanager.dto.ActivityDTO;
import amu.cvmanager.dto.CVDTO;
import amu.cvmanager.model.Activity;
import amu.cvmanager.model.CV;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
public class CVMapper {


    private ActivityMapper activityMapper;

    public CVMapper(ActivityMapper activityMapper) {
        this.activityMapper = activityMapper;
    }


    public CV fromDTO(CVDTO cvdto) {
        List<Activity> activities = new ArrayList<>();
        if(cvdto.activities() != null){
            for(ActivityDTO activityDTO : cvdto.activities()){
                activities.add(activityMapper.fromDTO(activityDTO));
            }
        }
        return new CV(
                cvdto.id(),
                activities
        );
    }

    public CVDTO toDTO(CV cv) {
        List<ActivityDTO> activities = new ArrayList<>();
        if(cv.getActivities() != null){
            for (Activity activity : cv.getActivities()) {
                activities.add(activityMapper.toDTO(activity));
            }
        }
        return new CVDTO(
                cv.getId(),
                activities
        );
    }
}
