package ml.socshared.worker.domain.storage;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SocialNetwork {
    @JsonProperty("VK")
    VK,
    @JsonProperty("FB")
    FACEBOOK
}