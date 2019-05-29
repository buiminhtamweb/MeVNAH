package com.example.toant.googlemap.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.toant.googlemap.R;
import com.example.toant.googlemap.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageSliderAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> imageList;
    private LayoutInflater inflater;

    public ImageSliderAdapter(Context context, List<String> list) {
        mContext = context;
        imageList = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ViewGroup imageLayout = (ViewGroup) inflater.inflate(R.layout.slider_home, collection, false);

//        ((ImageView) imageLayout.findViewById(R.id.imageView)).setImageResource(imageList.get(position).getResId());

        ImageView imageView = imageLayout.findViewById(R.id.imageView);
        Log.e("SliderAdapter", "instantiateItem: "+ imageList.get(position));
        Picasso.get().load(Constants.URL_SERVER_IMAGE + imageList.get(position))
                .placeholder(R.drawable.ic_copyright_symbol)
                .error(R.drawable.ic_copyright_symbol).into(imageView);
        collection.addView(imageLayout);
        return imageLayout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }



}
