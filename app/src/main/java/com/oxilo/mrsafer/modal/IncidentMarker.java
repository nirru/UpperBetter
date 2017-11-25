package com.oxilo.mrsafer.modal;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by ericbasendra on 17/06/16.
 */
public class IncidentMarker implements ClusterItem{

    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;

//    public final int profilePhoto;

    public IncidentMarker(LatLng position, String t, String s) {
        mPosition = position;
        mTitle = t;
        mSnippet = s;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getSnippet(){
        return mSnippet;
    }


}
