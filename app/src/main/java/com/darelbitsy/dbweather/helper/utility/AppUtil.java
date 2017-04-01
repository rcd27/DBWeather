package com.darelbitsy.dbweather.helper.utility;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import com.darelbitsy.dbweather.R;
import com.darelbitsy.dbweather.adapters.FeedDataInForeground;
import com.darelbitsy.dbweather.helper.AlarmConfigHelper;
import com.darelbitsy.dbweather.helper.holder.ConstantHolder;
import com.darelbitsy.dbweather.helper.receiver.AlarmWeatherReceiver;
import com.darelbitsy.dbweather.helper.services.KillCheckerService;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

import static com.darelbitsy.dbweather.helper.AlarmConfigHelper.MY_ACTION;
import static com.darelbitsy.dbweather.helper.holder.ConstantHolder.IS_ACCOUNT_PERMISSION_GRANTED;
import static com.darelbitsy.dbweather.helper.holder.ConstantHolder.IS_ALARM_ON;
import static com.darelbitsy.dbweather.helper.holder.ConstantHolder.IS_GPS_PERMISSION_GRANTED;
import static com.darelbitsy.dbweather.helper.holder.ConstantHolder.LIST_OF_TYPEFACES;
import static com.darelbitsy.dbweather.helper.holder.ConstantHolder.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.darelbitsy.dbweather.helper.holder.ConstantHolder.MY_PERMiSSIONS_REQUEST_GET_ACCOUNT;
import static com.darelbitsy.dbweather.helper.holder.ConstantHolder.PREFS_NAME;
import static com.darelbitsy.dbweather.helper.holder.ConstantHolder.USER_LANGUAGE;

/**
 * Created by Darel Bitsy on 22/02/17.
 */

public class AppUtil {

    public static final OkHttpClient translateOkHttpClient = new OkHttpClient.Builder()
            .connectTimeout(25, TimeUnit.SECONDS)
            .writeTimeout(25, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    public static final OkHttpClient.Builder weatherOkHttpClient = new OkHttpClient.Builder()
            .connectTimeout(25, TimeUnit.SECONDS)
            .writeTimeout(25, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true);

    public static final OkHttpClient.Builder newsOkHttpClient = new OkHttpClient.Builder()
            .connectTimeout(25, TimeUnit.SECONDS)
            .writeTimeout(25, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true);


    private AppUtil() {}

    public static Cache getCacheDirectory(Context context) {

        return new Cache(getFileCache(context), ConstantHolder.CACHE_SIZE);
    }

    public static File getFileCache(Context context) {
        return new File(context.getCacheDir(), "dbweather_cache_dir");
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo networkInfo;
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    public static boolean isAlarmSet(Context context) {
        int lastAlarm = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE)
                .getInt(AlarmConfigHelper.LAST_NOTIFICATION_PENDING_INTENT_ID, 0);
        if (lastAlarm == 0 ) { return false; }

        Intent notificationLIntent = new Intent(context, AlarmWeatherReceiver.class);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            notificationLIntent.setFlags(0);
        } else {
            notificationLIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        notificationLIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        notificationLIntent.setAction(MY_ACTION);

        return PendingIntent.getBroadcast(context.getApplicationContext(),
                lastAlarm,
                notificationLIntent,
                PendingIntent.FLAG_NO_CREATE) != null;
    }

    public static void askLocationPermIfNeeded(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            AppUtil.setGpsPermissionValue(activity);

        }
    }

    public static void askAccountInfoPermIfNeeded(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[] {Manifest.permission.GET_ACCOUNTS},
                    MY_PERMiSSIONS_REQUEST_GET_ACCOUNT);

        } else {
            AppUtil.setAccountPermissionValue(activity);
        }
    }

    public static boolean isAccountPermissionOn(Context context) {
        return context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE)
                .getBoolean(IS_ACCOUNT_PERMISSION_GRANTED, false);
    }

    public static void setAccountPermissionValue(Context context) {
        context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE)
                .edit()
                .putBoolean(IS_ACCOUNT_PERMISSION_GRANTED, true)
                .apply();
    }

    public static boolean isGpsPermissionOn(Context context) {
        return context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE)
                .getBoolean(IS_GPS_PERMISSION_GRANTED, false);
    }

    public static void setGpsPermissionValue(Context context) {
        context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE)
                .edit()
                .putBoolean(IS_GPS_PERMISSION_GRANTED, true)
                .apply();
    }


    public static void setNextAlarm(Context context) {
        ExecutorService executorService;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            executorService = new ForkJoinPool();
            executorService.submit(new AlarmConfigHelper(context)::setClothingNotificationAlarm);
            Log.i(ConstantHolder.TAG, "Setted the alarm ");

            executorService.submit(() -> FeedDataInForeground.setNextSync(context.getApplicationContext()));
            Log.i(ConstantHolder.TAG, "Setted the hourly sync");

        } else {
            executorService = Executors.newCachedThreadPool();
            executorService.submit(new AlarmConfigHelper(context)::setClothingNotificationAlarm);
            Log.i(ConstantHolder.TAG, "Setted the alarm ");

            executorService.submit(() -> FeedDataInForeground.setNextSync(context.getApplicationContext()));
            Log.i(ConstantHolder.TAG, "Setted the hourly sync ");
        }

        context.startService(new Intent(context, KillCheckerService.class));
        executorService.shutdown();

        context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE)
                .edit()
                .putBoolean(IS_ALARM_ON, true)
                .apply();
    }

    public static Typeface getAppGlobalTypeFace(Context context) {
        Typeface typeface = null;

        for (List<String> languages : LIST_OF_TYPEFACES.keySet()) {
            if (languages.contains(USER_LANGUAGE)) {
                typeface = Typeface.createFromAsset(context.getAssets(),
                        LIST_OF_TYPEFACES.get(languages));
            }
        }

        return typeface;
    }

    public static void setupVideoBackground(int resourceId, Context context, View view) {
        VideoView background = (VideoView) view.findViewById(R.id.backgroundVideo);
        background.stopPlayback();
        background.setVideoURI(Uri.parse("android.resource://" + context.getPackageName() + "/" + resourceId));
        background.setOnPreparedListener(mediaPlayer -> mediaPlayer.setLooping(true));
        if (background.getVisibility() != View.VISIBLE) {
            background.setVisibility(View.VISIBLE);
        }
        background.start();
    }
}