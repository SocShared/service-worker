package ml.socshared.worker.client;

import ml.socshared.worker.domain.vk.request.VkPostRequest;
import ml.socshared.worker.domain.vk.response.VkGroupResponse;
import ml.socshared.worker.domain.vk.response.VkPostResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "vk-adapter", url = "${feign.url.vk:}")
public interface VKAdapterClient {

    @PostMapping(value = "/api/v1/private/users/{systemUserId}/groups/{groupId}/posts",
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    VkPostResponse addPostInGroup(@PathVariable UUID systemUserId, @PathVariable String groupId,
                                  @RequestBody VkPostRequest message, @RequestHeader("Authorization") String token);

    @GetMapping("/api/v1/private/users/{systemUserId}/groups/{groupId}/online")
    Integer getGroupOnline(@PathVariable UUID systemUserId,@PathVariable String groupId,
                           @RequestHeader("Authorization") String token);

    @GetMapping("/api/v1/private/users/{systemUserId}/groups/{groupId}")
    VkGroupResponse getGroupById(@PathVariable UUID systemUserId, @PathVariable String groupId,
                                 @RequestHeader("Authorization") String token);


    @GetMapping("/api/v1/private/users/{systemUserId}/groups/{groupId}/posts/{postId}")
    VkPostResponse getPostOfGroupById(@PathVariable("systemUserId") UUID userId,
                                           @PathVariable("groupId") String groupId,
                                           @PathVariable("postId") String postId,
                                      @RequestHeader("Authorization") String token);



}