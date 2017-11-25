package com.oxilo.mrsafer.fragement;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oxilo.mrsafer.AppConstants;
import com.oxilo.mrsafer.AppController;
import com.oxilo.mrsafer.R;
import com.oxilo.mrsafer.activty.LandingActivity;
import com.oxilo.mrsafer.utility.ActivityUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Alerts.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Alerts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Alerts extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private double incident_lat;
    private double incident_long;


    public Alerts() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Alerts.
     */
    // TODO: Rename and change types and number of parameters
    public static Alerts newInstance(String param1, String param2) {
        Alerts fragment = new Alerts();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alerts, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUiView(view);
//        RecyclerListener();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = null;
        if (context instanceof Activity){
            activity=(Activity) context;
        }
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppConstants.TAB_POSITION = 3;
        ((LandingActivity) getActivity()).getMyView().setVisibility(View.VISIBLE);
        ((LandingActivity) getActivity()).getMyFragmentDivider().setVisibility(View.VISIBLE);
        ((LandingActivity)getActivity()).updateSecondTab();
        ((LandingActivity)getActivity()).disableSecondTab();
        AppConstants.IS_REFRESH = false;
        try {
            incident_lat = ((LandingActivity)getActivity()).getIncident_lat();
            incident_long = ((LandingActivity)getActivity()).getIncident_long();
            Log.e("ICIII","" + incident_lat);
            Log.e("IICDa","" + incident_long);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void onEmergencyEventClik(String ph_num);
    }

    private void initUiView(View view) {

        TextView altercation = (TextView)view.findViewById(R.id.altercation_text_view);
        TextView offence = (TextView)view.findViewById(R.id.offence_text_view);
        TextView harrasment = (TextView)view.findViewById(R.id.harrasment_text_view);
        TextView doubt = (TextView)view.findViewById(R.id.doubt_text_view);
        TextView incident = (TextView)view.findViewById(R.id.incident_text_view);
        TextView others = (TextView)view.findViewById(R.id.others_text_view);
        TextView ambulance = (TextView)view.findViewById(R.id.ambulance_text_view);
        TextView fireDepartment = (TextView)view.findViewById(R.id.fire_department_text_view);
        TextView police = (TextView)view.findViewById(R.id.police_text_view);

        altercation.setTypeface(AppController.mpRobotaRegular);
        offence.setTypeface(AppController.mpRobotaRegular);
        harrasment.setTypeface(AppController.mpRobotaRegular);
        doubt.setTypeface(AppController.mpRobotaRegular);
        incident.setTypeface(AppController.mpRobotaRegular);
        others.setTypeface(AppController.mpRobotaRegular);
        ambulance.setTypeface(AppController.mpRobotaRegular);
        fireDepartment.setTypeface(AppController.mpRobotaRegular);
        police.setTypeface(AppController.mpRobotaRegular);

        LinearLayout top_left = (LinearLayout)view.findViewById(R.id.top_left);
        LinearLayout top_middle = (LinearLayout)view.findViewById(R.id.top_middle);
        LinearLayout top_right = (LinearLayout)view.findViewById(R.id.top_right);
        LinearLayout top_bottom_left = (LinearLayout)view.findViewById(R.id.top_bottom_left);
        LinearLayout top_bottom_middle = (LinearLayout)view.findViewById(R.id.top_bottom_middle);
        LinearLayout top_bottom_right = (LinearLayout)view.findViewById(R.id.top_bottom_right);
        LinearLayout bottom_left = (LinearLayout)view.findViewById(R.id.bottom_left);
        LinearLayout bottom_middle = (LinearLayout)view.findViewById(R.id.bottom_middle);
        LinearLayout bottom_right = (LinearLayout)view.findViewById(R.id.bottom_right);

        top_left.setOnClickListener(l);
        top_middle.setOnClickListener(l);
        top_right.setOnClickListener(l);
        top_bottom_left.setOnClickListener(l);
        top_bottom_middle.setOnClickListener(l);
        top_bottom_right.setOnClickListener(l);
        bottom_left.setOnClickListener(l);
        bottom_middle.setOnClickListener(l);
        bottom_right.setOnClickListener(l);

    }


    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          switch (v.getId()){
              case R.id.top_left:
                  launcheventType(getString(R.string.report_an_altercation),1);
                  break;
              case R.id.top_middle:
                  launcheventType(getString(R.string.report_an_offence),2);
                  break;
              case R.id.top_right:
                  launcheventType(getString(R.string.report_an_harashment),3);
                  break;
              case R.id.top_bottom_left:
                  launcheventType(getString(R.string.report_an_doubt),4);
                  break;
              case R.id.top_bottom_middle:
                  launcheventType(getString(R.string.report_an_incident),5);
                  break;
              case R.id.top_bottom_right:
                  launcheventType(getString(R.string.report_an_occurrence),6);
                  break;
              case R.id.bottom_left:
                  mListener.onEmergencyEventClik(AppConstants.EMERGENY_AMBULANCE);
                  AppController.getInstance().confirmationDialog(getActivity()
                          ,getString(R.string.confirmation_title)
                          ,getString(R.string.confirmation_ambulance)
                          ,AppConstants.EMERGENY_AMBULANCE);
                  break;
              case R.id.bottom_middle:
                  mListener.onEmergencyEventClik(AppConstants.EMERGENY_FIRE);
                  AppController.getInstance().confirmationDialog(getActivity()
                          ,getString(R.string.confirmation_title)
                          ,getString(R.string.confirmation_fire_department)
                          ,AppConstants.EMERGENY_FIRE);
                  break;
              case R.id.bottom_right:
                  mListener.onEmergencyEventClik(AppConstants.EMERGENY_POLICE);
                  AppController.getInstance().confirmationDialog(getActivity()
                          ,getString(R.string.confirmation_title)
                          ,getString(R.string.confirmation_police)
                          ,AppConstants.EMERGENY_POLICE);
                  break;
              default:
                  break;
          }
        }
    };

     private void launcheventType(String eventType,int type){
         ActivityUtils.launcFragement(CaptureIncident.newInstance(eventType,type,incident_lat,incident_long),getActivity());
     }
}
