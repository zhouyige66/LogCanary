package cn.roy.server.impl;

import android.os.RemoteException;

import cn.roy.logcanary.core.IMessageReceiver;
import cn.roy.logcanary.core.ISendMessageCallback;
import cn.roy.logcanary.core.LogCanary;

/**
 * @Description
 * @Author zhouyige66
 * @Date 2022/7/30
 * @Version V1.0.0
 */
public class MessengerDelegate {
    private static final String TAG = MessengerDelegate.class.getSimpleName();

    public boolean isEnable() {
        LogCanary.d(TAG, "isEnable开始");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LogCanary.d(TAG, "isEnable结束");
        return true;
    }

    public void sendMessage(String message, ISendMessageCallback callback) {
        LogCanary.d(TAG, "sendMessage开始");
        try {
            Thread.sleep(3000);
            LogCanary.d(TAG, "callback.success开始");
            callback.success(0);
            LogCanary.d(TAG, "callback.success结束");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        LogCanary.d(TAG, "sendMessage结束");
    }

    public void registerReceiver(IMessageReceiver receiver) {
        LogCanary.d(TAG, "registerReceiver开始");
        try {
            Thread.sleep(3000);
            receiver.receive("收到消息");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        LogCanary.d(TAG, "registerReceiver结束");
    }

    public void unregisterReceiver(IMessageReceiver receiver) {
        LogCanary.d(TAG, "unregisterReceiver开始");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LogCanary.d(TAG, "unregisterReceiver结束");
    }

    public void start() {
        LogCanary.d(TAG, "start开始");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LogCanary.d(TAG, "start结束");
    }

    public void stop() {
        LogCanary.d(TAG, "stop开始");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LogCanary.d(TAG, "stop结束");
    }
}
