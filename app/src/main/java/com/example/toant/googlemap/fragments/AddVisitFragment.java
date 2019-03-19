package com.example.toant.googlemap.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.toant.googlemap.R;
import com.example.toant.googlemap.models.EventBusInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddVisitFragment extends Fragment {


    public AddVisitFragment() {
        // Required empty public constructor
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_visit, container, false);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");
        return view;
    }

}
