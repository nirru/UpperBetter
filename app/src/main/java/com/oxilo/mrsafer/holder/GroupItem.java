package com.oxilo.mrsafer.holder;


import android.os.Parcel;
import android.os.Parcelable;

import com.oxilo.mrsafer.modal.ActusList;
import com.oxilo.mrsafer.modal.ReportList;

import java.util.ArrayList;
import java.util.List;

public class GroupItem implements Parcelable{
	public String title;
	public List<ReportList> items = new ArrayList<ReportList>();
    public List<ActusList> actusItems = new ArrayList<>();

	public GroupItem() {

	}

	public GroupItem(Parcel in) {
		title = in.readString();
		items = in.createTypedArrayList(ReportList.CREATOR);
		actusItems = in.createTypedArrayList(ActusList.CREATOR);
	}

	public static final Creator<GroupItem> CREATOR = new Creator<GroupItem>() {
		@Override
		public GroupItem createFromParcel(Parcel in) {
			return new GroupItem(in);
		}

		@Override
		public GroupItem[] newArray(int size) {
			return new GroupItem[size];
		}
	};


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeTypedList(items);
		dest.writeTypedList(actusItems);
	}
}
