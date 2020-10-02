/*
 * Created by Mehmet Ozdemir on 8/31/20 10:05 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 8/31/20 10:05 AM
 */

package com.likapalab.locapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.likapalab.locapp.R;
import com.likapalab.locapp.models.entities.Venue;
import com.likapalab.locapp.models.interfaces.IOnItemSelectListener;

import java.util.ArrayList;

public class VenueListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //Class Variables
    private ArrayList<Venue> venueList;
    private IOnItemSelectListener itemSelectListener;

    public VenueListAdapter(ArrayList<Venue> venueList, IOnItemSelectListener itemSelectListener) {
        this.venueList = venueList;
        this.itemSelectListener = itemSelectListener;
    }

    public class VenueViewHolder extends RecyclerView.ViewHolder {
        private View venueBackgroundView;
        private ImageView venueIconImageView;
        private TextView venueNameTextView;
        private TextView venueDescriptionTextView;
        private TextView latTextView;
        private TextView lonTextView;
        private View viewOnMapView;

        public VenueViewHolder(View view) {
            super(view);
            venueBackgroundView = view.findViewById(R.id.view_venue_background);
            venueIconImageView = view.findViewById(R.id.image_view_venue_icon);
            venueNameTextView = view.findViewById(R.id.text_view_venue_name);
            venueDescriptionTextView = view.findViewById(R.id.text_view_venue_description);
            latTextView = view.findViewById(R.id.text_view_lat);
            lonTextView = view.findViewById(R.id.text_view_lon);
            viewOnMapView = view.findViewById(R.id.view_view_on_map);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_template_venue, parent, false);
        return new VenueViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VenueViewHolder venueViewHolder = (VenueViewHolder) holder;

        final Venue venue = venueList.get(position);
        if (venue != null) {
            venueViewHolder.venueNameTextView.setText(venue.getName());
            venueViewHolder.venueDescriptionTextView.setText(venue.getDescription());

            venueViewHolder.latTextView.setText(String.valueOf(venue.getLatitude()));
            venueViewHolder.lonTextView.setText(String.valueOf(venue.getLongitude()));

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
            venueViewHolder.venueBackgroundView.setBackgroundResource(backgroundResourceId);
            venueViewHolder.venueIconImageView.setImageResource(iconResourceId);

            venueViewHolder.viewOnMapView.setOnClickListener(view -> {
                if (itemSelectListener != null) {
                    itemSelectListener.onItemSelect(venue, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return venueList.size();
    }
}
