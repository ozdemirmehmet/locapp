/*
 * Created by Mehmet Ozdemir on 9/1/20 10:42 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/1/20 10:42 AM
 */

package com.likapalab.locapp.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.likapalab.locapp.fragments.VenueInfoFragment;
import com.likapalab.locapp.models.entities.Venue;

import java.util.ArrayList;

public class VenueInfoAdapter extends FragmentStatePagerAdapter {

    //Class Variables
    private ArrayList<Venue> venueList;

    public VenueInfoAdapter(FragmentManager fm, ArrayList<Venue> venueList) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.venueList = venueList;
    }

    @Override
    public Fragment getItem(final int position) {
        if (venueList != null) {
            return VenueInfoFragment.newInstance(venueList.get(position));
        }
        return null;
    }

    @Override
    public int getCount() {
        return venueList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return venueList.get(position).getName();
    }

    public ArrayList<Venue> getVenueList() {
        return venueList;
    }
}