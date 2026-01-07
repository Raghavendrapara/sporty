package org.rsc.sporty.initializer;
import org.jspecify.annotations.NonNull;
import org.rsc.sporty.service.SportsSyncService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
@Component
public class DataInitializer implements CommandLineRunner {
    private final SportsSyncService sportsSyncService;
    public DataInitializer(SportsSyncService sportsSyncService) {
        this.sportsSyncService = sportsSyncService;
    }
    @Override
    public void run(String @NonNull ... args) {
        System.out.println("... Syncing Sports from External API ...");
        try {
            sportsSyncService.fetchAndSaveSports();
            System.out.println("... Sports Sync Complete!");
        } catch (Exception e) {
            // Log error but don't crash app if their API is down
            System.err.println("Failed to sync sports: " + e.getMessage());
        }
    }
}