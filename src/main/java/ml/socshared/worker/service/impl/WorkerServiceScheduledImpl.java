package ml.socshared.worker.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.FeignException;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ml.socshared.worker.client.StorageClient;
import ml.socshared.worker.config.RabbitMQConfig;
import ml.socshared.worker.domain.RestResponsePage;
import ml.socshared.worker.domain.adapter.request.AdapterPostRequest;
import ml.socshared.worker.domain.adapter.response.AdapterPostResponse;
import ml.socshared.worker.domain.facebook.request.FacebookPostRequest;
import ml.socshared.worker.domain.facebook.response.FacebookPostResponse;
import ml.socshared.worker.domain.storage.GroupPostStatus;
import ml.socshared.worker.domain.storage.SocialNetwork;
import ml.socshared.worker.domain.storage.request.PublicationRequest;
import ml.socshared.worker.domain.storage.response.GroupResponse;
import ml.socshared.worker.domain.storage.response.PublicationResponse;
import ml.socshared.worker.domain.vk.request.VkPostRequest;
import ml.socshared.worker.domain.vk.response.VkPostResponse;
import ml.socshared.worker.exception.AbstractRestHandleableException;
import ml.socshared.worker.exception.impl.HttpBadRequestException;
import ml.socshared.worker.service.*;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkerServiceScheduledImpl implements WorkerServiceScheduled {

    private final StorageService storageService;
    private final FacebookService facebookService;
    private final VkService vkService;

    private final RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 100000)
    public void startPost() throws IOException {
        RestResponsePage<PublicationResponse> notPublishing = storageService.findNotPublishingAndReadyForPublishing();
        List<PublicationResponse> publicationResponseList = notPublishing.getContent();
        log.info("batch size publiction -> {}", publicationResponseList.size());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        for (PublicationResponse response : publicationResponseList) {
            PublicationRequest request = new PublicationRequest();
            request.setText(response.getText());
            List<String> groupIds = new ArrayList<>();
            log.info("prev publication response -> {}", response);
            for (GroupPostStatus status : response.getPostStatus()) {
                groupIds.add(status.getGroupId().toString());
            }
            request.setGroupIds(groupIds.toArray(String[]::new));
            request.setUserId(response.getUserId().toString());
            request.setType(response.getPostType());
            request.setPublicationId(response.getPublicationId().toString());
            log.info("Sending message...");
            PublicationRequest result = new PublicationRequest();
            result.setType(request.getType());
            result.setPublicationId(request.getPublicationId());
            result.setText(request.getText());
            result.setPostStatus(GroupPostStatus.PostStatus.PROCESSING);
            result.setGroupIds(groupIds.toArray(String[]::new));
            result.setUserId(request.getUserId());
            PublicationResponse resp = storageService.savePublication(result);
            log.info("publication resp add queue -> {}", resp);
            String serialize = objectMapper.writeValueAsString(resp);
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, serialize);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PUBLICATION_NAME)
    public void receiveMessage(String message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        PublicationResponse response = objectMapper.readValue(message, PublicationResponse.class);
        log.info("Received message as publication: {}", response);

        for (GroupPostStatus postStatus : response.getPostStatus()) {
            try {
                if (postStatus.getPostStatus() == GroupPostStatus.PostStatus.PROCESSING) {

                            SocAdapterService adapterService = null;
                            String socGroupId = "";
                            String socPostId = "";
                            if(postStatus.getSocialNetwork() == SocialNetwork.VK) {
                                adapterService = vkService;
                                socGroupId = postStatus.getGroupVkId();
                            } else {
                                adapterService = facebookService;
                                socGroupId = postStatus.getGroupFacebookId();
                            }
                            AdapterPostResponse socPostResponse = adapterService.savePost(response.getUserId(), socGroupId,
                                    new AdapterPostRequest(response.getText()));

                            log.info("{} post -> {}", postStatus.getSocialNetwork(),  socPostResponse);

                            PublicationRequest result = new PublicationRequest();
                            result.setType(response.getPostType());
                            result.setPublicationId(response.getPublicationId().toString());
                            result.setText(response.getText());
                            result.setStatusText("Success");
                            result.setPostStatus(GroupPostStatus.PostStatus.PUBLISHED);
                            result.setGroupIds(new String[]{postStatus.getGroupId().toString()});
                            result.setUserId(response.getUserId().toString());

                            if(postStatus.getSocialNetwork() == SocialNetwork.VK) {
                                result.setPostVkId(socPostResponse.getPostId());
                            } else {
                                result.setPostFacebookId(socPostResponse.getPostId());
                            }

                            result.setPublicationDateTime(new Date());
                            storageService.savePublication(result);
                    }
            } catch (AbstractRestHandleableException | RetryableException exc) {
                String mes = "Connection refused: " + exc.getMessage();
                if (exc instanceof AbstractRestHandleableException) {
                    Map<String, Object> mapError = objectMapper.readValue(exc.getMessage(), HashMap.class);
                    mes = (String) mapError.get("message");
                }
                log.error(mes);
                PublicationRequest result = new PublicationRequest();
                result.setType(response.getPostType());
                result.setPublicationId(response.getPublicationId().toString());
                result.setText(response.getText());
                result.setStatusText(mes);
                result.setPostStatus(GroupPostStatus.PostStatus.NOT_SUCCESSFUL);
                result.setGroupIds(new String[]{postStatus.getGroupId().toString()});
                result.setUserId(response.getUserId().toString());
                result.setPublicationDateTime(new Date());
                storageService.savePublication(result);
            }
        }
    }

}
