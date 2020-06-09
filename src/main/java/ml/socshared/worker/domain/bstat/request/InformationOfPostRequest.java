package ml.socshared.worker.domain.bstat.request;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class InformationOfPostRequest {
   String groupId;
   String postId;
   Integer shares;
   Integer likes;
   Integer comments;
   Integer views;
   ZonedDateTime time;
}
