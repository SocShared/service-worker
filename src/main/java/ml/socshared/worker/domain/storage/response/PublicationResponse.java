package ml.socshared.worker.domain.storage.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ml.socshared.worker.domain.storage.GroupPostStatus;
import ml.socshared.worker.domain.storage.PostType;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PublicationResponse {

    private UUID publicationId;
    private UUID userId;
    private String text;
    private Date publicationDateTime;
    private LocalDateTime createdAt;
    private PostType postType;
    private Set<GroupPostStatus> postStatus;

}
