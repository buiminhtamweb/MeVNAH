package com.example.toant.googlemap.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.toant.googlemap.R;
import com.example.toant.googlemap.models.MapLocation;
import com.example.toant.googlemap.models.Xa;
import com.example.toant.googlemap.utils.AdapterCallback;
import com.example.toant.googlemap.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



/**
 * Created by toant on 31/05/2018.
 */

public class LocationAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> implements Constants {

    private Context context;
    private ArrayList<MapLocation> arrayList,listDefault;
    private AdapterCallback adapterCallback;

    public LocationAdapter(Context context,ArrayList<MapLocation> arrayList,AdapterCallback adapterCallback){
        this.context = context;
        this.listDefault = arrayList;
        this.arrayList = arrayList;
        this.adapterCallback = adapterCallback;
    }

    public void search(String searching) {
        if (TextUtils.isEmpty(searching)) {
            arrayList = listDefault;
            notifyDataSetChanged();
        } else {
            ArrayList<MapLocation> searchList = new ArrayList<>();
            for (MapLocation item : listDefault) {
                if (item.getTenLocation().toLowerCase().contains(searching.toLowerCase()) || item.getDiaChiLocation().toLowerCase().contains(searching.toLowerCase())) {
                    searchList.add(item);
                }
            }
            arrayList = searchList;
            notifyDataSetChanged();
        }
    }

    public void filterData(Xa xa){
        if(xa == null){
            arrayList = listDefault;
            notifyDataSetChanged();
        }else {
            ArrayList<MapLocation> filterList = new ArrayList<>();
            for (MapLocation item : listDefault) {
                if (item.getIdPhuongXa().equals(xa.getIdPhuongXa())) {
                    filterList.add(item);
                }
            }
            arrayList = filterList;
            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_location, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MapLocation mapLocation = arrayList.get(position);
        ItemViewHolder vh = (ItemViewHolder) viewHolder;
        vh.position = position;
        vh.mapLocation = arrayList.get(position);
        vh.setData(mapLocation);
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

        private TextView tvName,tvAddress,tvLatng;
        private ImageView imgLocation;
        private int position = -1;
        private MapLocation mapLocation;
        private ItemViewHolder(final View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvLatng = itemView.findViewById(R.id.tvLatng);
            imgLocation = itemView.findViewById(R.id.imgLocation);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterCallback.adpaterCallback(mapLocation, EVENT_CLICK, position);
                }
            });
        }

        private void setData(MapLocation mapLocation){
            tvName.setText(mapLocation.getTenLocation());
            tvAddress.setText(Html.fromHtml(mapLocation.getDiaChiLocation()));
            tvLatng.setText("Latitude: " + mapLocation.getLatitudeLocation() + ", Longtitude: " + mapLocation.getLongitudeLocation());
            Picasso.get().load("http://admin.mevietnamanhhung.vn/uploads/_thumbs/images/service/" + mapLocation.getPhotoLocation())
                    .placeholder(R.drawable.ic_copyright_symbol)
                    .error(R.drawable.ic_copyright_symbol).into(imgLocation);
        }
    }
}
