package com.github.hwywl.ant.task.model;

import java.util.Objects;

/**
 * 任务对象
 *
 * @author HWY
 * 2021年9月16日13:47:55
 */
public class SysTask {
    /**
     * 任务id
     */
    private String taskId;

    /**
     * 使用 @Component 注解的类
     */
    private String beanName;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 方法参数
     */
    private String methodParams;

    /**
     * 定时任务，cron表达式
     */
    private String cronExpression;

    /**
     * 任务状态
     */
    private Integer taskStatus;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysTask sysTask = (SysTask) o;
        return Objects.equals(beanName, sysTask.beanName) &&
                Objects.equals(methodName, sysTask.methodName) &&
                Objects.equals(methodParams, sysTask.methodParams) &&
                Objects.equals(cronExpression, sysTask.cronExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beanName, methodName, methodParams, cronExpression);
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodParams() {
        return methodParams;
    }

    public void setMethodParams(String methodParams) {
        this.methodParams = methodParams;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}