package com.oxilo.mrsafer.activty;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.oxilo.mrsafer.AppConstants;
import com.oxilo.mrsafer.AppController;
import com.oxilo.mrsafer.R;
import com.oxilo.mrsafer.base.BaseActivity;
import com.oxilo.mrsafer.fragement.ActusDetailFragment;
import com.oxilo.mrsafer.fragement.ActusFragment;
import com.oxilo.mrsafer.fragement.Alerts;
import com.oxilo.mrsafer.fragement.CaptureIncident;
import com.oxilo.mrsafer.fragement.DetailFragment;
import com.oxilo.mrsafer.fragement.Position;
import com.oxilo.mrsafer.fragement.Reports;
import com.oxilo.mrsafer.fragement.WebFragment;
import com.oxilo.mrsafer.modal.Actus;
import com.oxilo.mrsafer.modal.IncidentMarker;
import com.oxilo.mrsafer.modal.ReportList;
import com.oxilo.mrsafer.modal.ReportResponse;
import com.oxilo.mrsafer.service.MrSaferWebService;
import com.oxilo.mrsafer.service.RetryWithDelay;
import com.oxilo.mrsafer.service.ServiceFactory;
import com.oxilo.mrsafer.utility.ActivityUtils;
import com.oxilo.mrsafer.utility.PermissionUtils;
import com.oxilo.mrsafer.view.MultiDrawable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LandingActivity extends BaseActivity implements
        Position.OnFragmentInteractionListener,
        Alerts.OnFragmentInteractionListener,
        Reports.OnFragmentInteractionListener,
        CaptureIncident.OnFragmentInteractionListener,
        DetailFragment.OnFragmentInteractionListener,
        ActusFragment.OnFragmentInteractionListener,
        ActusDetailFragment.OnFragmentInteractionListener,
        WebFragment.OnFragmentInteractionListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    Position fragment;

    //HAndling Map
    public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private GoogleMap mMap;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    MarkerOptions markerOptions;
    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;

    /**
     * Represents a Circle on Map.
     */
    Circle mapCircle;

    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;

    public static final String TAG = "TAG==";
    String phn_num = "17";
    private double incident_lat;
    private double incident_long;
    private int tab_pos = 1;
    private List<ReportList> reportList;
    public  ClusterManager<ReportList> mClusterManager;
    public  ReportList clickedClusterItem;
    private View mProgressView;
    private View mRootView;
    int current_page = 1;
    boolean isCLuster = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
        }
        super.setContentView(R.layout.activity_landing);

            // withholding the previously created fragment from being created again
            // On orientation change, it will prevent fragment recreation
            // its necessary to reserving the fragment stack inside each tab
            try {
                AppConstants.INCIDENT_MARKER_LIST = new ArrayList<>();
                initScreen();
                startPolling();
            }catch (Exception ex){
                ex.printStackTrace();
            }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onActusResult(int count) {
        setActusCount(count);
    }

    @Override
    public void updateReportCount(int size) {
        setCountReport(size);
    }

    @Override
    public void onEmergencyEventClik(String ph_num) {
        this.phn_num = ph_num;
    }


    @Override
    public void OnResultSelected(GoogleMap map, View mRootView, View mProgressView) {
        this.mMap = map;
        this.mRootView = mRootView;
        this.mProgressView = mProgressView;
        tab_pos = 1;
        if (map != null) {
            mMap = map;
            map.setMyLocationEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(LandingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions(LandingActivity.this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                    return;
                }
            } else {
                startLocationUpdates();
                map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                            startLocationUpdates();
                        }
                        return true;
                    }
                });
            }
        }
    }


    private void initScreen() {
        fragment = new Position();
        ActivityUtils.launcFragement(fragment, mContext);
    }


    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(LandingActivity.this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, LandingActivity.this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(LandingActivity.this,
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                ((AppCompatActivity) LandingActivity.this).finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }
        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }

        if (requestCode != AppConstants.MY_PERMISSIONS_REQUEST_CALL) {
            return;
        }
        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.CALL_PHONE)) {
            // Enable the my phone layer if the permission has been granted.
            Log.e("PHN_ nUM", "" + phn_num);
            AppController.getInstance().dialNumber(phn_num, LandingActivity.this);

        } else {
            // Display the missing permission error dialog when the fragments resume.
            if (ActivityCompat.shouldShowRequestPermissionRationale(LandingActivity.this,
                    Manifest.permission.CALL_PHONE)) {
                AppController.getInstance().confirmationDialog(LandingActivity.this
                        , getString(R.string.confirmation_title)
                        , getString(R.string.permission_phone)
                        , phn_num);
            }
        }



    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(LandingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(LandingActivity.this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mGoogleApiClient != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                        startLocationUpdates();
                    }
                    return true;
                }
            });
            mGoogleApiClient.connect();
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            displayLocation();
        }
    }


    /**
     * Creating google api client object
     */
    public synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(LandingActivity.this)
                .enableAutoManage(this, 0 /* clientId */, LandingActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(LocationServices.API)
                .build();

        createLocationRequest();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(LandingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                ActivityCompat.requestPermissions(LandingActivity.this, new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                return;
            } else {
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mCurrentLocation != null) {
                    displayLocation();
                } else {
                    startLocationUpdates();
                }
            }
        } else {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            displayLocation();
            startLocationUpdates();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(LandingActivity.this.getApplicationContext(), "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(LandingActivity.this.getApplicationContext(), "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        if (mCurrentLocation != null) {
            displayLocation();
            stopLocationUpdates();
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        //**************************
    }

    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    /**
     * Requests location updates from the FusedLocationApi.
     */
    public void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(LandingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                ActivityCompat.requestPermissions(LandingActivity.this, new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                return;
            } else {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    public void displayLocation() {
        try {
            if (mCurrentLocation != null) {
                if (AppConstants.TAB_POSITION == 1) {
//                    mMap.clear();
                    double latitude =  mCurrentLocation.getLatitude();
                    double longitude = mCurrentLocation.getLongitude();
                    incident_lat = latitude;
                    incident_long = longitude;
                    setUpMap();
                } else if (AppConstants.TAB_POSITION == 3) {
                    double latitude = mCurrentLocation.getLatitude();
                    double longitude = mCurrentLocation.getLongitude();
                    incident_lat = latitude;
                    incident_long = longitude;
                } else if (AppConstants.TAB_POSITION == 2) {
                    double latitude = mCurrentLocation.getLatitude();
                    double longitude = mCurrentLocation.getLongitude();
                    incident_lat = latitude;
                    incident_long = longitude;
                }

            } else {
                Toast.makeText(LandingActivity.this, "Couldn't get the location. Make sure location is enabled on the device", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }


    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((LandingActivity) getActivity()).onDialogDismissed();
        }
    }

    public String getAddressInfo(double latitude, double longitude, Context mContext) {

        String address1 = "Paris";
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        // Address found using the Geocoder.
        List<Address> addresses = null;

        String errorMessage = "";
        try {
            // Using getFromLocation() returns an array of Addresses for the area immediately
            // surrounding the given latitude and longitude. The results are a best guess and are
            // not guaranteed to be accurate.
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    // In this sample, we get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available);
            android.util.Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            android.util.Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + latitude +
                    ", Longitude = " + longitude, illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                android.util.Log.e(TAG, errorMessage);
            }
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            android.util.Log.i(TAG, getString(R.string.address_found));
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                android.util.Log.e(TAG, errorMessage);
            }
        } else {
            StringBuilder sb = new StringBuilder();
            Address address = addresses.get(0);


            // Fetch the address lines using {@code getAddressLine},
            // join them, and send them to the thread. The {@link android.location.address}
            // class provides other options for fetching address details that you may prefer
            // to use. Here are some examples:
            // getLocality() ("Mountain View", for example)
            // getAdminArea() ("CA", for example)
            // getPostalCode() ("94043", for example)
            // getCountryCode() ("US", for example)
            // getCountryName() ("United States", for example)

            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                sb.append(address.getAddressLine(i)).append(" ");
            }
//                sb.append(address.getLocality()).append(",");
            String postCode = address.getPostalCode();
            if (postCode != null) {
                sb.append(postCode).append(",");
            }


            sb.append(address.getCountryName());
            String addressLine = sb.toString();
            address1 = addressLine.toString().replaceAll("^,", "").replaceAll(",,", ",").replaceAll(",$", "");
            android.util.Log.i(TAG, getString(R.string.address_found));
        }

        return address1;
    }


    public double getIncident_long() {
        return incident_long;
    }

    public double getIncident_lat() {
        return incident_lat;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (checkPlayServices()) {
            if (!PermissionUtils.isLocationEnabled(LandingActivity.this)) {
                AppController.getInstance().showSettingsAlert(LandingActivity.this);
            } else {
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//                    mGoogleApiClient.connect();
                    startLocationUpdates();
                }
            }
        }
    }

    @Override
    public void refrestLocation() {
        AppConstants.TAB_POSITION = 1;
        mGoogleApiClient.stopAutoManage(LandingActivity.this);
        mGoogleApiClient.disconnect();
        buildGoogleApiClient();
        ActivityUtils.launcFragement(Position.newInstance("", ""), mContext);
    }

    @Override
    public void onReportStartGetLatlong() {
        AppConstants.TAB_POSITION = 3;
        ActivityUtils.launcFragement(Reports.newInstance("", ""), mContext);
    }

    @Override
    public void onRaiseAlertGetLatLong() {
        AppConstants.TAB_POSITION = 2;
        ActivityUtils.launcFragement(Alerts.newInstance("", ""), mContext);
    }

    @Override
    public void onActusClick() {
        AppConstants.TAB_POSITION = 4;
        ActivityUtils.launcFragement(ActusFragment.newInstance("", ""), mContext);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }



    private void setUpMap() throws Exception {
//        ActivityUtils.showProgress(true, mRootView, mProgressView, mContext);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(incident_lat, incident_long), 15.5f));
        mClusterManager = new ClusterManager<ReportList>(mContext, mMap);
//        mClusterManager.setRenderer(new MyClusterRenderer(mContext, mMap, mClusterManager));
        mClusterManager.setRenderer(new ClusterUtil());
//        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());

        mMap.setOnInfoWindowClickListener(mClusterManager); //added
//        mClusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<ReportList>() {
//            @Override
//            public void onClusterItemInfoWindowClick(ReportList incidentMarker) {
//
//            }
//        });

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ReportList>() {
                    @Override
                    public boolean onClusterItemClick(ReportList item) {
                        clickedClusterItem = item;
                        ActivityUtils.launcFragement(DetailFragment.newInstance(item,incident_lat,incident_long),mContext);
                        return false;
                    }
                });
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<ReportList>() {
            @Override
            public boolean onClusterClick(Cluster<ReportList> cluster) {
                clickedClusterItem = cluster.getItems().iterator().next();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        cluster.getPosition(), (float) Math.floor(mMap
                                .getCameraPosition().zoom + 1)), 300,
                        null);
                return false;
            }
        });


        getData(1);
