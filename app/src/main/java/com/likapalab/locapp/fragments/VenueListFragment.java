/*
 * Created by Mehmet Ozdemir on 8/31/20 9:57 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 8/31/20 9:55 AM
 */

package com.likapalab.locapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.likapalab.locapp.R;
import com.likapalab.locapp.adapters.VenueListAdapter;
import com.likapalab.locapp.models.entities.Venue;
import com.likapalab.locapp.models.interfaces.IOnItemSelectListener;

import java.util.ArrayList;

public class VenueListFragment extends Fragment {

    //Class Constants
    private final String TAG = VenueListFragment.class.getName();

    //Class Variables
    private ArrayList<Venue> venueList;
    private IOnItemSelectListener itemSelectListener;

    public VenueListFragment() {
        // Required empty public constructor
    }

    public static VenueListFragment newInstance(ArrayList<Venue> venueList, IOnItemSelectListener itemSelectListener) {
        VenueListFragment fragment = new VenueListFragment();
        fragment.venueList = venueList;
        fragment.itemSelectListener = itemSelectListener;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_venue_list, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        VenueListAdapter venueListAdapter = new VenueListAdapter(venueList, itemSelectListener);
        RecyclerView venueRecyclerView = view.findViewById(R.id.recycler_view_venue);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        venueRecyclerView.setLayoutManager(mLayoutManager);
        venueRecyclerView.setItemAnimator(new DefaultItemAnimator());
        venueRecyclerView.setAdapter(venueListAdapter);
    }
}