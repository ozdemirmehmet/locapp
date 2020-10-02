/*
 * Created by Mehmet Ozdemir on 9/4/20 2:37 PM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/4/20 2:37 PM
 */

package com.likapalab.locapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.api.entity.auth.Scope;
import com.huawei.hms.support.api.entity.hwid.HwIDConstant;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;
import com.likapalab.locapp.R;
import com.likapalab.locapp.helpers.SharedPreferencesHelper;
import com.likapalab.locapp.helpers.VenueHelper;
import com.likapalab.locapp.models.entities.Profile;
import com.likapalab.locapp.utils.Constants;
import com.likapalab.locapp.utils.CustomFunctions;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    //Class Constants
    private final String TAG = LoginActivity.class.getName();
    private final int PERMISSION_REQUEST_CODE_ALL_LOCATION = 53;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CustomFunctions.customizeActionBar(this, getSupportActionBar(), R.string.text_welcome);

        findViewById(R.id.button_huawei_login).setOnClickListener(view -> {
            view.setClickable(false);
            huaweiSignIn();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkLocationPermissions();
    }

    private void checkAutoLogin() {
        Profile userProfile = null;
        try {
            userProfile = new Profile((String) SharedPreferencesHelper.get(this, Constants.SHARED_PREFERENCES_KEY_USER_PROFILE, ""));
        } catch (JSONException e) {
            e.printStackTrace();
            userProfile = null;
        }
        if (userProfile != null) {
            openApp(userProfile);
        }
    }

    private void checkLocationPermissions() {
        Log.d(TAG, "Location permissions checking.");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, getString(R.string.text_toast_location_permissions), Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.ACCESS_BACKGROUND_LOCATION}, PERMISSION_REQUEST_CODE_ALL_LOCATION);
            }
        } else {
            checkAutoLogin();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_ALL_LOCATION:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    runOnUiThread(() -> {
                        new AlertDialog.Builder(this)
                                .setTitle(getString(R.string.text_dialog_title_location_permissions))
                                .setMessage(getString(R.string.text_dialog_message_location_permissions))
                                .setPositiveButton(getString(R.string.text_dialog_button_go_settings), (dialog, which) -> {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                })
                                .setNegativeButton(getString(R.string.text_dialog_button_exit), (dialogInterface, i) -> {
                                    this.finish();
                                })
                                .setCancelable(false)
                                .show();
                    });
                } else {
                    checkAutoLogin();
                }
                break;
        }
    }

    private void openApp(Profile profile) {
        VenueHelper.checkVenueList(this);
        Intent appIntent = new Intent(this, MainActivity.class);
        appIntent.putExtra(Constants.INTENT_PARAMETER_PROFILE, profile);
        startActivity(appIntent);
        this.finish();
    }

    private void huaweiSignIn() {
        List<Scope> scopeList = new ArrayList<>();
        scopeList.add(new Scope(HwIDConstant.SCOPE.SCOPE_ACCOUNT_EMAIL));
        scopeList.add(new Scope(HwIDConstant.SCOPE.ACCOUNT_BASEPROFILE));
        HuaweiIdAuthParams huaweiIdAuthParams = new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM).setEmail().setProfile().setScopeList(scopeList).setIdToken().createParams();
        HuaweiIdAuthService hmsService = HuaweiIdAuthManager.getService(this, huaweiIdAuthParams);
        startActivityForResult(hmsService.getSignInIntent(), Constants.ACTIVITY_REQUEST_CODE_HUAWEI_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.ACTIVITY_REQUEST_CODE_HUAWEI_SIGN_IN) {
            Task<AuthHuaweiId> authHuaweiIdTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data);
            if (authHuaweiIdTask.isSuccessful()) {
                Log.i(TAG, "Huawei sign in was completed successfully.");
                AuthHuaweiId huaweiAccount = authHuaweiIdTask.getResult();
                Profile userProfile = new Profile(huaweiAccount.getDisplayName(), huaweiAccount.getEmail(), huaweiAccount.getAvatarUriString());
                SharedPreferencesHelper.put(this, Constants.SHARED_PREFERENCES_KEY_USER_PROFILE, userProfile);
                openApp(userProfile);
            } else {
                Log.d(TAG, "Huawei sign in failed: " + ((ApiException) authHuaweiIdTask.getException()).getStatusCode());
            }
        }
    }
}