//        getData();
        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(
                new MyCustomAdapterForItems());
        mClusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForItems());
    }


    private void startPolling() throws Exception {
        final MrSaferWebService service = ServiceFactory.createRetrofitService(MrSaferWebService.class, AppConstants.BASE_URL);
        Observable
                .interval(0,10,TimeUnit.SECONDS)
                .flatMap(new Func1<Long, Observable<Actus>>() {
                    @Override
                    public Observable<Actus> call(Long aLong) {
                        return service.getActus("get_web",current_page,1000);
                    }
                })
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Actus>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Actus response) {
                        if (response.getStatus() == 1) {
                            setActusCount(response.getActusList().size());
                        } else {
                            Log.e("MY ERROR", "" + "SOME ERROR OCCURED");
                        }
                    }
                });

    }

    private void getData() throws Exception{
        MrSaferWebService service = ServiceFactory.createRetrofitService(MrSaferWebService.class, AppConstants.BASE_URL);
        Observable<Actus> reportResponseObservable = service.getActus("get_web",current_page,200);
        reportResponseObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Actus>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Actus response) {
                        if (response.getStatus() == 1) {
                            setActusCount(response.getActusList().size());
                        } else {
                            Log.e("MY ERROR", "" + "SOME ERROR OCCURED");
                        }

                    }
                });
    }

    private void getData(int current_page) throws Exception {
        MrSaferWebService service = ServiceFactory.createRetrofitService(MrSaferWebService.class, AppConstants.BASE_URL);
        Observable<ReportResponse> reportResponseObservable = service.getListOfInciden("get_report", current_page, 200, incident_lat, incident_long);
        reportResponseObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ReportResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("ERROR", e.getLocalizedMessage().toString());
                        ActivityUtils.showProgress(false, mRootView, mProgressView, mContext);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ReportResponse response) {
//                        ActivityUtils.showProgress(false, mRootView, mProgressView, mContext);
                        if (response.getStatus() == 1) {
                            setCountReport(response.getReportList().size());
                            addItems(response.getReportList());
                        } else {
                            Log.e("MY ERROR", "" + "SOME ERROR OCCURED");
                        }

                    }
                });
    }



    private void addItems(List<ReportList> reportLists) {
           mClusterManager.clearItems();
        if (reportLists.size()>0){
            Iterator itr = reportLists.iterator();
            while (itr.hasNext()) {
                ReportList report = (ReportList) itr.next();
                mClusterManager.addItem(report);
                AppConstants.INCIDENT_MARKER_LIST.add(report);
            }
            mClusterManager.cluster();
        }
    }




    private void addItems(List<ReportList> reportLists,boolean clear){
        mClusterManager.clearItems();
        if (reportLists.size()>0){
            Iterator itr = reportLists.iterator();
            while (itr.hasNext()) {
                ReportList report = (ReportList) itr.next();
                mClusterManager.addItem(report);
            }

            mClusterManager.cluster();
        }
    }

    private LatLng position(double lat, double lng) {
        return new LatLng(lat, lng);
    }

    public class MyCustomAdapterForItems implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyCustomAdapterForItems() {
            myContentsView = getLayoutInflater().inflate(
                    R.layout.info_window, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            TextView tvTitle = ((TextView) myContentsView
                    .findViewById(R.id.txtTitle));
            TextView tvSnippet = ((TextView) myContentsView
                    .findViewById(R.id.txtSnippet));

            tvTitle.setText(clickedClusterItem.gettitle());
            tvSnippet.setText(clickedClusterItem.getDescription());

            return myContentsView;
        }
    }


    private int setIcon(ReportList item) {
        int profileIcon;
        if (item.gettitle().equals("ALTERCATION")) {
            profileIcon = R.drawable.altercation_marker;
        }else if (item.gettitle().equals("OFFENCE")){
            profileIcon = R.drawable.offence_marker;
        }else if (item.gettitle().equals("HARASSMENT")){
            profileIcon = R.drawable.harassement_marker;
        }else if (item.gettitle().equals("DOUBT")){
            profileIcon = R.drawable.doubt_marker;
        }else if (item.gettitle().equals("INCIDENT")){
            profileIcon = R.drawable.incident_marker;
        }else if (item.gettitle().equals("OCCURRENCE")){
            profileIcon = R.drawable.other_marker;
        }else {
         //Not a valid choice
            profileIcon = R.drawable.altercation_marker;
        }

        return profileIcon;

    }

    public class ClusterUtil extends DefaultClusterRenderer<ReportList > {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;


        public ClusterUtil() {
            super(getApplicationContext(), mMap, mClusterManager);
            View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterIconGenerator.setBackground(null);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(getApplicationContext());
            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
            mIconGenerator.setBackground(null);
        }

        @Override
        protected void onBeforeClusterItemRendered(ReportList item, MarkerOptions markerOptions) {
            super.onBeforeClusterItemRendered(item, markerOptions);
            mImageView.setImageResource(setIcon(item));
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.gettitle());
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<ReportList> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (ReportList p : cluster.getItems()) {
                // Draw 4 at most.
//                if (profilePhotos.size() == 1) break;
//                Drawable drawable = getResources().getDrawable(setIcon(p));
//                drawable.setBounds(0, 0, width, height);
//                profilePhotos.add(drawable);
                mClusterImageView.setImageResource(R.drawable.digit_marker);
            }
//            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
//            multiDrawable.setBounds(0, 0, width, height);
//
//            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }


    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }


}
