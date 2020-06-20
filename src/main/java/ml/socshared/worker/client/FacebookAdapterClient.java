package ml.socshared.worker.client;

import ml.socshared.worker.domain.facebook.request.FacebookPostRequest;
import ml.socshared.worker.domain.facebook.response.FacebookPostResponse;
import ml.socshared.worker.domain.vk.response.VkPostResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "fb-adapter", url = "${feign.url.fb:}")
public interface FacebookAdapterClient extends SocAdapterClientInterface{
}
