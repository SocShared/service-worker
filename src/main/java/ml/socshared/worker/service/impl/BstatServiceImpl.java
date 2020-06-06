package ml.socshared.worker.service.impl;

import lombok.RequiredArgsConstructor;
import ml.socshared.worker.client.BstatClient;
import ml.socshared.worker.client.VKAdapterClient;
import ml.socshared.worker.service.BstatService;
import ml.socshared.worker.service.VkService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BstatServiceImpl implements BstatService {

    private final BstatClient client;

}
