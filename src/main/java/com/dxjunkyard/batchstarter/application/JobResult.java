package com.dxjunkyard.batchstarter.application;

import java.util.List;

public record JobResult(
        long total,
        long success,
        long failed
) {

    public static JobResult from(final List<ItemResult> itemResults) {
        final long total = itemResults.size();
        final long success = itemResults.stream().filter(ItemResult::isSuccess).count();
        final long failed = total - success;

        return new JobResult(total, success, failed);
    }

    public boolean isAllSuccess() {
        return total == success;
    }
}
