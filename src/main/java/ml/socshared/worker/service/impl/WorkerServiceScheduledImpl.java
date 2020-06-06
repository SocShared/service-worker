package ml.socshared.worker.service.impl;

import lombok.RequiredArgsConstructor;
import ml.socshared.worker.client.StorageClient;
import ml.socshared.worker.domain.RestResponsePage;
import ml.socshared.worker.domain.storage.response.PublicationResponse;
import ml.socshared.worker.service.FacebookService;
import ml.socshared.worker.service.StorageService;
import ml.socshared.worker.service.VkService;
import ml.socshared.worker.service.WorkerServiceScheduled;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkerServiceScheduledImpl implements WorkerServiceScheduled {

    private final StorageService storageService;
    private final FacebookService facebookService;
    private final VkService vkService;

    @Scheduled(fixedDelay = 300000)
    public void startPost() {
        RestResponsePage<PublicationResponse> notPublishing = storageService.findNotPublishingAndReadyForPublishing();
        System.out.println(notPublishing.getContent());
    }

}
