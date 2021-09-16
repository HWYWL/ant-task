package com.github.hwywl.ant.task.config;

import com.github.hwywl.ant.task.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 定时任务
 *
 * @author HWY
 * 2021年9月16日14:40:32
 */
public class SchedulingRunnable implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(SchedulingRunnable.class);

    /**
     * 任务id
     */
    private final String taskId;

    /**
     * 使用 @Component 注解的类
     */
    private final String beanName;

    /**
     * 方法名
     */
    private final String methodName;

    /**
     * 参数
     */
    private final String params;

    private Object targetBean;

    private Method method;

    public SchedulingRunnable(String taskId, String beanName, String methodName, String params) {
        this.taskId = taskId;
        this.beanName = beanName;
        this.methodName = methodName;
        this.params = params;

        try {
            targetBean = SpringContextUtil.getBean(beanName);

            if (StringUtils.hasText(params)) {
                method = targetBean.getClass().getDeclaredMethod(methodName, String.class);
            } else {
                method = targetBean.getClass().getDeclaredMethod(methodName);
            }

            ReflectionUtils.makeAccessible(method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        logger.info("定时任务开始执行 - taskId：{}，bean：{}，方法：{}，参数：{}", taskId, beanName, methodName, params);
        long startTime = System.currentTimeMillis();

        try {
            if (StringUtils.hasText(params)) {
                method.invoke(targetBean, params);
            } else {
                method.invoke(targetBean);
            }
        } catch (Exception ex) {
            logger.error(String.format("定时任务执行异常 - taskId：%s，bean：%s，方法：%s，参数：%s ", taskId, beanName, methodName, params), ex);
        }

        long times = System.currentTimeMillis() - startTime;
        logger.info("定时任务执行结束 - taskId：{}，bean：{}，方法：{}，参数：{}，耗时：{} 毫秒", taskId, beanName, methodName, params, times);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchedulingRunnable that = (SchedulingRunnable) o;
        if (params == null) {
            return beanName.equals(that.beanName) &&
                    methodName.equals(that.methodName) &&
                    that.params == null;
        }

        return beanName.equals(that.beanName) &&
                methodName.equals(that.methodName) &&
                params.equals(that.params);
    }

    @Override
    public int hashCode() {
        if (params == null) {
            return Objects.hash(beanName, methodName);
        }

        return Objects.hash(beanName, methodName, params);
    }
}
