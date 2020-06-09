package ml.socshared.worker.service;

import ml.socshared.worker.domain.bstat.request.BStatTarget;
import ml.socshared.worker.domain.bstat.request.BStatTargetRequest;

import java.util.List;

public interface BStatService {
    void startCollection(BStatTarget targetRequest);
}
