package com.example.toant.googlemap.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.toant.googlemap.R;
import com.example.toant.googlemap.utils.AdapterCallback;
import com.example.toant.googlemap.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by toant on 10/06/2018.
 */

public class ImageDetailAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> implements Constants {

    private Context context;
    private ArrayList<String> arrayList;
    private AdapterCallback adapterCallback;

    public ImageDetailAdapter(Context context,ArrayList<String> arrayList,AdapterCallback adapterCallback){
        this.context = context;
        this.arrayList = arrayList;
        this.adapterCallback = adapterCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_relative_image, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        String link = arrayList.get(position);
        ItemViewHolder vh = (ItemViewHolder) viewHolder;
        vh.position = position;
        vh.setData(link);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgLocation;
        private int position = -1;

        private ItemViewHolder(final View itemView) {
            super(itemView);
            imgLocation = itemView.findViewById(R.id.imgRelativeItem);
//            RelativeLayout.LayoutParams parameter = (RelativeLayout.LayoutParams) imgLocation.getLayoutParams();
//            parameter.setMargins(12, 0, 12, 0); // left, top, right, bottom
//            imgLocation.setLayoutParams(parameter);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterCallback.adpaterCallback(null, EVENT_CLICK, position);
                }
            });
        }

        private void setData(String string){
            Log.e("Img Detail: ", "setData: " + string );
            Picasso.get().load("http://admin.mevietnamanhhung.vn/uploads/_thumbs/images/service/" + string)
                    .fit().centerCrop()
                    .error(R.drawable.ic_copyright_symbol).into(imgLocation);
        }
    }
}
