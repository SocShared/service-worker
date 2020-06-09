package ml.socshared.worker.domain.bstat.rabbitmq.response;

import lombok.Data;
import ml.socshared.worker.domain.bstat.rabbitmq.RabbitMqType;
import ml.socshared.worker.domain.bstat.rabbitmq.response.RabbitMqResponse;

import java.util.Date;
import java.util.UUID;

@Data
public class RabbitMqGroupResponse extends RabbitMqResponse {

    public RabbitMqGroupResponse() {
        this.setType(RabbitMqType.GROUP);
    }

    UUID systemUserId;
    String groupId;
    Integer membersCount;
    Integer membersOnline;
}
