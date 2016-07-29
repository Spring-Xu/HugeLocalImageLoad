package com.cjstar.hugeimageload.utils;


/*
 * Copyright (C) 2015 CJstar, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;

/**
 * Created by CJstar on 15/8/20.
 */
public class ImageLoaderUtils {

    private static String TAG = "ImageLoaderUtils";

    /**
     * Load bitmap from resources
     *
     * @param res        resource
     * @param drawableId resource image id
     * @param imgH       destination image height
     * @param imgW       destination image width
     * @return
     */
    public static Bitmap loadHugeBitmapFromDrawable(Resources resources, int drawableId, int imgH, int imgW) {
        Log.d(TAG, "imgH:" + imgH + " imgW:" + imgW);

        BitmapFactory.Options options = new BitmapFactory.Options();

        //preload set inJustDecodeBounds true, this will load bitmap into memory
        options.inJustDecodeBounds = true;
        // options.mCancel = true;
        //options.inPreferredConfig = Bitmap.Config.ARGB_8888;//default is Bitmap.Config.ARGB_8888
        BitmapFactory.decodeResource(resources, drawableId, options);

        //get the image information include: height and width
        int height = options.outHeight;
        int width = options.outWidth;
        String mimeType = options.outMimeType;

        Log.d(TAG, "width:" + width + " height:" + height + " mimeType:" + mimeType);

        //get sample size
        int sampleSize = getScaleInSampleSize(width, height, imgW, imgH);
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        // options.mCancel = false;

        // Decode bitmap with inSampleSize set
        Log.d(TAG, "memory size:" + getBitmapSizeInMemory(width / sampleSize, height / sampleSize));
        Bitmap bitmap = BitmapFactory.decodeResource(resources, drawableId, options);
        Log.d(TAG, "w=" + bitmap.getWidth() + " h=" + bitmap.getHeight() + " bitmap size:" + bitmap.getRowBytes() * bitmap.getHeight());
        return bitmap;
    }

    /**
     * load the bitmap from SDCard with the imgW and imgH
     *
     * @param imgPath  resource path
     * @param imgH     result image height
     * @param imgW     result image width
     * @return result bitmap
     */
    public static Bitmap loadHugeBitmapFromSDCard(String imgPath, int imgH, int imgW) {
        Log.d(TAG, "imgH:" + imgH + " imgW:" + imgW);

        BitmapFactory.Options options = new BitmapFactory.Options();

        //preload set inJustDecodeBounds true, this will load bitmap into memory
        options.inJustDecodeBounds = true;
        // options.mCancel = true;
        //options.inPreferredConfig = Bitmap.Config.ARGB_8888;//default is Bitmap.Config.ARGB_8888
        BitmapFactory.decodeFile(imgPath, options);

        //get the image information include: height and width
        int height = options.outHeight;
        int width = options.outWidth;
        String mimeType = options.outMimeType;

        Log.d(TAG, "width:" + width + " height:" + height + " mimeType:" + mimeType);

        //get sample size
        int sampleSize = getScaleInSampleSize(width, height, imgW, imgH);
        options.inSampleSize = sampleSize;
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        // options.mCancel = false;

        Log.d(TAG, "memory size:" + getBitmapSizeInMemory(width / sampleSize, height / sampleSize));
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
        Log.d(TAG, "w=" + bitmap.getWidth() + " h=" + bitmap.getHeight() + " bitmap size:" + bitmap.getRowBytes() * bitmap.getHeight());

        return bitmap;
    }

    /**
     * get the image size in the RAM
     *
     * @param imageW
     * @param imageH
     * @return
     */
    public static long getBitmapSizeInMemory(int imageW, int imageH) {
        return imageH * imageW * getBytesPerPixel(Bitmap.Config.ARGB_8888);
    }

    /**
     * A helper function to return the byte usage per pixel of a bitmap based on its configuration.
     */
    public static int getBytesPerPixel(Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_8888) {
            return 4;
        } else if (config == Bitmap.Config.RGB_565) {
            return 2;
        } else if (config == Bitmap.Config.ARGB_4444) {
            return 2;
        } else if (config == Bitmap.Config.ALPHA_8) {
            return 1;
        }
        return 1;
    }


    /**
     * get the scale sample size
     *
     * @param resW resource width
     * @param resH resource height
     * @param desW result width
     * @param desH result height
     * @return
     */
    public static int getScaleInSampleSize(int resW, int resH, int desW, int desH) {
        int scaleW = resW / desW;
        int scaleH = resH / desH;
        int largeScale = scaleH > scaleW ? scaleH : scaleW;

        int sampleSize = 1;
        while (sampleSize < largeScale) {
            sampleSize *= 2;
        }

        Log.d(TAG, "sampleSize:" + sampleSize);

        return sampleSize;
    }

    /**
     * get image orientation rotate degree
     *
     * @param filepath
     * @return
     */
    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            Log.d(TAG, "cannot read exif" + ex);
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }
}
