package cn.roy.logcanary.no_op;

import android.content.Context;

import com.google.auto.service.AutoService;

import cn.roy.logcanary.core.LogCanaryAbility;

/**
 * @Description:
 * @Author: Roy Z
 * @Date: 2021/07/23
 * @Version: v1.0
 */
@AutoService(LogCanaryAbility.class)
public class LogCanaryAbilityForNoOp implements LogCanaryAbility {
    @Override
    public void inject(Context context) {

    }

    @Override
    public void v(String tag, String msg) {

    }

    @Override
    public void v(String tag, String msg, Throwable throwable) {

    }

    @Override
    public void d(String tag, String msg) {

    }

    @Override
    public void d(String tag, String msg, Throwable throwable) {

    }

    @Override
    public void i(String tag, String msg) {

    }

    @Override
    public void i(String tag, String msg, Throwable throwable) {

    }

    @Override
    public void w(String tag, String msg) {

    }

    @Override
    public void w(String tag, String msg, Throwable throwable) {

    }

    @Override
    public void e(String tag, String msg) {

    }

    @Override
    public void e(String tag, String msg, Throwable throwable) {

    }

}
