/*
 * Created by Mehmet Ozdemir on 9/22/20 9:47 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/22/20 9:47 AM
 */

package com.likapalab.locapp.services;

import android.content.Context;

import com.likapalab.locapp.R;
import com.likapalab.locapp.models.interfaces.IHuaweiMapServices;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    private static ApiService instance;

    private static IHuaweiMapServices huaweiMapServices;

    public static void createInstance(Context context) {
        if (instance == null) {
            instance = new ApiService(context);
        }
    }

    private ApiService(Context context) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .build();
        Retrofit huaweiServicesRetrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.huawei_map_services_base_url))
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        huaweiMapServices = huaweiServicesRetrofit.create(IHuaweiMapServices.class);
    }

    public static IHuaweiMapServices getHuaweiMapServices() {
        return huaweiMapServices;
    }

    public static final String ROUTE_SERVICE = "routeService/";
}