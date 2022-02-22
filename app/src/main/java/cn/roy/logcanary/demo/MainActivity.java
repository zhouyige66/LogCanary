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
        LogCanary.d("roy", "点击了测试：" + System.currentTimeMillis());
    }
}