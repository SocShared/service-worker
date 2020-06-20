package ml.socshared.worker.service;

import ml.socshared.worker.domain.vk.request.VkPostRequest;
import ml.socshared.worker.domain.vk.response.VkGroupResponse;
import ml.socshared.worker.domain.vk.response.VkPostResponse;

import java.util.UUID;

public interface VkService extends SocAdapterService{


    Integer getGroupOnline(UUID systemUserId, String socGroupId);

}
