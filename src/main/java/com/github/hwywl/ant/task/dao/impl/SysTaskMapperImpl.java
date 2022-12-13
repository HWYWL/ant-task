package com.github.hwywl.ant.task.dao.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.github.hwywl.ant.task.config.TaskConfig;
import com.github.hwywl.ant.task.dao.SysTaskMapper;
import com.github.hwywl.ant.task.model.SysTask;
import com.github.hwywl.ant.task.utils.TaskDateUtil;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 读取配置文件实现(用文件实现类似数据库的功能)
 *
 * @author HWY
 * 2021年9月16日15:03:07
 */
@Service
public class SysTaskMapperImpl implements SysTaskMapper {

    /**
     * 任务数据缓存
     */
    private static List<SysTask> tasks = new ArrayList<>();

    {
        /*
          初始化加载或者创建配置文件
         */
        String filePath = TaskConfig.filePath;
        if (FileUtil.exist(filePath)) {
            List<String> lines = FileUtil.readUtf8Lines(filePath);
            List<SysTask> taskList = lines.stream().map(e -> JSONUtil.toBean(e, SysTask.class)).collect(Collectors.toList());
            tasks.addAll(taskList);
        } else {
            FileUtil.touch(filePath);
        }
    }

    @Override
    public List<SysTask> findAllByTaskStatus(Integer status) {

        return tasks.stream().filter(task -> task.getTaskStatus().equals(status))
                .sorted(Comparator.comparing(SysTask::getCreateTime)).collect(Collectors.toList());
    }

    @Override
    public List<SysTask> findAll() {
        tasks.sort(Comparator.comparing(SysTask::getCreateTime));
        return tasks;
    }

    @Override
    public SysTask save(SysTask sysTask) {
        List<SysTask> collect = new ArrayList<>();
        sysTask.setTaskId("TaskId-" + RandomUtil.randomString(8));

        String day = TaskDateUtil.getNowDateStr();
        sysTask.setCreateTime(day);
        sysTask.setUpdateTime(day);

        collect.add(sysTask);
        collect.addAll(tasks);
        updateFile(collect);

        return sysTask;
    }

    @Override
    public void saveAndFlush(SysTask sysTask) {
        if (null == sysTask.getTaskId()) {
            save(sysTask);
        } else {
            sysTask.setUpdateTime(TaskDateUtil.getNowDateStr());

            List<SysTask> collect = tasks.stream().filter(task -> !task.getTaskId().equals(sysTask.getTaskId())).collect(Collectors.toList());
            collect.add(sysTask);
            updateFile(collect);
        }
    }

    @Override
    public SysTask findById(String id) {
        SysTask sysTask = new SysTask();
        Optional<SysTask> optional = tasks.stream().filter(task -> task.getTaskId().equals(id)).findFirst();
        if (optional.isPresent()) {
            sysTask = optional.get();
        }

        return sysTask;
    }

    @Override
    public void delete(String id) {
        List<SysTask> taskList = tasks.stream().filter(task -> !task.getTaskId().equals(id)).collect(Collectors.toList());

        updateFile(taskList);
    }

    /**
     * 更新配置文件和缓存
     *
     * @param taskList 任务数据
     */
    private void updateFile(List<SysTask> taskList) {
        // 清除缓存
        tasks.clear();
        tasks.addAll(taskList);

        List<String> collect = tasks.stream().map(JSONUtil::toJsonStr).collect(Collectors.toList());
        // 写入文件
        String filePath = TaskConfig.filePath;
        FileUtil.del(filePath);
        FileUtil.writeUtf8Lines(collect, filePath);
    }
}
