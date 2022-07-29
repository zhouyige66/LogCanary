package cn.roy.logcanary.op;

import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import cn.roy.logcanary.core.LogCanaryManager;
import cn.roy.logcanary.op.component.LogListActivity;

/**
 * @Description: 日志监视窗初始化
 * @Author: Roy Z
 * @Date: 2022/7/21
 * @Version: v1.0
 */
public class LogCanaryContentProvider extends ContentProvider {

    @Override
    public boolean onCreate() {
        Context context = getContext();
        LogCanaryManager.getInstance().init(context);
        if (LogCanaryManager.getInstance().getAbility() == null) {
            LogCanaryAbilityForOp ability = new LogCanaryAbilityForOp();
            LogCanaryManager.getInstance().inject(ability);
        }

        // 创建快捷方式
        Intent intent = new Intent(context, LogListActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        ShortcutInfoCompat shortcut = new ShortcutInfoCompat.Builder(context, "id1")
                .setIcon(IconCompat.createWithResource(context, R.mipmap.ic_float_log))
                .setShortLabel("Logs")
                .setLongLabel("Open the log activity")
                .setIntent(intent)
                .build();
        boolean b = ShortcutManagerCompat.pushDynamicShortcut(context, shortcut);
        System.out.println("创建快捷方式->" + b);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

}
