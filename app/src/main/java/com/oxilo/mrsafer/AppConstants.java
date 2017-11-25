package com.oxilo.mrsafer;

import android.Manifest;

import com.oxilo.mrsafer.modal.ReportList;

import java.util.List;

/**
 * Created by ericbasendra on 29/05/16.
 */
public class AppConstants {
    public static final int LONG_DELAY = 3500; // 3.5 seconds
    public static final int SHORT_DELAY = 2000; // 2 seconds
    public static final String LBM_EVENT_LOCATION_UPDATE = "lbmLocationUpdate";
    public static final String INTENT_FILTER_LOCATION_UPDATE = "intentFilterLocationUpdate";
    public static final String BASE_URL = "http://mistersafer.com/";
    public static final String ADD_REPORTS = "api/app.php";
    public static final String GET_REPORTS = "api/app.php";
    public static final String SEND_ABUSE = "api/app.php";
    public static final int MY_PERMISSIONS_REQUEST_CALL = 1;
    public static final int PERMISSION_ALL = 113;
    public static final int PERMISSION_VIDEO = 114;
    public static final int REQUEST_TAKE_PHOTO = 1;
    public static final int REQUEST_VIDEO_CAPTURE = 2;
    public static final String EMERGENY_POLICE="17";
    public static final String EMERGENY_FIRE="18";
    public static final String EMERGENY_AMBULANCE="15";
    public static int CURRENT_PAGE = 1;
    public static int OFFSET = 100;
    public static boolean IS_REFRESH = false;
    public static List<ReportList> INCIDENT_MARKER_LIST ;

    public static final int TOP_MARGIN = 5;
    public static final int ZERO_MARGIN = 0;

    public static int TAB_POSITION = 1;
    public static final String[] PERMISSIONS = { Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};


    public static final String argument = "1";
    public static final String fight = "2";
    public static final String weapons = "3";
    public static final String degradation = "4";
    public static final String theft = "5";
    public static final String burglary = "6";
    public static final String verbel_abuse = "7";
    public static final String fondling = "8";
    public static final String rape = "9";
    public static final String suspicious = "10";
    public static final String shady = "11";
    public static final String missing = "12";
    public static final String malaise = "13";
    public static final String accident = "14";
    public static final String fire = "15";

}
