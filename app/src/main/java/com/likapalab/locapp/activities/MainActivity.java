/*
 * Created by Mehmet Ozdemir on 8/31/20 10:06 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 8/31/20 10:00 AM
 */

package com.likapalab.locapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.likapalab.locapp.R;
import com.likapalab.locapp.fragments.VenueListFragment;
import com.likapalab.locapp.fragments.VenueMapFragment;
import com.likapalab.locapp.helpers.SharedPreferencesHelper;
import com.likapalab.locapp.helpers.VenueHelper;
import com.likapalab.locapp.models.entities.Profile;
import com.likapalab.locapp.models.entities.Venue;
import com.likapalab.locapp.models.interfaces.IOnItemSelectListener;
import com.likapalab.locapp.utils.Constants;
import com.likapalab.locapp.utils.CustomFunctions;
import com.likapalab.locapp.utils.DownloadImageTask;
import com.likapalab.locapp.widgets.LikapalabTextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Widgets
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomFunctions.customizeActionBar(this, getSupportActionBar(), R.string.text_venues);

        initProfile();
        initViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(venueListReceiver, new IntentFilter(Constants.INTENT_ACTION_VENUE_LIST_RECEIVED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(venueListReceiver);
    }

    private final BroadcastReceiver venueListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent reCreateIntent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(reCreateIntent);
            finish();
        }
    };

    private void initViewPager() {
        ArrayList<Venue> venueList = VenueHelper.getVenueList(this);
        VenueMapFragment venueMapFragment = VenueMapFragment.newInstance(venueList);
        VenueListFragment venueListFragment = VenueListFragment.newInstance(venueList, (IOnItemSelectListener) (item, position) -> {
            if (viewPager != null && viewPager.getAdapter() != null) {
                viewPager.setCurrentItem(1, true);
                venueMapFragment.showItem(position);
            }
        });

        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            private final Fragment[] fragments = new Fragment[]{
                    venueListFragment,
                    venueMapFragment
            };
            private final String[] fragmentNames = new String[]{
                    getString(R.string.text_list),
                    getString(R.string.text_map)
            };

            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return fragmentNames[position];
            }
        };

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            LikapalabTextView tabViewTextView = new LikapalabTextView(this);
            tabViewTextView.setText(tabLayout.getTabAt(i).getText());
            tabViewTextView.setTextColor(getResources().getColorStateList(R.color.color_style_tab_text));
            tabViewTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tabLayout.getTabAt(i).setCustomView(tabViewTextView);
        }
    }

    private void initProfile() {
        Profile profile = (Profile) getIntent().getSerializableExtra(Constants.INTENT_PARAMETER_PROFILE);
        if (profile != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.action_bar_main);
            getSupportActionBar().setElevation(getResources().getDimension(R.dimen.elevation_small));
            TextView titleTextView = getSupportActionBar().getCustomView().findViewById(R.id.text_view_title);
            titleTextView.setText(getString(R.string.text_venues));
            ImageView profileImageView = getSupportActionBar().getCustomView().findViewById(R.id.image_view_profile);
            profileImageView.setImageURI(Uri.parse(profile.getPhotoUrl()));
            profileImageView.setOnClickListener(view -> {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.text_dialog_title_log_out))
                        .setMessage(getString(R.string.text_dialog_message_log_out, profile.getEmail()))
                        .setPositiveButton(getString(R.string.text_dialog_button_yes), (dialog, which) -> {
                            SharedPreferencesHelper.remove(this, Constants.SHARED_PREFERENCES_KEY_USER_PROFILE);
                            Intent loginIntent = new Intent(this, LoginActivity.class);
                            startActivity(loginIntent);
                            this.finish();
                        })
                        .setNegativeButton(getString(R.string.text_dialog_button_no), (dialogInterface, i) -> {

                        })
                        .show();
            });

            new DownloadImageTask(profile.getPhotoUrl(), object -> {
                runOnUiThread(() -> profileImageView.setImageBitmap((Bitmap) object));
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
}