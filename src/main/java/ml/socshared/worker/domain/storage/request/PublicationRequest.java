package ml.socshared.worker.domain.storage.request;

import lombok.Getter;
import lombok.Setter;
import ml.socshared.worker.domain.storage.GroupPostStatus;
import ml.socshared.worker.domain.storage.PostType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class PublicationRequest {

    private String publicationId;
    private GroupPostStatus.PostStatus postStatus;
    private Date publicationDateTime;
    @NotEmpty
    private String userId;
    @NotEmpty
    private String[] groupIds;
    @NotNull
    private PostType type;
    @NotNull
    private String text;

}
