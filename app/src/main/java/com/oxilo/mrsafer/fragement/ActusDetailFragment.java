package com.oxilo.mrsafer.fragement;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oxilo.mrsafer.AppConstants;
import com.oxilo.mrsafer.AppController;
import com.oxilo.mrsafer.R;
import com.oxilo.mrsafer.activty.LandingActivity;
import com.oxilo.mrsafer.modal.ActusList;
import com.oxilo.mrsafer.modal.ReportList;
import com.oxilo.mrsafer.utility.ActivityUtils;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ActusDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActusDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActusDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ActusList actusItem;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextView actus_title,news_title,news_date,news_message,news_source,news_link;
    private ImageView video_picker,camera_icon,source_logo;

    public ActusDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActusDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActusDetailFragment newInstance(ActusList param1, String param2) {
        ActusDetailFragment fragment = new ActusDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            actusItem = getArguments().getParcelable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_actus_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUiView(view);
        setLabeToView();
        setLogoSource();
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
    }

    private void initUiView(View view){
        news_link = (TextView)view.findViewById(R.id.url_link);
        actus_title = (TextView)view.findViewById(R.id.actus_title);
        news_title = (TextView)view.findViewById(R.id.news_title);
        news_date = (TextView)view.findViewById(R.id.news_date);
        news_message = (TextView)view.findViewById(R.id.news_message);
        news_source = (TextView)view.findViewById(R.id.news_source);
        video_picker = (ImageView)view.findViewById(R.id.video_picker);
        camera_icon = (ImageView)view.findViewById(R.id.cameraicon);
        source_logo = (ImageView)view.findViewById(R.id.source_logo);

        actus_title.setTypeface(AppController.mpRobotaBold);
        news_title.setTypeface(AppController.mpRobotaRegular);
        news_date.setTypeface(AppController.mpRobotaRegular);
        news_message.setTypeface(AppController.mpRobotaRegular);
        news_source.setTypeface(AppController.mpRobotaRegular);
        news_link.setTypeface(AppController.mpRobotaRegular);


        video_picker.setOnClickListener(l);
        camera_icon.setOnClickListener(l);
        news_link.setOnClickListener(l);

        if (!actusItem.getImageFile().equals("")
                && !actusItem.getVideoFile().equals("")){
             camera_icon.setVisibility(View.VISIBLE);
             video_picker.setVisibility(View.VISIBLE);
        }else {
            camera_icon.setVisibility(View.INVISIBLE);
            video_picker.setVisibility(View.INVISIBLE);
        }
        if (!actusItem.getImageFile().equals("")){
            camera_icon.setVisibility(View.VISIBLE);
        }
        else{
           camera_icon.setVisibility(View.INVISIBLE);
        }
        if (!actusItem.getVideoFile().equals("")){
            video_picker.setVisibility(View.VISIBLE);
        }else {
            video_picker.setVisibility(View.INVISIBLE);
        }
    }

    private void setLabeToView(){
        try {
            news_title.setText(actusItem.getTitle());
            news_date.setText(actusItem.getDate().toString());
            news_message.setText(actusItem.getDescription());
            news_source.setText(actusItem.getNameSource());
            if (news_link!=null)
            news_link.setText(actusItem.getLinkSource());
            else
            news_link.setVisibility(View.INVISIBLE);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void setLogoSource(){
        if (!actusItem.getImageFile().equals("")){
            Picasso.with(getActivity())
                    .load(actusItem.getLogoSource().replaceAll(" ", "%20"))
                    .resize(100,100)
                    .centerInside()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.no_image_available)
                    .into(source_logo);
        }else{
            source_logo.setImageResource(R.drawable.no_image_available);
        }
    }



    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.cameraicon:
                    ActivityUtils.launcFragement(ImageFragment.newInstance(actusItem.getImageFile(),""),getActivity());
                    break;
                case R.id.video_picker:
                    ActivityUtils.launcFragement(ViewFragment.newInstance(actusItem.getVideoFile(),""),getActivity());
                    break;
                case R.id.url_link:
                    ActivityUtils.launcFragement(WebFragment.newInstance(actusItem.getLinkSource(),""),getActivity());
                    break;
                default:
                    break;
            }
        }
    };

}
