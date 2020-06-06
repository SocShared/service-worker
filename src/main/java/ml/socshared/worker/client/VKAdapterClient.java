package ml.socshared.worker.client;

import ml.socshared.worker.domain.vk.request.VkPostRequest;
import ml.socshared.worker.domain.vk.response.VkPostResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@FeignClient(name = "vk-adapter", url = "${feign.url.vk:}")
public interface VKAdapterClient {

    @PostMapping(value = "/private/users/{systemUserId}/groups/{groupId}/posts",
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    VkPostResponse addPostInGroup(@PathVariable UUID systemUserId, @PathVariable String groupId,
                                  @RequestBody VkPostRequest message, @RequestHeader("Authorization") String token);

}