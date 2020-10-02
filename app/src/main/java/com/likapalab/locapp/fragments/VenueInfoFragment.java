/*
 * Created by Mehmet Ozdemir on 9/1/20 10:46 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/1/20 10:46 AM
 */

package com.likapalab.locapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.likapalab.locapp.R;
import com.likapalab.locapp.activities.RouteActivity;
import com.likapalab.locapp.models.entities.Venue;
import com.likapalab.locapp.utils.Constants;

public class VenueInfoFragment extends Fragment {

    //Class Constants
    private final String TAG = VenueInfoFragment.class.getName();

    //Class Variables
    private Venue venue;

    public VenueInfoFragment() {
        // Required empty public constructor
    }

    public static VenueInfoFragment newInstance(Venue venue) {
        VenueInfoFragment fragment = new VenueInfoFragment();
        fragment.venue = venue;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_venue_info, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        TextView venueNameTextView = view.findViewById(R.id.text_view_venue_name);
        venueNameTextView.setText(venue.getName());
        TextView venueDescriptionTextView = view.findViewById(R.id.text_view_venue_description);
        venueDescriptionTextView.setText(venue.getDescription());

        TextView latTextView = view.findViewById(R.id.text_view_lat);
        latTextView.setText(String.valueOf(venue.getLatitude()));
        TextView lonTextView = view.findViewById(R.id.text_view_lon);
        lonTextView.setText(String.valueOf(venue.getLongitude()));

        Button routePreviewButton = view.findViewById(R.id.button_route_preview);
        routePreviewButton.setOnClickListener(view1 -> {
            Intent routePreviewIntent = new Intent(getActivity(), RouteActivity.class);
            routePreviewIntent.putExtra(Constants.INTENT_PARAMETER_VENUE, venue);
            startActivity(routePreviewIntent);
        });

        View venueBackgroundView = view.findViewById(R.id.view_venue_background);
        ImageView venueIconImageView = view.findViewById(R.id.image_view_venue_icon);
        int backgroundResourceId = 0;
        int iconResourceId = 0;
        int textColorResourceId = 0;
        switch (venue.getVenueCategory()) {
            case AIRPORT:
                backgroundResourceId = R.drawable.card_background_airport;
                iconResourceId = R.drawable.icon_airport;
                textColorResourceId = R.color.color_gradient_5_dark;
                break;
            case SCHOOL:
                backgroundResourceId = R.drawable.card_background_school;
                iconResourceId = R.drawable.icon_school;
                textColorResourceId = R.color.color_gradient_2_dark;
                break;
            case SHOPPING:
                backgroundResourceId = R.drawable.card_background_shopping;
                iconResourceId = R.drawable.icon_shopping;
                textColorResourceId = R.color.color_gradient_3_dark;
                break;
            case HOSPITAL:
                backgroundResourceId = R.drawable.card_background_hospital;
                iconResourceId = R.drawable.icon_hospital;
                textColorResourceId = R.color.color_gradient_1_dark;
                break;
        }
        venueBackgroundView.setBackgroundResource(backgroundResourceId);
        venueIconImageView.setImageResource(iconResourceId);
        routePreviewButton.setTextColor(ContextCompat.getColor(getActivity(), textColorResourceId));

        ViewTreeObserver viewTreeObserver = venueBackgroundView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                venueBackgroundView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                broadcastVenueInfoViewPagerHeight(venueBackgroundView.getHeight() + (int) getResources().getDimension(R.dimen.margin_container_small));
            }
        });
    }

    private void broadcastVenueInfoViewPagerHeight(int height) {
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT_ACTION_VENUE_INFO_VIEW_PAGER_HEIGHT_RECEIVED);
        intent.putExtra(Constants.INTENT_PARAMETER_VIEW_PAGER_HEIGHT, height);
        getActivity().sendBroadcast(intent);
    }
}