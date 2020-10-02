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
import com.likapalab.locapp.models.entities.Step;
import com.likapalab.locapp.models.entities.Venue;
import com.likapalab.locapp.models.interfaces.IOnItemSelectListener;

import java.util.ArrayList;

public class RouteInstructionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //Class Variables
    private ArrayList<Step> stepList;
    private IOnItemSelectListener itemSelectListener;

    public RouteInstructionListAdapter(ArrayList<Step> stepList, IOnItemSelectListener itemSelectListener) {
        this.stepList = stepList;
        this.itemSelectListener = itemSelectListener;
    }

    public class InstructionViewHolder extends RecyclerView.ViewHolder {
        private ImageView instructionActionImageView;
        private TextView instructionTextView;
        private TextView durationTextView;
        private TextView distanceTextView;

        public InstructionViewHolder(View view) {
            super(view);
            instructionActionImageView = view.findViewById(R.id.image_view_instruction_action);
            instructionTextView = view.findViewById(R.id.text_view_instruction);
            durationTextView = view.findViewById(R.id.text_view_duration);
            distanceTextView = view.findViewById(R.id.text_view_distance);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_template_instruction, parent, false);
        return new InstructionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        InstructionViewHolder instructionViewHolder = (InstructionViewHolder) holder;

        final Step step = stepList.get(position);
        if (step != null) {
            instructionViewHolder.instructionTextView.setText(step.getInstruction());
            instructionViewHolder.durationTextView.setText(step.getDurationText());
            instructionViewHolder.distanceTextView.setText(step.getDistanceText());

            if (step.getInstruction().equals("")) {
                instructionViewHolder.instructionTextView.setText(step.getRoadName());
            }

            switch (step.getAction()) {
                case STRAIGHT:
                    instructionViewHolder.instructionActionImageView.setImageResource(R.drawable.icon_go_straight);
                    break;
                case TURN_LEFT:
                case RAMP_LEFT:
                case TURN_SHARP_LEFT:
                case TURN_SLIGHT_LEFT:
                    instructionViewHolder.instructionActionImageView.setImageResource(R.drawable.icon_turn_left);
                    break;
                case TURN_RIGHT:
                case RAMP_RIGHT:
                case TURN_SHARP_RIGHT:
                case TURN_SLIGHT_RIGHT:
                    instructionViewHolder.instructionActionImageView.setImageResource(R.drawable.icon_turn_right);
                    break;
                case FORK_LEFT:
                    instructionViewHolder.instructionActionImageView.setImageResource(R.drawable.icon_fork_left);
                    break;
                case FORK_RIGHT:
                    instructionViewHolder.instructionActionImageView.setImageResource(R.drawable.icon_fork_right);
                    break;
                case ROUNDABOUT_LEFT:
                    instructionViewHolder.instructionActionImageView.setImageResource(R.drawable.icon_round_left);
                    break;
                case ROUNDABOUT_RIGHT:
                    instructionViewHolder.instructionActionImageView.setImageResource(R.drawable.icon_round_right);
                    break;
                case UTURN_LEFT:
                    instructionViewHolder.instructionActionImageView.setImageResource(R.drawable.icon_u_turn_left);
                    break;
                case UTURN_RIGHT:
                    instructionViewHolder.instructionActionImageView.setImageResource(R.drawable.icon_u_turn_right);
                    break;
                case MERGE:
                    instructionViewHolder.instructionActionImageView.setImageResource(R.drawable.icon_merge);
                    break;
                case FERRY:
                case FERRY_TRAIN:
                    instructionViewHolder.instructionActionImageView.setImageResource(R.drawable.icon_ferry);
                    break;
                case END:
                    instructionViewHolder.instructionActionImageView.setImageResource(R.drawable.icon_finish_flag);
                    break;
                default:
                    instructionViewHolder.instructionActionImageView.setImageResource(R.drawable.icon_quetion_mark);
                    break;
            }

            instructionViewHolder.itemView.setOnClickListener(view -> {
                if (itemSelectListener != null) {
                    itemSelectListener.onItemSelect(step, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }
}
