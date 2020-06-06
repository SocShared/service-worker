package ml.socshared.worker.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "fb-adapter", url = "${feign.url.fb:}")
public interface FacebookAdapterClient {

}
