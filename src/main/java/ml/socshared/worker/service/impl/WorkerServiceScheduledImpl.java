package ml.socshared.worker.service.impl;

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
import ml.socshared.worker.service.FacebookService;
import ml.socshared.worker.service.StorageService;
import ml.socshared.worker.service.VkService;
import ml.socshared.worker.service.WorkerServiceScheduled;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Scheduled(fixedDelay = 300000)
    public void startPost() {
        RestResponsePage<PublicationResponse> notPublishing = storageService.findNotPublishingAndReadyForPublishing();
        List<PublicationResponse> publicationResponseList = notPublishing.getContent();

        for (PublicationResponse response : publicationResponseList) {
            PublicationRequest request = new PublicationRequest();
            request.setText(request.getText());
            List<String> groupIds = new ArrayList<>();
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
            storageService.savePublication(result);
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, request);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PUBLICATION_NAME)
    public void receiveMessage(PublicationRequest request) {
        log.info("Received message as publication: {}", request.toString());
        for (String group : request.getGroupIds()) {
            GroupResponse groupResponse = storageService.getGroup(UUID.fromString(group));
            try {
                switch (groupResponse.getSocialNetwork()) {
                    case VK:
                        VkPostResponse vkPostResponse = saveVkPost(groupResponse, request);
                        log.info("vk post -> " + vkPostResponse);
                        PublicationRequest resultVk = new PublicationRequest();
                        resultVk.setType(request.getType());
                        resultVk.setPublicationId(request.getPublicationId());
                        resultVk.setText(request.getText());
                        resultVk.setPostStatus(GroupPostStatus.PostStatus.PUBLISHED);
                        resultVk.setGroupIds(new String[]{group});
                        resultVk.setUserId(request.getUserId());
                        storageService.savePublication(resultVk);
                        break;
                    case FACEBOOK:
                        FacebookPostResponse facebookPostResponse = saveFbPost(groupResponse, request);
                        log.info("fb post -> " + facebookPostResponse);
                        PublicationRequest resultFb = new PublicationRequest();
                        resultFb.setType(request.getType());
                        resultFb.setPublicationId(request.getPublicationId());
                        resultFb.setText(request.getText());
                        resultFb.setPostStatus(GroupPostStatus.PostStatus.PUBLISHED);
                        resultFb.setGroupIds(new String[]{group});
                        resultFb.setUserId(request.getUserId());
                        storageService.savePublication(resultFb);
                        break;
                }
            } catch (Exception exc) {
                log.error(exc.getMessage());
                PublicationRequest resultFb = new PublicationRequest();
                resultFb.setType(request.getType());
                resultFb.setPublicationId(request.getPublicationId());
                resultFb.setText(request.getText());
                resultFb.setPostStatus(GroupPostStatus.PostStatus.NOT_SUCCESSFUL);
                resultFb.setGroupIds(new String[]{group});
                resultFb.setUserId(request.getUserId());
                storageService.savePublication(resultFb);
            }
        }
    }

    private VkPostResponse saveVkPost(GroupResponse groupResponse, PublicationRequest request) {
        VkPostRequest reqVk = new VkPostRequest();
        reqVk.setMessage(request.getText());

        return vkService.savePost(UUID.fromString(request.getUserId()), groupResponse.getVkId(), reqVk);
    }

    private FacebookPostResponse saveFbPost(GroupResponse groupResponse, PublicationRequest request) {
        FacebookPostRequest reqFb = new FacebookPostRequest();
        reqFb.setMessage(request.getText());

        return facebookService.savePost(UUID.fromString(request.getUserId()), groupResponse.getFacebookId(), reqFb);
    }
}
