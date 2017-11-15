package com.colorchen.tool;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.List;

/**
 * des：关于app 系统级别的工具类
 * Author ChenQ on 2017-9-27
 * email：wxchenq@yutong.com
 * use:
 *  获取app版本名- getAppVersionName
 *  判断是否是android- isFiveAndroid
 *  判断当前应用程序处于前台还是后台 - isApplicationBroughtToBackground
 *  获得app的版本名字 - getVersionName
 *  获得App的版本号 - getAppVersionCode
 *  是否安装谷歌商店 - isInstallGooglePlay
 *  是都安装谷歌服务 - isInstallGoogleServices
 *  是都安装某个app -  isAppInstalled
 *  获得本手机型号 - getPhoneModel
 *  判断是否是WIfi - isWifi
 *  跳转拨号页面 - goTocCallPhonePage
 *  跳转拨号页面并拨打电话 - callPhone
 *  跳转到设置页面 - goToSysSetting
 *  是否安装某个app - isInstallPackage
 *
 */
public class AppUtil {
    private Application application;
    private static AppUtil INSTANCE;
    public static boolean DEBUG = false;
    public static boolean API_TEST = false;

    public static final AppUtil getInstance() {
        if (null == INSTANCE) {
            synchronized (AppUtil.class) {
                if (null == INSTANCE) {
                    INSTANCE = new AppUtil();
                }
            }
        }
        return INSTANCE;
    }

    private AppUtil() {
    }

    public void init(Application application) {
        this.application = application;
    }

    public Context getApplication() {
        return this.application;
    }

    public String getStringFromResouce(@StringRes int resId) {
        return application.getResources().getString(resId);
    }

    public Drawable getDrawableFromResouce(@DrawableRes int resId) {
        return ContextCompat.getDrawable(application, resId);
    }

    public int getColorFromResource(@ColorRes int resId) {
        return ContextCompat.getColor(application, resId);
    }
    /**
     * 获取app版本名
     */
    public static String getAppVersionName() {
        PackageManager pm = getInstance().application.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(getInstance().application.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断是否是android 5.0 以上
     *
     * @return
     */
    public static boolean isFiveAndroid() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * 判断当前应用程序处于前台还是后台
     *
     * @return 如果运行在后台就返回true
     */
    public static boolean isApplicationBroughtToBackground() {
        ActivityManager am = (ActivityManager) getInstance().getApplication().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(getInstance().getApplication().getPackageName())) {
                return true;
            }
        }
        return false;

    }

    /**
     * 获取应用程序版本名称信息
     */
    public static String getVersionName() {
        try {
            PackageManager packageManager = getInstance().application.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getInstance().getApplication().getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取app版本号
     */
    public static int getAppVersionCode() {
        PackageManager pm = getInstance().getApplication().getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(getInstance().getApplication().getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 用于得到最优的线程池数量大小
     *
     * @param max
     * @return
     */
    public static int getDefaultThreadPoolSize(int max) {
        int availableProcessors = 2 * Runtime.getRuntime().availableProcessors() + 1;
        return availableProcessors > max ? max : availableProcessors;
    }

    /**
     * 谷歌商店是否安装
     *
     * @return
     */
    public static boolean isInstallGooglePlay() {
        return isAppInstalled(getInstance().getApplication(), "com.android.vending");
    }

    /**
     * 谷歌服务是否安装
     *
     * @return
     */
    public static boolean isInstallGoogleServices() {
        return isAppInstalled(getInstance().application, "com.google.android.gms");
    }

    /**
     * 是否安装xx app
     * @param context
     * @param packageName 包名
     * @return
     */
    private static boolean isAppInstalled(Context context, String packageName) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        if (packages != null && packages.size() > 0) {
            for (PackageInfo packageInfo : packages) {
                if (packageInfo.packageName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获得手机型号信息
     *
     * @return
     */
    public static String getPhoneModel() {
        return Build.MANUFACTURER + " " + Build.MODEL;
    }


    /**
     * 五、判断是wifi还是3g网络,用户的体现性在这里了，wifi就可以建议下载或者在线播放。
     *
     * @return
     */

    public static boolean isWifi() {
        ConnectivityManager cm = (ConnectivityManager) getInstance().application
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        return networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static void goTocCallPhonePage(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));//直接跳转拨号页面
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ActivityCompat.checkSelfPermission(getInstance().getApplication(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            goToSysSetting();
            Toast.makeText(getInstance().application, "请设置拨号权限！", Toast.LENGTH_SHORT).show();
            return;
        }
        getInstance().getApplication().startActivity(intent);
    }

    public static void callPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));//直接拨号
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ActivityCompat.checkSelfPermission(getInstance().getApplication(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            goToSysSetting();
            Toast.makeText(getInstance().application, "请设置拨号权限！", Toast.LENGTH_SHORT).show();
            return;
        }
        getInstance().getApplication().startActivity(intent);
    }

    /**
     * 跳转到系统设置页面
     */
    public static void goToSysSetting() {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        getInstance().getApplication().startActivity(intent);
    }

    /**
     * 是否安装某个软件
     *
     * @param context 上下文
     * @param pkgName app 包名
     * @return true 已安装 false 反之
     */
    public static boolean isInstallPackage(Context context, String pkgName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo != null;
    }

}
