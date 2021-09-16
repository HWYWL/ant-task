package com.github.hwywl.ant.task.config;

import java.util.concurrent.ScheduledFuture;

/**
 * 取消任务
 *
 * @author HWY
 * 2021-9-16 13:54:09
 */
public final class ScheduledTask {

    volatile ScheduledFuture<?> future;

    /**
     * 取消定时任务
     */
    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }
}

