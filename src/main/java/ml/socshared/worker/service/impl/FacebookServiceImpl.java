package ml.socshared.worker.service.impl;

import lombok.RequiredArgsConstructor;
import ml.socshared.worker.client.FacebookAdapterClient;
import ml.socshared.worker.domain.adapter.request.AdapterPostRequest;
import ml.socshared.worker.domain.adapter.response.AdapterGroupResponse;
import ml.socshared.worker.domain.adapter.response.AdapterPostResponse;
import ml.socshared.worker.domain.facebook.request.FacebookPostRequest;
import ml.socshared.worker.domain.facebook.response.FacebookPostResponse;
import ml.socshared.worker.domain.vk.request.VkPostRequest;
import ml.socshared.worker.security.model.TokenObject;
import ml.socshared.worker.service.FacebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FacebookServiceImpl implements FacebookService {

    @Value("#{tokenGetter.tokenFB}")
    private TokenObject tokenFB;

    private final FacebookAdapterClient client;

    @Override
    public AdapterPostResponse savePost(UUID systemUserId, String groupId, AdapterPostRequest request) {
        return client.addPost(systemUserId, groupId, request, "Bearer " + tokenFB.getToken());
    }

    @Override
    public AdapterPostResponse getPostOfGroupById(UUID systemUserId, String groupId, String postId) {
        return client.getPostOfGroupById(systemUserId, groupId, postId, "Bearer " + tokenFB.getToken());
    }


    @Override
    public AdapterGroupResponse getGroupById(UUID systemUserId, String socGroupId) {
        return client.getGroupById(systemUserId, socGroupId, "Bearer " + tokenFB.getToken());
    }
}
