package com.example.toant.googlemap.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.toant.googlemap.R;
import com.example.toant.googlemap.adapters.RelativeImageAdapter;
import com.example.toant.googlemap.helper.ImageHelper;
import com.example.toant.googlemap.helper.PermissionHelper;
import com.example.toant.googlemap.models.City;
import com.example.toant.googlemap.models.District;
import com.example.toant.googlemap.models.EventBusInfo;
import com.example.toant.googlemap.models.Xa;
import com.example.toant.googlemap.utils.NetworkProcessor;
import com.example.toant.googlemap.utils.Utils;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.schibstedspain.leku.LocationPickerActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AddNewActivity extends BaseActivity implements View.OnTouchListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = AddNewActivity.class.getSimpleName();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private EditText edtOrganizationName, edtLocation, edtPhoneNumber, edtIntroduction;
    private Button btnTakeAvatar, btnRelativeImage;
    private Spinner spnCity, spnDistrict, spnXa, spnXA;
    private ImageView imgAddAvt, imgRelativeAvt;
    private Location location;
    private Bitmap myBitmap, bitmapAvatar;
    private Uri picUri;
    private String fileName = "", address = "", lat = "0.0", longt = "0.0", id_huyen = "", id_xa = "";
    private ArrayList<City> listCity = new ArrayList();
    private ArrayList<District> listDistrict = new ArrayList();
    private ArrayList<Xa> listXa = new ArrayList();
    private ArrayAdapter<String> cityAdapter, districtAdapter, xaAdapter;
    private ImageHelper imageHelper;
    private PermissionHelper permissionHelper;
    private Boolean isAvatar = true;
    private ArrayList<String> photoListArray = new ArrayList<>();
    private GridView gvRelativeImage;
    private RelativeImageAdapter relativeImageAdapter;
    private ArrayList<Bitmap> listImageBitmap = new ArrayList<>();
    private ArrayList<Uri> listImageUri = new ArrayList<>();
    private Uri uriAvatar;
    private ImageButton btnSelectLocation;
    private LinearLayout linlaHeaderProgress;

    @Override
    public void initUI() {
        showHomeButton();
        setUpContent();
    }

    @SuppressLint("ResourceType")
    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_add_new;
    }

    @SuppressLint("MissingPermission")
    private void setUpContent() {
        edtOrganizationName = findViewById(R.id.edtOrganizationName);
        edtLocation = findViewById(R.id.edtLocation);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtIntroduction = findViewById(R.id.edtIntroduction);
        btnTakeAvatar = findViewById(R.id.btnTakeAvatar);
        btnRelativeImage = findViewById(R.id.btnRelativeImage);
        spnCity = findViewById(R.id.spnCity);
        spnDistrict = findViewById(R.id.spnDistrict);
        spnXA = findViewById(R.id.spnXA);
        imgAddAvt = findViewById(R.id.imgAddAvt);
        imgRelativeAvt = findViewById(R.id.imgRelativeAvt);
        gvRelativeImage = findViewById(R.id.gvRelativeImage);
        btnSelectLocation = findViewById(R.id.btnSelectLocation);
        btnTakeAvatar.setOnClickListener(this);
        btnRelativeImage.setOnClickListener(this);
        spnCity.setOnItemSelectedListener(this);
        spnDistrict.setOnItemSelectedListener(this);
        spnXA.setOnItemSelectedListener(this);
        btnSelectLocation.setOnClickListener(this);

//        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        relativeImageAdapter = new RelativeImageAdapter(this, listImageBitmap);
        gvRelativeImage.setAdapter(relativeImageAdapter);
        Intent intent = getIntent();
        if (intent != null) {
            String json = intent.getStringExtra(LOCATION);
            location = new Gson().fromJson(json, Location.class);
            Log.i(TAG, "setUpContent: " + json);
        }

        cityAdapter = CreateSpinner(spnCity, getString(R.string.btnCity));
        districtAdapter = CreateSpinner(spnDistrict, getString(R.string.btnDistrict));
        xaAdapter = CreateSpinner(spnXA, "Xã");


        imageHelper = new ImageHelper(this);
        permissionHelper = new PermissionHelper(this);

        NetworkProcessor.GET(this, ID_API_TINHTHANH, API_TINHTHANH, City.class, false, this, false);

    }

    /*SPINNER CITY- DISTRICT*/

    private ArrayAdapter<String> CreateSpinner(Spinner spinner, String firstItem) {
        ArrayList<String> listData = new ArrayList<>();
        listData.add(0, firstItem);
        Log.i(TAG, "Thêm " + listData.size());
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listData);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        return spinnerArrayAdapter;
    }


    private void UpdateSpinner(ArrayList<String> listData, ArrayAdapter<String> arrayAdapter, String firstItem) {
        listData.add(0, firstItem);
        arrayAdapter.clear();
        arrayAdapter.addAll(listData);
        arrayAdapter.notifyDataSetChanged();
    }

    /*END SPINNER*/

    /*LINK URL - PATH */

    private String CreateBaseURLInsert(String ten, String id_type, String id_huyen, String dia_chi, String sdt, String lat, String longt, String gioi_thieu, String photo, ArrayList<String> photolist) {
        String photoListStr = "";
        for (int i = 0; i < photolist.size(); i++) {
            photoListStr = photolist.size() == 1 ? photoListStr + photolist.get(i) : (i == 0 || i < photolist.size() - 1) ? photoListStr + photolist.get(i) + "," : photoListStr + photolist.get(i);
        }
        photoListStr = "[" + photoListStr + "]";

        return API_INSERT_LOCATION + "?ten=" + ten + "&id_type=" + id_type + "&id_huyen=" + id_huyen + "&dia_chi=" + dia_chi + "&sdt=" + sdt + "&lat=" + lat + "&long=" + longt + "&gioi_thieu=" + gioi_thieu + "&photo=" + photo + "&photolist=" + photoListStr;
    }

    private boolean CheckDataIsValid(String ten) {
        if (ten.isEmpty()) {
            Utils.showMessageOK(this, getString(R.string.msg_empty_ogr_name), null, true);
            return false;
        }
        if (edtLocation.getText().toString().equals("")) {
            Utils.showMessageOK(this, getString(R.string.msg_empty_location), null, true);
            return false;
        }
        if (id_huyen.isEmpty()) {
            Utils.showMessageOK(this, getString(R.string.msg_empty_district), null, true);
            return false;
        }
        if (myBitmap == null) {
            Utils.showMessageOK(this, getString(R.string.msg_empty_avatar), null, true);
            return false;
        }
        return true;
    }

    private String CreateURLInsert() {
        String ten = edtOrganizationName.getText().toString();
        String sdt = edtPhoneNumber.getText().toString();
        String gioi_thieu = edtIntroduction.getText().toString();
//        String dia_chi = address;
        String dia_chi = edtLocation.getText().toString();

        String id_type = "";
        String id_huyen = this.id_xa.equals("") ? this.id_huyen : this.id_xa;
        String photo = fileName;
        String lat = this.lat;
        String longt = this.longt;

//        String location = edtLocation.getText().toString();
//        int index = location.indexOf(",");
//        String lat = location.substring(0,index).trim();
//        String longt = location.substring(index+1,location.length()).trim();

        if (CheckDataIsValid(ten)) {
            return CreateBaseURLInsert(ten, id_type, id_huyen, dia_chi, sdt, lat, longt, gioi_thieu, photo, photoListArray);
        }
        return "";
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public String getPathFromFileManager(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    /*END LINK URL- PATH*/

    private void ClickEventTakePhoto(boolean isAvatar) {

        if (null != imageHelper.getPickImageChooserIntent(isAvatar)) {


            startActivityForResult(imageHelper.getPickImageChooserIntent(isAvatar), CAMERA_REQUEST);
        } else {
            Utils.showMessageOKCancel(this, getString(R.string.msg_need_permission),
                    (dialog, which) -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(permissionHelper.permissionsRejected.toArray(new String[permissionHelper.permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        isAvatar = v.getId() == R.id.btnTakeAvatar;
        switch (v.getId()) {
            case R.id.btnTakeAvatar:
                ClickEventTakePhoto(isAvatar);
                break;
            case R.id.btnRelativeImage:
                ClickEventTakePhoto(isAvatar);
                break;
            case R.id.btnSelectLocation:
                if (location != null) {
                    Intent locationPickerIntent = new LocationPickerActivity.Builder().withLocation(location.getLatitude(), location.getLongitude())
                            .withSearchZone("vi_VN").withGooglePlacesEnabled().build(getApplicationContext());
                    startActivityForResult(locationPickerIntent, 1);
                } else {
                    Intent locationPickerIntent = new LocationPickerActivity.Builder().withSearchZone("vi_VN").withGooglePlacesEnabled().build(getApplicationContext());
                    startActivityForResult(locationPickerIntent, 1);
                }
                break;
        }
        super.onClick(v);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        picUri = savedInstanceState.getParcelable("pic_uri");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) { // GPS
            if (resultCode == RESULT_OK) {
                double latitude = data.getDoubleExtra(LocationPickerActivity.LATITUDE, 0);
                this.lat = latitude + "";
                Log.d("LATITUDE****", String.valueOf(latitude));
                double longitude = data.getDoubleExtra(LocationPickerActivity.LONGITUDE, 0);
                this.longt = longitude + "";
                Log.d("LONGITUDE****", String.valueOf(longitude));
                String address = data.getStringExtra(LocationPickerActivity.LOCATION_ADDRESS);
                Log.d("ADDRESS****", String.valueOf(address));
                this.address = address;
                String postalcode = data.getStringExtra(LocationPickerActivity.ZIPCODE);
                Log.d("POSTALCODE****", String.valueOf(postalcode));
                Address fullAddress = data.getParcelableExtra(LocationPickerActivity.ADDRESS);
                if (fullAddress != null) {
                    Log.d("FULL ADDRESS****", fullAddress.toString());
                }
                String text = lat + "," + longt;
                edtLocation.setText(text);
            }
            if (resultCode == RESULT_CANCELED && this.address.equals("")) {
                Toast.makeText(this, getString(R.string.msg_not_select_location), Toast.LENGTH_SHORT).show();
            }
        }


        //GET DATA IMAGE RESULT
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            myBitmap = null;


            int count = data.getClipData() == null ? 0 : data.getClipData().getItemCount();
            Log.i(TAG, "onActivityResult: " + count);
            if (count > 1) { //CHọn nhiều hình ảnh
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    Bitmap bitmap;
                    try {
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        if (bitmap != null) {
                            bitmap = imageHelper.getResizedBitmap(bitmap, 300);
                            listImageBitmap.add(bitmap);
                        } else {
                            Toast.makeText(this, "Không thể chọn tấm ảnh thứ " + (i + 1) + ". Vui lòng chọn tấm ảnh khác.", Toast.LENGTH_SHORT).show();
                        }
                        listImageUri.add(uri); //DEL
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                relativeImageAdapter.notifyDataSetChanged();
            } else {

                //Fix

                //Ảnh chụp từ camera
                if (data.getData() == null) {

                    Bundle extras = data.getExtras();
                    myBitmap = (Bitmap) extras.get("data");
                    myBitmap = imageHelper.getResizedBitmap(myBitmap, 300);


                } else if (imageHelper.getPickImageResultUri(data) != null) {// gallery
                    Log.e(TAG, "onActivityResult: DATA " + data.getData().toString());
                    picUri = imageHelper.getPickImageResultUri(data);
                    try {
                        myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);

                        if (myBitmap != null) {
                            myBitmap = imageHelper.getResizedBitmap(myBitmap, 300);
                        } else {
                            Toast.makeText(this, "Không thể chọn tấm ảnh này. Vui lòng chọn tấm ảnh khác.", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
//                    if (data.getExtras() != null) {// camera
//                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//                        if (myBitmap != null) {
//                            myBitmap = imageHelper.getResizedBitmap(myBitmap, 300);
//                            picUri = imageHelper.getImageUri(bitmap);
//                        } else {
//                            Toast.makeText(this, "Không thể chọn tấm ảnh này. Vui lòng chọn tấm ảnh khác.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
                }

                if (isAvatar) {

                    //Fix

                    if (data.getData() == null) { //Kiểm tra gửi từ Camera

                        Bundle extras = data.getExtras();
                        myBitmap = (Bitmap) extras.get("data");
                        myBitmap = imageHelper.getResizedBitmap(myBitmap, 300);
                        imgAddAvt.setImageBitmap(myBitmap);
                        bitmapAvatar = myBitmap; // Gán giá trị cho Bitmap Avatar
                        uriAvatar = imageHelper.getPickImageResultUri(data);
                    } else {
                        Log.e(TAG, "onActivityResult: DATA " + data.getData().toString());
                        uriAvatar = imageHelper.getPickImageResultUri(data);
                        if (myBitmap != null) {
//                            File sourceFile = new File(pathAvatar);
                            Log.e(TAG, "onActivityResult: name file" + myBitmap);
                            imgAddAvt.setImageBitmap(myBitmap);
                        } else {
                            Toast.makeText(this, "Không thể chọn tấm ảnh này. Vui lòng chọn tấm ảnh khác.", Toast.LENGTH_SHORT).show();
                        }
                    }


                } else {
                    if (myBitmap != null) {
                        listImageBitmap.add(myBitmap);
                    } else {
                        Toast.makeText(this, "Không thể chọn tấm ảnh này. Vui lòng chọn tấm ảnh khác.", Toast.LENGTH_SHORT).show();
                    }
                    // listImageUri.add(picUri);
                }

                relativeImageAdapter.notifyDataSetChanged();
//                imgRelativeAvt.setImageBitmap(myBitmap);
                if (imgRelativeAvt.getVisibility() == View.VISIBLE) {
                    imgRelativeAvt.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.getItem(1).setIcon(R.drawable.ic_save_black_24dp);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.delete:
                deleteContent();
                return true;
            case R.id.save:

                //Fix
                if (!CreateURLInsert().isEmpty()) {
                    if (listImageBitmap.size() == 0) {
                        // không có hình ảnh liên quan, up load avatar.
//                        Log.i("Nganngan", "" + getPath(uriAvatar));
                        Bitmap bitmap = ((BitmapDrawable) imgAddAvt.getDrawable()).getBitmap();
                        File sourceFile = bitmapConvertToFile(this, bitmap);
                        NetworkProcessor.UploadImage(this, sourceFile, ID_API_INSERT_IMAGE, this);
                    } else {
                        for (int i = 0; i < listImageBitmap.size(); i++) {

                            File file = bitmapConvertToFile(this, listImageBitmap.get(i));
                            int newId = i == listImageBitmap.size() - 1 ? ID_API_INSERT_LAST_IMAGE_RELATIVE : ID_API_INSERT_IMAGE_RELATIVE;
                            NetworkProcessor.UploadImage(this, file, newId, this);
                            linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
                            linlaHeaderProgress.setVisibility(View.VISIBLE);
                        }
                    }
                }
                return true;
            default:
                //return super.onOptionsItemSelected(item);
                return false;
        }


    }

    //Fix
    private File bitmapConvertToFile(Context context, Bitmap bitmap) {
        //create a file to write bitmap data
        Date date = new Date();
        long filename = date.getTime();
        File f = new File(context.getCacheDir(), String.valueOf(filename));
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;

    }

    private void deleteContent() {
        edtIntroduction.setText("");
        edtLocation.setText("");
        edtPhoneNumber.setText("");
        edtOrganizationName.setText("");
        listImageUri.clear();
        listImageBitmap.clear();
        uriAvatar = null;
        picUri = null;
        imgAddAvt.setImageBitmap(null);
        relativeImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spnCity:
                if (position > 0) {
                    Log.i(TAG, "onItemSelected: " + listCity.get(position).getTenTinhThanh());
                    NetworkProcessor.GET(this, ID_API_QUANHUYEN, API_QUANHUYEN + listCity.get(position - 1).getIdTinhThanh(), District.class, false, this, false);
                    break;
                } else {
                    spnDistrict.setSelection(0);
                }
            case R.id.spnDistrict:
                if (position > 0) {
                    District district = listDistrict.get(position - 1);
                    id_huyen = district.getIdQuanHuyen();
                    NetworkProcessor.GET(this, ID_API_GET_XA, API_GET_XA + id_huyen, Xa.class, false, this, false);
                }
                break;
            case R.id.spnXA:
                if (position > 0) {
                    Xa xa = listXa.get(position - 1);
                    id_xa = xa.getIdPhuongXa();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void downloadSuccess(int processId, Object data) {
        if (processId == ID_API_INSERT_IMAGE) {
            Log.i(TAG, "downloadSuccess: " + data);
            fileName = (String) data;
            // insert location sau khi upload avatar:
            CreateURLInsert();
            if (!CreateURLInsert().isEmpty()) {
                NetworkProcessor.GET(this, ID_API_INSERT_LOCATION, CreateURLInsert(), null, true, this, false);
            }
        }

        if (processId == ID_API_INSERT_IMAGE_RELATIVE) {
            String name = (String) data;
            photoListArray.add(name);
        }
        // Đến khi up load tấm ảnh liên quan cuối cùng mới bắt đầu upload avatar
        if (processId == ID_API_INSERT_LAST_IMAGE_RELATIVE) {
            String name = (String) data;
            photoListArray.add(name);

            //Fix
            File sourceFile = bitmapConvertToFile(AddNewActivity.this, bitmapAvatar);
            NetworkProcessor.UploadImage(this, sourceFile, ID_API_INSERT_IMAGE, this);
        }

        if (processId == ID_API_TINHTHANH) {
            listCity = (ArrayList<City>) data;
            ArrayList<String> arrayList = new ArrayList<>();
            for (City city : listCity) {
                arrayList.add(city.getTenTinhThanh());
            }
            UpdateSpinner(arrayList, cityAdapter, getString(R.string.btnCity));
        }
        if (processId == ID_API_QUANHUYEN) {
            listDistrict = (ArrayList<District>) data;
            ArrayList<String> arrayList = new ArrayList<>();
            for (District district : listDistrict) {
                arrayList.add(district.getTenQuanHuyen());
            }
            Log.d("nhutnhut", "" + arrayList);
            UpdateSpinner(arrayList, districtAdapter, getString(R.string.btnDistrict));
        }
        if (processId == ID_API_GET_XA) {
            if (!(data instanceof String)) {
                listXa = (ArrayList<Xa>) data;
                ArrayList<String> arrayList = new ArrayList<>();
                for (Xa xa : listXa) {
                    arrayList.add(xa.getTenPhuongXa());
                }
                UpdateSpinner(arrayList, xaAdapter, "Xã");
                Log.d("nhutnhut", "" + arrayList);
            }
        }
        if (processId == ID_API_INSERT_LOCATION) {
            String msg = (String) data;
            if (msg.toLowerCase().equals("ok")) {
                Utils.showMessageOK(this, getString(R.string.msg_insert_succesful), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Thêm thành công.
                        MarkerOptions markerOptions = new MarkerOptions();
                        LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(longt));
                        markerOptions.position(latLng);
                        markerOptions.title(address);
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        postEvent(new EventBusInfo(ID_EVENTBUS, markerOptions));
                        Log.i(TAG, "onClick: DONE");
                        finish();
                    }
                }, false);
            }
        }
        super.downloadSuccess(processId, data);
    }

    @Override
    public void downloadError(int processId, String msg) {
        Toast.makeText(this, getString(R.string.msg_cannot_upload), Toast.LENGTH_SHORT).show();
        super.downloadError(processId, msg);
    }

}