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
            ServiceLoader<LogCanaryAbility> loader = ServiceLoader.load(LogCanaryAbility.class);
            if (loader.iterator().hasNext()) {
                ability = loader.iterator().next();
                ability.inject(context);
            }
            if (ability == null) {
                System.out.println("SPI机制失效,需要注入LogCanaryAbility实现");
            }
        }
        return ability;
    }

    public void inject(LogCanaryAbility ability) {
        this.ability = ability;
        if (context != null) {
            this.ability.inject(context);
        }
    }

}
