package cn.roy.logcanary.demo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import cn.roy.logcanary.core.IMessageReceiver;
import cn.roy.logcanary.core.IMessenger;
import cn.roy.logcanary.core.IMessengerPlus;
import cn.roy.logcanary.core.ISendMessageCallback;
import cn.roy.logcanary.core.LogCanary;
import cn.roy.logcanary.core.LogCanaryManager;

public class MainActivity extends AppCompatActivity {
    private IMessenger messenger;
    private IMessengerPlus messengerPlus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogCanaryManager.getInstance().init(this.getApplicationContext());
    }

    public void test(View view) {
        switch (view.getId()) {
            case R.id.btnLogV:
                LogCanary.v("roy", "点击了测试：" + System.currentTimeMillis());
                break;
            case R.id.btnLogD:
                LogCanary.d("yige", "点击了测试：" + System.currentTimeMillis());
                break;
            case R.id.btnLogI:
                LogCanary.i("zhouyi", "点击了测试：" + System.currentTimeMillis());
                break;
            case R.id.btnLogW:
                LogCanary.w("kk20", "点击了测试：" + System.currentTimeMillis());
                break;
            case R.id.btnLogE:
                LogCanary.e("zzy", "点击了测试：" + System.currentTimeMillis());
                break;
            case R.id.btnBindMessenger:
                LogCanary.d("Messenger", "绑定Messenger");
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("cn.roy.server", "cn.roy.server.ServerService"));
                bindService(intent, serviceConnection, BIND_AUTO_CREATE);
                view.setEnabled(false);
                break;
            case R.id.btnIsEnable:
                LogCanary.d("Messenger", "调用Messenger.isEnable()开始");
                try {
                    messenger.isEnable();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                LogCanary.d("Messenger", "调用Messenger.isEnable()结束");
                break;
            case R.id.btnSendMessage:
                LogCanary.d("Messenger", "调用Messenger.sendMessage()开始");
                try {
                    messenger.sendMessage("", new ISendMessageCallback.Stub() {
                        @Override
                        public void success(int msgId) throws RemoteException {
                            LogCanary.d("Messenger", "调用SendMessageCallback.success()开始");
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            LogCanary.d("Messenger", "调用SendMessageCallback.success()结束");
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                LogCanary.d("Messenger", "调用Messenger.sendMessage()结束");
                break;
            case R.id.btnRegisterReceiver:
                LogCanary.d("Messenger", "调用Messenger.registerReceiver()开始");
                try {
                    messenger.registerReceiver(new IMessageReceiver.Stub() {
                        @Override
                        public void receive(String message) throws RemoteException {

                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                LogCanary.d("Messenger", "调用Messenger.registerReceiver()结束");
                break;
            case R.id.btnStart:
                LogCanary.d("Messenger", "调用Messenger.start()开始");
                try {
                    messenger.start();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                LogCanary.d("Messenger", "调用Messenger.start()结束");
                break;
            case R.id.btnStop:
                LogCanary.d("Messenger", "调用Messenger.stop()开始");
                try {
                    messenger.stop();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                LogCanary.d("Messenger", "调用Messenger.stop()结束");
                break;
            case R.id.btnBindMessengerPlus:
                LogCanary.d("MessengerPlus", "绑定MessengerPlus");
                Intent intent2 = new Intent();
                intent2.setComponent(new ComponentName("cn.roy.server", "cn.roy.server.ServerPlusService"));
                bindService(intent2, serviceConnection2, BIND_AUTO_CREATE);
                break;
            case R.id.btnIsEnable2:
                LogCanary.d("MessengerPlus", "调用MessengerPlus.isEnable()开始");
                try {
                    messengerPlus.isEnable();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                LogCanary.d("MessengerPlus", "调用MessengerPlus.isEnable()结束");
                break;
            case R.id.btnSendMessage2:
                LogCanary.d("MessengerPlus", "调用MessengerPlus.sendMessage()开始");
                try {
                    messengerPlus.sendMessage("", new ISendMessageCallback.Stub() {
                        @Override
                        public void success(int msgId) throws RemoteException {
                            LogCanary.d("MessengerPlus", "调用SendMessageCallback.success()开始");
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            LogCanary.d("MessengerPlus", "调用SendMessageCallback.success()结束");
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                LogCanary.d("MessengerPlus", "调用MessengerPlus.sendMessage()结束");
                break;
            case R.id.btnRegisterReceiver2:
                LogCanary.d("MessengerPlus", "调用MessengerPlus.registerReceiver()开始");
                try {
                    messengerPlus.registerReceiver(new IMessageReceiver.Stub() {
                        @Override
                        public void receive(String message) throws RemoteException {

                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                LogCanary.d("MessengerPlus", "调用MessengerPlus.registerReceiver()结束");
                break;
            case R.id.btnStart2:
                LogCanary.d("MessengerPlus", "调用MessengerPlus.start()开始");
                try {
                    messengerPlus.start();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                LogCanary.d("MessengerPlus", "调用MessengerPlus.start()结束");
                break;
            case R.id.btnStop2:
                LogCanary.d("MessengerPlus", "调用MessengerPlus.stop()开始");
                try {
                    messengerPlus.stop();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                LogCanary.d("MessengerPlus", "调用MessengerPlus.stop()结束");
                break;
            default:
                break;
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogCanary.i("Messenger", "绑定Messenger成功");
            messenger = IMessenger.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            findViewById(R.id.btnBindMessenger).setEnabled(true);
        }
    };

    ServiceConnection serviceConnection2 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogCanary.i("Messenger", "绑定MessengerPlus成功");
            messengerPlus = IMessengerPlus.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            findViewById(R.id.btnBindMessengerPlus).setEnabled(true);
        }
    };

}