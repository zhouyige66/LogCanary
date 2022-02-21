package cn.roy.logcanary.op;

import android.content.Context;
import android.util.Log;

import com.google.auto.service.AutoService;

import cn.roy.logcanary.core.LogCanaryAbility;
import cn.roy.logcanary.op.bean.LogBean;

/**
 * @Description: 监视工具
 * @Author: Roy Z
 * @Date: 2021/06/25
 * @Version: v1.0
 */
@AutoService(LogCanaryAbility.class)
public class LogCanaryAbilityForOp implements LogCanaryAbility {

    @Override
    public void setContext(Context context) {
        LogCanaryDelegate.getInstance().setContext(context);
    }

    public void v(String tag, String msg) {
        addLog(LogBean.VERBOSE, tag, msg);
    }

    public void v(String tag, String msg, Throwable throwable) {
        addLog(LogBean.VERBOSE, tag, msg + '\n' + Log.getStackTraceString(throwable));
    }

    public void d(String tag, String msg) {
        addLog(LogBean.DEBUG, tag, msg);
    }

    public void d(String tag, String msg, Throwable throwable) {
        addLog(LogBean.DEBUG, tag, msg + '\n' + Log.getStackTraceString(throwable));
    }

    public void i(String tag, String msg) {
        addLog(LogBean.INFO, tag, msg);
    }

    public void i(String tag, String msg, Throwable throwable) {
        addLog(LogBean.INFO, tag, msg + '\n' + Log.getStackTraceString(throwable));
    }

    public void w(String tag, String msg) {
        addLog(LogBean.WARN, tag, msg);
    }

    public void w(String tag, String msg, Throwable throwable) {
        addLog(LogBean.WARN, tag, msg + '\n' + Log.getStackTraceString(throwable));
    }

    public void e(String tag, String msg) {
        addLog(LogBean.ERROR, tag, msg);
    }

    public void e(String tag, String msg, Throwable throwable) {
        addLog(LogBean.ERROR, tag, msg + '\n' + Log.getStackTraceString(throwable));
    }

    private void addLog(int level, String tag, String msg) {
        LogCanaryDelegate.getInstance().addLog(level, tag, msg);
    }

}
