package ml.socshared.worker.service.impl;

import lombok.RequiredArgsConstructor;
import ml.socshared.worker.client.FacebookAdapterClient;
import ml.socshared.worker.client.VKAdapterClient;
import ml.socshared.worker.domain.vk.request.VkPostRequest;
import ml.socshared.worker.domain.vk.response.VkPostResponse;
import ml.socshared.worker.security.model.TokenObject;
import ml.socshared.worker.service.FacebookService;
import ml.socshared.worker.service.VkService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VkServiceImpl implements VkService {

    @Value("#{tokenGetter.tokenVK}")
    private TokenObject tokenVK;

    private final VKAdapterClient client;

    @Override
    public VkPostResponse savePost(UUID systemUserId, String groupId, VkPostRequest request) {
        return client.addPostInGroup(systemUserId, groupId, request, "Bearer " + tokenVK.getToken());
    }
}
