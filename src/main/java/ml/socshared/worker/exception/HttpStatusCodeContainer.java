package ml.socshared.worker.exception;

import org.springframework.http.HttpStatus;

public interface HttpStatusCodeContainer {
    HttpStatus getHttpStatus();
}

