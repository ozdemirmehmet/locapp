/*
 * Created by Mehmet Ozdemir on 9/7/20 4:40 PM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/7/20 4:40 PM
 */

package com.likapalab.locapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;

import com.huawei.hms.maps.model.BitmapDescriptor;
import com.huawei.hms.maps.model.BitmapDescriptorFactory;
import com.likapalab.locapp.LikapalabApp;
import com.likapalab.locapp.R;
import com.likapalab.locapp.widgets.LikapalabTextView;

public class CustomFunctions {

    //Class Constants
    private static final String TAG = CustomFunctions.class.getName();

    public static void customizeActionBar(Context context, ActionBar actionBar, int titleResourceId) {
        Log.d(TAG, "Action bar is being customized.");
        LikapalabTextView textView = new LikapalabTextView(context);
        textView.setText(context.getString(titleResourceId));
        textView.setTextSize(context.getResources().getDimension(R.dimen.text_size_xsmall));
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(context.getResources().getColor(android.R.color.white));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(textView);
    }

    public static void postEventToAnalytics(String eventName, Bundle eventContentBundle) {
        Log.d(TAG, "Analytics event is being posted.");
        LikapalabApp.getAnalyticsInstance().onEvent(eventName, eventContentBundle);
    }

    public static BitmapDescriptor getBitmapDescriptor(Context context, int id) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, id);

        int h = vectorDrawable.getIntrinsicHeight();
        int w = vectorDrawable.getIntrinsicWidth();

        vectorDrawable.setBounds(0, 0, w, h);

        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bm);
    }
}
