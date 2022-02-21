package cn.roy.logcanary.op.component;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cn.roy.logcanary.op.R;
import cn.roy.logcanary.op.util.AppOpsManagerUtil;

/**
 * @Description 悬浮窗权限请求授权页面
 * @Author Roy Z
 * @Date 2018/5/1
 * @Version V1.0.0
 */
public class ApplyAlertWindowPermissionActivity extends AppCompatActivity {
    private static final int OVERLAYS_CODE = 10001;

    public static void applyAlertWindowPermission(Context context) {
        Intent intent = new Intent(context, ApplyAlertWindowPermissionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppOpsManagerUtil.checkDrawOverlays(this)) {
            Toast.makeText(this, "已经获取授权，无需再次获取", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            showApplyPermissionDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAYS_CODE) {
            if (AppOpsManagerUtil.checkDrawOverlays(this)) {
                setResult(RESULT_OK);
            } else {
                Toast.makeText(this, "获取\"悬浮窗\"权限失败", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
            }
            finish();
        }
    }

    @TargetApi(23)
    private void requestDrawOverlays() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, OVERLAYS_CODE);
    }

    private BottomSheetDialog applyPermissionTipDialog = null;

    private void showApplyPermissionDialog() {
        View view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_apply_alert_window_permission, null);
        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
                    applyPermissionTipDialog.dismiss();
                    setResult(RESULT_CANCELED);
                    finish();
                }
        );
        view.findViewById(R.id.btn_confirm).setOnClickListener(v -> {
                    applyPermissionTipDialog.dismiss();
                    requestDrawOverlays();
                }
        );
        applyPermissionTipDialog = new BottomSheetDialog(this);
        applyPermissionTipDialog.setContentView(view);
        applyPermissionTipDialog.setCancelable(false);
        applyPermissionTipDialog.setCanceledOnTouchOutside(false);
        applyPermissionTipDialog.show();
    }

    /**
     * 获取屏幕高度（包含状态栏、导航栏）
     *
     * @return
     */
    private int getRealDisplayHeight() {
        int height = 0;
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            Class c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            height = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return height;
    }

    /**
     * 获取应用显示区高度（不包含状态栏、导航栏）
     *
     * @return
     */
    private int getDefaultDisplayHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        if (statusBarHeight == 0) {
            try {
                Class c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    /**
     * 获取虚拟按键导航栏高度
     *
     * @return
     */
    private int getNavigationBarHeight() {
        int height = getNavigationBarHeight1();
        if (height == -1) {
            height = getNavigationBarHeight2();
        }
        return height;
    }

    /**
     * 获取虚拟按键导航栏高度(方法一)
     *
     * @return
     */
    private int getNavigationBarHeight1() {
        int height = getRealDisplayHeight();
        if (height == 0) {
            return -1;
        }
        int vh = height - getDefaultDisplayHeight();
        return vh;
    }

    /**
     * 获取虚拟按键导航栏高度（方法二）
     *
     * @return
     */
    private int getNavigationBarHeight2() {
        if (!isNavigationBarShow()) {
            return 0;
        }
        Resources resources = this.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen",
                "android");
        //获取NavigationBar的高度
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 判断导航栏是否显示
     *
     * @return
     */
    private boolean isNavigationBarShow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(this).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if (menu || back) {
                return false;
            } else {
                return true;
            }
        }
    }

}
