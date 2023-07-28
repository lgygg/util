package com.lgy.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 主要针对图片的处理
 */
public class PhotoUtil {

    /**
     * 图像常用压缩 正常情况下我们应该把两者相结合的，所以有了下面的算法（在项目中直接用，清晰度在手机上没问题）
     *
     * @param path         图片的路径
     * @param savePath     图片的保存路径
     * @param imageMaxSize 压缩目标：例如，如果值设为100，意思就是压缩的图片大小小于100k
     */
    @SuppressLint("NewApi")
    public static File scale(String path, String savePath, int imageMaxSize) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        File outputFile = new File(path);
        long fileSize = outputFile.length();
        final long fileMaxSize = (imageMaxSize <= 0 ? 100 : imageMaxSize) * 1024;
        if (fileSize >= fileMaxSize) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, options);
                int height = options.outHeight;
                int width = options.outWidth;
                double scale = Math.sqrt((float) fileSize / fileMaxSize);
                options.outHeight = (int) (height / scale);
                options.outWidth = (int) (width / scale);
                options.inSampleSize = (int) (scale + 0.5);
                options.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                outputFile = new File(TextUtils.isEmpty(savePath)?createImageFile().getPath():savePath);
                FileOutputStream fos = null;
                fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                fos.close();
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                } else {
                    File tempFile = outputFile;
                    outputFile = new File(TextUtils.isEmpty(savePath)?createImageFile().getPath():savePath);
                    copyFileUsingFileChannels(tempFile, outputFile);
                }

            } catch (Exception e) {

                e.printStackTrace();
                return null;
            }

        }
        return outputFile;
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        byte[] byteArray = null;
        ByteArrayOutputStream stream = null;
        try {
            stream = new ByteArrayOutputStream();
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return byteArray;
    }

    public static Bitmap byteArrayToBitmap(byte[] byteArr) {
        return BitmapFactory.decodeByteArray(byteArr, 0, byteArr.length);
    }

    @SuppressLint("NewApi")
    public static Uri createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = null;
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Save a file: path for use with ACTION_VIEW intents
        return Uri.fromFile(image);
    }

    public static File createImageFile(Activity activity) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/pics");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    public static void copyFileUsingFileChannels(File source, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            try {
                inputChannel = new FileInputStream(source).getChannel();
                outputChannel = new FileOutputStream(dest).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            } catch (IOException e) {

                e.printStackTrace();
            }
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    /**
     * 质量压缩
     */
    public static String compressImage(Bitmap image, File file, int imageMaxSize) {

        if (image == null || file == null) {
            return null;
        }
        FileOutputStream fos = null;
        ByteArrayOutputStream baos = null;
        try {
            if (imageMaxSize <= 0) {
                imageMaxSize = 200;
            }
            baos = new ByteArrayOutputStream();
            int options = 100;
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            while (baos.toByteArray().length / 1024 > imageMaxSize) {  //循环判断如果压缩后图片是否大于200kb,大于继续压缩
                baos.reset();//重置baos即清空baos
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
                options -= 10;//每次都减少10
                if (options <= 0) {
                    break;
                }
            }

            byte[] byteArr = baos.toByteArray();
            if (byteArr == null || byteArr.length == 0) {
                return null;
            }
            fos = new FileOutputStream(file);
            fos.write(byteArr);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) {
                    fos.close();//关闭输出流
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.getPath();
    }


    //保存图片
    public static void saveBitmap(Bitmap bitmap, String path) throws Exception{

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] buffer = bos.toByteArray();
        if (buffer != null) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(buffer);
            outputStream.close();
        }
    }


    public static Bitmap getImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, newOpts);
        // 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 1280;// 这里设置高度为800f
        float ww = 1280;// 这里设置宽度为480f
        int be = 1;// be=1表示不缩放
        if (w > 1280 || h > 1280) {
            if (w > h && w > ww) {   // 如果宽度大的话根据宽度固定大小缩放
                be = (int) (newOpts.outWidth / ww);
            } else if (w < h && h > hh) {
                //  如果高度高的话根据宽度固定大小缩放
                be = (int) (newOpts.outHeight / hh);
            }
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
//        newOpts.outWidth =   w/be;
//        newOpts.outHeight =   h/be;
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        return BitmapFactory.decodeFile(srcPath, newOpts);
    }


    /**
     * 获取图片的Base64字符串
     *
     * @param path
     * @param imageMaxSize 默认值100K
     * @return
     */
    public static String file2Base64(String path, int imageMaxSize) {
        try {
            return encodeBase64File(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * encodeBase64File:(将文件转成base64 字符串). <br/>
     *
     * @param file 文件路径
     * @return
     * @throws Exception
     */
    public static String encodeBase64File(File file) throws Exception {

        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();

        //去除空格
        String tempString = Base64.encodeToString(buffer, Base64.DEFAULT);
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(tempString);
        tempString = m.replaceAll("");

        return tempString;
    }

    /**
     * 旋转图片
     * @param bitmap
     * @param angle : 角度（90，180,...)
     * @return
     */
    public static Bitmap rotatingBitmap(Bitmap bitmap, int angle)
    {
        try
        {
            if (null == bitmap) return null;
            Matrix matrix = new Matrix();
            if (null != matrix)
            {
                matrix.postRotate(angle);
                return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 伸缩bitmap
     *
     * @param bitmap
     * @param sx：0-1
     * @param sy：0-1
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, float sx, float sy)
    {
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy); // 长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    /**
     * 放大缩小图片
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h)
    {
        if (bitmap == null)
        {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        Bitmap newbmp = zoomBitmap(bitmap, scaleWidth, scaleHeight);
        if (bitmap != null & !bitmap.isRecycled())
        {
            bitmap.recycle();
        }
        return newbmp;
    }

    /**
     *
     * @param background 背景图
     * @param foreground 前景图
     * @return
     */
    public static Bitmap combineBitmap(Bitmap background, Bitmap foreground,int x,int y) {
        if (background == null) {
            return null;
        }
        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(background, 0, 0, null);

        canvas.drawBitmap(foreground, x, y, null);
        canvas.save();
        canvas.restore();
        return newBitmap;
    }
}
