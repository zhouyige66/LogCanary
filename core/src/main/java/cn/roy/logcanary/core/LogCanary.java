package cn.roy.logcanary.core;

/**
 * @Description: 工具类
 * @Author: Roy Z
 * @Date: 2021/08/12
 * @Version: v1.0
 */
public class LogCanary {
    private LogCanary() {

    }

    public static void v(String tag, String msg) {
        LogCanaryAbility ability = LogCanaryManager.getInstance().getAbility();
        if (ability == null) {
            return;
        }
        ability.v(tag, msg);
    }

    public static void v(String tag, String msg, Throwable throwable) {
        LogCanaryAbility ability = LogCanaryManager.getInstance().getAbility();
        if (ability == null) {
            return;
        }
        ability.v(tag, msg, throwable);
    }

    public static void d(String tag, String msg) {
        LogCanaryAbility ability = LogCanaryManager.getInstance().getAbility();
        if (ability == null) {
            return;
        }
        ability.d(tag, msg);
    }

    public static void d(String tag, String msg, Throwable throwable) {
        LogCanaryAbility ability = LogCanaryManager.getInstance().getAbility();
        if (ability == null) {
            return;
        }
        ability.d(tag, msg, throwable);
    }

    public static void i(String tag, String msg) {
        LogCanaryAbility ability = LogCanaryManager.getInstance().getAbility();
        if (ability == null) {
            return;
        }
        ability.i(tag, msg);
    }

    public static void i(String tag, String msg, Throwable throwable) {
        LogCanaryAbility ability = LogCanaryManager.getInstance().getAbility();
        if (ability == null) {
            return;
        }
        ability.i(tag, msg, throwable);
    }

    public static void w(String tag, String msg) {
        LogCanaryAbility ability = LogCanaryManager.getInstance().getAbility();
        if (ability == null) {
            return;
        }
        ability.w(tag, msg);
    }

    public static void w(String tag, String msg, Throwable throwable) {
        LogCanaryAbility ability = LogCanaryManager.getInstance().getAbility();
        if (ability == null) {
            return;
        }
        ability.w(tag, msg, throwable);
    }

    public static void e(String tag, String msg) {
        LogCanaryAbility ability = LogCanaryManager.getInstance().getAbility();
        if (ability == null) {
            return;
        }
        ability.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable throwable) {
        LogCanaryAbility ability = LogCanaryManager.getInstance().getAbility();
        if (ability == null) {
            return;
        }
        ability.e(tag, msg, throwable);
    }

}
