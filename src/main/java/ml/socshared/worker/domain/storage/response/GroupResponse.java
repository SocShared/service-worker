package ml.socshared.worker.domain.storage.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ml.socshared.worker.domain.storage.SocialNetwork;

import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupResponse {

    private String groupId;
    private SocialNetwork socialNetwork;
    private String name;
    private String vkId;
    private String facebookId;
    private UUID userId;

}