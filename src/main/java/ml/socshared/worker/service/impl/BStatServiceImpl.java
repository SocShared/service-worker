package ml.socshared.worker.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ml.socshared.worker.client.BstatClient;
import ml.socshared.worker.client.VKAdapterClient;
import ml.socshared.worker.config.RabbitMQConfig;
import ml.socshared.worker.domain.adapter.response.AdapterGroupResponse;
import ml.socshared.worker.domain.adapter.response.AdapterPostResponse;
import ml.socshared.worker.domain.bstat.rabbitmq.RabbitMqType;
import ml.socshared.worker.domain.bstat.rabbitmq.request.RabbitMqSocialRequest;
import ml.socshared.worker.domain.bstat.rabbitmq.response.RabbitMqPostResponse;
import ml.socshared.worker.domain.bstat.rabbitmq.response.RabbitMqResponseAll;
import ml.socshared.worker.domain.bstat.request.BStatTarget;
import ml.socshared.worker.domain.storage.SocialNetwork;
import ml.socshared.worker.domain.bstat.rabbitmq.response.RabbitMqGroupResponse;
import ml.socshared.worker.domain.vk.response.VkGroupResponse;
import ml.socshared.worker.domain.vk.response.VkPostResponse;
import ml.socshared.worker.security.model.TokenObject;
import ml.socshared.worker.service.BStatService;
import ml.socshared.worker.service.FacebookService;
import ml.socshared.worker.service.SocAdapterService;
import ml.socshared.worker.service.VkService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BStatServiceImpl implements BStatService {

    private final VkService vkService;
    private final FacebookService fbService;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void startCollection(BStatTarget target) {
        log.info("received targets; {}", target);
        ObjectMapper objectMapper = new ObjectMapper();
            try{
                RabbitMqSocialRequest request = new RabbitMqSocialRequest(target.getSocialNetwork(),
                        RabbitMqType.GROUP, target.getSystemUserId(), target.getGroupId(), target.getPostId());
                String serialize = objectMapper.writeValueAsString(request);
                rabbitTemplate.convertAndSend(RabbitMQConfig.BSTAT_REQUEST_EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, serialize);
            } catch (JsonProcessingException e) {
                log.error("Error serialize to json -> {}", target.toString());
                e.printStackTrace();
            }
        }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_BSTAT_REQUEST_NAME)
    public void statisticCollection(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        RabbitMqSocialRequest target = null;
        try{
            target = objectMapper.readValue(message, RabbitMqSocialRequest.class);
        } catch (JsonProcessingException e) {
            log.warn("Error parsing json -> {}", message);
            e.printStackTrace();
            return;
        }
        SocAdapterService socialService = null;
        if(target.getSocialNetwork() == SocialNetwork.VK) {
            socialService = vkService;
        } else {
            socialService = fbService;
        }
            log.info("Request get statistics of {}", target.getSocialNetwork());
            if(target.getType().equals(RabbitMqType.POST)) {
                try {
                    AdapterPostResponse result = socialService.getPostOfGroupById(target.getSystemUserId(),
                                                     target.getGroupId(), target.getPostId());
                    RabbitMqResponseAll response = new RabbitMqResponseAll();
                    response.setType(RabbitMqType.POST);
                    response.setSocialNetwork(target.getSocialNetwork());
                    response.setGroupId(target.getGroupId());
                    response.setPostId(target.getPostId());
                    response.setSystemUserId(target.getSystemUserId());
                    response.setCommentsCount(result.getCommentsCount());
                    response.setLikesCount(result.getLikesCount());
                    response.setRepostsCount(result.getRepostsCount());
                    response.setViewsCount(result.getViewsCount());
                    String serialize = objectMapper.writeValueAsString(response);
                    rabbitTemplate.convertAndSend(RabbitMQConfig.BSTAT_RESPONSE_EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, serialize);
                } catch (Exception exp) {
                    log.error("Error get post statistic; {}", exp.getMessage());
                    exp.printStackTrace();
                }

            } else {
                try{

                    AdapterGroupResponse group = socialService.getGroupById(target.getSystemUserId(), target.getGroupId());
                    Integer groupOnline = 0;

                    if(target.getSocialNetwork() == SocialNetwork.VK) {
                        groupOnline = vkService.getGroupOnline(target.getSystemUserId(), target.getGroupId());
                    }

                    RabbitMqResponseAll response = new RabbitMqResponseAll();
                    response.setType(RabbitMqType.GROUP);
                    response.setSocialNetwork(target.getSocialNetwork());
                    response.setGroupId(group.getGroupId());
                    response.setMembersCount(group.getMembersCount());
                    response.setMembersOnline(groupOnline);
                    response.setSystemUserId(target.getSystemUserId());
                    String serialize = objectMapper.writeValueAsString(response);
                    rabbitTemplate.convertAndSend(RabbitMQConfig.BSTAT_RESPONSE_EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, serialize);
                } catch (Exception exp) {
                    log.error("Error get group statistic; {}", exp.getMessage());
                    exp.printStackTrace();
                }

            }

    }

    private void RabbitMqGroupInfoSend(VkPostResponse response, SocialNetwork soc) {

    }

}
