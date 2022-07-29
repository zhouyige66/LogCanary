package cn.roy.logcanary.op.logback;

import android.content.Context;
import android.text.TextUtils;

import org.slf4j.LoggerFactory;

import java.util.List;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.android.LogcatAppender;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.LevelFilter;
import cn.roy.logcanary.op.util.AndroidStorageUtil;

/**
 * @Description: Logback配置器
 * @Author: Roy Z
 * @Date: 2019-08-09 10:23
 * @Version: v1.0
 */
public class LogConfigBuilder {
    private Context context;
    private LoggerContext loggerContext;
    private Logger rootLogger;
    private Level rootLevel = Level.DEBUG;
    /**
     * logcat配置
     */
    private Level logcatLevel;
    private String logcatEncodePattern;

    public LogConfigBuilder(Context context) {
        this.context = context.getApplicationContext();
        this.loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        this.rootLogger = (ch.qos.logback.classic.Logger)
                LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    }

    public LogConfigBuilder setRootLevel(Level rootLevel) {
        this.rootLevel = rootLevel;
        return this;
    }

    public LogConfigBuilder setLogcatAppenderProp(Level logcatLevel, String encodePattern) {
        this.logcatLevel = logcatLevel;
        this.logcatEncodePattern = encodePattern;
        return this;
    }

    public void buildForSingleFileModel(FileAppenderProperty prop) {
        configLogcatAppender();
        rootLogger.addAppender(FileAppenderFactory.create(loggerContext, prop,
                true));
    }

    public void buildForMultipleFileModel(List<FileAppenderProperty> props) {
        configLogcatAppender();
        for (FileAppenderProperty prop : props) {
            rootLogger.addAppender(FileAppenderFactory.create(loggerContext, prop,
                    false));
        }
    }

    public void buildDefault() {
        setLogcatAppenderProp(Level.DEBUG, FileAppenderProperty.PATTERN_DEFAULT);
        configLogcatAppender();
        // 日志文件夹
        String externalFilesDir = AndroidStorageUtil.getExternalFilesDir(context);
        String logFilePath = externalFilesDir.concat("log/log.txt");
        String fileNamePattern = externalFilesDir.concat("log/%d{yyyy-MM-dd}/log_%i.txt");
        // 存储文件总大小1G
        long totalFileSize = 1024 * 1024 * 1024;
        // 单个文件最大10M
        long singleFileSize = 1024 * 1024 * 10;
        // 默认保存最大天数为7
        int maxHistory = 7;
        FileAppenderProperty prop = new FileAppenderProperty.Builder(Level.TRACE)
                .setLogFilePath(logFilePath)
                .setLogFileNamePattern(fileNamePattern)
                .setSingleFileSize(singleFileSize)
                .setTotalFileSize(totalFileSize)
                .setMaxHistory(maxHistory)
                .build();
        rootLogger.addAppender(FileAppenderFactory.create(loggerContext, prop, true));
    }

    private void configLogcatAppender() {
        loggerContext.stop();
        rootLogger.setLevel(rootLevel);
        if (logcatLevel != null) {
            // 输出格式
            PatternLayoutEncoder encoder = new PatternLayoutEncoder();
            encoder.setContext(loggerContext);
            if (TextUtils.isEmpty(logcatEncodePattern)) {
                encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} ${PID} [%thread] %-5level %logger{36} [%method-%line] - %msg%n");
            } else {
                encoder.setPattern(logcatEncodePattern);
            }
            encoder.start();
            // 过滤等级
            LevelFilter filter = new LevelFilter();
            filter.setContext(loggerContext);
            filter.setLevel(logcatLevel);
            filter.start();
            // setup LogcatAppender
            LogcatAppender logcatAppender = new LogcatAppender();
            logcatAppender.setContext(loggerContext);
            logcatAppender.setEncoder(encoder);
            logcatAppender.start();
            logcatAppender.addFilter(filter);

            rootLogger.addAppender(logcatAppender);
        }
    }

}
