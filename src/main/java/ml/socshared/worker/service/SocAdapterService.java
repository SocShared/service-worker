package ml.socshared.worker.service;

import ml.socshared.worker.domain.adapter.request.AdapterPostRequest;
import ml.socshared.worker.domain.adapter.response.AdapterGroupResponse;
import ml.socshared.worker.domain.adapter.response.AdapterPostResponse;
import ml.socshared.worker.domain.vk.request.VkPostRequest;
import ml.socshared.worker.domain.vk.response.VkGroupResponse;
import ml.socshared.worker.domain.vk.response.VkPostResponse;

import java.util.UUID;

public interface SocAdapterService {
    AdapterPostResponse savePost(UUID systemUserId, String groupId, AdapterPostRequest request);
    AdapterPostResponse getPostOfGroupById(UUID systemUserId, String socGroupId, String socPostId);
    AdapterGroupResponse getGroupById(UUID systemUserId, String socGroupId);
}
