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

import com.oxilo.mrsafer.AppConstants;
import com.oxilo.mrsafer.R;
import com.oxilo.mrsafer.activty.LandingActivity;
import com.oxilo.mrsafer.adapter.ActusListAdapter;
import com.oxilo.mrsafer.adapter.ReportsListAdapter;
import com.oxilo.mrsafer.holder.GroupItem;
import com.oxilo.mrsafer.modal.Actus;
import com.oxilo.mrsafer.modal.ActusList;
import com.oxilo.mrsafer.modal.ReportList;
import com.oxilo.mrsafer.modal.ReportResponse;
import com.oxilo.mrsafer.service.MrSaferWebService;
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
 * {@link ActusFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActusFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String LIST = "list";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View mProgressView;
    private View mRootView;

    private GroupItem groupItem;
    private CustomRecyclerView recyclerView;
    private ActusListAdapter actusListAdapter;
    int previous_count = 0;
    int latest_count = 0;

    public ActusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActusFragment newInstance(String param1, String param2) {
        ActusFragment fragment = new ActusFragment();
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
        return inflater.inflate(R.layout.fragment_actus, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUiView(view);
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
        void onActusResult(int count);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppConstants.TAB_POSITION = 4;
        ((LandingActivity) getActivity()).getMyView().setVisibility(View.VISIBLE);
        ((LandingActivity) getActivity()).getMyFragmentDivider().setVisibility(View.VISIBLE);
        ((LandingActivity)getActivity()).updateFourthTab();
        ((LandingActivity)getActivity()).disableFourthTab();
        try {
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
        actusListAdapter = new ActusListAdapter(groupItem.actusItems,getActivity());
        actusListAdapter.setOnItemClickListener(new ActusListAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                ActusList actusItem = groupItem.actusItems.get(position);
                ActivityUtils.launcFragement(ActusDetailFragment.newInstance(actusItem,""),getActivity());
            }
        });
        recyclerView.setAdapter(actusListAdapter);
    }

    private void prepareList(List<ActusList> actusLists){
        for (int i = 0; i < actusLists.size(); i ++){
            actusListAdapter.addItem(actusLists.get(i));
        }
    }


    private void getData(int current_page) throws Exception{
        MrSaferWebService service = ServiceFactory.createRetrofitService(MrSaferWebService.class, AppConstants.BASE_URL);
        Observable<Actus> reportResponseObservable = service.getActus("get_web",current_page,25);
        reportResponseObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Actus>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ActivityUtils.showProgress(false, mRootView, mProgressView, getActivity());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Actus response) {
                        ActivityUtils.showProgress(false, mRootView, mProgressView, getActivity());
                        if (response.getStatus() == 1) {
                            prepareList(response.getActusList());
                        } else {
                            Log.e("MY ERROR", "" + "SOME ERROR OCCURED");
                        }

                    }
                });
    }
}
