/*
 * Created by Mehmet Ozdemir on 9/1/20 10:46 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/1/20 10:46 AM
 */

package com.likapalab.locapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.likapalab.locapp.R;
import com.likapalab.locapp.activities.RouteActivity;
import com.likapalab.locapp.models.entities.Step;
import com.likapalab.locapp.models.entities.Venue;
import com.likapalab.locapp.utils.Constants;

public class InstructionInfoFragment extends Fragment {

    //Class Constants
    private final String TAG = InstructionInfoFragment.class.getName();

    //Class Variables
    private Step step;

    public InstructionInfoFragment() {
        // Required empty public constructor
    }

    public static InstructionInfoFragment newInstance(Step step) {
        InstructionInfoFragment fragment = new InstructionInfoFragment();
        fragment.step = step;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_instruction_info, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        ImageView instructionActionImageView = view.findViewById(R.id.image_view_instruction_action);
        TextView instructionTextView = view.findViewById(R.id.text_view_instruction);
        TextView durationTextView = view.findViewById(R.id.text_view_duration);
        TextView distanceTextView = view.findViewById(R.id.text_view_distance);

        instructionTextView.setText(step.getInstruction());
        durationTextView.setText(step.getDurationText());
        distanceTextView.setText(step.getDistanceText());

        if (step.getInstruction().equals("")) {
            instructionTextView.setText(step.getRoadName());
        }

        switch (step.getAction()) {
            case STRAIGHT:
                instructionActionImageView.setImageResource(R.drawable.icon_go_straight);
                break;
            case TURN_LEFT:
            case RAMP_LEFT:
            case TURN_SHARP_LEFT:
            case TURN_SLIGHT_LEFT:
                instructionActionImageView.setImageResource(R.drawable.icon_turn_left);
                break;
            case TURN_RIGHT:
            case RAMP_RIGHT:
            case TURN_SHARP_RIGHT:
            case TURN_SLIGHT_RIGHT:
                instructionActionImageView.setImageResource(R.drawable.icon_turn_right);
                break;
            case FORK_LEFT:
                instructionActionImageView.setImageResource(R.drawable.icon_fork_left);
                break;
            case FORK_RIGHT:
                instructionActionImageView.setImageResource(R.drawable.icon_fork_right);
                break;
            case ROUNDABOUT_LEFT:
                instructionActionImageView.setImageResource(R.drawable.icon_round_left);
                break;
            case ROUNDABOUT_RIGHT:
                instructionActionImageView.setImageResource(R.drawable.icon_round_right);
                break;
            case UTURN_LEFT:
                instructionActionImageView.setImageResource(R.drawable.icon_u_turn_left);
                break;
            case UTURN_RIGHT:
                instructionActionImageView.setImageResource(R.drawable.icon_u_turn_right);
                break;
            case MERGE:
                instructionActionImageView.setImageResource(R.drawable.icon_merge);
                break;
            case FERRY:
            case FERRY_TRAIN:
                instructionActionImageView.setImageResource(R.drawable.icon_ferry);
                break;
            case END:
                instructionActionImageView.setImageResource(R.drawable.icon_finish_flag);
                break;
            default:
                instructionActionImageView.setImageResource(R.drawable.icon_quetion_mark);
                break;
        }

        View parenView = view.findViewById(R.id.view_instruction_content);
        ViewTreeObserver viewTreeObserver = parenView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                parenView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                broadcastVenueInfoViewPagerHeight(parenView.getHeight() + (int) getResources().getDimension(R.dimen.margin_container_small));
            }
        });
    }

    private void broadcastVenueInfoViewPagerHeight(int height) {
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT_ACTION_INSTRUCTION_INFO_VIEW_PAGER_HEIGHT_RECEIVED);
        intent.putExtra(Constants.INTENT_PARAMETER_VIEW_PAGER_HEIGHT, height);
        getActivity().sendBroadcast(intent);
    }
}