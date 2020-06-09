package ml.socshared.worker.domain.bstat.rabbitmq.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ml.socshared.worker.domain.bstat.rabbitmq.RabbitMqType;
import ml.socshared.worker.domain.storage.SocialNetwork;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RabbitMqSocialRequest {
    SocialNetwork socialNetwork;
    RabbitMqType type;
    UUID systemUserId;
    String groupId;
    String postId;
}
