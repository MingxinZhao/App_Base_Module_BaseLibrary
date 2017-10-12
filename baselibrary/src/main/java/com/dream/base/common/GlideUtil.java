package com.dream.base.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import dream.base.R;

/**
 * Description: TODO
 * author: Wang
 * date: 4/15/17 09:22
 * email:life_artist@163.com
 * Copyright©2017 by wang. All rights reserved.
 */
public class GlideUtil {

    public static void loadCircleImage(final Context context, final ImageView imageView, String path) {
        loadCircleImage(context, imageView, path, R.mipmap.ic_default_avatar_icon);
    }

    public static void loadCircleImage(final Context context, final ImageView imageView, Uri path) {
        if (path == null) {
            loadErrorImage(context, imageView, R.mipmap.ic_default_avatar_icon);
            return;
        }

        Glide.with(context).load(path).asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable;
                        if (resource == null) {
                            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_default_avatar_icon);
                            circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
                        } else {
                            circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        }
                        circularBitmapDrawable.setCircular(true);
                        if (imageView != null) {
                            imageView.setImageDrawable(circularBitmapDrawable);
                        }
                    }
                });
    }

    public static void loadCircleImage(final Context context, final ImageView imageView, String path, final int errRes) {

        if (TextUtils.isEmpty(path)) {
            loadErrorImage(context, imageView, errRes);
            return;
        }

        Glide.with(context).load(path).asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable;
                        if (resource == null) {
                            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), errRes);
                            circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
                        } else {
                            circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        }
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    private static void loadErrorImage(Context context, ImageView imageView, int errRes) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), errRes);
        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        circularBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(circularBitmapDrawable);
    }

}
