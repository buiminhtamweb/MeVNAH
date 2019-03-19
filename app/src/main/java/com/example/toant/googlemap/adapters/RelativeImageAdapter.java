package com.example.toant.googlemap.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.toant.googlemap.R;
import com.example.toant.googlemap.utils.Constants;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Created by toant on 08/06/2018.
 */

public class RelativeImageAdapter extends  BaseAdapter implements Constants {

    private Context context;
    private ArrayList<Bitmap> listImage;

    public RelativeImageAdapter(Context context,ArrayList<Bitmap> listImage){
        this.context =  context;
        this.listImage = listImage;
    }

    @Override
    public int getCount() {
        return listImage.size();
    }

    @Override
    public Object getItem(int position) {
        return listImage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.item_relative_image, null);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.setData(listImage.get(position));

        return convertView;
    }

    class ViewHolder {

        private ImageView imgRelativeItem;

        private ViewHolder(final View itemView) {
            imgRelativeItem = itemView.findViewById(R.id.imgRelativeItem);
        }

        private void setData(Bitmap bitmap){
            imgRelativeItem.setImageBitmap(bitmap);
        }
    }

}
