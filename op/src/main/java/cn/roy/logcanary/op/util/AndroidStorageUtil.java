package cn.roy.logcanary.op.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description: Android存储工具
 * @Author: Roy Z
 * @Date: 2019-08-07 17:49
 * @Version: v1.0
 */
public class AndroidStorageUtil {
    public static final int CODE_APPLY_STORAGE_PERMISSION = 98760;

    /**
     * 判断存储权限是否已经被授予
     *
     * @param context
     * @return
     */
    public static boolean isStoragePermissionGranted(Context context) {
        boolean hasPermission = false;
        // 以23版本为界（Build.VERSION_CODES.M）
        int targetSDKVersion = 0;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            targetSDKVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int sdkInt = Build.VERSION.SDK_INT;
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (sdkInt >= 23) {// Android 6.0及以上
            if (targetSDKVersion >= 23) {// 动态授权，检测方案不同
                hasPermission = context.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {// 使用兼容类PermissionChecker
                hasPermission = PermissionChecker.checkSelfPermission(context, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        } else {
            // 检测结果不准确，有可能被应用管家之类的关闭了，最好是try catch处理一下
            hasPermission = ContextCompat.checkSelfPermission(context, permission) ==
                    PackageManager.PERMISSION_GRANTED;
        }

        return hasPermission;
    }

    /**
     * 申请存储权限
     *
     * @param activity
     */
    public static void applyStoragePermission(Activity activity) {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        ActivityCompat.requestPermissions(activity, permissions, CODE_APPLY_STORAGE_PERMISSION);
    }

    /**
     * 判断外置sd卡是否挂载
     *
     * @param mContext
     * @return
     */
    public static boolean isSDCardMounted(Context mContext) {
        boolean isMounted = false;
        StorageManager mStorageManager = (StorageManager)
                mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Method getState = storageVolumeClazz.getMethod("getState");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                String state = (String) getState.invoke(storageVolumeElement);
                if (removable && state.equals(Environment.MEDIA_MOUNTED)) {
                    isMounted = true;
                    break;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return isMounted;
    }

    /**
     * 获取SDCard根路径
     *
     * @return
     */
    public static String getSDCardDir() {
        if (isExternalStorageAvailable()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            return null;
        }
    }

    /**
     * 检测外部存储（SD卡等）是否可读写
     *
     * @return
     */
    public static boolean isExternalStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取应用内部文件存储路径
     *
     * @return
     */
    public static String getInternalFilesDir(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }

    /**
     * 获取应用外部文件存储目录
     *
     * @param context
     * @return
     */
    public static String getExternalFilesDir(Context context) {
        return context.getExternalFilesDir(null).getAbsolutePath() + File.separator;
    }

    /**********TODO 功能：存储容量相关方法**********/

    /**
     * 获取内部存储容量（内部存储总容量-系统占用容量）
     *
     * @return
     */
    public static long getInternalTotalSize() {
        long[] size = getFileSpaceInformation(Environment.getDataDirectory());
        if (size == null) {
            return -1;
        }
        return size[0];
    }

    /**
     * 获取内部存储可用容量
     *
     * @return
     */
    public static long getInternalAvailableSize() {
        long[] size = getFileSpaceInformation(Environment.getDataDirectory());
        if (size == null) {
            return -1;
        }
        return size[1];
    }

    /**
     * 获取SD卡容量大小（sd卡总容量-系统占用容量）
     *
     * @return
     */
    public static long getSDCardTotalSize() {
        if (!isExternalStorageAvailable()) {
            return -1;
        }

        long[] size = getFileSpaceInformation(Environment.getExternalStorageDirectory());
        if (size == null) {
            return -1;
        }
        return size[0];
    }

    /**
     * 获取SD卡可用容量大小
     *
     * @return
     */
    public static long getSDCardAvailableSize() {
        if (!isExternalStorageAvailable()) {
            return -1;
        }

        long[] size = getFileSpaceInformation(Environment.getExternalStorageDirectory());
        if (size == null) {
            return -1;
        }
        return size[1];
    }

    /**
     * 根据文件获取总容量、可用容量、空余容量（包含可用于不可用部分）
     *
     * @param file Environment.getRootDirectory();
     *             Environment.getDataDirectory();
     *             Environment.getExternalStorageDirectory();
     *             Environment.getDownloadCacheDirectory();
     *             Environment.getExternalStoragePublicDirectory();
     * @return [总容量，可用容量，空余容量]
     */
    public static long[] getFileSpaceInformation(File file) {
        if (!file.exists()) {
            return null;
        }

        StatFs statFs = new StatFs(file.getAbsolutePath());
        // 块大小
        long blockSize = statFs.getBlockSize();
        // 存储块总数量
        long blockCount = statFs.getBlockCount();
        // 可用块数量
        long availableCount = statFs.getAvailableBlocks();
        // 剩余块数量，注：这个包含保留块（including reserved blocks）即应用无法使用的空间
        long freeBlocks = statFs.getFreeBlocks();

        long[] size = new long[3];
        size[0] = blockSize * blockCount;
        size[1] = blockSize * availableCount;
        size[2] = blockSize * freeBlocks;

        return size;
    }

}
