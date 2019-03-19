package com.example.toant.googlemap.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.toant.googlemap.utils.AdapterCallback;
import com.example.toant.googlemap.utils.Constants;
import com.example.toant.googlemap.utils.Utils;

/**
 * Created by toant on 11/06/2018.
 */

public class LocationBroadcastReciever extends BroadcastReceiver implements Constants{

    private static final String TAG = LocationBroadcastReciever.class.getSimpleName();
    private AdapterCallback adapterCallback;

    public LocationBroadcastReciever(AdapterCallback adapterCallback){
        this.adapterCallback = adapterCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: "+Utils.isLocationServiceEnable(context));
        if(!Utils.isLocationServiceEnable(context)){
            Log.i(TAG, "onReceive: IF: " + intent);
            adapterCallback.adpaterCallback(null,ID_BROADCAST,0);
        }
    }
}
