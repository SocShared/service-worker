package ml.socshared.worker.controller.v1;

import lombok.RequiredArgsConstructor;
import ml.socshared.worker.service.FacebookService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
public class FacebookController {

    private final FacebookService service;

}
