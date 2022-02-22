package cn.roy.logcanary.op.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 日志工具
 * @Author: Roy Z
 * @Date: 2019-08-02 20:44
 * @Version: v1.0
 */
public class LoggerUtil {

    private LoggerUtil() {

    }

    private static Logger getLogger() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        Logger logger;
        if (stackTraceElements.length > 5) {
            StackTraceElement stackTraceElement = stackTraceElements[4];
            String className = stackTraceElement.getClassName();
            if (className.contains("$")) {
                className = className + "@line:" + stackTraceElement.getLineNumber();
            }
            logger = LoggerFactory.getLogger(className);
        } else {
            logger = LoggerFactory.getLogger(LoggerUtil.class);
        }
        return logger;
    }

    public static void v(String log) {
        getLogger().trace(log);
    }

    public static void v(String log, Throwable throwable) {
        getLogger().trace(log, throwable);
    }

    public static void d(String log) {
        getLogger().debug(log);
    }

    public static void d(String log, Throwable throwable) {
        getLogger().debug(log, throwable);
    }

    public static void i(String log) {
        getLogger().info(log);
    }

    public static void i(String log, Throwable throwable) {
        getLogger().info(log, throwable);
    }

    public static void w(String log) {
        getLogger().warn(log);
    }

    public static void w(String log, Throwable throwable) {
        getLogger().warn(log, throwable);
    }

    public static void e(String log) {
        getLogger().error(log);
    }

    public static void e(String log, Throwable throwable) {
        getLogger().error(log, throwable);
    }
}
