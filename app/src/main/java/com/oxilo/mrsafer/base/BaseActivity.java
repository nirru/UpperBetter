package com.oxilo.mrsafer.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oxilo.mrsafer.AppController;
import com.oxilo.mrsafer.R;

import org.w3c.dom.Text;


public abstract class BaseActivity extends AppCompatActivity{

    public Context mContext = null;
    private Toolbar toolbar;
    private TextView tabTitleForFirstTab;
    private TextView tabTitleForSecondTab;
    private TextView tabTitleForThirdTab;
    private TextView tabTitleForFourthTab;
    private ImageView first_tab_icon,second_teb_icon,third_tab_icon,fourth_tab_icon;
    private Snackbar snackbar;

    ImageView imageReports;
    CoordinatorLayout coordinatorLayout;
    View myView;
    RelativeLayout firstTab,secondTab;
    RelativeLayout thirdTab,fourthTab;

    private TextView counter,counter_actus;
    private TextView fragment_divider;
    private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        mContext = BaseActivity.this;
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initToolbar();
        initUiView();
        tabClickEvent();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);
    }

    private void initUiView(){
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
    }

    public Snackbar getSnackbar(String message){
        return snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
    }

    public ImageView getImageReports() {
        return imageReports;
    }


    public Toolbar getToolbar(){
        return toolbar;
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Only Activity has this special callback method
     * Fragment doesn't have any onBackPressed callback
     *
     * Logic:
     * Each time when the back button is pressed, this Activity will propagate the call to the
     * container Fragment and that Fragment will propagate the call to its each tab Fragment
     * those Fragments will propagate this method call to their child Fragments and
     * eventually all the propagated calls will get back to this initial method
     *
     * If the container Fragment or any of its Tab Fragments and/or Tab child Fragments couldn't
     * handle the onBackPressed propagated call then this Activity will handle the callback itself
     */
    @Override
    public void onBackPressed() {
        int i = getSupportFragmentManager().getBackStackEntryCount();
        if (i>=2){
            getSupportFragmentManager().popBackStack();
        }else{
            finish();
        }
    }
    private void tabClickEvent(){
        myView = findViewById(R.id.header);
        frameLayout = (FrameLayout)findViewById(R.id.main_content);
        fragment_divider = (TextView) findViewById(R.id.fragment_divider);
        firstTab = (RelativeLayout) myView.findViewById(R.id.tab_first);
        secondTab = (RelativeLayout) myView.findViewById(R.id.tab_second);
        thirdTab = (RelativeLayout) myView.findViewById(R.id.tab_third);
        fourthTab = (RelativeLayout)myView.findViewById(R.id.tab_fourth);

        tabTitleForFirstTab = (TextView)myView.findViewById(R.id.position_id);
        tabTitleForSecondTab = (TextView)myView.findViewById(R.id.alert_id);
        tabTitleForThirdTab = (TextView)myView.findViewById(R.id.reports_id);
        tabTitleForFourthTab = (TextView)myView.findViewById(R.id.actus_id);

        first_tab_icon = (ImageView)myView.findViewById(R.id.first_tab_icob);
        second_teb_icon = (ImageView)myView.findViewById(R.id.second_tab_icon);
        third_tab_icon = (ImageView)myView.findViewById(R.id.third_tab_icon);
        fourth_tab_icon = (ImageView)myView.findViewById(R.id.fourth_tab_icon);
        counter = (TextView)myView.findViewById(R.id.counter);
        counter_actus = (TextView)myView.findViewById(R.id.counter_1);

        counter.setTypeface(AppController.mpRobotaMeduim);
        counter_actus.setTypeface(AppController.mpRobotaMeduim);
        tabTitleForFirstTab.setTypeface(AppController.mpRobotaMeduim);
        tabTitleForSecondTab.setTypeface(AppController.mpRobotaMeduim);
        tabTitleForThirdTab.setTypeface(AppController.mpRobotaMeduim);
        tabTitleForFourthTab.setTypeface(AppController.mpRobotaMeduim);

        firstTab.setOnClickListener(l);
        secondTab.setOnClickListener(l);
        thirdTab.setOnClickListener(l);
        fourthTab.setOnClickListener(l);
    }

    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tab_first:
                    refrestLocation();
                    break;
                case R.id.tab_second:
                    onRaiseAlertGetLatLong();
                    break;
                case R.id.tab_third:
                    onReportStartGetLatlong();
                    break;
                case R.id.tab_fourth:
                    onActusClick();
                    break;
            }
        }
    };

    public void updateFirstTab(){
        first_tab_icon.setImageResource(R.drawable.ic_tab_first_selected_option);
        second_teb_icon.setImageResource(R.drawable.ic_tab_second_unselected_option);
        third_tab_icon.setImageResource(R.drawable.ic_tab_third_unselected_option);
        fourth_tab_icon.setImageResource(R.drawable.ic_tab_fourth_unselected_option);
        tabTitleForFirstTab.setTextColor(mContext.getResources().getColor(R.color.selected_text_color));
        tabTitleForSecondTab.setTextColor(mContext.getResources().getColor(R.color.unselected_text_color));
        tabTitleForThirdTab.setTextColor(mContext.getResources().getColor(R.color.unselected_text_color));
        tabTitleForFourthTab.setTextColor(mContext.getResources().getColor(R.color.unselected_text_color));
    }

    public void updateSecondTab(){
        first_tab_icon.setImageResource(R.drawable.ic_tab_first_unselected_option);
        second_teb_icon.setImageResource(R.drawable.ic_tab_second_selected_option);
        third_tab_icon.setImageResource(R.drawable.ic_tab_third_unselected_option);
        fourth_tab_icon.setImageResource(R.drawable.ic_tab_fourth_unselected_option);
        tabTitleForSecondTab.setTextColor(mContext.getResources().getColor(R.color.selected_text_color));
        tabTitleForThirdTab.setTextColor(mContext.getResources().getColor(R.color.unselected_text_color));
        tabTitleForFirstTab.setTextColor(mContext.getResources().getColor(R.color.unselected_text_color));
        tabTitleForFourthTab.setTextColor(mContext.getResources().getColor(R.color.unselected_text_color));
    }

    public void updateThirdTab(){

        first_tab_icon.setImageResource(R.drawable.ic_tab_first_unselected_option);
        second_teb_icon.setImageResource(R.drawable.ic_tab_second_unselected_option);
        third_tab_icon.setImageResource(R.drawable.ic_tab_third_selected_option);
        fourth_tab_icon.setImageResource(R.drawable.ic_tab_fourth_unselected_option);
        tabTitleForThirdTab.setTextColor(mContext.getResources().getColor(R.color.selected_text_color));
        tabTitleForSecondTab.setTextColor(mContext.getResources().getColor(R.color.unselected_text_color));
        tabTitleForFirstTab.setTextColor(mContext.getResources().getColor(R.color.unselected_text_color));
        tabTitleForFourthTab.setTextColor(mContext.getResources().getColor(R.color.unselected_text_color));
    }

    public void updateFourthTab(){

        first_tab_icon.setImageResource(R.drawable.ic_tab_first_unselected_option);
        second_teb_icon.setImageResource(R.drawable.ic_tab_second_unselected_option);
        third_tab_icon.setImageResource(R.drawable.ic_tab_third_unselected_option);
        fourth_tab_icon.setImageResource(R.drawable.ic_tab_fourth_selected_option);
        tabTitleForThirdTab.setTextColor(mContext.getResources().getColor(R.color.unselected_text_color));
        tabTitleForSecondTab.setTextColor(mContext.getResources().getColor(R.color.unselected_text_color));
        tabTitleForFirstTab.setTextColor(mContext.getResources().getColor(R.color.unselected_text_color));
        tabTitleForFourthTab.setTextColor(mContext.getResources().getColor(R.color.selected_text_color));
    }

    public void disableFirstTab(){
        firstTab.setEnabled(false);
        secondTab.setEnabled(true);
        thirdTab.setEnabled(true);
        fourthTab.setEnabled(true);
    }

    public void disableSecondTab(){
        firstTab.setEnabled(true);
        secondTab.setEnabled(false);
        thirdTab.setEnabled(true);
        fourthTab.setEnabled(true);
    }

    public void disableThirdTab(){
        firstTab.setEnabled(true);
        secondTab.setEnabled(true);
        thirdTab.setEnabled(false);
        fourthTab.setEnabled(true);
    }

    public void disableFourthTab(){
        firstTab.setEnabled(true);
        secondTab.setEnabled(true);
        thirdTab.setEnabled(true);
        fourthTab.setEnabled(false);
    }

    public void setCountReport(int count){
        if (count == 0) {
            counter.setVisibility(View.GONE);
        } else if(count<10){
            counter.setVisibility(View.VISIBLE);
            counter.setText("0" + count);
        }else{
            counter.setVisibility(View.VISIBLE);
            counter.setText("" + count);
        }
    }

    public void setActusCount(int count){
        if (count == 0) {
            counter_actus.setVisibility(View.GONE);
        } else if(count<10){
            counter_actus.setVisibility(View.VISIBLE);
            counter_actus.setText("0" + count);
        }else{
            counter_actus.setVisibility(View.VISIBLE);
            counter_actus.setText("" + count);
        }
    }
    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.badge_layout, null);
        view.setBackgroundResource(backgroundImageId);
        TextView counterTextPanel = (TextView)view.findViewById(R.id.badge_textView);

        if (count == 0) {
            counterTextPanel.setVisibility(View.GONE);
        } else {
            counterTextPanel.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }



    public TextView getTabTitleForThirdTab() {
        return tabTitleForThirdTab;
    }

    public TextView getTabTitleForSecondTab() {
        return tabTitleForSecondTab;
    }

    public TextView getTabTitleForFirstTab() {
        return tabTitleForFirstTab;
    }

    public TextView getMyFragmentDivider(){
        return fragment_divider;
    }


    public View getMyView() {
        return myView;
    }

    public int getAlertsCount(){
        if (counter.getText().toString().equals(""))
            return 0;
        else return Integer.parseInt(counter.getText().toString());
    }



public abstract void refrestLocation();
public abstract void onReportStartGetLatlong();
public abstract void onRaiseAlertGetLatLong();
public abstract void onActusClick();
}
