package com.example.toant.googlemap.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.toant.googlemap.R;
import com.example.toant.googlemap.adapters.ImageDetailAdapter;
import com.example.toant.googlemap.adapters.ImageSliderAdapter;
import com.example.toant.googlemap.models.CityDistrict;
import com.example.toant.googlemap.models.MapLocation;
import com.example.toant.googlemap.utils.NetworkProcessor;
import com.example.toant.googlemap.utils.Utils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends BaseActivity {

    private static final String TAG = "DetailActivity";
    private MapLocation detailLocation = null;
    private RecyclerView rvDetail;
    private TextView tvLocationName, tvLocationAddress, tvLocationIntroduction, tvLocationCity;
    private RelativeLayout flViewImage;
    private ImageDetailAdapter imageDetailAdapter;
    private ImageButton btnCloseImage;
    private ScrollView llDetail;
    private ImageView imageView;
    private Button mBtnChiDuong;
    private int currentPage = 0;
    private int NUM_PAGES = 0;

    private CirclePageIndicator indicator;
    private ViewPager viewPagerSliderImage;


    private ArrayList<String> listLinkImage = new ArrayList<>();

    @Override
    public void initUI() {
        Log.e(TAG, "initUI: ");
        showHomeButton();
        setUpContent();

    }

    private void downLoadData(String id) {
        NetworkProcessor.GET(this, ID_API_GET_CITY, API_GET_CITY + id, CityDistrict.class, false, this, false);
    }

    @Override
    public void downloadSuccess(int processId, Object data) {
        super.downloadSuccess(processId, data);
        Log.e(TAG, "downloadSuccess: " + data);

        if (processId == ID_API_TINHTHANH) {
            ArrayList<CityDistrict> arrayList = (ArrayList<CityDistrict>) data;

            if (arrayList.size() > 0) {
                tvLocationCity.setText(arrayList.get(0).getTenQuanHuyen() + " - " + arrayList.get(0).getTenTinhThanh());
            }
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_detail;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpContent() {

        imageView = findViewById(R.id.imagethumb);
        rvDetail = findViewById(R.id.rvDetail);
        tvLocationName = findViewById(R.id.tvLocationName);
        tvLocationAddress = findViewById(R.id.tvLocationAddress);
        tvLocationIntroduction = findViewById(R.id.tvLocationIntroduction);
        tvLocationCity = findViewById(R.id.tvLocationCity);
        flViewImage = findViewById(R.id.flViewImage);
        btnCloseImage = findViewById(R.id.btnCloseImage);
        mBtnChiDuong = findViewById(R.id.btn_chi_duong);
        /// Mở Gmaps để chỉ đường
        mBtnChiDuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + detailLocation.getLatitudeLocation() + ",+" + detailLocation.getLongitudeLocation());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        llDetail = findViewById(R.id.llDetail);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        viewPagerSliderImage = (ViewPager) findViewById(R.id.viewPagerSliderImage);

        btnCloseImage.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            String json = intent.getStringExtra(DETAIL);
            detailLocation = new Gson().fromJson(json, MapLocation.class);

            //Avatar
            Picasso.get().load("http://admin.mevietnamanhhung.vn/uploads/images/service/" + detailLocation.getPhotoLocation())
                    .fit().centerCrop()
                    .placeholder(R.drawable.ic_copyright_symbol)
                    .error(R.drawable.ic_copyright_symbol).into(imageView);
            tvLocationName.setText(detailLocation.getTenLocation());
            tvLocationAddress.setText(detailLocation.getDiaChiLocation());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvLocationIntroduction.setText(Html.fromHtml(detailLocation.getNoiDungLocation(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvLocationIntroduction.setText(Html.fromHtml(detailLocation.getNoiDungLocation()));
            }

            tvLocationCity.setText(detailLocation.tenPhuongXa + " - " + detailLocation.tenQuanHuyen + " - " + detailLocation.tenTinhThanh);
            String listStr = detailLocation.getPhotoListLocation();
            listLinkImage = Utils.ConvertStringToArrayListImage(listStr);
            Log.i("Nganngan", "" + detailLocation.getIdLocation());

//            Hình liên quan
            LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            rvDetail.setHasFixedSize(true);
            manager.setItemPrefetchEnabled(false);
            rvDetail.setLayoutManager(manager);
            imageDetailAdapter = new ImageDetailAdapter(this, listLinkImage, this);
            rvDetail.setAdapter(imageDetailAdapter);

        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btnCloseImage) {
            flViewImage.setVisibility(View.GONE);
            llDetail.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void adpaterCallback(Object data, int processId, int position) {
        if (processId == EVENT_CLICK) {

            initImageSlider(viewPagerSliderImage, indicator, getBaseContext(), listLinkImage);
            viewPagerSliderImage.setCurrentItem(position, true);
            flViewImage.setVisibility(View.VISIBLE);
            llDetail.setVisibility(View.GONE);
        }
    }

    private void initImageSlider(ViewPager viewPager, CirclePageIndicator indicator, Context mContext, List<String> listUrl) {

        //Set the pager with an adapter
        viewPager.setAdapter(new ImageSliderAdapter(mContext, listUrl));

        indicator.setViewPager(viewPager);

        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES = listUrl.size();


        // Auto start of viewpager
//        final Handler handler = new Handler();
//        final Runnable Update = new Runnable() {
//            public void run() {
//                if (currentPage == NUM_PAGES) {
//                    currentPage = 0;
//                }
//
//            }
//        };
//        Timer swipeTimer = new Timer();
//        swipeTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }
}




