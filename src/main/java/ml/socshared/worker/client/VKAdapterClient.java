package ml.socshared.worker.client;

import ml.socshared.worker.domain.vk.request.VkPostRequest;
import ml.socshared.worker.domain.vk.response.VkGroupResponse;
import ml.socshared.worker.domain.vk.response.VkPostResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "vk-adapter", url = "${feign.url.vk:}")
public interface VKAdapterClient extends SocAdapterClientInterface {

    @GetMapping("/api/v1/private/users/{systemUserId}/groups/{groupId}/online")
    Integer getGroupOnline(@PathVariable UUID systemUserId,@PathVariable String groupId,
                           @RequestHeader("Authorization") String token);

}