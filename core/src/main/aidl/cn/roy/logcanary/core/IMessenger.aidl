package cn.roy.logcanary.core;

import cn.roy.logcanary.core.ISendMessageCallback;
import cn.roy.logcanary.core.IMessageReceiver;

interface IMessenger {
    boolean isEnable();

    void sendMessage(String message,ISendMessageCallback callback);

    void registerReceiver(IMessageReceiver receiver);

    void unregisterReceiver(IMessageReceiver receiver);

    void start();

    void stop();
}