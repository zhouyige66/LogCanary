package cn.roy.logcanary.core;

import android.content.Context;

/**
 * @Description: 日志基本能力
 * @Author: Roy Z
 * @Date: 2021/08/12
 * @Version: v1.0
 */
public interface LogCanaryAbility {

    void setContext(Context context);

    void v(String tag, String msg);

    void v(String tag, String msg, Throwable throwable);

    void d(String tag, String msg);

    void d(String tag, String msg, Throwable throwable);

    void i(String tag, String msg);

    void i(String tag, String msg, Throwable throwable);

    void w(String tag, String msg);

    void w(String tag, String msg, Throwable throwable);

    void e(String tag, String msg);

    void e(String tag, String msg, Throwable throwable);

}
