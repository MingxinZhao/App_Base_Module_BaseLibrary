package com.dream.base.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import junit.framework.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by wang on 2017/5/26.
 */

public class AsocialShareUtil {


    private static final String TAG = "SDK_Sample.Util";

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static byte[] getHtmlByteArray(final String url) {
        URL htmlUrl = null;
        InputStream inStream = null;
        try {
            htmlUrl = new URL(url);
            URLConnection connection = htmlUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = inputStreamToByte(inStream);

        return data;
    }

    public static byte[] inputStreamToByte(InputStream is) {
        try {
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                bytestream.write(ch);
            }
            byte imgdata[] = bytestream.toByteArray();
            bytestream.close();
            return imgdata;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] readFromFile(String fileName, int offset, int len) {
        if (fileName == null) {
            return null;
        }

        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }

        if (len == -1) {
            len = (int) file.length();
        }

        if (offset < 0) {
            return null;
        }
        if (len <= 0) {
            return null;
        }
        if (offset + len > (int) file.length()) {
            return null;
        }

        byte[] b = null;
        try {
            RandomAccessFile in = new RandomAccessFile(fileName, "r");
            b = new byte[len]; // 创建合适文件大小的数组
            in.seek(offset);
            in.readFully(b);
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;

    public static Bitmap extractThumbNail(final String path, final int height, final int width, final boolean crop) {
        Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);
//        if(TextUtils.isEmpty(path) || height <=0 || width <= 0) {
//            return null;
//        }

        BitmapFactory.Options options = new BitmapFactory.Options();

        try {
            options.inJustDecodeBounds = true;
            Bitmap tmp = BitmapFactory.decodeFile(path, options);
            if (tmp != null) {
                tmp.recycle();
                tmp = null;
            }
            final double beY = options.outHeight * 1.0 / height;
            final double beX = options.outWidth * 1.0 / width;
            options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
            if (options.inSampleSize <= 1) {
                options.inSampleSize = 1;
            }

            // NOTE: out of memory error
            while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
                options.inSampleSize++;
            }

            int newHeight = height;
            int newWidth = width;
            if (crop) {
                if (beY > beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            } else {
                if (beY < beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            }

            options.inJustDecodeBounds = false;
            Bitmap bm = BitmapFactory.decodeFile(path, options);
            if (bm == null) {
                return null;
            }
            final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
            if (scale != null && scale != bm) {
                bm.recycle();
                bm = scale;
            }

            if (crop) {
                final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
                if (cropped == null) {
                    return bm;
                }

                bm.recycle();
                bm = cropped;
            }
            return bm;

        } catch (final OutOfMemoryError e) {
            options = null;
        }

        return null;
    }


    public static Bitmap addWaterMark(Bitmap src, String water, Context context) {
        Bitmap tarBitmap = src.copy(Bitmap.Config.ARGB_8888, true);
        int w = tarBitmap.getWidth();
        int h = tarBitmap.getHeight();
        Canvas canvas = new Canvas(tarBitmap);
        //启用抗锯齿和使用设备的文本字距
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
        //字体的相关设置
        textPaint.setTextSize(100.0f);//字体大小
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setColor(Color.YELLOW);
        textPaint.setShadowLayer(3f, 1, 1, context.getResources().getColor(android.R.color.background_dark));
        //图片上添加水印的位置，这里设置的是中下部3/4处
        canvas.drawText(water, w / 2 - 50, (float) (h * 0.75), textPaint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return tarBitmap;
    }


    public static Bitmap compressBmpFromBmp(Bitmap image) {
        return compressBmpFromBmp(image, 100);
    }

    public static Bitmap compressBmpFromBmp(Bitmap image, int maxLength) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        while (baos.toByteArray().length / 1024 > maxLength) {
            baos.reset();
            options -= 10;
            if (options < 0) {
                options = 10;
            }
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        if (options == 100) {
            return image;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

    public static Bitmap compressBmpFromFile(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;//
        float ww = 480f;//
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置采样率

        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收

        return BitmapFactory.decodeFile(srcPath, newOpts);
    }

    public static Bitmap compressBmpSize(Bitmap image, int maxLength, float maxWidth, float maxHeight) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
//            Log.d("liuwei","baos.toByteArray().length / 1024="+baos.toByteArray().length / 1024);
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 70, baos);//这里压缩70%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是1920*1080分辨率，所以高和宽我们设置为
        float hh = 1920f;//这里设置高度为1920f
        float ww = 1080f;//这里设置宽度为1080f
        if (maxWidth > 0) {
            ww = maxWidth;
        }
        if (maxHeight > 0) {
            hh = maxHeight;
        }
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w >= h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressBmpFromBmp(bitmap, maxLength);//压缩好比例大小后再进行质量压缩
    }

    public static final boolean isValidUrl(String var0) {
        return var0 == null ? false : var0.startsWith("http://") || var0.startsWith("https://");
    }

    public static final boolean isValidPath(String var0) {
        if (var0 == null) {
            return false;
        } else {
            File var1 = new File(var0);
            return var1 != null && var1.exists();
        }
    }

    public static boolean isQQInstalled(Context context) {
        boolean installed = false;
        PackageManager var2 = context.getPackageManager();
        try {
            PackageInfo var3 = var2.getPackageInfo("com.tencent.mobileqq", 0);
            installed = true;
        } catch (PackageManager.NameNotFoundException var5) {
        }

        return installed;
    }

    public static byte[] Bitmap2Bytes(Bitmap var1) {
        byte[] imageData = null;
        try {
            ByteArrayOutputStream var2 = new ByteArrayOutputStream();
            var1.compress(Bitmap.CompressFormat.JPEG, 85, var2);
            imageData = var2.toByteArray();
            var2.close();
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return imageData;
    }
}
