package com.github.hwywl.ant.task.utils;

import cn.hutool.core.date.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * 用于快速获取时间工具
 *
 * @author huangwenyi
 */
public class TaskDateUtil {
    /**
     * 获取服务器时区
     */
    private static final ZoneId SYSTEM_DEFAULT_ZONE_ID = ZoneId.systemDefault();

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

    /**
     * 根据上海时区 产生新的{@link DateTime}对象
     *
     * @return {@link DateTime}对象
     * @since 0.0.1
     */
    public static DateTime dateCtt() {
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(SYSTEM_DEFAULT_ZONE_ID);
        ZonedDateTime shanghaiTime = zonedDateTime.withZoneSameInstant(zoneId);

        // 将服务器时间转换为其他时区时间
        ZonedDateTime otherTime = LocalDateTimeUtil.of(shanghaiTime).atZone(SYSTEM_DEFAULT_ZONE_ID).withZoneSameInstant(zoneId);
        // 计算两个时间的差异
        long hoursDiff = ChronoUnit.HOURS.between(zonedDateTime, otherTime);

        return DateUtil.date().offset(DateField.HOUR_OF_DAY, (int) hoursDiff);
    }

    /**
     * 根据时区 产生新的{@link DateTime}对象
     *
     * @param zoneId ZoneId对象
     * @return {@link DateTime}对象
     * @since 0.0.1
     */
    public static DateTime date(ZoneId zoneId) {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(SYSTEM_DEFAULT_ZONE_ID);
        ZonedDateTime shanghaiTime = zonedDateTime.withZoneSameInstant(zoneId);

        // 将服务器时间转换为其他时区时间
        ZonedDateTime otherTime = LocalDateTimeUtil.of(shanghaiTime).atZone(SYSTEM_DEFAULT_ZONE_ID).withZoneSameInstant(zoneId);
        // 计算两个时间的差异
        long hoursDiff = ChronoUnit.HOURS.between(zonedDateTime, otherTime);

        return DateUtil.date().offset(DateField.HOUR_OF_DAY, (int) hoursDiff);
    }

    /**
     * 根据时区和便宜天数 产生新的{@link DateTime}对象
     *
     * @param zoneId    ZoneId对象
     * @param offsetDay 偏移量，通过正/负的数字偏移相应的天数
     * @return {@link DateTime}对象
     * @since 0.0.1
     */
    public static DateTime date(ZoneId zoneId, int offsetDay) {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(SYSTEM_DEFAULT_ZONE_ID);
        ZonedDateTime shanghaiTime = zonedDateTime.withZoneSameInstant(zoneId);

        // 将服务器时间转换为其他时区时间
        ZonedDateTime otherTime = LocalDateTimeUtil.of(shanghaiTime).atZone(SYSTEM_DEFAULT_ZONE_ID).withZoneSameInstant(zoneId);
        // 计算两个时间的差异
        long hoursDiff = ChronoUnit.HOURS.between(zonedDateTime, otherTime);

        return DateUtil.date().offset(DateField.HOUR_OF_DAY, (int) hoursDiff).offset(DateField.DAY_OF_YEAR, offsetDay);
    }
}
