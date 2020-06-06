package ml.socshared.worker.service;

import ml.socshared.worker.domain.RestResponsePage;
import ml.socshared.worker.domain.storage.request.PublicationRequest;
import ml.socshared.worker.domain.storage.response.PublicationResponse;

public interface StorageService {

    PublicationResponse savePublication(PublicationRequest request);
    RestResponsePage<PublicationResponse> findNotPublishingAndReadyForPublishing();

}
