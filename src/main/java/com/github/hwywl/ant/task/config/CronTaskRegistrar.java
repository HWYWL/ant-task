package com.github.hwywl.ant.task.config;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    private final Map<Runnable, ScheduledTask> scheduledTasks = new ConcurrentHashMap<>(16);

    @Autowired
    private TaskScheduler taskScheduler;

    /**
     * 添加任务
     *
     * @param task           任务
     * @param cronExpression 表达式
     */
    public void addCronTask(Runnable task, String cronExpression) {
        CronTask cronTask = new CronTask(task, cronExpression);
        if (this.scheduledTasks.containsKey(task)) {
            removeCronTask(task);
        }

        ScheduledTask scheduledTask = new ScheduledTask();
        scheduledTask.future = this.taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());

        this.scheduledTasks.put(task, scheduledTask);
    }

    /**
     * 移除任务
     * @param task 任务
     */
    public void removeCronTask(Runnable task) {
        ScheduledTask scheduledTask = this.scheduledTasks.remove(task);
        if (scheduledTask != null)
            scheduledTask.cancel();
    }

    @Override
    public void destroy() {
        for (ScheduledTask task : this.scheduledTasks.values()) {
            task.cancel();
        }

        this.scheduledTasks.clear();
    }
}