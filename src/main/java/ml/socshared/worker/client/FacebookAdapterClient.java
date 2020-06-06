package ml.socshared.worker.client;

import ml.socshared.worker.domain.facebook.request.FacebookPostRequest;
import ml.socshared.worker.domain.facebook.response.FacebookPostResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "fb-adapter", url = "${feign.url.fb:}")
public interface FacebookAdapterClient {

    @PostMapping(value = "/private/users/{systemUserId}/groups/{groupId}/posts", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FacebookPostResponse addPost(@PathVariable UUID systemUserId, @PathVariable String groupId,
                                 @RequestBody FacebookPostRequest request, @RequestHeader("Authorization") String token);

}
