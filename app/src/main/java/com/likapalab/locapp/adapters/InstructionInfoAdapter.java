/*
 * Created by Mehmet Ozdemir on 9/1/20 10:42 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/1/20 10:42 AM
 */

package com.likapalab.locapp.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.likapalab.locapp.fragments.InstructionInfoFragment;
import com.likapalab.locapp.fragments.VenueInfoFragment;
import com.likapalab.locapp.models.entities.Step;
import com.likapalab.locapp.models.entities.Venue;

import java.util.ArrayList;

public class InstructionInfoAdapter extends FragmentStatePagerAdapter {

    //Class Variables
    private ArrayList<Step> stepList;

    public InstructionInfoAdapter(FragmentManager fm, ArrayList<Step> stepList) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.stepList = stepList;
    }

    @Override
    public Fragment getItem(final int position) {
        if (stepList != null) {
            return InstructionInfoFragment.newInstance(stepList.get(position));
        }
        return null;
    }

    @Override
    public int getCount() {
        return stepList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    public ArrayList<Step> getStepList() {
        return stepList;
    }
}