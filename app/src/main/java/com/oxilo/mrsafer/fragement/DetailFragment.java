package com.oxilo.mrsafer.fragement;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.oxilo.mrsafer.AppConstants;
import com.oxilo.mrsafer.AppController;
import com.oxilo.mrsafer.R;
import com.oxilo.mrsafer.activty.LandingActivity;
import com.oxilo.mrsafer.modal.ReportList;
import com.oxilo.mrsafer.modal.ReportResponse;
import com.oxilo.mrsafer.modal.Response;
import com.oxilo.mrsafer.service.MrSaferWebService;
import com.oxilo.mrsafer.service.RetryWithDelay;
import com.oxilo.mrsafer.service.ServiceFactory;
import com.oxilo.mrsafer.utility.ActivityUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private boolean bVideoIsBeingTouched = false;
    private boolean bCameraIsBeingTouched = false;

    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;
    private ImageView cameraIcon,videoIcon,shareIcon;
    private ImageView incident_image,image_abuse;
    private TextView incident_title,incident_desc,incident_times,incident_distance,incident_address,report_abuse;
    private TextView header_title;
    private ReportList reportList;
    private double current_lat,current_long;
    private String address = "paris";
    private RelativeLayout relative_abuse;
    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param reportList Parameter 1.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(ReportList reportList,double current_lat,double current_long) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, reportList);
        args.putDouble(ARG_PARAM2, current_lat);
        args.putDouble(ARG_PARAM3, current_long);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reportList = getArguments().getParcelable(ARG_PARAM1);
            current_lat = getArguments().getDouble(ARG_PARAM2);
            current_long = getArguments().getDouble(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUiView(view);
        incident_image.setImageResource(ActivityUtils.getIncidentImage(reportList,getActivity()));
        showCameraAndVideoIcon();
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((LandingActivity) getActivity()).getMyView().setVisibility(View.VISIBLE);
        ((LandingActivity) getActivity()).getMyFragmentDivider().setVisibility(View.VISIBLE);
        address =    ((LandingActivity) getActivity()).getAddressInfo(
                Double.parseDouble(reportList.getLat()),
                Double.parseDouble(reportList.getLog()),
                getActivity());


        if (reportList.getIsVictim().equals("0")){
            header_title.setText(getString(R.string.report_by_victim));
        }else{
            header_title.setText(getString(R.string.report_by_witness));
        }
        incident_title.setText(ActivityUtils.getIncidentSubType(reportList,getActivity()));
        incident_times.setText(reportList.getDate());
//        String distance = ActivityUtils.findDistanceInMilesFromMyCurrentLocation(current_lat,current_long,Double.parseDouble(reportList.getLat()),Double.parseDouble((reportList).getLog()));
//        String distance = String.format("%.2f", reportList.getDistance());
        if( ActivityUtils.getLanguage().equals("français") ){
            String distance = ActivityUtils.findDistanceInKMFromMyCurrentLocation(current_lat,current_long,Double.parseDouble(reportList.getLat()),Double.parseDouble((reportList).getLog()));
            incident_distance.setText(distance + " " + getString(R.string.kms_from_here));
        }else {
            String distance = ActivityUtils.findDistanceInMilesFromMyCurrentLocation(current_lat,current_long,Double.parseDouble(reportList.getLat()),Double.parseDouble((reportList).getLog()));
            incident_distance.setText(distance + " " + getString(R.string.miles_from_here));
        }
        incident_address.setText(address);
        incident_desc.setText(reportList.getDescription());
    }

    private void initUiView(View view){
        header_title = (TextView)view.findViewById(R.id.title);
        cameraIcon = (ImageView)view.findViewById(R.id.camera);
        videoIcon = (ImageView)view.findViewById(R.id.video);
        shareIcon = (ImageView)view.findViewById(R.id.share);
        incident_image =  (ImageView)view.findViewById(R.id.report_capture_img_id);
        incident_title = (TextView)view.findViewById(R.id.report_id);
        incident_desc = (TextView)view.findViewById(R.id.desc);
        incident_times = (TextView)view.findViewById(R.id.incident_time);
        incident_distance = (TextView)view.findViewById(R.id.incident_distance);
        incident_address = (TextView)view.findViewById(R.id.address_id);
        relative_abuse = (RelativeLayout)view.findViewById(R.id.relative_abuse) ;
        image_abuse = (ImageView)view.findViewById(R.id.image_abuse);
        report_abuse = (TextView)view.findViewById(R.id.report_abuse);
        cameraIcon.setOnClickListener(l);
        videoIcon.setOnClickListener(l);
        shareIcon.setOnClickListener(l);
        relative_abuse.setOnClickListener(l);
//        report_abuse.setOnClickListener(l);

        header_title.setTypeface(AppController.mpRobotaBold);
        incident_title.setTypeface(AppController.mpRobotaMeduim);
        incident_address.setTypeface(AppController.mpRobotaMeduim);
        incident_distance.setTypeface(AppController.mpRobotaMeduim);
        incident_times.setTypeface(AppController.mpRobotaMeduim);
    }

    private void showCameraAndVideoIcon(){
        if (!reportList.getImageFile().equals("") && !reportList.getVideoFile().equals("")){
            cameraIcon.setVisibility(View.VISIBLE);
            videoIcon.setVisibility(View.VISIBLE);
        }
        else if (!reportList.getImageFile().equals("")){
            cameraIcon.setVisibility(View.VISIBLE);
            videoIcon.setVisibility(View.GONE);
        }else if (!reportList.getVideoFile().equals("")){
            videoIcon.setVisibility(View.VISIBLE);
            cameraIcon.setVisibility(View.GONE);
        }else{
            //Not a valid Condition
            cameraIcon.setVisibility(View.GONE);
            videoIcon.setVisibility(View.GONE);
        }
    }

    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.video:
                     ActivityUtils.launcFragement(ViewFragment.newInstance(reportList.getVideoFile(),""),getActivity());
                    break;
                case R.id.camera:
                     ActivityUtils.launcFragement(ImageFragment.newInstance(reportList.getImageFile(),""),getActivity());
                    break;
                case R.id.share:
                    shareIntent();
                    break;
                case R.id.image_abuse:
                    sendEmail();
                    break;
                case R.id.relative_abuse:
                    sendEmail();
                    break;
                default:
                    break;
            }
        }
    };




    private void shareIntent(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "SEND TO"));
    }

    private void sendEmail() {
        try {
            String ad_ress = address;
            String desc =    reportList.getDescription();
            String sub_type = incident_title.getText().toString();
            String victim = header_title.getText().toString();
            String imageUrl = reportList.getImageFile().toString();
            String videoUrl = reportList.getVideoFile();
            String subject  = getActivity().getString(R.string.email_subject);
            String distance = incident_distance.getText().toString();
            String times = reportList.getDate().toString();
             MrSaferWebService service = ServiceFactory.createRetrofitService(MrSaferWebService.class, AppConstants.BASE_URL);
            Observable<Response> reportResponseObservable = service.sendAbuseReport("send_mail",ad_ress,desc
                    ,sub_type,victim,imageUrl,videoUrl
                    ,subject,distance,times);
                     reportResponseObservable.subscribeOn(Schedulers.newThread())
                    .retryWhen(new RetryWithDelay(3,2000))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Response>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("ERROR", e.getLocalizedMessage().toString());
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Response response) {
                            AppController.getInstance().showSimpleDialog(getActivity(),getString(R.string.report_abuse),getString(R.string.report_abuse_message));
//                            if (response.getStatus() == 1) {
//                                Log.e("Succsss Message","" + response.getMessage());
//                            } else {
//                                Log.e("MY ERROR", "" + "SOME ERROR OCCURED");
//                            }

                        }
                    });
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


}
