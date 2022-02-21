package cn.roy.logcanary.op.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

/**
 * @Description 权限检查工具
 * @Author Roy Z
 * @Date 2018/1/18
 * @Version V1.0.0
 */
public class AppOpsManagerUtil {

    @TargetApi(19)
    public static boolean check(Context context, String op) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            try {
                mode = appOps.checkOp(op, android.os.Process.myUid(), context.getPackageName());
            } catch (SecurityException e) {
                e.printStackTrace();
                mode = 1;
            }
        }

        return mode == AppOpsManager.MODE_ALLOWED;
    }

    @TargetApi(21)
    public static boolean checkUsageState(Context context) {
        return check(context, AppOpsManager.OPSTR_GET_USAGE_STATS);
    }

    /**
     * 检查是否获取悬浮窗权限
     *
     * @param context
     * @return true-已获取，false-未获取
     */
    public static boolean checkDrawOverlays(Context context) {
        boolean canDrawOverlays;
        if (Build.VERSION.SDK_INT >= 23) {
            canDrawOverlays = Settings.canDrawOverlays(context);
        } else {
            String permission = Manifest.permission.SYSTEM_ALERT_WINDOW;
            canDrawOverlays = ActivityCompat.checkSelfPermission(context, permission) ==
                    PermissionChecker.PERMISSION_GRANTED;
        }
        return canDrawOverlays;
    }

}
