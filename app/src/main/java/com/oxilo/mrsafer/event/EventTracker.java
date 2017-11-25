package com.oxilo.mrsafer.event;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oxilo.mrsafer.R;

/**
 * Created by ericbasendra on 11/06/16.
 */
public class EventTracker {

    private static EventTracker mInstance = new EventTracker();
    private ImageView mSubType1,mSubType2,mSubType3;
    private String ph_number;
    private Context mContext;
    private String title;
    private String sub_type;
    public static final int KEY_CLICK_EVENT = 1;

    public EventTracker() {
    }
    public static EventTracker getInstance() {
        return mInstance;
    }

    public void changeEventImageAndTitle(Context mContext, ImageView subType1, ImageView subType2, ImageView subType3,
                                         TextView subtype_title_1, TextView subtype_title_2, TextView subtype_title_3,
                                         ImageView eventImage, RelativeLayout event_sub_type_container, EditText report_desc_view,int type){
        this.mContext  = mContext;
        this.mSubType1 = subType1;
        this.mSubType2 = subType2;
        this.mSubType3 = subType3;
        switch (type){
            case 1:
                setTitle(mContext.getString(R.string.altercation));
                eventImage.setImageResource(R.drawable.altercation);
                subType1.setImageResource(R.drawable.ic_alter_unselected_option_one);
                subType2.setImageResource(R.drawable.ic_alter_unselected_option_two);
                subType3.setImageResource(R.drawable.ic_alter_unselected_option_three);

                subtype_title_1.setText(mContext.getString(R.string.argument));
                subtype_title_2.setText(mContext.getString(R.string.fight));
                subtype_title_3.setText(mContext.getString(R.string.weapons));

                subType1.setTag(1);
                subType2.setTag(1);
                subType3.setTag(1);
                break;
            case 2:
                setTitle(mContext.getString(R.string.offence));
                eventImage.setImageResource(R.drawable.offence);
                subType1.setImageResource(R.drawable.ic_offence_unselected_one);
                subType2.setImageResource(R.drawable.ic_offence_unselected_two);
                subType3.setImageResource(R.drawable.ic_offence_unselected_three);

                subtype_title_1.setText(mContext.getString(R.string.degradation));
                subtype_title_2.setText(mContext.getString(R.string.theft));
                subtype_title_3.setText(mContext.getString(R.string.burglary));

                subType1.setTag(2);
                subType2.setTag(2);
                subType3.setTag(2);
                break;
            case 3:
                setTitle(mContext.getString(R.string.harassment));
                eventImage.setImageResource(R.drawable.harassment);
                subType1.setImageResource(R.drawable.ic_harass_unselected_option_one);
                subType2.setImageResource(R.drawable.ic_harass_unselected_option_two);
                subType3.setImageResource(R.drawable.ic_harass_unselected_option_three);

                subtype_title_1.setText(mContext.getString(R.string.verbel_abuse));
                subtype_title_2.setText(mContext.getString(R.string.fondling));
                subtype_title_3.setText(mContext.getString(R.string.rape));

                subType1.setTag(3);
                subType2.setTag(3);
                subType3.setTag(3);
                break;
            case 4:
                setTitle(mContext.getString(R.string.doubt));
                eventImage.setImageResource(R.drawable.doubt);
                subType1.setImageResource(R.drawable.ic_doubt_unselected_option_one);
                subType2.setImageResource(R.drawable.ic_doubt_unselected_option_two);
                subType3.setImageResource(R.drawable.ic_doubt_unselected_option_three);

                subtype_title_1.setText(mContext.getString(R.string.suspicious));
                subtype_title_2.setText(mContext.getString(R.string.shady));
                subtype_title_3.setText(mContext.getString(R.string.missing));

                subType1.setTag(4);
                subType2.setTag(4);
                subType3.setTag(4);
                break;
            case 5:
                setTitle(mContext.getString(R.string.incident));
                eventImage.setImageResource(R.drawable.incident);
                subType1.setImageResource(R.drawable.ic_incident_unselected_option_one);
                subType2.setImageResource(R.drawable.ic_incident_unselected_option_two);
                subType3.setImageResource(R.drawable.ic_incident_unselected_option_three);

                subtype_title_1.setText(mContext.getString(R.string.malaise));
                subtype_title_2.setText(mContext.getString(R.string.accident));
                subtype_title_3.setText(mContext.getString(R.string.fire));

                subType1.setTag(5);
                subType2.setTag(5);
                subType3.setTag(5);
                break;
            case 6:
                setTitle(mContext.getString(R.string.others));
                DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
                float dp = 250f;
                float fpixels = metrics.density * dp;
                int pixels = (int) (fpixels + 0.5f);
                report_desc_view.getLayoutParams().height = pixels;
                eventImage.setImageResource(R.drawable.other);
                event_sub_type_container.setVisibility(View.GONE);

                mSubType1.setTag(R.id.TAG_KEY_CLICK_EVENT,10);
                mSubType2.setTag(R.id.TAG_KEY_CLICK_EVENT,10);
                mSubType3.setTag(R.id.TAG_KEY_CLICK_EVENT,10);
                break;
            default:
                break;
        }

        mSubType1.setOnClickListener(l);
        mSubType2.setOnClickListener(l);
        mSubType3.setOnClickListener(l);
    }

    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           switch (Integer.parseInt(v.getTag().toString())){
               case 1:
                   updateForAltercationOnSubtypeClick(v);
                   break;
               case 2:
                   updateForOffenceOnSubtypeClick(v);
                   break;
               case 3:
                   updateForHarassOnSubtypeClick(v);
                   break;
               case 4:
                   updateForDoubtOnSubtypeClick(v);
                   break;
               case 5:
                   updateForIncidentOnSubtypeClick(v);
                   break;
               default:
                   break;
           }

