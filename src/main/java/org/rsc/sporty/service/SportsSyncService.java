package org.rsc.sporty.service;

import lombok.extern.slf4j.Slf4j;
import org.rsc.sporty.dto.ApiResponse;
import org.rsc.sporty.entity.Sport;
import org.rsc.sporty.repository.SportRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
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
    private final TransactionTemplate transactionTemplate;

    public SportsSyncService(SportRepository sportRepository,
                             RestClient.Builder builder,
                             @Value("${sports.api.url}") String apiUrl,
                             PlatformTransactionManager transactionManager) {
        this.sportRepository = sportRepository;
        this.restClient = builder.baseUrl(apiUrl).build();
        this.apiUrl = apiUrl;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Async
    @Scheduled(cron = "0 0 2 * * *")
    @EventListener(ApplicationReadyEvent.class)
    public void syncSports() {
        log.info("Starting sports synchronization from {}", apiUrl);
        try {

            ApiResponse response = restClient.get()
                    .retrieve()
                    .body(ApiResponse.class);

            if (response == null || response.data() == null) {
                return;
            }
            transactionTemplate.executeWithoutResult(status -> {

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
                }
            });
            log.info("Finished sports synchronization from {}", apiUrl);
        } catch (Exception e) {
            log.error("Failed to sync sports", e);
        }
    }
}
