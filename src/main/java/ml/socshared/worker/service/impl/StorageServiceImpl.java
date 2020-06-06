package ml.socshared.worker.service.impl;

import lombok.RequiredArgsConstructor;
import ml.socshared.worker.service.StorageService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final StorageService storageService;

}
