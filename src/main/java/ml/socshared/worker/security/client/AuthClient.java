package ml.socshared.worker.security.client;

import ml.socshared.worker.domain.response.SuccessResponse;
import ml.socshared.worker.security.request.CheckTokenRequest;
import ml.socshared.worker.security.request.ServiceTokenRequest;
import ml.socshared.worker.security.response.ServiceTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-client", url = "${feign.url.auth:}")
public interface AuthClient {

    @PostMapping(value = "/api/v1/public/service/validate_token", produces = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse send(CheckTokenRequest request);

    @PostMapping(value = "/api/v1/public/service/token", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ServiceTokenResponse getServiceToken(@RequestBody ServiceTokenRequest request);

}
