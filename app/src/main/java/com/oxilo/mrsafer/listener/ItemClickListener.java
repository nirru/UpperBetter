package com.oxilo.mrsafer.listener;

import android.view.View;

import java.io.IOException;

/**
 * Created by Dinesh on 15/11/15.
 * <p/>
 * This interface will be overridden in viewHolder classes
 * to be able to call onClick method on corresponding recyclerView items
 */
public interface ItemClickListener {
        void onClick(int position) throws IOException;
}