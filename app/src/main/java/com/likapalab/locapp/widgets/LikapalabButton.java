/*
 * Created by Mehmet Ozdemir on 8/31/20 10:56 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 8/31/20 10:56 AM
 */

package com.likapalab.locapp.widgets;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.likapalab.locapp.LikapalabApp;

public class LikapalabButton extends AppCompatButton {

    public LikapalabButton(Context context) {
        this(context, null);
        this.setTypeface(LikapalabApp.getLikapalabFont());
    }

    public LikapalabButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.setTypeface(LikapalabApp.getLikapalabFont());
    }

    public LikapalabButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setTypeface(LikapalabApp.getLikapalabFont());
    }
}
