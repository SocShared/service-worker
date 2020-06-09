package ml.socshared.worker.client.decoder;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import ml.socshared.worker.exception.impl.HttpBadRequestException;
import ml.socshared.worker.exception.impl.HttpForbiddenException;
import ml.socshared.worker.exception.impl.HttpNotFoundException;
import ml.socshared.worker.exception.impl.HttpUnauthorizedException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            String msg = methodKey + " return error: " + response.body().toString();
            log.warn(msg);
            return new HttpNotFoundException(msg);
        } else if (response.status() == 401) {
            return new HttpUnauthorizedException("unauthorized");
        } else if (response.status() == 403) {
            return new HttpForbiddenException("forbidden");
        } else if (response.status() == 400) {
            String msg;
            if(response.body() != null) {
                msg = methodKey + " return error: " + response.body().toString();
            } else {
                msg =  methodKey + " return http status code: 400";
            }

            log.warn(msg);
            return new HttpBadRequestException(msg);
        }

        String msg = "Unexpected error. ";
        if(response.body() != null) {
            msg = msg + response.body().toString();
        }
        return new Exception(msg);
    }


}


