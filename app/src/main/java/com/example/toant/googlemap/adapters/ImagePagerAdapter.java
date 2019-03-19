package com.example.toant.googlemap.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by toant on 10/06/2018.
 */

public class ImagePagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<String> imageList;

    public ImagePagerAdapter(Context context, ArrayList<String> arrayList){
        this.context = context;
        this.imageList = arrayList;
    }


    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }
}
