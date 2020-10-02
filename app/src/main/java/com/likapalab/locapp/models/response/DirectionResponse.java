/*
 * Created by Mehmet Ozdemir on 9/22/20 10:11 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/22/20 10:11 AM
 */

package com.likapalab.locapp.models.response;

import com.google.gson.annotations.SerializedName;
import com.likapalab.locapp.models.entities.Route;

import java.util.ArrayList;

public class DirectionResponse {

    @SerializedName("routes")
    private ArrayList<Route> routes;
    @SerializedName("returnCode")
    private String returnCode;
    @SerializedName("returnDesc")
    private String returnDesc;

    public DirectionResponse() {
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnDesc() {
        return returnDesc;
    }

    public void setReturnDesc(String returnDesc) {
        this.returnDesc = returnDesc;
    }
}
