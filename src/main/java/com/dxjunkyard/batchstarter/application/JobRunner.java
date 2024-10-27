package com.dxjunkyard.batchstarter.application;

import com.dxjunkyard.batchstarter.application.job.Job;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobRunner implements ApplicationRunner, ExitCodeGenerator {

    private final Job job;

    private int exitCode;

    @Override
    public void run(final ApplicationArguments args) {

        CountDownLatch latch = new CountDownLatch(1);

        job.execute()
                .collectList()
                .map(JobResult::from)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        result -> {
                            log.info("Job result: {}", result);
                            if (!result.isAllSuccess()) {
                                setFailedExitCode();
                            } else {
                                setSuccessExitCode();
                            }
                        },
                        error -> {
                            log.error("An error occurred", error);
                            latch.countDown();
                            setFailedExitCode();
                        },
                        latch::countDown
                );

        try {
            latch.await(); // 完了を待機
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Batch process interrupted.");
            setFailedExitCode();
        }
    }

    private void setFailedExitCode() {
        exitCode = 1;
    }

    private void setSuccessExitCode() {
        exitCode = 0;
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }
}
