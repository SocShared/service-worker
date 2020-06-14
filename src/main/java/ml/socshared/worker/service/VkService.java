package ml.socshared.worker.service;

import ml.socshared.worker.domain.vk.request.VkPostRequest;
import ml.socshared.worker.domain.vk.response.VkGroupResponse;
import ml.socshared.worker.domain.vk.response.VkPostResponse;

import java.util.UUID;

public interface VkService {

    VkPostResponse savePost(UUID systemUserId, String groupId, VkPostRequest request);
    VkPostResponse getPostOfGroupById(UUID systemUserId, String socGroupId, String socPostId);
    VkGroupResponse getGroupById(UUID systemUserId, String socGroupId);
    Integer getGroupOnline(UUID systemUserId, String socGroupId);

}
