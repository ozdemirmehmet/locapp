/*
 * Created by Mehmet Ozdemir on 9/8/20 4:17 PM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/8/20 4:17 PM
 */

package com.likapalab.locapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.likapalab.locapp.models.interfaces.IOnItemDownloadedListener;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    //Class Constans
    private final String TAG = DownloadImageTask.class.getName();

    //Class Variables
    private String url;
    private IOnItemDownloadedListener onItemDownloadedListener;

    public DownloadImageTask(String url, IOnItemDownloadedListener onItemDownloadedListener) {
        this.url = url;
        this.onItemDownloadedListener = onItemDownloadedListener;
    }

    protected Bitmap doInBackground(String... params) {
        Bitmap image = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            image = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e(TAG, "Image download failed: " + e.getMessage());
            e.printStackTrace();
        }
        return image;
    }

    protected void onPostExecute(Bitmap result) {
        if (result != null && onItemDownloadedListener != null) {
            onItemDownloadedListener.onItemDownloaded(result);
        }
    }
}
