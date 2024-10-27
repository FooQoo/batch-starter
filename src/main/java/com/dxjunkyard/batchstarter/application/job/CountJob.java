package com.dxjunkyard.batchstarter.application.job;

import com.dxjunkyard.batchstarter.application.ItemResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
@Profile("count")
public class CountJob implements Job {

    @Override
    public Flux<ItemResult> execute() {
        final int totalItems = 100;
        AtomicInteger processedItems = new AtomicInteger(0);

        return Flux.range(1, totalItems)
                .delayElements(Duration.ofMillis(50)) // シミュレーション用に遅延
                .map(item -> {
                    // 処理の模擬
                    int current = processedItems.incrementAndGet();
                    if (current % 10 == 0) {
                        logProgress(current, totalItems);
                    }

                    return new ItemResult(true);
                });
    }

    private void logProgress(int current, int total) {
        int progress = (int) ((double) current / total * 100);
        log.info("Progress: {}/{} ({}%)", current, total, progress);
    }
}
