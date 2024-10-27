package com.dxjunkyard.batchstarter.application.job;

import com.dxjunkyard.batchstarter.application.ItemResult;
import reactor.core.publisher.Flux;

public interface Job {

    Flux<ItemResult> execute();
}
