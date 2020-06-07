package ml.socshared.worker.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "bstat-adapter", url = "${feign.url.bstat:}")
public interface BstatClient {

}
