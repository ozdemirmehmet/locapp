/*
 * Created by Mehmet Ozdemir on 9/21/20 4:03 PM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/21/20 4:03 PM
 */

package com.likapalab.locapp.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.likapalab.locapp.R;
import com.likapalab.locapp.adapters.RouteInstructionListAdapter;
import com.likapalab.locapp.models.entities.Route;
import com.likapalab.locapp.models.entities.Venue;
import com.likapalab.locapp.models.interfaces.IOnItemSelectListener;
import com.likapalab.locapp.utils.Constants;

public class RouteInstructionListFragment extends Fragment {

    //Widgets
    private RecyclerView instructionRecyclerView;
    private View venueCardView;
    private TextView venueNameTextView;
    private TextView venueDescriptionTextView;
    private TextView durationTextView;
    private TextView distanceTextView;
    private View venueBackgroundView;
    private ImageView venueIconImageView;

    //Class Variables
    private Venue venue;
    private IOnItemSelectListener itemSelectListener;

    public RouteInstructionListFragment() {
        // Required empty public constructor
    }

    public static RouteInstructionListFragment newInstance(Venue venue, IOnItemSelectListener itemSelectListener) {
        RouteInstructionListFragment fragment = new RouteInstructionListFragment();
        fragment.venue = venue;
        fragment.itemSelectListener = itemSelectListener;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_route_instruction_list, container, false);
        init(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().registerReceiver(routeReceiver, new IntentFilter(Constants.INTENT_ACTION_ROUTE_RECEIVED));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(routeReceiver);
    }

    private void init(View view) {
        instructionRecyclerView = view.findViewById(R.id.recycler_view_instruction);

        venueCardView = view.findViewById(R.id.card_view_venue);
        venueNameTextView = view.findViewById(R.id.text_view_venue_name);
        venueDescriptionTextView = view.findViewById(R.id.text_view_venue_description);
        durationTextView = view.findViewById(R.id.text_view_duration);
        distanceTextView = view.findViewById(R.id.text_view_distance);
        venueBackgroundView = view.findViewById(R.id.view_venue_background);
        venueIconImageView = view.findViewById(R.id.image_view_venue_icon);
    }

    private final BroadcastReceiver routeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(Constants.INTENT_PARAMETER_ROUTE) && intent.hasExtra(Constants.INTENT_PARAMETER_START_LOCATION)) {
                Route route = (Route) intent.getSerializableExtra(Constants.INTENT_PARAMETER_ROUTE);
                com.likapalab.locapp.models.entities.LatLng startLocation = (com.likapalab.locapp.models.entities.LatLng) intent.getSerializableExtra(Constants.INTENT_PARAMETER_START_LOCATION);

                prepareInstructionList(route);
                prepareVenueCard(route);
            }
        }
    };

    private void prepareInstructionList(Route route) {
        RouteInstructionListAdapter routeInstructionListAdapter = new RouteInstructionListAdapter(route.getPaths().get(0).getSteps(), itemSelectListener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        instructionRecyclerView.setLayoutManager(mLayoutManager);
        instructionRecyclerView.setItemAnimator(new DefaultItemAnimator());
        instructionRecyclerView.setAdapter(routeInstructionListAdapter);
        instructionRecyclerView.requestLayout();
    }

    private void prepareVenueCard(Route route) {
        venueNameTextView.setText(venue.getName());
        venueDescriptionTextView.setText(venue.getDescription());

        durationTextView.setText(route.getPaths().get(0).getDurationText());
        distanceTextView.setText(route.getPaths().get(0).getDistanceText());

        int backgroundResourceId = 0;
        int iconResourceId = 0;
        switch (venue.getVenueCategory()) {
            case AIRPORT:
                backgroundResourceId = R.drawable.card_background_airport;
                iconResourceId = R.drawable.icon_airport;
                break;
            case SCHOOL:
                backgroundResourceId = R.drawable.card_background_school;
                iconResourceId = R.drawable.icon_school;
                break;
            case SHOPPING:
                backgroundResourceId = R.drawable.card_background_shopping;
                iconResourceId = R.drawable.icon_shopping;
                break;
            case HOSPITAL:
                backgroundResourceId = R.drawable.card_background_hospital;
                iconResourceId = R.drawable.icon_hospital;
                break;
        }
        venueBackgroundView.setBackgroundResource(backgroundResourceId);
        venueIconImageView.setImageResource(iconResourceId);

        venueCardView.setVisibility(View.VISIBLE);
    }
}