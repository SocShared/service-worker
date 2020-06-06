package ml.socshared.worker.domain.storage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class GroupPostStatus {

    private UUID groupId;
    private PostStatus postStatus;

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
