/*
 * Created by Mehmet Ozdemir on 8/31/20 10:57 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 8/31/20 10:57 AM
 */

package com.likapalab.locapp;

import android.app.Application;
import android.graphics.Typeface;

import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.hms.analytics.HiAnalyticsTools;
import com.likapalab.locapp.helpers.VenueHelper;
import com.likapalab.locapp.services.ApiService;

public class LikapalabApp extends Application {

    //Class Constants
    private static final String LIKAPALAB_FONT_PATH = "likapalab_font_bold.ttf";

    //Class Variables
    private static Typeface likapalabFont;
    private static HiAnalyticsInstance analyticsInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        ApiService.createInstance(this);

        initTypeface();
        initAnalytics();
    }

    private void initTypeface() {
        likapalabFont = Typeface.createFromAsset(getAssets(), LIKAPALAB_FONT_PATH);
    }

    public static Typeface getLikapalabFont() {
        return likapalabFont;
    }

    private void initAnalytics() {
        HiAnalyticsTools.enableLog();
        analyticsInstance = HiAnalytics.getInstance(this);
    }

    public static HiAnalyticsInstance getAnalyticsInstance() {
        return analyticsInstance;
    }
}
