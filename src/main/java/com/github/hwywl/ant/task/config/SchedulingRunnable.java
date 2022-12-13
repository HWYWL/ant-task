package com.github.hwywl.ant.task.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;
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
    private final Object params;

    private Object targetBean;

    private Method method;

    public SchedulingRunnable(String taskId, String beanName, String methodName, String params) {
        this.taskId = taskId;
        this.beanName = beanName;
        this.methodName = methodName;
        this.params = params;

        try {
            targetBean = SpringUtil.getBean(beanName);
            Class<?> aClass = targetBean.getClass();
            Method[] methods = ReflectUtil.getMethods(aClass);
            boolean anyMatch = Arrays.stream(methods).anyMatch(e -> e.getName().equals(methodName));

            if (anyMatch) {
                method = Arrays.stream(methods).filter(e -> e.getName().equals(methodName)).findFirst().get();
                ReflectionUtils.makeAccessible(method);
            } else {
                logger.error("在反射中未找到相匹配的方法：" + methodName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        logger.info("定时任务开始执行 - taskId：{}，bean：{}，方法：{}，参数：{}", taskId, beanName, methodName, params);
        long startTime = System.currentTimeMillis();

        try {
            Parameter[] parameters = method.getParameters();
            if (parameters.length == 0 || ObjectUtil.isEmpty(params)) {
                method.invoke(targetBean);
            } else if (parameters.length == 1) {
                Object parameterObj = params;
                Parameter parameter = parameters[0];

                Class<?> parameterType = parameters[0].getType();
                // 如果不是基础类型需要对参数进行处理
                if (!parameterType.isPrimitive() && !parameterType.isAssignableFrom(String.class)) {
                    Type type = parameter.getParameterizedType();
                    Class<?> clazz = Class.forName(type.getTypeName());
                    parameterObj = JSONUtil.toBean((String) params, clazz);
                }

                method.invoke(targetBean, parameterObj);
            } else {
                logger.error("当前版本不支持多个参数的方法调用，请封装到一个Bean中，可以使用JSON格式传参！");
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

    public String getTaskId() {
        return taskId;
    }
}
