
package com.hapen.navigationdrawertest;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.app.wanna.android.utils.firebaseadapter.FirebaseRecyclerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;
import android.support.v7.widget.SearchView;
//import android.view.Ca
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.dmallcott.dismissibleimageview.DismissibleImageView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import android.support.v4.view.ViewPager;


import java.util.Collection;
import java.util.List;

import com.github.chrisbanes.photoview.PhotoView;
import com.hapen.navigationdrawertest.R;
import com.squareup.picasso.Picasso;



import java.util.ArrayList;


/**
public class ViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public ViewHolder(View itemView) {
        super(itemView);

        mView = itemView;

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });
        //item long click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });

    }


    //set details to recycler view row
    public void setDetails(Context ctx, String title, String post_organization_logo, String post_organization_name,String post_event_category, String post_event_date, String flyer){
        //Views

        ImageView organizationLogo = (ImageView)mView.findViewById(R.id.card_organization_logo);
        TextView organizationName = mView.findViewById(R.id.card_organization_name);
        TextView eventCategory = mView.findViewById(R.id.card_category);
        TextView eventTitle = mView.findViewById(R.id.card_event_title);
        TextView eventDate = mView.findViewById(R.id.card_event_date);
        DismissibleImageView eventFlyer =  (DismissibleImageView)mView.findViewById(R.id.card_event_flyer);
        CardView mCardView = mView.findViewById(R.id.cardview);

        //set data to views
        eventTitle.setText(title);
        Picasso.with(ctx).load(post_organization_logo).into(organizationLogo);
        organizationName.setText(post_organization_name);
        eventCategory.setText(post_event_category);
        eventDate.setText(post_event_date);
        Picasso.with(ctx).load(flyer).into(eventFlyer);





    }

    private ViewHolder.ClickListener mClickListener;

    //interface to send callbacks
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View  view, int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }


    public void setEventTitle(String title){
        TextView post_title = (TextView)mView.findViewById(R.id.card_event_title);
        post_title.setText(title);
    }

    public void setOrganizationLogo(Context ctx, String organizationLogo){
        ImageView post_organization_logo = (ImageView)mView.findViewById(R.id.card_organization_logo);
        Picasso.with(ctx).load(organizationLogo).into(post_organization_logo);

    }

    public void setOrganizationName(String organizationName){
        TextView post_organization_name = (TextView)mView.findViewById(R.id.card_organization_name);
        post_organization_name.setText(organizationName);
    }
    public void setEventCategory(String eventCategory){
        TextView post_event_category = (TextView)mView.findViewById(R.id.card_category);
        post_event_category.setText(eventCategory);
    }
    public void setEventDate(String eventDate){
        TextView post_event_date = (TextView)mView.findViewById(R.id.card_event_date);
        post_event_date.setText(eventDate);
    }


    public void setEventFlyer(Context ctx, String eventFlyer) {
        DismissibleImageView post_event_flyer = (DismissibleImageView) mView.findViewById(R.id.card_event_flyer);
        Picasso.with(ctx).load(eventFlyer).into(post_event_flyer);
    }

    public void setEventDescription(String eventDescription){
        TextView post_event_date = (TextView)mView.findViewById(R.id.card_event_date);
        post_event_date.setText(eventDescription);

    }

    }
**/

