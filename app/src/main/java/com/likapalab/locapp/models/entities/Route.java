/*
 * Created by Mehmet Ozdemir on 9/22/20 1:37 PM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/22/20 1:37 PM
 */

package com.likapalab.locapp.models.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Route implements Serializable {

    @SerializedName("paths")
    private ArrayList<Path> paths;
    @SerializedName("bounds")
    private Bound bounds;

    public Route() {
    }

    public ArrayList<Path> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<Path> paths) {
        this.paths = paths;
    }

    public Bound getBounds() {
        return bounds;
    }

    public void setBounds(Bound bounds) {
        this.bounds = bounds;
    }
}
