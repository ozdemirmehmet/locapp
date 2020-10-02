/*
 * Created by Mehmet Ozdemir on 9/22/20 1:44 PM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/22/20 1:44 PM
 */

package com.likapalab.locapp.models.entities;

import com.google.gson.annotations.SerializedName;
import com.likapalab.locapp.models.enums.EDirectionActionType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

public class Step implements Serializable {

    @SerializedName("duration")
    private double duration;
    @SerializedName("orientation")
    private int orientation;
    @SerializedName("durationText")
    private String durationText;
    @SerializedName("distance")
    private double distance;
    @SerializedName("startLocation")
    private LatLng startLocation;
    @SerializedName("instruction")
    private String instruction;
    @SerializedName("action")
    private String action;
    @SerializedName("distanceText")
    private String distanceText;
    @SerializedName("endLocation")
    private LatLng endLocation;
    @SerializedName("polyline")
    private ArrayList<LatLng> polyline;
    @SerializedName("roadName")
    private String roadName;

    public Step() {
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public String getDurationText() {
        return durationText;
    }

    public void setDurationText(String durationText) {
        this.durationText = durationText;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public LatLng getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(LatLng startLocation) {
        this.startLocation = startLocation;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public EDirectionActionType getAction() {
        return EDirectionActionType.valueOf(action.replaceAll("-","_").toUpperCase(Locale.US));
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDistanceText() {
        return distanceText;
    }

    public void setDistanceText(String distanceText) {
        this.distanceText = distanceText;
    }

    public LatLng getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(LatLng endLocation) {
        this.endLocation = endLocation;
    }

    public ArrayList<LatLng> getPolyline() {
        return polyline;
    }

    public void setPolyline(ArrayList<LatLng> polyline) {
        this.polyline = polyline;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }
}
