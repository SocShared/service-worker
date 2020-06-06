package ml.socshared.worker.service;

import ml.socshared.worker.domain.facebook.request.FacebookPostRequest;
import ml.socshared.worker.domain.facebook.response.FacebookPostResponse;

import java.util.UUID;

public interface FacebookService {

    FacebookPostResponse savePost(UUID systemUserId, String groupId, FacebookPostRequest request);

}
