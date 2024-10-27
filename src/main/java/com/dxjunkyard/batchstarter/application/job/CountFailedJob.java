package com.dxjunkyard.batchstarter.application.job;

import com.dxjunkyard.batchstarter.application.ItemResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Slf4j
@Profile("countFailed")
public class CountFailedJob implements Job {

    @Override
    public Flux<ItemResult> execute() {
        return Flux.just(new ItemResult(false));
    }
}
