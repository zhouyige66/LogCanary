package cn.roy.logcanary.core;

import android.content.Context;

import java.util.ServiceLoader;

/**
 * @Description: 日志监视窗初始化
 * @Author: Roy Z
 * @Date: 2021/08/12
 * @Version: v1.0
 */
public class LogCanaryManager {
    private static LogCanaryManager instance;

    public static LogCanaryManager getInstance() {
        if (instance == null) {
            synchronized (LogCanaryManager.class) {
                if (instance == null) {
                    instance = new LogCanaryManager();
                }
            }
        }
        return instance;
    }

    private Context context;
    private LogCanaryAbility ability;

    // 私有构造方法
    private LogCanaryManager() {
    }

    public void init(Context context) {
        this.context = context.getApplicationContext();
    }

    public boolean hasInit() {
        return context != null;
    }

    /**
     * SPI机制实现获取注册的服务提供者
     *
     * @return
     */
    public LogCanaryAbility getAbility() {
        if (ability == null) {
            ServiceLoader<LogCanaryAbility> monitoringAbilities =
                    ServiceLoader.load(LogCanaryAbility.class);
            if (monitoringAbilities != null) {
                ability = monitoringAbilities.iterator().next();
            }
            if (ability != null) {
                ability.setContext(context);
            }
        }
        return ability;
    }

}
