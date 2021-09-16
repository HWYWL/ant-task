package com.github.hwywl.ant.task.controller;

import com.github.hwywl.ant.task.utils.MessageResult;
import com.github.hwywl.ant.task.model.SysTask;
import com.github.hwywl.ant.task.service.SysTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 页面接口
 *
 * @author HWY
 * 2021年9月16日16:03:49
 */
@RestController
@RequestMapping("/tasks")
public class SysTaskController {
    @Autowired
    SysTaskService sysTaskService;

    @GetMapping("/allTasks")
    public List<SysTask> getAllTasks() {
        return sysTaskService.getAllTasks();
    }

    @PostMapping("/addTask")
    public MessageResult addTask(@RequestBody SysTask sysTask) {
        Boolean flag = sysTaskService.addTask(sysTask);
        if (flag) {
            return MessageResult.ok("任务添加成功");
        }
        return MessageResult.error("任务重复，添加失败");
    }

    @PutMapping("/updateTask")
    public MessageResult updateTask(@RequestBody SysTask sysTask) {
        Boolean flag = sysTaskService.updateTask(sysTask);
        if (flag) {
            return MessageResult.ok("任务更新成功");
        }
        return MessageResult.error("任务更新失败");
    }

    @DeleteMapping("/deleteTask")
    public MessageResult deleteTask(String id) {
        Boolean flag = sysTaskService.deleteTasksById(id);
        if (flag) {
            return MessageResult.ok("删除成功");
        }
        return MessageResult.error("删除失败");
    }
}
