package cn.roy.logcanary.demo;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import cn.roy.logcanary.core.LogCanary;
import cn.roy.logcanary.core.LogCanaryManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogCanaryManager.getInstance().init(this.getApplicationContext());
    }

    public void test(View view) {
        LogCanary.v("roy", "点击了测试：" + System.currentTimeMillis());
        LogCanary.d("yige", "点击了测试：" + System.currentTimeMillis());
        LogCanary.i("zhouyi", "点击了测试：" + System.currentTimeMillis());
        LogCanary.w("kk20", "点击了测试：" + System.currentTimeMillis());
        LogCanary.e("zzy", "点击了测试：" + System.currentTimeMillis());
    }
}