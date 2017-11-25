package com.oxilo.mrsafer.activty;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.oxilo.mrsafer.AppController;
import com.oxilo.mrsafer.AppSharePrefs;
import com.oxilo.mrsafer.R;
import com.oxilo.mrsafer.listener.SocialAuthListener;

import org.w3c.dom.Text;


public class WelcomeActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView textView1,textView2,txtView,txtAggrement;
    Button reg_btn,twitter_button;
    private SocialAuthListener socialAuthListener;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = WelcomeActivity.this;
        if (isAlreadyLogin()){
            Intent intent = new Intent(mContext, LandingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{
            setContentView(R.layout.activity_welcome);
            initInstances();


        }


    }

    private void initInstances() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        socialAuthListener = new SocialAuthListener(mContext);
//        textView1 = (TextView) findViewById(R.id.left);
//        textView2 = (TextView) findViewById(R.id.right);
//        txtView = (TextView)findViewById(R.id.txt_view);
//        txtAggrement = (TextView)findViewById(R.id.txt_aggrement);
        reg_btn = (Button) findViewById(R.id.register);
//        twitter_button = (AppCompatButton)findViewById(R.id.twitter_button);
//
//        textView1.setTypeface(AppController.mpRobotaBold);
//        textView2.setTypeface(AppController.mpRobotaBold);
//        txtView.setTypeface(AppController.mpRobotaBold);
//        txtAggrement.setTypeface(AppController.mpRobotaRegular);
//        fb_button.setTypeface(AppController.mpRobotaRegular);
//        twitter_button.setTypeface(AppController.mpRobotaRegular);

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (AppController.getInstance().isOnline(mContext)){
                  Intent i = new Intent(WelcomeActivity.this,LandingActivity.class);
                   startActivity(i);
               }
                else
                   AppController.getInstance().showInternetConnectionDialog(mContext);
            }
        });

//        twitter_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (AppController.getInstance().isOnline(mContext))
//                    socialAuthListener.twitterAuthentication();
//                else
//                    AppController.getInstance().showInternetConnectionDialog(mContext);
//
//
//            }
//        });
    }

    private boolean isAlreadyLogin(){
        AppSharePrefs appSharePrefs = AppController.getInstance().getAppSharePrefs();
        if (appSharePrefs.getObject("user",String.class) != null){
            return true;
        }else{
            return false;
        }
    }


}
