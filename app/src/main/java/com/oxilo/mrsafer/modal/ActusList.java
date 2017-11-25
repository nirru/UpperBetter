package com.oxilo.mrsafer.modal;

/**
 * Created by ericbasendra on 13/07/16.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActusList implements Parcelable{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("image_file")
    @Expose
    private String imageFile;
    @SerializedName("video_file")
    @Expose
    private String videoFile;
    @SerializedName("logo_source")
    @Expose
    private String logoSource;
    @SerializedName("name_source")
    @Expose
    private String nameSource;
    @SerializedName("link_source")
    @Expose
    private String linkSource;

    protected ActusList(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        date = in.readString();
        imageFile = in.readString();
        videoFile = in.readString();
        logoSource = in.readString();
        nameSource = in.readString();
        linkSource = in.readString();
    }

    public static final Creator<ActusList> CREATOR = new Creator<ActusList>() {
        @Override
        public ActusList createFromParcel(Parcel in) {
            return new ActusList(in);
        }

        @Override
        public ActusList[] newArray(int size) {
            return new ActusList[size];
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
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
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
     * The logoSource
     */
    public String getLogoSource() {
        return logoSource;
    }

    /**
     *
     * @param logoSource
     * The logo_source
     */
    public void setLogoSource(String logoSource) {
        this.logoSource = logoSource;
    }

    /**
     *
     * @return
     * The nameSource
     */
    public String getNameSource() {
        return nameSource;
    }

    /**
     *
     * @param nameSource
     * The name_source
     */
    public void setNameSource(String nameSource) {
        this.nameSource = nameSource;
    }
    /**
     *
     * @return
     * The linkSource
     */
    public String getLinkSource() {
        return linkSource;
    }

    /**
     *
     * @param linkSource
     * The linkSource
     */
    public void setLinkSource(String linkSource) {
        this.linkSource = linkSource;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(imageFile);
        dest.writeString(videoFile);
        dest.writeString(logoSource);
        dest.writeString(nameSource);
        dest.writeString(linkSource);
    }
}