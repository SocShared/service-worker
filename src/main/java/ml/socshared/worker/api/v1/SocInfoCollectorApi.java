package ml.socshared.worker.api.v1;

import ml.socshared.worker.domain.bstat.request.BStatTarget;
import ml.socshared.worker.domain.response.SuccessResponse;

public interface SocInfoCollectorApi {

    SuccessResponse startCollection(BStatTarget targetRequest);
}
