package com.hapen.navigationdrawertest;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private ArrayList<CardItem> mArrayList;

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public ImageView organizationLogo;
        public TextView organizationName;
        public TextView eventCategory;
        public TextView eventTitle;
        public TextView eventDate;
        public ImageView eventFlyer;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            organizationLogo = itemView.findViewById(R.id.card_organization_logo);
            organizationName = itemView.findViewById(R.id.card_organization_name);
            eventCategory = itemView.findViewById(R.id.card_category);
            eventTitle = itemView.findViewById(R.id.card_event_title);
            eventDate = itemView.findViewById(R.id.card_event_date);
            eventFlyer = itemView.findViewById(R.id.card_event_flyer);
        }
    }

    public RecyclerAdapter(ArrayList<CardItem> arrayList) {
        mArrayList = arrayList;
    }

    @NonNull
    @Override
    // Pass layout here
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item, viewGroup,
                false);
        RecyclerViewHolder rvh = new RecyclerViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i) {
        CardItem currentItem = mArrayList.get(i);
        recyclerViewHolder.organizationLogo.setImageResource(currentItem.getOrganizationLogo());
        recyclerViewHolder.organizationName.setText(currentItem.getOrganizationName());
        recyclerViewHolder.eventCategory.setText(currentItem.getEventCategory());
        recyclerViewHolder.eventTitle.setText(currentItem.getEventTitle());
        recyclerViewHolder.eventDate.setText(currentItem.getEventDate());
        recyclerViewHolder.eventFlyer.setImageResource(currentItem.getEventFlyer());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }
}
