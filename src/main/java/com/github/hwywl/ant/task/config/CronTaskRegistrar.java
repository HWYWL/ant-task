package com.github.hwywl.ant.task.config;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 任务注册
 *
 * @author HWY
 * 2021年9月16日13:56:50
 */
@Component
public class CronTaskRegistrar implements DisposableBean {

    /**
     * 存放任务线程
     */
    private final Map<SchedulingRunnable, ScheduledTask> scheduledTasks = new ConcurrentHashMap<>(16);

    @Autowired
    private TaskScheduler taskScheduler;

    /**
     * 添加任务
     *
     * @param schedulingRunnable 任务
     * @param cronExpression     表达式
     */
    public void addCronTask(SchedulingRunnable schedulingRunnable, String cronExpression) {
        CronTask cronTask = new CronTask(schedulingRunnable, cronExpression);

        if (this.scheduledTasks.size() > 0) {
            boolean present = this.scheduledTasks.keySet().stream()
                    .anyMatch(task -> task.getTaskId().equals(schedulingRunnable.getTaskId()));

            if (present) {
                removeCronTask(schedulingRunnable);
            }
        }

        ScheduledTask scheduledTask = new ScheduledTask();
        scheduledTask.future = this.taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());

        this.scheduledTasks.put(schedulingRunnable, scheduledTask);
    }

    /**
     * 移除任务
     *
     * @param schedulingRunnable 任务
     */
    public void removeCronTask(SchedulingRunnable schedulingRunnable) {
        for (SchedulingRunnable runnableTask : this.scheduledTasks.keySet()) {
            if (runnableTask.getTaskId().equals(schedulingRunnable.getTaskId())) {
                ScheduledTask scheduledTask = this.scheduledTasks.remove(runnableTask);
                if (scheduledTask != null)
                    scheduledTask.cancel();

                break;
            }
        }
    }

    @Override
    public void destroy() {
        for (ScheduledTask task : this.scheduledTasks.values()) {
            task.cancel();
        }

        this.scheduledTasks.clear();
    }
}