package ml.socshared.worker.domain.bstat.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InformationOfGroupRequest {
    String groupId;
    Integer subscribersOnline;
    Integer subscribersNumber;
    LocalDateTime time;
}

