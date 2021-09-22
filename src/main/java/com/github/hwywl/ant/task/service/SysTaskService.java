package com.github.hwywl.ant.task.service;

import com.github.hwywl.ant.task.config.CronTaskRegistrar;
import com.github.hwywl.ant.task.config.SchedulingRunnable;
import com.github.hwywl.ant.task.dao.SysTaskMapper;
import com.github.hwywl.ant.task.model.SysTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作任务数据
 *
 * @author HWY
 * 2021年9月16日14:05:09
 */
@Service
public class SysTaskService {

    @Autowired
    SysTaskMapper sysTaskMapper;

    @Autowired
    CronTaskRegistrar cronTaskRegistrar;

    public List<SysTask> getAllTasks() {
        return sysTaskMapper.findAll();
    }

    public Boolean addTask(SysTask sysTask) {
        List<SysTask> all = sysTaskMapper.findAll();
        for (SysTask task : all) {
            if (task.equals(sysTask)) {
                //任务重复，添加失败
                return false;
            }
        }
        //添加
        SysTask sj = sysTaskMapper.save(sysTask);
        //添加成功，如果新加的task是开启状态，就顺便开启
        SchedulingRunnable schedulingRunnable = new SchedulingRunnable(sysTask.getTaskId(), sysTask.getBeanName(), sysTask.getMethodName(), sysTask.getMethodParams());
        if (sj.getTaskStatus() == 1) {
            cronTaskRegistrar.addCronTask(schedulingRunnable, sysTask.getCronExpression());
        }
        //添加成功
        return true;
    }

    public List<SysTask> getTasksByStatus(int status) {
        return sysTaskMapper.findAllByTaskStatus(status);
    }

    public Boolean updateTask(SysTask sysTask) {
        sysTaskMapper.saveAndFlush(sysTask);
        SchedulingRunnable schedulingRunnable = new SchedulingRunnable(sysTask.getTaskId(), sysTask.getBeanName(), sysTask.getMethodName(), sysTask.getMethodParams());
        if (sysTask.getTaskStatus() == 1) {
            cronTaskRegistrar.addCronTask(schedulingRunnable, sysTask.getCronExpression());
        } else {
            cronTaskRegistrar.removeCronTask(schedulingRunnable);
        }
        return true;
    }

    public Boolean deleteTasksById(String id) {
        SysTask sysTask = sysTaskMapper.findById(id);
        SchedulingRunnable schedulingRunnable = new SchedulingRunnable(sysTask.getTaskId(), sysTask.getBeanName(), sysTask.getMethodName(), sysTask.getMethodParams());
        cronTaskRegistrar.removeCronTask(schedulingRunnable);
        sysTaskMapper.delete(id);
        return true;
    }
}
