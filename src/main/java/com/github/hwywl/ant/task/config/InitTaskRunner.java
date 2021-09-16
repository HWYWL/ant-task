package com.github.hwywl.ant.task.config;

import com.github.hwywl.ant.task.model.SysTask;
import com.github.hwywl.ant.task.service.SysTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 程序启动自动加载任务
 *
 * @author HWY
 * 2021年9月16日16:45:49
 */
@Component
public class InitTaskRunner implements CommandLineRunner {
    @Autowired
    CronTaskRegistrar cronTaskRegistrar;
    @Autowired
    SysTaskService sysTaskService;

    @Override
    public void run(String... args) throws Exception {
        List<SysTask> list = sysTaskService.getTasksByStatus(1);
        for (SysTask sysTask : list) {
            cronTaskRegistrar.addCronTask(new SchedulingRunnable(sysTask.getTaskId(), sysTask.getBeanName(), sysTask.getMethodName(),
                    sysTask.getMethodParams()), sysTask.getCronExpression());
        }
    }
}
