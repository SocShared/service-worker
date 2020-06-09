package ml.socshared.worker.client;

import ml.socshared.worker.domain.bstat.request.DataList;
import ml.socshared.worker.domain.bstat.request.InformationOfGroupRequest;
import ml.socshared.worker.domain.bstat.request.InformationOfPostRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "bstat-adapter", url = "${feign.url.bstat:}")
public interface BstatClient {

    @PostMapping("api/v1/private/callback/group_info")
    void setTimeSeriesOfPost(@RequestBody DataList<InformationOfPostRequest> data,
                             @RequestHeader("Authorization") String token);

    @GetMapping("api/v1/private/time")
    void setTimeSeriesOfGroup(@RequestBody DataList<InformationOfGroupRequest> data,
                              @RequestHeader("Authorization") String token);
}
