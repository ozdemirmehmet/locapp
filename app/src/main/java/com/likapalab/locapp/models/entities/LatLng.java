/*
 * Created by Mehmet Ozdemir on 9/22/20 10:27 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/22/20 10:27 AM
 */

package com.likapalab.locapp.models.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LatLng implements Serializable {

    @SerializedName("lat")
    private double lat;
    @SerializedName("lng")
    private double lng;

    public LatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public com.huawei.hms.maps.model.LatLng toHuaweiLatLng() {
        return new com.huawei.hms.maps.model.LatLng(lat, lng);
    }
}
