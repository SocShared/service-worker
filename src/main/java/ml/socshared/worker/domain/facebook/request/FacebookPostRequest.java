package ml.socshared.worker.domain.facebook.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class FacebookPostRequest {

    private String message;

}
