package ml.socshared.worker.service.impl;

import lombok.RequiredArgsConstructor;
import ml.socshared.worker.client.StorageClient;
import ml.socshared.worker.domain.RestResponsePage;
import ml.socshared.worker.domain.storage.request.PublicationRequest;
import ml.socshared.worker.domain.storage.response.PublicationResponse;
import ml.socshared.worker.security.model.TokenObject;
import ml.socshared.worker.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    @Value("#{tokenGetter.tokenStorageService}")
    private TokenObject tokenStorageService;

    private final StorageClient client;

    @Override
    public PublicationResponse savePublication(PublicationRequest request) {
        return client.save(request, "Bearer " + tokenStorageService.getToken());
    }

    @Override
    public RestResponsePage<PublicationResponse> findNotPublishingAndReadyForPublishing() {
        return client.findNotPublishing(0, 50, "Bearer " + tokenStorageService.getToken());
    }
}
