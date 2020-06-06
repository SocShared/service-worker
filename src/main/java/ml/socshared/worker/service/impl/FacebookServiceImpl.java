package ml.socshared.worker.service.impl;

import lombok.RequiredArgsConstructor;
import ml.socshared.worker.client.FacebookAdapterClient;
import ml.socshared.worker.service.FacebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FacebookServiceImpl implements FacebookService {

    private final FacebookAdapterClient client;

}
