package cn.roy.logcanary.core;

import cn.roy.logcanary.core.ISendMessageCallback;
import cn.roy.logcanary.core.IMessageReceiver;

interface IMessengerPlus {
    boolean isEnable();

    oneway void sendMessage(String message,ISendMessageCallback callback);

    oneway void registerReceiver(IMessageReceiver receiver);

    oneway void unregisterReceiver(IMessageReceiver receiver);

    oneway void start();

    oneway void stop();
}