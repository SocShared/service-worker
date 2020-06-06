package ml.socshared.worker.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "vk-adapter", url = "${feign.url.vk:}")
public interface VKAdapterClient {

}
