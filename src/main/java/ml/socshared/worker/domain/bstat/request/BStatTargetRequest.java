package ml.socshared.worker.domain.bstat.request;

import lombok.Data;
import ml.socshared.worker.domain.storage.SocialNetwork;

import java.util.List;
import java.util.UUID;

@Data
public class BStatTargetRequest {
    List<BStatTarget> request;
}
