/*
 * Created by Mehmet Ozdemir on 9/22/20 1:37 PM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/22/20 1:37 PM
 */

package com.likapalab.locapp.models.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Bound implements Serializable {

    @SerializedName("southwest")
    private LatLng southwest;
    @SerializedName("northeast")
    private LatLng northeast;

    public Bound() {
    }

    public LatLng getSouthwest() {
        return southwest;
    }

    public void setSouthwest(LatLng southwest) {
        this.southwest = southwest;
    }

    public LatLng getNortheast() {
        return northeast;
    }

    public void setNortheast(LatLng northeast) {
        this.northeast = northeast;
    }
}
