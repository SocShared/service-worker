package ml.socshared.worker.controller.v1;

import lombok.RequiredArgsConstructor;
import ml.socshared.worker.api.v1.SocInfoCollectorApi;
import ml.socshared.worker.domain.bstat.request.BStatTarget;
import ml.socshared.worker.domain.bstat.request.BStatTargetRequest;
import ml.socshared.worker.domain.response.SuccessResponse;
import ml.socshared.worker.service.BStatService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;


@RestController
@RequestMapping("api/v1/stat")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class SocInfoCollectorController implements SocInfoCollectorApi {

    private final BStatService bStatService;

    @Override
    @PostMapping("/private/collection")
    @PreAuthorize("hasRole('SERVICE')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SuccessResponse startCollection(@RequestBody BStatTarget targetRequest) {
        bStatService.startCollection(targetRequest);
        return new SuccessResponse(true);
    }
}
