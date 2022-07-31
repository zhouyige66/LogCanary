package cn.roy.server.impl;

import android.os.RemoteException;

import cn.roy.logcanary.core.IMessageReceiver;
import cn.roy.logcanary.core.IMessenger;
import cn.roy.logcanary.core.ISendMessageCallback;

/**
 * @Description 消息实现
 * @Author zhouyige66
 * @Date 2022/7/30
 * @Version V1.0.0
 */
public class Messenger extends IMessenger.Stub {
    private MessengerDelegate delegate = new MessengerDelegate();

    @Override
    public boolean isEnable() throws RemoteException {
        return delegate.isEnable();
    }

    @Override
    public void sendMessage(String message, ISendMessageCallback callback) throws RemoteException {
        delegate.sendMessage(message, callback);
    }

    @Override
    public void registerReceiver(IMessageReceiver receiver) throws RemoteException {
        delegate.registerReceiver(receiver);
    }

    @Override
    public void unregisterReceiver(IMessageReceiver receiver) throws RemoteException {
        delegate.unregisterReceiver(receiver);
    }

    @Override
    public void start() throws RemoteException {
        delegate.start();
    }

    @Override
    public void stop() throws RemoteException {
        delegate.stop();
    }
}
