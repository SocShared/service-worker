package ml.socshared.worker.service.impl;

import lombok.RequiredArgsConstructor;
import ml.socshared.worker.client.FacebookAdapterClient;
import ml.socshared.worker.domain.facebook.request.FacebookPostRequest;
import ml.socshared.worker.domain.facebook.response.FacebookPostResponse;
import ml.socshared.worker.security.model.TokenObject;
import ml.socshared.worker.service.FacebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FacebookServiceImpl implements FacebookService {

    @Value("#{tokenGetter.tokenFB}")
    private TokenObject tokenFB;

    private final FacebookAdapterClient client;

    @Override
    public FacebookPostResponse savePost(UUID systemUserId, String groupId, FacebookPostRequest request) {
        return client.addPost(systemUserId, groupId, request, "Bearer " + tokenFB.getToken());
    }
}
