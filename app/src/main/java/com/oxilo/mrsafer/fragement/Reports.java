package com.oxilo.mrsafer.fragement;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oxilo.mrsafer.AppConstants;
import com.oxilo.mrsafer.AppController;
import com.oxilo.mrsafer.R;
import com.oxilo.mrsafer.activty.LandingActivity;
import com.oxilo.mrsafer.adapter.ReportsListAdapter;
import com.oxilo.mrsafer.holder.ChildItem;
import com.oxilo.mrsafer.holder.GroupItem;
import com.oxilo.mrsafer.modal.ReportList;
import com.oxilo.mrsafer.modal.ReportResponse;
import com.oxilo.mrsafer.modal.Response;
import com.oxilo.mrsafer.service.MrSaferWebService;
import com.oxilo.mrsafer.service.RetryWithDelay;
import com.oxilo.mrsafer.service.ServiceFactory;
import com.oxilo.mrsafer.utility.ActivityUtils;
import com.oxilo.mrsafer.view.CustomRecyclerView;
import com.oxilo.mrsafer.view.EndlessRecyclerOnScrollListener;
import com.oxilo.mrsafer.view.MyCustomLayoutManager;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Reports.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Reports#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Reports extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String LIST = "list";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CustomRecyclerView recyclerView;
    private ReportsListAdapter reportsListAdapter;
    private GroupItem groupItem;

    private OnFragmentInteractionListener mListener;

    private double incident_lat;
    private double incident_long;

    private View mProgressView;
    private View mRootView;

    public Reports() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Reports.
     */
    // TODO: Rename and change types and number of parameters
    public static Reports newInstance(String param1, String param2) {
        Reports fragment = new Reports();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reports, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUiView(view);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST,groupItem);
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
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppConstants.TAB_POSITION = 2;
        ((LandingActivity) getActivity()).getMyView().setVisibility(View.VISIBLE);
        ((LandingActivity) getActivity()).getMyFragmentDivider().setVisibility(View.VISIBLE);
        ((LandingActivity)getActivity()).updateThirdTab();
        ((LandingActivity)getActivity()).disableThirdTab();
        AppConstants.IS_REFRESH = false;
                //Do something after xx ms
           try {
                   incident_lat  =   ((LandingActivity)getActivity()).getIncident_lat();
                   incident_long =   ((LandingActivity)getActivity()).getIncident_long();
                   Log.e("INCIDENT_LAT", "" + incident_lat);
                   Log.e("INCIDENT_LNG","" + incident_long);
                   getRetainData(savedInstanceState);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
    }

    private void getRetainData(Bundle savedInstanceState) throws Exception {
        if (savedInstanceState != null) {
            //probably orientation change
            groupItem.items = (List<ReportList>) savedInstanceState.getParcelable(LIST);
        } else {
            if (groupItem != null) {
                //returning from backstack, data is fine, do nothing
                showRecycleWithDataFilled();
            } else {
                //newly created, compute data
                initInstance();
                showRecycleWithDataFilled();
                ActivityUtils.showProgress(true, mRootView, mProgressView, getActivity());
                getData(1);
            }
        }
    }


    private void initUiView(View view){
        mRootView = view.findViewById(R.id.login_form);
        mProgressView = view.findViewById(R.id.login_progress);
        recyclerView = (CustomRecyclerView)view.findViewById(R.id.recyleview);
        MyCustomLayoutManager linearLayoutManager = new MyCustomLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                try {
                    getData(current_page);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private void initInstance(){
        groupItem = new GroupItem();

    }

    private void showRecycleWithDataFilled(){
        reportsListAdapter = new ReportsListAdapter(groupItem.items,incident_lat,incident_long,getActivity());
        reportsListAdapter.setOnItemClickListener(new ReportsListAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                ReportList reportList = groupItem.items.get(position);
                ActivityUtils.launcFragement(DetailFragment.newInstance(reportList,incident_lat,incident_long),getActivity());
            }
        });
        recyclerView.setAdapter(reportsListAdapter);
    }

    private void prepareList(List<ReportList> reportLists){
        for (int i = 0; i < reportLists.size(); i ++){
            reportsListAdapter.addItem(reportLists.get(i));
        }
    }


    private void getData(int current_page) throws Exception{
        MrSaferWebService service = ServiceFactory.createRetrofitService(MrSaferWebService.class, AppConstants.BASE_URL);
        Observable<ReportResponse> reportResponseObservable = service.getListOfInciden("get_report",current_page,20,incident_lat,incident_long);
        reportResponseObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new Subscriber<ReportResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("ERROR", e.getLocalizedMessage().toString());
                        ActivityUtils.showProgress(false, mRootView, mProgressView, getActivity());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ReportResponse response) {
                        ActivityUtils.showProgress(false, mRootView, mProgressView, getActivity());
                        if (response.getStatus() == 1) {
                              Log.e("cdbkcfd","" + response.getReportList().size());
                              prepareList(response.getReportList());
                        } else {
                            Log.e("MY ERROR", "" + "SOME ERROR OCCURED");
                        }

                    }
                });
    }
}
