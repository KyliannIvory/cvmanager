package amu.cvmanager.data;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final DataSeederService dataSeederService;

    public DataInitializer(DataSeederService dataSeederService) {
        this.dataSeederService = dataSeederService;
    }

    @PostConstruct
    public void initDatabase() {
        dataSeederService.seedDatabase();
        System.out.println(dataSeederService.getStats());

    }
}