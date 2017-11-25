package com.oxilo.mrsafer;

import android.Manifest;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.oxilo.mrsafer.activty.LandingActivity;
import com.oxilo.mrsafer.listener.DialogListener;
import com.oxilo.mrsafer.listener.ItemClickListener;
import com.oxilo.mrsafer.listener.YesNoListener;
import com.oxilo.mrsafer.utility.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Stack;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;

/**
 * Created by Nirmal on 29/05/16.
 */
public class AppController extends Application{

    private static AppController mInstance;
    public static Typeface mpRobotaBold,mpRobotaRegular,mpRobotaMeduim;
    private AppSharePrefs appSharePrefs;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        appSharePrefs = AppSharePrefs.getComplexPreferences(getBaseContext(), "MRSAFER", MODE_PRIVATE);
        initTypeFace();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }
    private void initTypeFace() {
        mpRobotaBold = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Bold.ttf");
        mpRobotaRegular = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Regular.ttf");
        mpRobotaMeduim = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Medium.ttf");
    }

    public boolean isOnline(Context context) {
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }

    public void cameraPermission(final Context mContext, final String title,final String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.MyDialogTheme);

        // Setting Dialog Title
        builder.setTitle(title);
        builder.setMessage(message);

        // On pressing Settings button
        builder.setPositiveButton(getString(R.string.agree), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                hasPermissions(mContext,AppConstants.PERMISSIONS);
            }
        });
        builder.setNegativeButton(R.string.disagree, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        // display dialog
        dialog.show();
    }
    public void confirmationDialog(final Context mContext, final String title,final String message, final String phn_number){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.MyDialogTheme);

        // Setting Dialog Title
        builder.setTitle(title);
        builder.setMessage(message);

        // On pressing Settings button
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                dialNumber(phn_number,mContext);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        // display dialog
        dialog.show();
    }

    public void showInternetConnectionDialog(Context mContext){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.MyDialogTheme);
        builder.setTitle(getString(R.string.dialog_title));
        builder.setMessage(getString(R.string.dialog_message));

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        // display dialog
        dialog.show();
    }

    public void showSettingsAlert(final Context mContext){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.MyDialogTheme);

        // Setting Dialog Title
        builder.setTitle(getString(R.string.setting_dialog_title));
        builder.setMessage(getString(R.string.setting_dialog_message));

        // On pressing Settings button
        builder.setPositiveButton(mContext.getString(R.string.agree), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        builder.setNegativeButton(mContext.getString(R.string.disagree), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
               dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        // display dialog
        dialog.show();
    }



    public void showSimpleDialog(final Context mContext,String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.MyDialogTheme);

        // Setting Dialog Title
        builder.setTitle(title);
        builder.setMessage(message);

        // On pressing Settings button
        builder.setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        // display dialog
        dialog.show();
    }

    public void showDialog(final Context mContext,String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.MyDialogTheme);

        // Setting Dialog Title
        builder.setTitle(title);
        builder.setMessage(message);

        // On pressing Settings button
        builder.setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                dialog.dismiss();
                ((LandingActivity)mContext).getSupportFragmentManager().popBackStack();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        // display dialog
        dialog.show();
    }


    public void pickerDialog(final Context mContext, String title,final ItemClickListener itemClickListener){
        CharSequence colors[] = new CharSequence[] {"Gallery", "Camera"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.PikerDialogTheme);

        // Setting Dialog Title
        builder.setTitle(title);
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on Item[which]
                dialog.dismiss();
                try {
                    itemClickListener.onClick(which);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        // display dialog
        dialog.show();
    }

    public void viewOrDeleteDialog(final Context mContext, String title,final DialogListener itemClickListener){
        CharSequence colors[] = new CharSequence[] {"VIEW", "DELETE"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.PikerDialogTheme);

        // Setting Dialog Title
        builder.setTitle(title);
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on Item[which]
                dialog.dismiss();
                try {
                    itemClickListener.onItemClick(which);
                }  catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        // display dialog
        dialog.show();
    }

  public void dialNumber(String phn_number,Context mContext){
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
          if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
              // TODO: Consider calling
              ActivityCompat.requestPermissions((LandingActivity)mContext,new String[]{CALL_PHONE},AppConstants.MY_PERMISSIONS_REQUEST_CALL);
              return;
          }else{
              try {
                  Intent my_callIntent = new Intent(Intent.ACTION_CALL);
                  my_callIntent.setData(Uri.parse("tel:"+phn_number));
                  //here the word 'tel' is important for making a call...
                  mContext.startActivity(my_callIntent);
              } catch (ActivityNotFoundException e) {
                  Toast.makeText(getApplicationContext(), "Error in your phone call"+e.getMessage(), Toast.LENGTH_LONG).show();
              }
          }
      }else{
          try {
              Intent my_callIntent = new Intent(Intent.ACTION_CALL);
              my_callIntent.setData(Uri.parse("tel:"+phn_number));
              //here the word 'tel' is important for making a call...
              mContext.startActivity(my_callIntent);
          } catch (ActivityNotFoundException e) {
              Toast.makeText(getApplicationContext(), "Error in your phone call"+e.getMessage(), Toast.LENGTH_LONG).show();
          }
      }
  }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public AppSharePrefs getAppSharePrefs() {
        if(appSharePrefs != null) {
            return appSharePrefs;
        }
        return null;
    }

}
