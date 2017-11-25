package com.oxilo.mrsafer.service;

import com.oxilo.mrsafer.AppConstants;
import com.oxilo.mrsafer.modal.Actus;
import com.oxilo.mrsafer.modal.ReportResponse;
import com.oxilo.mrsafer.modal.Response;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by ericbasendra on 10/06/16.
 */
public interface MrSaferWebService {

    @Headers("Accept: multipart/form-data")
    @Multipart
    @POST(AppConstants.ADD_REPORTS)
    Observable<Response> uploadFileWithVideoAndImage(@Part MultipartBody.Part image,
                                    @Part MultipartBody.Part video,
                                    @Part("title") RequestBody title,
                                    @Part("desc") RequestBody desc,
                                    @Part("lat") RequestBody lat,
                                    @Part("long") RequestBody longitude,
                                    @Part("email") RequestBody email,
                                    @Part("type") RequestBody type,
                                    @Part("sub_type") RequestBody sub_type,
                                    @Part("victim") RequestBody victim,
                                    @Part("api") RequestBody api_name,
                                    @Part("selected_id") RequestBody selectedId);

    @Headers("Accept: multipart/form-data")
    @Multipart
    @POST(AppConstants.ADD_REPORTS)
    Observable<Response> uploadImageFile(@Part MultipartBody.Part image,
                                    @Part("title") RequestBody title,
                                    @Part("desc") RequestBody desc,
                                    @Part("lat") RequestBody lat,
                                    @Part("long") RequestBody longitude,
                                    @Part("email") RequestBody email,
                                    @Part("type") RequestBody type,
                                    @Part("sub_type") RequestBody sub_type,
                                    @Part("victim") RequestBody victim,
                                    @Part("api") RequestBody api_name,
                                    @Part("selected_id") RequestBody selectedId);

    @Headers("Accept: multipart/form-data")
    @Multipart
    @POST(AppConstants.ADD_REPORTS)
    Observable<Response> uploadVideoFile(@Part MultipartBody.Part video,
                                         @Part("title") RequestBody title,
                                         @Part("desc") RequestBody desc,
                                         @Part("lat") RequestBody lat,
                                         @Part("long") RequestBody longitude,
                                         @Part("email") RequestBody email,
                                         @Part("type") RequestBody type,
                                         @Part("sub_type") RequestBody sub_type,
                                         @Part("victim") RequestBody victim,
                                         @Part("api") RequestBody api_name,
                                         @Part("selected_id") RequestBody selectedId);


    @Headers("Accept: multipart/form-data")
    @Multipart
    @POST(AppConstants.ADD_REPORTS)
    Observable<Response> uploadWithoutFile(@Part("title") RequestBody title,
                                           @Part("desc") RequestBody desc,
                                           @Part("lat") RequestBody lat,
                                           @Part("long") RequestBody longitude,
                                           @Part("email") RequestBody email,
                                           @Part("type") RequestBody type,
                                           @Part("sub_type") RequestBody sub_type,
                                           @Part("victim") RequestBody victim,
                                           @Part("api") RequestBody api_name,
                                           @Part("selected_id") RequestBody selectedId);


    @FormUrlEncoded
    @POST(AppConstants.GET_REPORTS)
    Observable<ReportResponse> getListOfInciden(@Field("api") String api_name
    ,@Field("page") long page
    ,@Field("limit") int limit
    ,@Field("lat") double lat
    ,@Field("long") double longitude);

    @FormUrlEncoded
    @POST(AppConstants.GET_REPORTS)
    Observable<ReportResponse> getListOfInciden(@Field("api") String api_name
            ,@Field("lat") double lat
            ,@Field("long") double longitude);

    @FormUrlEncoded
    @POST(AppConstants.GET_REPORTS)
    Observable<Actus> getActus(@Field("api") String api_name
            , @Field("page") long page
            , @Field("limit") int limit);


    @Headers("Accept: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(AppConstants.SEND_ABUSE)
    Observable<Response> sendAbuseReport(
             @Field("api") String api_name
            ,@Field("address") String address
            ,@Field("desc") String desc
            ,@Field("sub_type") String sub_type
            ,@Field("victim") String victim
            ,@Field("image_url") String image_url
            ,@Field("video_url") String video_url
            ,@Field("subject") String subject
            ,@Field("distance") String distance
            ,@Field("time") String time);
}
