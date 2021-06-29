package com.example.dreamproperty.buyProperty;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.dreamproperty.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class PropertyDetailsAdapter extends SliderViewAdapter<PropertyDetailsAdapter.PropertyDetailsHolder> {

    List<String> imageList;
    PropertyDetailsAdapter(List<String> list){
        this.imageList = list;
    }

    @Override
    public PropertyDetailsHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.propertysliderimage_item, parent, false);
        return new PropertyDetailsHolder(view);
    }
    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(PropertyDetailsHolder viewHolder, int position) {
        Glide.with(getApplicationContext())
                .load(imageList.get(position))
                .into(viewHolder.imageView);
    }
    @Override
    public int getCount() {
        return imageList.size();
    }

    class PropertyDetailsHolder extends SliderViewAdapter.ViewHolder{
        ImageView imageView;
        public PropertyDetailsHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
