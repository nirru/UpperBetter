package com.oxilo.mrsafer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oxilo.mrsafer.AppController;
import com.oxilo.mrsafer.R;
import com.oxilo.mrsafer.holder.ChildItem;
import com.oxilo.mrsafer.modal.ReportList;
import com.oxilo.mrsafer.utility.ActivityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by ericbasendra on 02/12/15.
 */
public class ReportsListAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private Context mContext;
    public List<T> dataSet;
    private static MyClickListener myClickListener;
    private int mLastPosition = 5;
    private double current_lat,current_long;

    public ReportsListAdapter(List<T> productLists, double current_lat, double current_long,Context mContext) {
        this.mContext = mContext;
        this.dataSet = productLists;
        this.current_lat = current_lat;
        this.current_long = current_long;
    }
    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void animateTo(List<T> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }


    private void applyAndAnimateRemovals(List<T> newModels) {
        for (int i = dataSet.size() - 1; i >= 0; i--) {
            final T model = dataSet.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }


    private void applyAndAnimateAdditions(List<T> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final T model = newModels.get(i);
            if (!dataSet.contains(model)) {
                addItem(i, model);
            }
        }
    }


    private void applyAndAnimateMovedItems(List<T> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final T model = newModels.get(toPosition);
            final int fromPosition = dataSet.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public void addItem(T item) {
        if (!dataSet.contains(item)) {
            dataSet.add(item);
            notifyItemInserted(dataSet.size() - 1);
        }
    }

    public void addItem(int position, T model) {
        dataSet.add(position, model);
        notifyItemInserted(position);
    }

    public void removeItem(T item) {
        int indexOfItem = dataSet.indexOf(item);
        if (indexOfItem != -1) {
            this.dataSet.remove(indexOfItem);
            notifyItemRemoved(indexOfItem);
        }
    }

    public T removeItem(int position) {
        final T model = dataSet.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void clearItem(){
        if (dataSet != null)
            dataSet.clear();
    }

    public void moveItem(int fromPosition, int toPosition) {
        final T model = dataSet.remove(fromPosition);
        dataSet.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public T getItem(int index) {
        if (dataSet != null && dataSet.get(index) != null) {
            return dataSet.get(index);
        } else {
            throw new IllegalArgumentException("Item with index " + index + " doesn't exist, dataSet is " + dataSet);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position)!=null? VIEW_ITEM: VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        if (viewType == VIEW_ITEM){
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.report_copy, parent, false);
            vh = new EventViewHolder(itemView);
        }
        else if(viewType == VIEW_PROG){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);
            vh = new ProgressViewHolder(v);
        }else {
            throw new IllegalStateException("Invalid type, this type ot items " + viewType + " can't be handled");
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof EventViewHolder){
            T dataItem = dataSet.get(position);
            if( ActivityUtils.getLanguage().equals("français") ){
                Log.e("LATLONG: "+current_lat,""+current_long);
                Log.e("LATLONG:REPORT "+Double.parseDouble(((ReportList)dataItem).getLat()),""+Double.parseDouble(((ReportList)dataItem).getLog()));

                String distance = ActivityUtils.findDistanceInKMFromMyCurrentLocation(current_lat,current_long,Double.parseDouble(((ReportList)dataItem).getLat()),Double.parseDouble(((ReportList)dataItem).getLog()));
                ((EventViewHolder) holder).milesView.setText("" + distance + " " + "kms");
            }else {
                String distance = ActivityUtils.findDistanceInMilesFromMyCurrentLocation(current_lat,current_long,Double.parseDouble(((ReportList)dataItem).getLat()),Double.parseDouble(((ReportList)dataItem).getLog()));
               ((EventViewHolder) holder).milesView.setText("" + distance + " " + "miles");
            }
            ((EventViewHolder) holder).timeView.setText(((ReportList)dataItem).getDate().toString());
            ((EventViewHolder) holder).titleView.setText(ActivityUtils.getIncidentSubType((ReportList)dataItem,mContext));
            ((EventViewHolder) holder).eventImageView.setImageResource(ActivityUtils.getIncidentImage((ReportList)dataItem,mContext));
            if (!((ReportList)dataItem).getImageFile().equals("")
                    && !((ReportList)dataItem).getVideoFile().equals("")){
                ((EventViewHolder) holder).cameraView.setVisibility(View.VISIBLE);
                ((EventViewHolder) holder).videoView.setVisibility(View.VISIBLE);
                ((EventViewHolder) holder).videoNewView.setVisibility(View.GONE);
            }else if (!((ReportList)dataItem).getImageFile().equals("")){
                ((EventViewHolder) holder).cameraView.setVisibility(View.VISIBLE);
                ((EventViewHolder) holder).videoView.setVisibility(View.GONE);
                ((EventViewHolder) holder).videoNewView.setVisibility(View.GONE);
            }else if (!((ReportList)dataItem).getVideoFile().equals("")){
                ((EventViewHolder) holder).cameraView.setVisibility(View.GONE);
                ((EventViewHolder) holder).videoView.setVisibility(View.GONE);
                ((EventViewHolder) holder).videoNewView.setVisibility(View.VISIBLE);
            }else{
                ((EventViewHolder) holder).cameraView.setVisibility(View.GONE);
                ((EventViewHolder) holder).videoView.setVisibility(View.GONE);
                ((EventViewHolder) holder).videoNewView.setVisibility(View.GONE);
            }
        }else{
            ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        if (dataSet!=null)
            return dataSet.size();
        else
            return 0;
    }



      // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView eventImageView,videoView,cameraView,videoNewView;
        TextView milesView,timeView,titleView;
        View parent_row;
        public EventViewHolder(View itemView) {
            super(itemView);
            eventImageView = (ImageView) itemView.findViewById(R.id.event_image);
            videoView = (ImageView) itemView.findViewById(R.id.videoicon);
            cameraView = (ImageView) itemView.findViewById(R.id.cameraicon);
            videoNewView = (ImageView) itemView.findViewById(R.id.videoAlter);
            milesView = (TextView)  itemView.findViewById(R.id.miles);
            timeView = (TextView)  itemView.findViewById(R.id.time);
            titleView = (TextView)  itemView.findViewById(R.id.event_title);
            parent_row = (View)itemView.findViewById(R.id.parent_row);

            parent_row.setOnClickListener(this);

            milesView.setTypeface(AppController.mpRobotaMeduim);
            timeView.setTypeface(AppController.mpRobotaMeduim);
            titleView.setTypeface(AppController.mpRobotaBold);
        }

        @Override
        public void onClick(View view) {
            try{
                if(myClickListener!=null){
                myClickListener.onItemClick(getLayoutPosition(), view);
                }else{
                    Toast.makeText(view.getContext(),"Click Event Null",Toast.LENGTH_SHORT).show();
                }
            }catch(NullPointerException e){
                Toast.makeText(view.getContext(),"Click Event Null Ex",Toast.LENGTH_SHORT).show();
            }
        }
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar)v.findViewById(R.id.progress_bar);
        }
    }


    /**
     * y Custom Item Listener
     */

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }


}
