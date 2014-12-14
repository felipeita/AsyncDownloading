package com.androidtalks.asyncdownloading.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.androidtalks.asyncdownloading.R;
import com.koushikdutta.ion.Ion;

/**
 * Created by leonelmendez on 14/12/14.
 */
public class GridAdapter extends ArrayAdapter<String> {



    public GridAdapter(Context context){
        super(context,0);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_image_layout,parent,false);

        // find the image view
        final ImageView iv = (ImageView) convertView.findViewById(R.id.grid_image);

        // select the image view
        Ion.with(iv)
                .centerCrop()
                .error(R.drawable.error)
                .load(getItem(position));

        return convertView;

    }



}
