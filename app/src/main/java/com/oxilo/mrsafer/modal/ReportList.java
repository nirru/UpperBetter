package com.oxilo.mrsafer.modal;

/**
 * Created by ericbasendra on 13/06/16.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

public class ReportList implements ClusterItem,Parcelable{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("sub_type")
    @Expose
    private String subType;
    @SerializedName("image_file")
    @Expose
    private String imageFile;
    @SerializedName("video_file")
    @Expose
    private String videoFile;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("log")
    @Expose
    private String log;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("is_victim")
    @Expose
    private String isVictim;
    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("selected_id")
    @Expose
    private String selectedId;

    protected ReportList(Parcel in) {
        id = in.readString();
        email = in.readString();
        description = in.readString();
        type = in.readString();
        subType = in.readString();
        imageFile = in.readString();
        videoFile = in.readString();
        lat = in.readString();
        log = in.readString();
        distance = in.readDouble();
        isVictim = in.readString();
        date = in.readString();
        title = in.readString();
        selectedId = in.readString();
    }

    public static final Creator<ReportList> CREATOR = new Creator<ReportList>() {
        @Override
        public ReportList createFromParcel(Parcel in) {
            return new ReportList(in);
        }

        @Override
        public ReportList[] newArray(int size) {
            return new ReportList[size];
        }
    };

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The subType
     */
    public String getSubType() {
        return subType;
    }

    /**
     *
     * @param subType
     * The sub_type
     */
    public void setSubType(String subType) {
        this.subType = subType;
    }


    /**
     *
     * @return
     * The imageFile
     */
    public String getImageFile() {
        return imageFile;
    }

    /**
     *
     * @param imageFile
     * The image_file
     */
    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    /**
     *
     * @return
     * The videoFile
     */
    public String getVideoFile() {
        return videoFile;
    }

    /**
     *
     * @param videoFile
     * The video_file
     */
    public void setVideoFile(String videoFile) {
        this.videoFile = videoFile;
    }

    /**
     *
     * @return
     * The lat
     */
    public String getLat() {
        return lat;
    }

    /**
     *
     * @param lat
     * The lat
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     *
     * @return
     * The log
     */
    public String getLog() {
        return log;
    }

    /**
     *
     * @param log
     * The log
     */
    public void setLog(String log) {
        this.log = log;
    }

    /**
     *
     * @return
     * The distance
     */
    public Double getDistance() {
        return distance;
    }

    /**
     *
     * @param distance
     * The distance
     */
    public void setDistance(Double distance) {
        this.distance = distance;
    }

    /**
     *
     * @return
     * The isVictim
     */
    public String getIsVictim() {
        return isVictim;
    }

    /**
     *
     * @param isVictim
     * The is_victim
     */
    public void setIsVictim(String isVictim) {
        this.isVictim = isVictim;
    }

    /**
     *
     * @return
     * The date
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @return
     * The title
     */
    public String gettitle() {
        return title;
    }

    /**
     *
     * @param title
     * The id
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     *
     * @return
     * The selectedId
     */
    public String getSelectedId() {
        return selectedId;
    }

    /**
     *
     * @param selectedId1
     * The id
     */
    public void setSelectedId(String selectedId1) {
        this.selectedId = selectedId1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(description);
        dest.writeString(type);
        dest.writeString(subType);
        dest.writeString(imageFile);
        dest.writeString(videoFile);
        dest.writeString(lat);
        dest.writeString(log);
        dest.writeDouble(distance);
        dest.writeString(isVictim);
        dest.writeString(date);
        dest.writeString(title);
        dest.writeString(selectedId);
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(Double.parseDouble(getLat()),Double.parseDouble(getLog()));
    }


}