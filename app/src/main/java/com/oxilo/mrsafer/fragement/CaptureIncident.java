package com.oxilo.mrsafer.fragement;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraAccessException;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.oxilo.mrsafer.AppConstants;
import com.oxilo.mrsafer.AppController;
import com.oxilo.mrsafer.R;
import com.oxilo.mrsafer.activty.LandingActivity;
import com.oxilo.mrsafer.event.EventTracker;
import com.oxilo.mrsafer.listener.DialogListener;
import com.oxilo.mrsafer.modal.Response;
import com.oxilo.mrsafer.service.MrSaferWebService;
import com.oxilo.mrsafer.service.RetryWithDelay;
import com.oxilo.mrsafer.service.ServiceFactory;
import com.oxilo.mrsafer.utility.ActivityUtils;
import com.oxilo.mrsafer.utility.FileUtils;
import com.oxilo.mrsafer.utility.PermissionUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CaptureIncident.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CaptureIncident#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CaptureIncident extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    // TODO: Rename and change types of parameters
    private String eventTitle;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ImageView image_picker;
    private ImageView video_picker;
    private Button btn_send;
    private EditText mDescriptionView;
    TextView eventTitleView;
    TextView victimView, witnessView;
    String mCurrentPhotoPath, mCurrentVideoPath;
    public FrameLayout frameLayout;
    private View mProgressView;
    private View mRootView;
    int mVictimOrWitness = 0;
    int type = 0;
    private ImageView eventImage;
    private ImageView eventSubType_1, eventSubType_2, eventSubType_3;
    private RelativeLayout event_sub_type_container;
    private TextView subtype_title_1, subtype_title_2, subtype_title_3;
    private EditText report_desc_view;
    private double incident_lat;
    private double incident_long;
    OkHttpClient client;
    Snackbar snackbar;
    int report_count;

    public CaptureIncident() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CaptureIncident.
     */
    // TODO: Rename and change types and number of parameters
    public static CaptureIncident newInstance(String param1, int param2,double param3,double param4) {
        CaptureIncident fragment = new CaptureIncident();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        args.putDouble(ARG_PARAM3, param3);
        args.putDouble(ARG_PARAM4, param4);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventTitle = getArguments().getString(ARG_PARAM1);
            type = getArguments().getInt(ARG_PARAM2);
            incident_lat = getArguments().getDouble(ARG_PARAM3);
            incident_long = getArguments().getDouble(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_capture_incident, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUiView(view);
        initInstance();
        EventTracker.getInstance().changeEventImageAndTitle(getActivity(),
                eventSubType_1, eventSubType_2, eventSubType_3,
                subtype_title_1, subtype_title_2, subtype_title_3, eventImage, event_sub_type_container, report_desc_view, type);
    }

    private void initInstance() {
//        cameraUtil = new CameraUtil(getActivity());
        client = new OkHttpClient();
    }

    private void initUiView(View view) {
        report_desc_view = (EditText) view.findViewById(R.id.report_desc_id);
        event_sub_type_container = (RelativeLayout) view.findViewById(R.id.relative_third_child);
        eventImage = (ImageView) view.findViewById(R.id.report_capture_img_id);
        eventSubType_1 = (ImageView) view.findViewById(R.id.subtype_1);
        eventSubType_2 = (ImageView) view.findViewById(R.id.subtype_2);
        eventSubType_3 = (ImageView) view.findViewById(R.id.subtype_3);
        subtype_title_1 = (TextView) view.findViewById(R.id.subtype_title_1);
        subtype_title_2 = (TextView) view.findViewById(R.id.subtype_title_2);
        subtype_title_3 = (TextView) view.findViewById(R.id.subtype_title_3);
        eventTitleView = (TextView) view.findViewById(R.id.report_id);
        image_picker = (ImageView) view.findViewById(R.id.image_picker);
        video_picker = (ImageView) view.findViewById(R.id.video_picker);
        frameLayout = (FrameLayout) view.findViewById(R.id.framelayout);
        btn_send = (AppCompatButton) view.findViewById(R.id.send_btn_style);
        mDescriptionView = (EditText) view.findViewById(R.id.report_desc_id);
        victimView = (TextView) view.findViewById(R.id.victim);
        witnessView = (TextView) view.findViewById(R.id.witness);
        mRootView = view.findViewById(R.id.login_form);
        mProgressView = view.findViewById(R.id.login_progress);

        image_picker.setOnClickListener(l);
        frameLayout.setOnClickListener(l);
        btn_send.setOnClickListener(l);
        victimView.setOnClickListener(l);
        witnessView.setOnClickListener(l);

        image_picker.setTag(1);
        frameLayout.setTag(1);
        victimView.setTag(10);
        witnessView.setTag(10);

        eventTitleView.setTypeface(AppController.mpRobotaMeduim);
        eventTitleView.setText(eventTitle);
        subtype_title_1.setTypeface(AppController.mpRobotaMeduim);
        subtype_title_2.setTypeface(AppController.mpRobotaMeduim);
        subtype_title_3.setTypeface(AppController.mpRobotaMeduim);
    }


    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.image_picker:
                    //ImageView is empty open the dialog to select the image
                    if (image_picker.getTag().equals(1)) {
                        try {
                            captureImageFromcamera();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //ImageView has already Image either view it or delete it
                        startActionForImage("PERFORM ACTION", 2);
                    }
                    break;
                case R.id.framelayout:
                    if (frameLayout.getTag().equals(1)) {
                        try {
                            captureVideoFromCamera();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        //ImageView has already Image either view it or delete it
                        startActionForVideo("PERFORM ACTION", 2);
                    }

                    break;
                case R.id.send_btn_style:
                    sendReports();
                    break;
                case R.id.victim:
                    mVictimOrWitness = 0;
                    victimView.setTag(11);
                    witnessView.setTag(11);
                    victimView.setEnabled(false);
                    witnessView.setEnabled(true);
                    victimView.setTypeface(AppController.mpRobotaBold);
                    witnessView.setTypeface(AppController.mpRobotaRegular);
                    victimView.setTextColor(getResources().getColor(R.color.selected_text_color));
                    witnessView.setTextColor(getResources().getColor(R.color.unselected_text_color));
                    break;
                case R.id.witness:
                    mVictimOrWitness = 1;
                    victimView.setTag(11);
                    witnessView.setTag(11);
                    witnessView.setEnabled(false);
                    victimView.setEnabled(true);
                    witnessView.setTypeface(AppController.mpRobotaBold);
                    victimView.setTypeface(AppController.mpRobotaRegular);
                    witnessView.setTextColor(getResources().getColor(R.color.selected_text_color));
                    victimView.setTextColor(getResources().getColor(R.color.unselected_text_color));
                    break;
                default:
                    Toast.makeText(getActivity(), "Not a valid selection", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void sendReports() {
        mDescriptionView.setError(null);
        // Store values at the time of the login attempt.
        String campaign_title = mDescriptionView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid Campaign Title.
        if (TextUtils.isEmpty(campaign_title)) {
            mDescriptionView.setError(getString(R.string.error_field_required));
            focusView = mDescriptionView;
            cancel = true;
        }
        else if (campaign_title.length()<20) {
            mDescriptionView.setError(getString(R.string.error_field_required));
            focusView = mDescriptionView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (!witnessView.getTag().equals(11) && !victimView.getTag().equals(11)){
                   snackbar.setText("Choose victim or witness");
                   snackbar.show();
            }else if (!eventSubType_1.getTag(R.id.TAG_KEY_CLICK_EVENT).equals(10) && !eventSubType_2.getTag(R.id.TAG_KEY_CLICK_EVENT).equals(10) && !eventSubType_3.getTag(R.id.TAG_KEY_CLICK_EVENT).equals(10)){
                   snackbar.setText(getString(R.string.choose_betwwen_three_icon));
                   snackbar.show();
            }else{
                ActivityUtils.showProgress(true, mRootView, mProgressView, getActivity());
                try {
                    sendData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void startActionForImage(String title, int tag) {
        AppController.getInstance().viewOrDeleteDialog(getActivity(), title, new DialogListener() {
            @Override
            public void onItemClick(int pos) throws Exception {
                if (pos == 0) {
                    showImageInGallery();
                } else {
                    deleteImage();
                }
            }
        });
    }


    private void startActionForVideo(String title, int pos) {
        AppController.getInstance().viewOrDeleteDialog(getActivity(), title, new DialogListener() {
            @Override
            public void onItemClick(int position) {
                if (position == 0) {
                    showVideoInDefaultPlayer();
                } else {
                    deleteVideo();
                }
            }

        });
    }

    private void showVideoInDefaultPlayer() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(mCurrentVideoPath), "video/*");
        startActivity(intent);
    }

    private void captureVideoFromCamera() {
        if(!AppController.getInstance().hasPermissions(getActivity(), AppConstants.PERMISSIONS)){
            requestPermissions(AppConstants.PERMISSIONS, AppConstants.PERMISSION_VIDEO);
        }else{
            Intent takePictureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = FileUtils.createVideoFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    ex.printStackTrace();
                    AppController.getInstance().showSimpleDialog(getActivity(), getString(R.string.error), getString(R.string.camera_denied));
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                    takePictureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
                    takePictureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                    startActivityForResult(takePictureIntent, AppConstants.REQUEST_VIDEO_CAPTURE);
                }
            }
        }
    }

    private void showImageInGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + mCurrentPhotoPath), "image/*");
        startActivity(intent);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private void galleryAddVideo() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentVideoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    public void captureImageFromcamera() throws IOException {
        if(!AppController.getInstance().hasPermissions(getActivity(), AppConstants.PERMISSIONS)){
            requestPermissions(AppConstants.PERMISSIONS, AppConstants.PERMISSION_ALL);
        }else{
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's aon camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
//                File photoFile = null;
//                try {
//                    photoFile = FileUtils.createImageFile();
//                } catch (IOException ex) {
//                    // Error occurred while creating the File
//                    ex.printStackTrace();
//                    AppController.getInstance().showSimpleDialog(getActivity(), getString(R.string.error), getString(R.string.camera_denied));
//                }
                // Continue only if the File was successfully created
//                if (photoFile != null) {
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                            Uri.fromFile(photoFile));
                    startActivityForResult(takePictureIntent, AppConstants.REQUEST_TAKE_PHOTO);
//                }
            }
        }

//        takePictureFromCamera();
    }



    private void setPic(String mCurrentPhotoPath) {
        // Get the dimensions of the View
        this.mCurrentPhotoPath = mCurrentPhotoPath;
        int targetW = image_picker.getWidth();
        int targetH = image_picker.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
//
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        image_picker.setScaleType(ImageView.ScaleType.FIT_XY);
        image_picker.setImageBitmap(bitmap);
        image_picker.setTag(2);
    }



    private void setVid() {
        // Get the dimensions of the View
        // MINI_KIND, size: 512 x 384 thumbnail
        try {
            mCurrentVideoPath = FileUtils.getmCurrentVideoPath();
            frameLayout.setTag(2);
            Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(mCurrentVideoPath, MediaStore.Video.Thumbnails.MINI_KIND);
            video_picker.setScaleType(ImageView.ScaleType.FIT_XY);
            video_picker.setImageBitmap(bmThumbnail);
            frameLayout.setForeground(getResources().getDrawable(R.drawable.ic_play_circle_outline_black));
            frameLayout.setForegroundGravity(Gravity.CENTER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteImage() {
        File fdelete = new File(mCurrentPhotoPath);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Log.e("-->", "file Deleted :" + mCurrentPhotoPath);
                image_picker.setTag(1);
                image_picker.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                image_picker.setImageResource(0);
                image_picker.setImageResource(R.drawable.camera_icon);
                callBroadCast();
            } else {
                Log.e("-->", "file not Deleted :" + mCurrentPhotoPath);
            }
        }
    }

    public void deleteVideo() {
        File fdelete = new File(mCurrentVideoPath);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Log.e("-->", "file Deleted :" + mCurrentVideoPath);
                frameLayout.setTag(1);
                video_picker.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                video_picker.setImageResource(0);
                video_picker.setImageResource(R.drawable.vedio_icon);
                frameLayout.setForeground(null);
                callBroadCast();
            } else {
                Log.e("-->", "file not Deleted :" + mCurrentVideoPath);
            }
        }
    }


    public void callBroadCast() {
        MediaScannerConnection.scanFile(getActivity(), new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            /*
             *   (non-Javadoc)
             * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
             */
            public void onScanCompleted(String path, Uri uri) {
                Log.e("ExternalStorage", "Scanned " + path + ":");
                Log.e("ExternalStorage", "-> uri=" + uri);
            }
        });
    }

    private void sendData() throws Exception {
        MrSaferWebService service = ServiceFactory.createRetrofitService(MrSaferWebService.class, AppConstants.BASE_URL);
        File imageFile = null;
        File videoFile = null;
        MultipartBody.Part imageBody = null;
        MultipartBody.Part videoBody = null;
        RequestBody requestImageFile = null;
        RequestBody requestVideoFile = null;
        Observable<Response> addReports = null;
        if (mCurrentPhotoPath != null && mCurrentVideoPath != null){
            imageFile = new File(mCurrentPhotoPath);
            requestImageFile =
                    RequestBody.create(MediaType.parse("image/*"), imageFile);
            imageBody = MultipartBody.Part.createFormData("image", imageFile.getName(), requestImageFile);

            videoFile = new File(mCurrentVideoPath);
            requestVideoFile =
                    RequestBody.create(MediaType.parse("video/*"), videoFile);
            videoBody = MultipartBody.Part.createFormData("video", videoFile.getName(), requestVideoFile);
        }
        else if (mCurrentPhotoPath != null){
            imageFile = new File(mCurrentPhotoPath);
            requestImageFile =
                    RequestBody.create(MediaType.parse("image/*"), imageFile);
            imageBody = MultipartBody.Part.createFormData("image", imageFile.getName(), requestImageFile);
        }
        else if (mCurrentVideoPath != null){
            videoFile = new File(mCurrentVideoPath);
            requestVideoFile =
                    RequestBody.create(MediaType.parse("video/*"), videoFile);
            videoBody = MultipartBody.Part.createFormData("video", videoFile.getName(), requestVideoFile);

        }else{
            //do nothing
        }

        // add another part within the multipart request
        Log.e("SUBTYPE:",""+EventTracker.getInstance().getSub_type());
        String mTitle = EventTracker.getInstance().getTitle().toString();
        String mSubType = EventTracker.getInstance().getSub_type();
        String mSelectedId =ActivityUtils.getSelectedId(mSubType , getActivity());
        String descriptionString = mDescriptionView.getText().toString().trim();
        RequestBody selectedId = RequestBody.create(MediaType.parse("multipart/form-data"), mSelectedId);
        RequestBody title = RequestBody.create(MediaType.parse("multipart/form-data"), mTitle);
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);
        RequestBody lat = RequestBody.create(MediaType.parse("multipart/form-data"), "" + incident_lat);
        RequestBody longitude = RequestBody.create(MediaType.parse("multipart/form-data"), "" + incident_long);
        RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), "email");
        RequestBody type1 = RequestBody.create(MediaType.parse("multipart/form-data"), "" + type);
        RequestBody sub_type = RequestBody.create(MediaType.parse("multipart/form-data"), "" + mSubType);
        RequestBody victim = RequestBody.create(MediaType.parse("multipart/form-data"), "" + mVictimOrWitness);
        RequestBody api = RequestBody.create(MediaType.parse("multipart/form-data"), "add_report");
        if (mCurrentPhotoPath != null && mCurrentVideoPath != null){
            addReports = service.uploadFileWithVideoAndImage(imageBody,videoBody,title, description, lat, longitude, email, type1, sub_type,victim, api,selectedId);
        }else if (mCurrentVideoPath != null){
            addReports = service.uploadVideoFile(videoBody,title, description, lat, longitude, email, type1, sub_type,victim, api,selectedId);
        }else if (mCurrentPhotoPath != null){
            addReports = service.uploadImageFile(imageBody,title, description, lat, longitude, email, type1, sub_type,victim, api,selectedId);
        }else{
            addReports = service.uploadWithoutFile(title, description, lat, longitude, email, type1, sub_type,victim, api,selectedId);
        }

        addReports.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new Subscriber<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            ActivityUtils.showProgress(false, mRootView, mProgressView, getActivity());
                            Toast.makeText(getActivity(),
                                    e.getMessage(),
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(Response response) {
                        ActivityUtils.showProgress(false, mRootView, mProgressView, getActivity());
                        Log.e("REE","" + response.getStatus() + "===" + response.getMessage());
                        if (response.getStatus() == 1) {
                            mListener.updateReportCount(report_count + 1);
                            AppController.getInstance().showDialog(getActivity(), getString(R.string.message), getString(R.string.data_successful));
                        } else {
                            Log.e("MY ERROR", "" + "SOME ERROR OCCURED");
                        }

                    }
                }
        );

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == AppConstants.REQUEST_TAKE_PHOTO) {
                try {
                    if(data!=null){
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        final File file =  ActivityUtils.saveImageToExternalStorage(photo);
                        setPic(file.getAbsolutePath());
                        galleryAddPic();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (requestCode == AppConstants.REQUEST_VIDEO_CAPTURE) {
                setVid();
                galleryAddVideo();
            }
        }
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
        void updateReportCount(int size);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((LandingActivity) getActivity()).getMyView().setVisibility(View.VISIBLE);
        ((LandingActivity) getActivity()).getMyFragmentDivider().setVisibility(View.VISIBLE);
        ((LandingActivity) getActivity()).disableSecondTab();
        report_count =  ((LandingActivity) getActivity()).getAlertsCount();
        snackbar = ((LandingActivity) getActivity()).getSnackbar("");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         if (PermissionUtils.isPermissionGranted(permissions,grantResults,Manifest.permission.CAMERA)
                && PermissionUtils.isPermissionGranted(permissions,grantResults,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && PermissionUtils.isPermissionGranted(permissions,grantResults,Manifest.permission.READ_EXTERNAL_STORAGE)){
            try {
                if (requestCode      == AppConstants.PERMISSION_ALL)
                captureImageFromcamera();
                else if (requestCode == AppConstants.PERMISSION_VIDEO)
                    captureVideoFromCamera();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
             if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ||
                     shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                     shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                 AppController.getInstance().cameraPermission(getActivity(),getString(R.string.error),getString(R.string.read_write));

             }else{
                 snackbar.setText(getString(R.string.required_permission));
                 snackbar.show();
             }
         }
    }

}
