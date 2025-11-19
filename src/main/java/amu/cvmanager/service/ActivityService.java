package amu.cvmanager.service;

import amu.cvmanager.exception.ActivityNotFoundException;
import amu.cvmanager.model.Activity;
import amu.cvmanager.model.CV;
import amu.cvmanager.repository.ActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ActivityService {


    private final ActivityRepository activityRepository;
    private final CVService cvService;

    public ActivityService(ActivityRepository activityRepository, CVService cvService) {
        this.activityRepository = activityRepository;
        this.cvService = cvService;
    }

    public Activity createActivity(long cvId, Activity activity) {
        CV cv = cvService.findCVById(cvId);
        cv.addActivity(activity);
        return activityRepository.save(activity);
    }

    public Activity findActivityById(long id) {
        return activityRepository.findById(id).
                orElseThrow(()-> new ActivityNotFoundException("activity not found"));
    }

    public Activity findActivityByName(String name){
        return activityRepository.findByTitleLike(name).orElseThrow(()-> new ActivityNotFoundException("activity not found"));
    }

    public List<Activity> findAllActivities(){
        return activityRepository.findAll();
    }

    public Activity updateActivity(long id , Activity activity) {
        Activity foundedActivity =  findActivityById(id);

        foundedActivity.setYear(activity.getYear());
        foundedActivity.setType(activity.getType());
        foundedActivity.setTitle(activity.getTitle());
        foundedActivity.setDescription(activity.getDescription());
        foundedActivity.setWebAddress(activity.getWebAddress());

        return activityRepository.save(foundedActivity);
    }

    public void deleteActivityById(long id) {
        Activity activity = findActivityById(id);

        CV cv = activity.getCv();
        if (cv != null) {
            cv.removeActivity(activity);
        }

        activityRepository.deleteById(id);
    }


}
