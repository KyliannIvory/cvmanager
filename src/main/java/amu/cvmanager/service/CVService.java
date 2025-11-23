package amu.cvmanager.service;

import amu.cvmanager.exception.CVNotFoundException;
import amu.cvmanager.model.Activity;
import amu.cvmanager.model.CV;
import amu.cvmanager.repository.CVRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CVService {

    private final CVRepository cvRepository;

    public CVService(CVRepository cvRepository) {
        this.cvRepository = cvRepository;
    }

    public List<CV> findAllCVs() {
        return cvRepository.findAll();
    }


    public CV createCV(CV cv){
        return cvRepository.save(cv);
    }

    public CV findCVById(long id){
        return cvRepository.findById(id)
                .orElseThrow(() -> new CVNotFoundException("cv not found"));
    }

    public CV updateCV(long id , CV cv){
        CV foundedCV = findCVById(id);

        foundedCV.setPerson(cv.getPerson());
        foundedCV.getActivities().clear();
        for(Activity activity : cv.getActivities()){
            foundedCV.addActivity(activity);
        }

        return cvRepository.save(foundedCV);
    }

    public void deleteCVById(long id){
        CV foundedCV = findCVById(id);
        cvRepository.deleteById(id);
    }


    public List<CV> searchCVs(String searchTerm){
        if (searchTerm == null || searchTerm.trim().isEmpty()){
            return cvRepository.findAll();
        }
        return cvRepository.searchCVs(searchTerm);
    }


}
