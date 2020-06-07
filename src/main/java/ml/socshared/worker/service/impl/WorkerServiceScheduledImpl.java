package ml.socshared.worker.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ml.socshared.worker.client.StorageClient;
import ml.socshared.worker.config.RabbitMQConfig;
import ml.socshared.worker.domain.RestResponsePage;
import ml.socshared.worker.domain.facebook.request.FacebookPostRequest;
import ml.socshared.worker.domain.facebook.response.FacebookPostResponse;
import ml.socshared.worker.domain.storage.GroupPostStatus;
import ml.socshared.worker.domain.storage.request.PublicationRequest;
import ml.socshared.worker.domain.storage.response.GroupResponse;
import ml.socshared.worker.domain.storage.response.PublicationResponse;
import ml.socshared.worker.domain.vk.request.VkPostRequest;
import ml.socshared.worker.domain.vk.response.VkPostResponse;
import ml.socshared.worker.exception.AbstractRestHandleableException;
import ml.socshared.worker.exception.impl.HttpBadRequestException;
import ml.socshared.worker.service.FacebookService;
import ml.socshared.worker.service.StorageService;
import ml.socshared.worker.service.VkService;
import ml.socshared.worker.service.WorkerServiceScheduled;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ignore) {
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        PublicationResponse response = objectMapper.readValue(message, PublicationResponse.class);
        log.info("Received message as publication: {}", response);

        for (GroupPostStatus postStatus : response.getPostStatus()) {
            GroupResponse groupResponse = storageService.getGroup(postStatus.getGroupId());
            log.info("GroupResponse -> {}", groupResponse);
            try {
                if (postStatus.getPostStatus() == GroupPostStatus.PostStatus.PROCESSING) {
                    switch (groupResponse.getSocialNetwork()) {
                        case VK:
                            VkPostResponse vkPostResponse = saveVkPost(groupResponse, response.getUserId(), response.getText());
                            log.info("vk post -> " + vkPostResponse);
                            PublicationRequest resultVk = new PublicationRequest();
                            resultVk.setType(response.getPostType());
                            resultVk.setPublicationId(response.getPublicationId().toString());
                            resultVk.setText(response.getText());
                            resultVk.setPostStatus(GroupPostStatus.PostStatus.PUBLISHED);
                            resultVk.setGroupIds(new String[]{groupResponse.getGroupId()});
                            resultVk.setUserId(response.getUserId().toString());
                            resultVk.setPublicationDateTime(new Date());
                            storageService.savePublication(resultVk);
                            break;
                        case FACEBOOK:
                            FacebookPostResponse facebookPostResponse = saveFbPost(groupResponse, response.getUserId(), response.getText());
                            log.info("fb post -> " + facebookPostResponse);
                            PublicationRequest resultFb = new PublicationRequest();
                            resultFb.setType(response.getPostType());
                            resultFb.setPublicationId(response.getPublicationId().toString());
                            resultFb.setText(response.getText());
                            resultFb.setPostStatus(GroupPostStatus.PostStatus.PUBLISHED);
                            resultFb.setGroupIds(new String[]{groupResponse.getGroupId()});
                            resultFb.setUserId(response.getUserId().toString());
                            resultFb.setPublicationDateTime(new Date());
                            storageService.savePublication(resultFb);
                            break;
                    }
                }
            } catch (AbstractRestHandleableException | FeignException exc) {
                log.error(exc.getMessage());
                PublicationRequest result = new PublicationRequest();
                result.setType(response.getPostType());
                result.setPublicationId(response.getPublicationId().toString());
                result.setText(response.getText());
                result.setPostStatus(GroupPostStatus.PostStatus.NOT_SUCCESSFUL);
                result.setGroupIds(new String[]{groupResponse.getGroupId()});
                result.setUserId(response.getUserId().toString());
                result.setPublicationDateTime(new Date());
                storageService.savePublication(result);
            }
        }
    }

    private VkPostResponse saveVkPost(GroupResponse groupResponse, UUID userId, String message) {
        VkPostRequest reqVk = new VkPostRequest();
        reqVk.setMessage(message);

        log.info("message vk -> {} to group -> {}", message, groupResponse);

        return vkService.savePost(userId, groupResponse.getVkId(), reqVk);
    }

    private FacebookPostResponse saveFbPost(GroupResponse groupResponse, UUID userId, String message) {
        FacebookPostRequest reqFb = new FacebookPostRequest();
        reqFb.setMessage(message);

        log.info("message fb -> {} to group -> {}", message, groupResponse);

        return facebookService.savePost(userId, groupResponse.getFacebookId(), reqFb);
    }
}
