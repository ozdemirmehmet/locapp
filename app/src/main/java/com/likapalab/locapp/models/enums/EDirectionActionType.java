/*
 * Created by Mehmet Ozdemir on 9/24/20 9:18 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/24/20 9:18 AM
 */

package com.likapalab.locapp.models.enums;

public enum EDirectionActionType {
    STRAIGHT("straight"),
    TURN_LEFT("turn-left"),
    TURN_RIGHT("turn-right"),
    TURN_SLIGHT_LEFT("turn-slight-left"),
    TURN_SLIGHT_RIGHT("turn-slight-right"),
    TURN_SHARP_LEFT("turn-sharp-left"),
    TURN_SHARP_RIGHT("turn-sharp-right"),
    UTURN_LEFT("uturn-left"),
    UTURN_RIGHT("uturn-right"),
    RAMP_LEFT("ramp-left"),
    RAMP_RIGHT("ramp-right"),
    MERGE("merge"),
    FORK_LEFT("fork-left"),
    FORK_RIGHT("fork-right"),
    FERRY("ferry"),
    FERRY_TRAIN("ferry-train"),
    ROUNDABOUT_LEFT("roundabout-left"),
    ROUNDABOUT_RIGHT("roundabout-right"),
    END("end"),
    UNKNOWN("unknown");

    private String action;

    EDirectionActionType(String action) {
        this.action = action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
