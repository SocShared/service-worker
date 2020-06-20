package ml.socshared.worker.client;

import ml.socshared.worker.domain.adapter.request.AdapterPostRequest;
import ml.socshared.worker.domain.adapter.response.AdapterGroupResponse;
import ml.socshared.worker.domain.adapter.response.AdapterPostResponse;
import ml.socshared.worker.domain.vk.request.VkPostRequest;
import ml.socshared.worker.domain.vk.response.VkGroupResponse;
import ml.socshared.worker.domain.vk.response.VkPostResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public interface SocAdapterClientInterface {

    @PostMapping(value = "/api/v1/private/users/{systemUserId}/groups/{groupId}/posts",
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    AdapterPostResponse addPost(@PathVariable UUID systemUserId, @PathVariable String groupId,
                                @RequestBody AdapterPostRequest message, @RequestHeader("Authorization") String token);



    @GetMapping("/api/v1/private/users/{systemUserId}/groups/{groupId}")
    AdapterGroupResponse getGroupById(@PathVariable UUID systemUserId, @PathVariable String groupId,
                                      @RequestHeader("Authorization") String token);


    @GetMapping("/api/v1/private/users/{systemUserId}/groups/{groupId}/posts/{postId}")
    AdapterPostResponse getPostOfGroupById(@PathVariable("systemUserId") UUID userId,
                                      @PathVariable("groupId") String groupId,
                                      @PathVariable("postId") String postId,
                                      @RequestHeader("Authorization") String token);

}
