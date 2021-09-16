package com.github.hwywl.ant.task.dao;

import com.github.hwywl.ant.task.model.SysTask;

import java.util.List;

/**
 * 读取配置文件
 *
 * @author HWY
 * 2021年9月16日14:28:06
 */
public interface SysTaskMapper {
    /**
     * 根据状态获取任务
     *
     * @param status 状态，0：禁用、1：启用
     * @return 任务数据
     */
    List<SysTask> findAllByTaskStatus(Integer status);

    /**
     * 获取所有数据
     *
     * @return 任务数据
     */
    List<SysTask> findAll();

    /**
     * 保存数据
     * @param sysTask 任务数据
     * @return 任务数据，加入id
     */
    SysTask save(SysTask sysTask);

    void saveAndFlush(SysTask sysTask);

    /**
     * 根据id查找数据
     *
     * @param id id
     * @return 任务数据
     */
    SysTask findById(String id);

    /**
     * 根据id删除数据
     *
     * @param id id
     */
    void delete(String id);
}
