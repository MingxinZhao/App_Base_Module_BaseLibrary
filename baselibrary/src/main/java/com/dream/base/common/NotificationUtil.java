package com.dream.base.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.text.SimpleDateFormat;
import java.util.Date;

import dream.base.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by wang on 2017/4/24.
 */

public class NotificationUtil {

    private SimpleDateFormat format;

    private NotificationManager notificationManager;

    private static NotificationUtil instance;

    private NotificationUtil(Context context) {
        format = new SimpleDateFormat("HH:mm");
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    }

    public static NotificationUtil getInstance(Context context) {
        if (instance == null) {
            instance = new NotificationUtil(context);
        }
        return instance;
    }


    /**
     * 设置notification参数
     *
     * @param context
     * @param title
     * @param description
     * @param image
     * @param intent
     * @param code
     */
    public void setNotificationData(final Context context, final String title, final String description, final String image, Intent intent, final int code, final int logoRes) {

        try {

            Date date = new Date();
            final String timeFormat = format.format(date);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            if (intent != null) {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);
            } else {
                builder.setContentIntent(getDefaultIntent(context, Notification.FLAG_AUTO_CANCEL));
            }

            builder.setContentTitle(title).setContentText(description);
            builder.setTicker("新消息")//通知首次出现在通知栏，带上升动画效果的
                    .setWhen(date.getTime())//通知产生的时间，会在通知信息里显示
                    .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
                    .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
//                    .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                    .setSmallIcon(logoRes)
                    .setLights(Color.GREEN, 300, 1000);

            if (!TextUtils.isEmpty(image)) {
                Glide.with(context).load(image).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                if (resource != null) {
                                    builder.setLargeIcon(resource);
                                } else {
                                    builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), logoRes));
                                }

                                showNotification(builder, code, context, image, title, description, timeFormat);
                            }
                        });
            } else {
                builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), logoRes));
                showNotification(builder, code, context, image, title, description, timeFormat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNotification(NotificationCompat.Builder builder, int code, Context context, String image, String title, String description, String timeFormat) {
        Notification notification = builder.build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(code, notification);
    }

    private PendingIntent getDefaultIntent(Context context, int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, new Intent(), flags);
        return pendingIntent;
    }

}
