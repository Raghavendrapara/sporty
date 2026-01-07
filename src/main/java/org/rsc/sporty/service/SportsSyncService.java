package org.rsc.sporty.service;
import org.rsc.sporty.entity.Sport;
import org.rsc.sporty.repository.SportRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;
@Service
public class SportsSyncService {

    @Value("${sports.api.url}")
    private String fetchSportUrl;
    private final SportRepository sportRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    public SportsSyncService(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }
    public void fetchAndSaveSports() {
        ResponseEntity<Map> response = restTemplate.getForEntity(fetchSportUrl, Map.class);
        List<Map<String, Object>> sports = (List<Map<String, Object>>) response.getBody().get("data");
        for (Map<String, Object> sportData : sports) {
            String externalId = (String) sportData.get("id"); // or whatever their key is
            String name = (String) sportData.get("name");
            if (!sportRepository.existsByExternalId(externalId)) {
                Sport sport = new Sport();
                sport.setExternalId(externalId);
                sport.setName(name);
                sportRepository.save(sport);
            }
        }
    }
}