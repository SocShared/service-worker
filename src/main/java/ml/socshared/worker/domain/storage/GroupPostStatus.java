package ml.socshared.worker.domain.storage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class GroupPostStatus {

    private UUID groupId;
    private PostStatus postStatus;
    private String statusText;
    private SocialNetwork socialNetwork;
    private String postFacebookId;
    private String postVkId;
    private String groupFacebookId;
    private String groupVkId;

    public enum PostStatus {
        @JsonProperty("published")
        PUBLISHED,
        @JsonProperty("awaiting")
        AWAITING,
        @JsonProperty("not_successful")
        NOT_SUCCESSFUL,
        @JsonProperty("processing")
        PROCESSING
    }

}
