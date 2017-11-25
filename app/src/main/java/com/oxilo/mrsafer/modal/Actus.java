package com.oxilo.mrsafer.modal;

/**
 * Created by ericbasendra on 13/07/16.
 */
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Actus {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("ReportList")
    @Expose
    private List<ActusList> actusList = new ArrayList<ActusList>();

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
    public List<ActusList> getActusList() {
        return actusList;
    }

    /**
     *
     * @param actusList
     * The ReportList
     */
    public void setActusList(List<ActusList> actusList) {
        this.actusList = actusList;
    }

}
