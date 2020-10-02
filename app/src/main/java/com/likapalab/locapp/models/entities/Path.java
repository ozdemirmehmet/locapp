/*
 * Created by Mehmet Ozdemir on 9/22/20 1:37 PM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/22/20 1:37 PM
 */

package com.likapalab.locapp.models.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Path implements Serializable {

    @SerializedName("duration")
    private double duration;
    @SerializedName("durationText")
    private String durationText;
    @SerializedName("durationInTrafficText")
    private String durationInTrafficText;
    @SerializedName("durationInTraffic")
    private double durationInTraffic;
    @SerializedName("distance")
    private double distance;
    @SerializedName("startLocation")
    private LatLng startLocation;
    @SerializedName("startAddress")
    private String startAddress;
    @SerializedName("distanceText")
    private String distanceText;
    @SerializedName("endLocation")
    private LatLng endLocation;
    @SerializedName("endAddress")
    private String endAddress;
    @SerializedName("steps")
    private ArrayList<Step> steps;

    public Path() {
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getDurationText() {
        return durationText;
    }

    public void setDurationText(String durationText) {
        this.durationText = durationText;
    }

    public String getDurationInTrafficText() {
        return durationInTrafficText;
    }

    public void setDurationInTrafficText(String durationInTrafficText) {
        this.durationInTrafficText = durationInTrafficText;
    }

    public double getDurationInTraffic() {
        return durationInTraffic;
    }

    public void setDurationInTraffic(double durationInTraffic) {
        this.durationInTraffic = durationInTraffic;
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

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
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

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }
}
