package ml.socshared.worker.domain.storage;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PostType {
    @JsonProperty("in_real_time")
    IN_REAL_TIME,
    @JsonProperty("deferred")
    DEFERRED
}