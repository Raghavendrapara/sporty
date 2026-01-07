package org.rsc.sporty.service;
import jakarta.annotation.PostConstruct;
import org.rsc.sporty.dto.SportItem;
import org.springframework.transaction.annotation.Transactional;
import org.rsc.sporty.dto.ApiResponse;
import org.rsc.sporty.entity.Sport;
import org.rsc.sporty.repository.SportRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SportsSyncService {

    @Value("${sports.api.url}")
    private String apiUrl;

    private final SportRepository sportRepository;
    private final RestTemplate restTemplate;

    public SportsSyncService(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
        this.restTemplate = new RestTemplate();
    }

    @PostConstruct
    @Transactional
    public void syncSports() {
        try {
            ApiResponse response = restTemplate.getForObject(apiUrl, ApiResponse.class);

            if (response != null && response.data() != null) {
                for (SportItem item : response.data()) {

                    // Verify we actually got an ID before saving
                    if (item.externalId() == null) {
                        System.err.println("Skipping item with null ID: " + item);
                        continue;
                    }

                    if (!sportRepository.existsByExternalId(item.externalId())) {
                        Sport sport = new Sport();
                        sport.setName(item.name());
                        sport.setExternalId(item.externalId());
                        sportRepository.save(sport);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}