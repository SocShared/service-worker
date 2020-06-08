package ml.socshared.worker.domain.vk.response;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
public class VkPostResponse {

    private UUID systemUserId;
    private String userId; //Id пользователя, который сделал пост
    private String groupId;
    private String postId;
    private String message;
    private String adapterId = "vk";
    private Integer commentsCount;
    private Integer likesCount = 0;
    private Integer repostsCount = 0;
    private Integer viewsCount = 0;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
