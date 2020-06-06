package ml.socshared.worker.service.impl;

import lombok.RequiredArgsConstructor;
import ml.socshared.worker.client.FacebookAdapterClient;
import ml.socshared.worker.client.VKAdapterClient;
import ml.socshared.worker.security.model.TokenObject;
import ml.socshared.worker.service.FacebookService;
import ml.socshared.worker.service.VkService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VkServiceImpl implements VkService {

    @Value("#{tokenGetter.tokenVK}")
    private TokenObject tokenVK;

    private final VKAdapterClient client;

}
