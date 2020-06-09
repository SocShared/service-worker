package ml.socshared.worker.domain.bstat.rabbitmq.response;

import lombok.Data;
import ml.socshared.worker.domain.bstat.rabbitmq.RabbitMqType;

@Data
public class RabbitMqResponse {
    RabbitMqType type;
}
