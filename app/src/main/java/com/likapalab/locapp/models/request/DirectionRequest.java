/*
 * Created by Mehmet Ozdemir on 9/22/20 10:09 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/22/20 10:07 AM
 */

package com.likapalab.locapp.models.request;

import com.google.gson.annotations.SerializedName;
import com.likapalab.locapp.models.entities.LatLng;

import java.io.Serializable;

public class DirectionRequest implements Serializable {

    @SerializedName("origin")
    private LatLng origin;
    @SerializedName("destination")
    private LatLng destination;

    public DirectionRequest(LatLng origin, LatLng destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public LatLng getOrigin() {
        return origin;
    }

    public void setOrigin(LatLng origin) {
        this.origin = origin;
    }

    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }
}