            mSubType1.setTag(R.id.TAG_KEY_CLICK_EVENT,10);
            mSubType2.setTag(R.id.TAG_KEY_CLICK_EVENT,10);
            mSubType3.setTag(R.id.TAG_KEY_CLICK_EVENT,10);
        }
    };

    private void updateForAltercationOnSubtypeClick(View v){
        if (v.getId() == R.id.subtype_1){
            setSub_type(mContext.getString(R.string.argument));
            mSubType1.setImageResource(R.drawable.ic_alter_selected_option_one);
            mSubType2.setImageResource(R.drawable.ic_alter_unselected_option_two);
            mSubType3.setImageResource(R.drawable.ic_alter_unselected_option_three);
        }else if (v.getId() == R.id.subtype_2){
            setSub_type(mContext.getString(R.string.fight));
            mSubType1.setImageResource(R.drawable.ic_alter_unselected_option_one);
            mSubType2.setImageResource(R.drawable.ic_alter_selected_option_two);
            mSubType3.setImageResource(R.drawable.ic_alter_unselected_option_three);
        }else if (v.getId() == R.id.subtype_3){
            setSub_type(mContext.getString(R.string.weapons));
            mSubType1.setImageResource(R.drawable.ic_alter_unselected_option_one);
            mSubType2.setImageResource(R.drawable.ic_alter_unselected_option_two);
            mSubType3.setImageResource(R.drawable.ic_alter_selected_option_three);
        }

    }

    private void updateForOffenceOnSubtypeClick(View v){
        if (v.getId() == R.id.subtype_1){
            setSub_type(mContext.getString(R.string.degradation));
            mSubType1.setImageResource(R.drawable.ic_offence_selected_one);
            mSubType2.setImageResource(R.drawable.ic_offence_unselected_two);
            mSubType3.setImageResource(R.drawable.ic_offence_unselected_three);
        }else if (v.getId() == R.id.subtype_2){
            setSub_type(mContext.getString(R.string.theft));
            mSubType1.setImageResource(R.drawable.ic_offence_unselected_one);
            mSubType2.setImageResource(R.drawable.ic_offence_selected_two);
            mSubType3.setImageResource(R.drawable.ic_offence_unselected_three);
        }else if (v.getId() == R.id.subtype_3){
            setSub_type(mContext.getString(R.string.burglary));
            mSubType1.setImageResource(R.drawable.ic_offence_unselected_one);
            mSubType2.setImageResource(R.drawable.ic_offence_unselected_two);
            mSubType3.setImageResource(R.drawable.ic_offence_selected_three);
        }
    }

    private void updateForHarassOnSubtypeClick(View v){
        if (v.getId() == R.id.subtype_1){
            setSub_type(mContext.getString(R.string.verbel_abuse));
            mSubType1.setImageResource(R.drawable.ic_harass_selected_option_one);
            mSubType2.setImageResource(R.drawable.ic_harass_unselected_option_two);
            mSubType3.setImageResource(R.drawable.ic_harass_unselected_option_three);
        }else if (v.getId() == R.id.subtype_2){
            setSub_type(mContext.getString(R.string.fondling));
            mSubType1.setImageResource(R.drawable.ic_harass_unselected_option_one);
            mSubType2.setImageResource(R.drawable.ic_harass_selected_option_two);
            mSubType3.setImageResource(R.drawable.ic_harass_unselected_option_three);
        }else if (v.getId() == R.id.subtype_3){
            setSub_type(mContext.getString(R.string.rape));
            mSubType1.setImageResource(R.drawable.ic_harass_unselected_option_one);
            mSubType2.setImageResource(R.drawable.ic_harass_unselected_option_two);
            mSubType3.setImageResource(R.drawable.ic_harass_selected_option_three);
        }
    }

    private void updateForDoubtOnSubtypeClick(View v){
        if (v.getId() == R.id.subtype_1){
            setSub_type(mContext.getString(R.string.suspicious));
            mSubType1.setImageResource(R.drawable.ic_doubt_selected_option_one);
            mSubType2.setImageResource(R.drawable.ic_doubt_unselected_option_two);
            mSubType3.setImageResource(R.drawable.ic_doubt_unselected_option_three);
        }else if (v.getId() == R.id.subtype_2){
            setSub_type(mContext.getString(R.string.shady));
            mSubType1.setImageResource(R.drawable.ic_doubt_unselected_option_one);
            mSubType2.setImageResource(R.drawable.ic_doubt_selected_option_two);
            mSubType3.setImageResource(R.drawable.ic_doubt_unselected_option_three);
        }else if (v.getId() == R.id.subtype_3){
            setSub_type(mContext.getString(R.string.missing));
            mSubType1.setImageResource(R.drawable.ic_doubt_unselected_option_one);
            mSubType2.setImageResource(R.drawable.ic_doubt_unselected_option_two);
            mSubType3.setImageResource(R.drawable.ic_doubt_selected_option_three);
        }
    }

    private void updateForIncidentOnSubtypeClick(View v){
        if (v.getId() == R.id.subtype_1){
            setSub_type(mContext.getString(R.string.malaise));
            mSubType1.setImageResource(R.drawable.ic_incident_selected_option_one);
            mSubType2.setImageResource(R.drawable.ic_incident_unselected_option_two);
            mSubType3.setImageResource(R.drawable.ic_incident_unselected_option_three);
        }else if (v.getId() == R.id.subtype_2){
            setSub_type(mContext.getString(R.string.accident));
            mSubType1.setImageResource(R.drawable.ic_incident_unselected_option_one);
            mSubType2.setImageResource(R.drawable.ic_incident_selected_option_two);
            mSubType3.setImageResource(R.drawable.ic_incident_unselected_option_three);
        }else if (v.getId() == R.id.subtype_3){
            setSub_type(mContext.getString(R.string.fire));
            mSubType1.setImageResource(R.drawable.ic_incident_unselected_option_one);
            mSubType2.setImageResource(R.drawable.ic_incident_unselected_option_two);
            mSubType3.setImageResource(R.drawable.ic_incident_selected_option_three);
        }
    }

    public String getPh_number() {
        return ph_number;
    }

    public void setPh_number(String ph_number) {
        this.ph_number = ph_number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getSub_type() {
        return sub_type;
    }

    public void setSub_type(String sub_type) {
        this.sub_type = sub_type;
    }
}
