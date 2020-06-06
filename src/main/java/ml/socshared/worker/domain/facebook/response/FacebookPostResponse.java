package ml.socshared.worker.domain.facebook.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class FacebookPostResponse {

    private UUID systemUserId;
    private String groupId;
    private String postId;
    private String userId;
    private String adapterId;
    private String message;
    private Integer likesCount;
    private Integer dislikesCount;
    private Integer commentsCount;
    private Integer repostsCount;
    private Integer viewsCount;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
