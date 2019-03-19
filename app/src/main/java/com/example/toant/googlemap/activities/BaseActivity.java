package com.example.toant.googlemap.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.example.toant.googlemap.R;
import com.example.toant.googlemap.helper.PermissionHelper;
import com.example.toant.googlemap.models.EventBusInfo;
import com.example.toant.googlemap.utils.AdapterCallback;
import com.example.toant.googlemap.utils.Constants;
import com.example.toant.googlemap.utils.DownloadCallback;
import com.example.toant.googlemap.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created by toant on 31/05/2018.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, AdapterCallback,Constants, DownloadCallback{

    private PermissionHelper permissionHelper;
    private final static int ALL_PERMISSIONS_RESULT = 107;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        grantPerMission();
        registerEvent();
        initUI();
    }

    public abstract void initUI();

    @StringRes
    public abstract int getLayoutResourceId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterEvent();
    }

    private void grantPerMission() {
        permissionHelper = new PermissionHelper(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionHelper.permissionsToRequest.size() > 0){
                requestPermissions(permissionHelper.permissionsToRequest.toArray(new String[permissionHelper.permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionHelper.permissionsToRequest) {
                    if (permissionHelper.hasPermission(perms)) {
                    } else {
                        permissionHelper.permissionsRejected.add(perms);
                    }
                }
                if (permissionHelper.permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionHelper.permissionsRejected.get(0))) {
                            Utils.showMessageOKCancel(this,getString(R.string.msg_need_permission),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionHelper.permissionsRejected.toArray(new String[permissionHelper.permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
        }

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void adpaterCallback(Object data, int processId, int position) {

    }

    protected void registerEvent() {
        EventBus.getDefault().register(this);
    }

    protected void unregisterEvent() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusInfo eventBusInfo) {

    }

    protected void postEvent(EventBusInfo eventBusInfo) {
        EventBus.getDefault().post(eventBusInfo);
    }

    public void showHomeButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void downloadSuccess(int processId, Object data) {

    }

    @Override
    public void downloadError(int processId, String msg) {

    }
}
