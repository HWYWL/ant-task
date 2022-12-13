package com.github.hwywl.ant.task.utils;

import cn.hutool.core.date.DatePattern;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 用于快速获取时间工具
 *
 * @author huangwenyi
 */
public class TaskDateUtil {
    /**
     * 获取当前日期
     *
     * @return 日期
     */
    public static String getNowDateStr() {
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        Instant instant = Clock.system(zoneId).instant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        return localDateTime.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN));
    }
}
