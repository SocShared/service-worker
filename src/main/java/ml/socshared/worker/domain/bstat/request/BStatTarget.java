package ml.socshared.worker.domain.bstat.request;

import lombok.Data;
import ml.socshared.worker.domain.storage.SocialNetwork;

import java.util.UUID;

@Data
public class BStatTarget {
    UUID systemUserId;
    String postId;
    String groupId;
    SocialNetwork socialNetwork;
}
