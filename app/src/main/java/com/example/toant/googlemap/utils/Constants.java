package com.example.toant.googlemap.utils;

/**
 * Created by toant on 31/05/2018.
 */

public interface Constants {
    String URL_SERVER = "http://admin.mevietnamanhhung.vn/api/";
    String URL_SERVER_IMAGE = "http://admin.mevietnamanhhung.vn/uploads/images/service/";
    String URL_SERVER_THUMB_IMAGE = "http://admin.mevietnamanhhung.vn/uploads/_thumbs/images/service/";

    /*PARSE DATA*/
    String MESSAGE = "message";
    String STATUS_CODE = "status";
    String DATA = "data";
    String RESULT = "results";

    /*VARIABLES*/
    String LOCATION = "LOCATION";
    String DESTINATION = "DESTINATION";
    String ISSHOW = "ISSHOW";
    String DETAIL = "DETAIL";

    /*API*/
    String API_LOGIN = URL_SERVER + "userLogin.php";
    String API_LOCATION = URL_SERVER + "getLocations.php";
    String API_QUANHUYEN = URL_SERVER + "getQuanHuyen.php?idTinhThanh=";
    String API_TINHTHANH = URL_SERVER + "getTinhThanh.php";
    String API_INSERT_LOCATION = URL_SERVER + "insertLocation.php";
    String API_INSERT_IMAGE = URL_SERVER + "uploadImages.php";
    String API_GET_CITY = URL_SERVER + "getName.php?idQuanHuyen=";
    String API_GET_XA= URL_SERVER + "getPhuongXa.php?idQuanHuyen=";

    String API_GOOGLE_DIRECTION = "https://maps.googleapis.com/maps/api/directions/json?";
    String API_GOOGLE_DISTANCEMATRIX="https://maps.googleapis.com/maps/api/distancematrix/json?origins=";

    /*ID API*/
    int ID_API_LOGIN = Utils.PROCESS++;
    int ID_API_LOCATION = Utils.PROCESS++;
    int ID_API_QUANHUYEN = Utils.PROCESS++;
    int ID_API_TINHTHANH = Utils.PROCESS++;
    int ID_API_INSERT_LOCATION = Utils.PROCESS++;
    int ID_API_INSERT_IMAGE = Utils.PROCESS++;
    int ID_API_INSERT_IMAGE_RELATIVE = Utils.PROCESS++;
    int ID_API_INSERT_LAST_IMAGE_RELATIVE = Utils.PROCESS++;
    int ID_API_GET_CITY = Utils.PROCESS++;
    int ID_API_GET_XA = Utils.PROCESS++;

    int ID_API_GOOGLE_DIRECTION = Utils.PROCESS++;
    int ID_EVENTBUS = Utils.PROCESS++;
    int ID_BROADCAST = Utils.PROCESS++;
    int ID_API_GOOGLE_DISTANCEMATRIX= Utils.PROCESS++;

    /*REQUESR CODE*/
    int GALLERY_PICTURE = Utils.PROCESS++;
    int CAMERA_REQUEST = Utils.PROCESS++;
    int EVENT_CLICK = Utils.PROCESS++;
}
