package ml.socshared.worker.service.impl;

import lombok.RequiredArgsConstructor;
import ml.socshared.worker.client.StorageClient;
import ml.socshared.worker.security.model.TokenObject;
import ml.socshared.worker.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    @Value("#{tokenGetter.tokenStorageService}")
    private TokenObject tokenStorageService;

    private final StorageClient client;

}
