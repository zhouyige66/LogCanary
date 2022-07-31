package cn.roy.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import cn.roy.server.impl.MessengerPlus;

/**
 * @Description
 * @Author zhouyige66
 * @Date 2022/7/30
 * @Version V1.0.0
 */
public class ServerPlusService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MessengerPlus();
    }

}
