package com.oxilo.mrsafer.utility;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.oxilo.mrsafer.AppConstants;
import com.oxilo.mrsafer.AppController;
import com.oxilo.mrsafer.R;
import com.oxilo.mrsafer.activty.LandingActivity;
import com.oxilo.mrsafer.modal.ReportList;
import com.oxilo.mrsafer.modal.ReportResponse;
import com.oxilo.mrsafer.service.MrSaferWebService;
import com.oxilo.mrsafer.service.ServiceFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ericbasendra on 15/11/15.
 */
public class ActivityUtils {

    public static void launcFragement(Fragment fragment, Context context){
         FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
         FragmentTransaction ft = fragmentManager.beginTransaction();
         ft.addToBackStack(null);
         ft.setCustomAnimations(R.anim.move_right_in_activity, R.anim.move_left_out_activity,R.anim.move_left_in_activity, R.anim.move_right_out_activity);
         ft.replace(R.id.main_content, fragment);
         ft.commit();
    }

    public static  Bitmap crupAndScale (Bitmap source,int scale){
        int factor = source.getHeight() <= source.getWidth() ? source.getHeight(): source.getWidth();
        int longer = source.getHeight() >= source.getWidth() ? source.getHeight(): source.getWidth();
        int x = source.getHeight() >= source.getWidth() ?0:(longer-factor)/2;
        int y = source.getHeight() <= source.getWidth() ?0:(longer-factor)/2;
        source = Bitmap.createBitmap(source, x, y, factor, factor);
        source = Bitmap.createScaledBitmap(source, scale, scale, false);
        return source;
    }


