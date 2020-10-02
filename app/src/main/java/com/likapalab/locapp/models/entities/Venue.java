/*
 * Created by Mehmet Ozdemir on 8/31/20 10:03 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 8/31/20 10:03 AM
 */

package com.likapalab.locapp.models.entities;

import com.likapalab.locapp.models.enums.EVenueCategory;

import java.io.Serializable;

public class Venue implements Serializable {

    private int id;
    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private double geofenceRadius;
    private EVenueCategory venueCategory;

    public Venue(int id, String name, String description, double latitude, double longitude, double geofenceRadius, EVenueCategory venueCategory) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.geofenceRadius = geofenceRadius;
        this.venueCategory = venueCategory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getGeofenceRadius() {
        return geofenceRadius;
    }

    public void setGeofenceRadius(double geofenceRadius) {
        this.geofenceRadius = geofenceRadius;
    }

    public EVenueCategory getVenueCategory() {
        return venueCategory;
    }

    public void setVenueCategory(EVenueCategory venueCategory) {
        this.venueCategory = venueCategory;
    }
}
