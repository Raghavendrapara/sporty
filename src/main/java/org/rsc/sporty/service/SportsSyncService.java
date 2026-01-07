package org.rsc.sporty.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.rsc.sporty.dto.ApiResponse;
import org.rsc.sporty.entity.Sport;
import org.rsc.sporty.repository.SportRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SportsSyncService {

    private final SportRepository sportRepository;
    private final RestClient restClient;
    private final String apiUrl;

    public SportsSyncService(SportRepository sportRepository,
                             RestClient.Builder builder,
                             @Value("${sports.api.url}") String apiUrl) {
        this.sportRepository = sportRepository;
        this.restClient = builder.baseUrl(apiUrl).build();
        this.apiUrl = apiUrl;
    }

    @PostConstruct
    @Transactional
    public void syncSports() {
        log.info("Starting sports synchronization from {}", apiUrl);
        try {
            ApiResponse response = restClient.get()
                    .retrieve()
                    .body(ApiResponse.class);

            if (response != null && response.data() != null) {
                Set<String> existingIds = sportRepository.findAllExternalIds();
                
                List<Sport> newSports = response.data().stream()
                        .filter(item -> item.externalId() != null)
                        .filter(item -> !existingIds.contains(item.externalId()))
                        .map(item -> {
                            Sport sport = new Sport();
                            sport.setName(item.name());
                            sport.setExternalId(item.externalId());
                            return sport;
                        })
                        .collect(Collectors.toList());

                if (!newSports.isEmpty()) {
                    sportRepository.saveAll(newSports);
                    log.info("Synced {} new sports.", newSports.size());
                } else {
                    log.info("No new sports to sync.");
                }
            }
        } catch (Exception e) {
            log.error("Failed to sync sports", e);
        }
    }
}
