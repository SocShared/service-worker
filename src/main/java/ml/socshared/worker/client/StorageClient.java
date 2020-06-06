package ml.socshared.worker.client;

import ml.socshared.worker.domain.RestResponsePage;
import ml.socshared.worker.domain.storage.request.PublicationRequest;
import ml.socshared.worker.domain.storage.response.PublicationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@FeignClient(name = "vk-adapter", url = "${feign.url.storage:}")
public interface StorageClient {

    @PostMapping(value = "/private/publications", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    PublicationResponse save(@RequestBody PublicationRequest request, @RequestHeader("Authorization") String token);

    @GetMapping(value = "/private/publications/status/not_publishing")
    RestResponsePage<PublicationResponse> findNotPublishing(@Min(0) @NotNull @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                            @Min(0) @Max(100) @NotNull @RequestParam(name = "size", defaultValue = "100") Integer size,
                                                            @RequestHeader("Authorization") String token);

}