    public static void setupUI(View view,final Context mContext) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(v,mContext);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView,mContext);
            }
        }
    }

    public static void hideSoftKeyboard(View view, Context mConext) {
        InputMethodManager inputMethodManager =(InputMethodManager)mConext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final boolean show,final View mLoginFormView,final View mProgressView,Context mContext) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                int shortAnimTime = mContext.getResources().getInteger(android.R.integer.config_shortAnimTime);

                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });

                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                mProgressView.animate().setDuration(shortAnimTime).alpha(
                        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });
            } else {
                // The ViewPropertyAnimator APIs are not available, so simply show
                // and hide the relevant UI components.
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

public static String findDistanceInMilesFromMyCurrentLocation(double myCurrentLat,double myCurrentLong,
                                                      double incidentLat,double incidentLong){
    String distanceInMiles = "0.0";
    try {
        Location loc1 = new Location("");
        loc1.setLatitude(roundTwoDecimals(myCurrentLat));
        loc1.setLongitude(roundTwoDecimals(myCurrentLong));

        Location loc2 = new Location("");
        loc2.setLatitude(roundTwoDecimals(incidentLat));
        loc2.setLongitude(roundTwoDecimals(incidentLong));
        double distanceInMeters = loc1.distanceTo(loc2);
        distanceInMiles = String.format("%.2f", getMiles(distanceInMeters));
    }catch (Exception ex){
        ex.printStackTrace();
    }
     return distanceInMiles;
}

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String findDistanceInKMFromMyCurrentLocation(double myCurrentLat,double myCurrentLong,
                                                                  double incidentLat,double incidentLong){
        String distanceInMiles = "0.0";
        try {
            Location loc1 = new Location("");
            loc1.setLatitude(myCurrentLat);
            loc1.setLongitude(myCurrentLong);

            Location loc2 = new Location("");
            loc2.setLatitude(incidentLat);
            loc2.setLongitude(incidentLong);
            double distanceInMeters = loc1.distanceTo(loc2);
            distanceInMiles = "" + round(getKm(distanceInMeters),2);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return distanceInMiles;
    }
    public static double getMiles(double i) {
        return i*0.000621371192;
    }
    public static double getKm(double i){
        return i*0.001;
    }

    static double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.#######");
        return Double.valueOf(twoDForm.format(d));
    }

    public static File saveImageToExternalStorage(Bitmap bmp) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()){
            storageDir.mkdir();
        }
        File image = null;
        FileOutputStream fos = null;
        /*--- this method will save your downloaded image to SD card ---*/

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        /*--- you can select your preferred CompressFormat and quality.
         * I'm going to use JPEG and 100% quality ---*/
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        /*--- create a new file on SD card ---*/

        try {
             image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*--- create a new FileOutputStream and write bytes to file ---*/
        try {
            fos = new FileOutputStream(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bytes.toByteArray());
            fos.close();
//            Toast.makeText(mContext, "Image saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private void startPolling(int current_page,double incident_lat, double incident_long) throws Exception {
        MrSaferWebService service = ServiceFactory.createRetrofitService(MrSaferWebService.class, AppConstants.BASE_URL);
        final Observable<ReportResponse> reportResponseObservable = service.getListOfInciden("get_report", current_page, 50, incident_lat, incident_long);
        Observable.interval(20, TimeUnit.MINUTES)
                .flatMap(new Func1<Long, Observable<ReportResponse>>() {
                    @Override
                    public Observable<ReportResponse> call(Long aLong) {
                        return reportResponseObservable;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ReportResponse>() {
                    @Override
                    public void call(ReportResponse response) {
                        Log.i("HEARTBEAT_INTERVAL", "Response from HEARTBEAT");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        // TODO: 22/03/16 ADD ERROR HANDLING
                    }
                });

    }


    public static String getLanguage(){
        return Locale.getDefault().getDisplayLanguage();
    }

    public static int getIncidentImage(ReportList dataItem,Context mContext){
        int image_resource = 0;
        if (((ReportList)dataItem).getSelectedId().equals(AppConstants.argument)){
            image_resource = R.drawable.ic_alter_selected_option_one;
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.argument)){
            image_resource = R.drawable.ic_alter_selected_option_two;
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.weapons)){
            image_resource = R.drawable.ic_alter_selected_option_three;
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.degradation)){
            image_resource = R.drawable.ic_offence_selected_one;
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.theft)){
            image_resource  = R.drawable.ic_offence_selected_two;
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.burglary)){
            image_resource = R.drawable.ic_offence_selected_three;
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.verbel_abuse)){
            image_resource = R.drawable.ic_harass_selected_option_one;
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.fondling)){
            image_resource = R.drawable.ic_harass_selected_option_two;
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.rape)){
            image_resource = R.drawable.ic_harass_selected_option_three;
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.suspicious)){
            image_resource = R.drawable.ic_doubt_selected_option_one;
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.shady)){
            image_resource = R.drawable.ic_doubt_selected_option_two;
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.missing)){
            image_resource = R.drawable.ic_doubt_selected_option_three;
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.malaise)){
            image_resource = R.drawable.ic_incident_selected_option_one;
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.accident)){
            image_resource = R.drawable.ic_incident_selected_option_two;
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.fire)){
            image_resource = R.drawable.ic_incident_selected_option_three;
        }
        else{
            //Not a Valid match
            image_resource = R.drawable.other;
        }
        return image_resource;
    }

    public static String getIncidentSubType(ReportList dataItem,Context mContext){
        String sub_type = "NOTHING";
        if (((ReportList)dataItem).getSelectedId().equals(AppConstants.argument)) {
            sub_type = mContext.getString(R.string.argument);
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.fight)){
            sub_type = mContext.getString(R.string.fight);
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.weapons)){
            sub_type = mContext.getResources().getString(R.string.weapons);
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.degradation)){
            sub_type = mContext.getResources().getString(R.string.degradation);
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.theft)){
            sub_type  = mContext.getResources().getString(R.string.theft);
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.burglary)){
            sub_type = mContext.getResources().getString(R.string.burglary);
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.verbel_abuse)){
            sub_type = mContext.getResources().getString(R.string.verbel_abuse);
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.fondling)){
            sub_type = mContext.getResources().getString(R.string.fondling);
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.rape)){
            sub_type = mContext.getResources().getString(R.string.rape);
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.suspicious)){
            sub_type = mContext.getResources().getString(R.string.suspicious);
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.shady)){
            sub_type = mContext.getResources().getString(R.string.shady);
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.missing)){
            sub_type = mContext.getResources().getString(R.string.missing);
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.malaise)){
            sub_type = mContext.getResources().getString(R.string.malaise);
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.accident)){
            sub_type = mContext.getResources().getString(R.string.accident);
        }
        else if (((ReportList)dataItem).getSelectedId().equals(AppConstants.fire)){
            sub_type = mContext.getResources().getString(R.string.fire);
        }
        else{
            //Not a Valid match
            sub_type = mContext.getString(R.string.others);
        }
        return sub_type;
    }

    public static String getSelectedId(String mSubtype, Context mContext){
        String SelectedId ="0";
        if(mSubtype.equals(mContext.getString(R.string.argument))){
            SelectedId = AppConstants.argument;
        }else if(mSubtype.equals(mContext.getString(R.string.fight))){
            SelectedId = AppConstants.fight;
        }else if(mSubtype.equals(mContext.getString(R.string.weapons))){
            SelectedId = AppConstants.weapons;
        }else if(mSubtype.equals(mContext.getString(R.string.degradation))){
            SelectedId = AppConstants.degradation;
        }else if(mSubtype.equals(mContext.getString(R.string.theft))){
            SelectedId = AppConstants.theft;
        }else if(mSubtype.equals(mContext.getString(R.string.burglary))){
            SelectedId = AppConstants.burglary;
        }else if(mSubtype.equals(mContext.getString(R.string.verbel_abuse))){
            SelectedId = AppConstants.verbel_abuse;
        }else if(mSubtype.equals(mContext.getString(R.string.fondling))){
            SelectedId = AppConstants.fondling;
        }else if(mSubtype.equals(mContext.getString(R.string.rape))){
            SelectedId = AppConstants.rape;
        }else if(mSubtype.equals(mContext.getString(R.string.suspicious))){
            SelectedId = AppConstants.suspicious;
        }else if(mSubtype.equals(mContext.getString(R.string.shady))){
            SelectedId = AppConstants.shady;
        }else if(mSubtype.equals(mContext.getString(R.string.missing))){
            SelectedId = AppConstants.missing;
        }else if(mSubtype.equals(mContext.getString(R.string.malaise))){
            SelectedId = AppConstants.malaise;
        }else if(mSubtype.equals(mContext.getString(R.string.accident))){
            SelectedId = AppConstants.accident;
        }else if(mSubtype.equals(mContext.getString(R.string.fire))){
            SelectedId = AppConstants.fire;
        }else{
            SelectedId = "0";
        }
        return SelectedId;
    }

}