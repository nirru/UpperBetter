package com.oxilo.mrsafer.modal;

/**
 * Created by ericbasendra on 13/06/16.
 */
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportResponse implements Parcelable{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("ReportList")
    @Expose
    private List<ReportList> reportList = new ArrayList<ReportList>();

    protected ReportResponse(Parcel in) {
        message = in.readString();
        reportList = in.createTypedArrayList(ReportList.CREATOR);
    }

    public static final Creator<ReportResponse> CREATOR = new Creator<ReportResponse>() {
        @Override
        public ReportResponse createFromParcel(Parcel in) {
            return new ReportResponse(in);
        }

        @Override
        public ReportResponse[] newArray(int size) {
            return new ReportResponse[size];
        }
    };

    /**
     *
     * @return
     * The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     * The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return
     * The reportList
     */
    public List<ReportList> getReportList() {
        return reportList;
    }

    /**
     *
     * @param reportList
     * The ReportList
     */
    public void setReportList(List<ReportList> reportList) {
        this.reportList = reportList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeTypedList(reportList);
    }
}

