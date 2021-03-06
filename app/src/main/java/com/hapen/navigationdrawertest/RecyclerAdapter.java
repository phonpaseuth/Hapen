
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
///////////////////////////import com.firebase.ui.database.FirebaseRecyclerOptions;
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






public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> implements Filterable{
//public class RecyclerAdapter extends FirebaseRecyclerAdapter<CardItem, RecyclerViewHolder>{

   // private RecycleItemClick recycleItemClick;
    //private static final String TAG = "PeopleListAdapter";
    private final ArrayList<CardItem> mArrayList;
    private final
    ArrayList<CardItem> mArrayListFull;
    private Context mContext;
    //ImageView imageView;
    boolean isImageFitToScreen = false;
    private boolean zoomOut =  false;
    private OnItemClickListener mListener;
    private Activity context;

    private DetailActivity myadapter;




    public interface OnItemClickListener{
        void onITemClick(int position);
    };






    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
        //boolean zoomOut =  false;
    }

    RecyclerAdapter(Context mContext,ArrayList<CardItem> mArrayList) {
        //super(mContext, R.layout.card_item, mArrayList);
        this.mContext=mContext;
        this.mArrayList= mArrayList;
        mArrayListFull = new ArrayList<>(mArrayList);
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public ImageView organizationLogo;
        public TextView organizationName;
        public TextView eventCategory;
        public TextView eventTitle;
        public TextView eventDate;
        public TextView eventDescription;
        public DismissibleImageView eventFlyer;
        public CardView mCardView;
        //private View mView;
        View mView;
        private boolean zoomOut =  false;
        private ViewPager viewPager;




        public RecyclerViewHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);

            mView = itemView;


            organizationLogo = itemView.findViewById(R.id.card_organization_logo);
            organizationName = itemView.findViewById(R.id.card_organization_name);
            eventCategory = itemView.findViewById(R.id.card_category);
            eventTitle = itemView.findViewById(R.id.card_event_title);
            eventDate = itemView.findViewById(R.id.card_event_date);
            eventFlyer = itemView.findViewById(R.id.card_event_flyer);
            mCardView = itemView.findViewById(R.id.cardview);

            //viewPager = itemView.findViewById(R.id.viewpager);

            //DismissibleImageView dismissibleImageView = itemView.findViewById(R.id.card_event_flyer);

            //final ImageView imageView  = itemView.findViewById(R.id.card_event_flyer)



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


        public void setEventFlyer(Context ctx, String eventFlyer){
            DismissibleImageView post_event_flyer = (DismissibleImageView)mView.findViewById(R.id.card_event_flyer);
            Picasso.with(ctx).load(eventFlyer).into(post_event_flyer);

        }



        public void setImage(Context context, String image){
            DismissibleImageView postImage = (DismissibleImageView) mView.findViewById(R.id.card_event_flyer);
            Picasso.with(context)
                    .load(image)
                    .error(android.R.drawable.stat_notify_error)
                    .placeholder(R.drawable.event_flyer_macafee)
                    .fit()
                    .centerCrop()
                    .into(postImage);
        }

    }

    public RecyclerAdapter(ArrayList<CardItem> mArrayList) {
        this.mArrayList = mArrayList;
        mArrayListFull = new ArrayList<>(mArrayList);
    }

    @NonNull
    @Override
    // Pass layout here
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item, viewGroup,
                false);
        RecyclerViewHolder rvh = new RecyclerViewHolder(v, mListener);

        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder recyclerViewHolder, final int i) {
        CardItem currentItem = mArrayList.get(i);
        //CardItem currentItem = mArrayList.get(i);
        recyclerViewHolder.setEventCategory(currentItem.getEventCategory());
        recyclerViewHolder.setOrganizationName(currentItem.getOrganizationName());
        recyclerViewHolder.setEventCategory(currentItem.getEventCategory());
        recyclerViewHolder.setEventTitle(currentItem.getEventTitle());
        recyclerViewHolder.setEventDate(currentItem.getEventDate());
        recyclerViewHolder.setEventFlyer(mContext.getApplicationContext(), currentItem.getEventFlyer());
        recyclerViewHolder.setOrganizationLogo(mContext.getApplicationContext(), currentItem.getOrganizationLogo());
        //recyclerViewHolder.setE(mContext.getApplicationContext(), currentItem.getOrganizationLogo());



        recyclerViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                // passing info to the detail activity
                Intent mIntent = new Intent(context, DetailActivity.class);
                mIntent.putExtra("Organization Logo", mArrayList.get(recyclerViewHolder.getAdapterPosition()).getOrganizationLogo());
                mIntent.putExtra("Organization Name", mArrayList.get(recyclerViewHolder.getAdapterPosition()).getOrganizationName());
                mIntent.putExtra("Category", mArrayList.get(recyclerViewHolder.getAdapterPosition()).getEventCategory());
                mIntent.putExtra("Title", mArrayList.get(recyclerViewHolder.getAdapterPosition()).getEventTitle());
                mIntent.putExtra("Image", mArrayList.get(recyclerViewHolder.getAdapterPosition()).getEventFlyer());
                mIntent.putExtra("Date", mArrayList.get(recyclerViewHolder.getAdapterPosition()).getEventDate());
                mIntent.putExtra("Description", mArrayList.get(recyclerViewHolder.getAdapterPosition()).getEventDescription());

                context.startActivity(mIntent);




            }
        });








    }


    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    private void firebaseSearch(String searchText){
        Query firebaseSearchQuery ;
    }



    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //final FilterResults results = new FilterResults();
            ArrayList<CardItem> filteredList = new ArrayList<>();
            //ArrayList<CardItem> filteredList = mArrayList;



            //if (mArrayListFull == null) {
              //  mArrayListFull = new ArrayList<>(filteredList);
            //}
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mArrayListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (CardItem item : mArrayListFull) {
                    if (item.getEventTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                    else if (item.getOrganizationName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }

                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;

        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mArrayList.clear();
            //mArrayList.addAll((ArrayList) results.values);
            //mArrayList.addAll((Collection<?extends CardItem>) results.values);
            //mArrayList.addAll((ArrayList) results.values);
            mArrayList.addAll((ArrayList<CardItem>) results.values);

            notifyDataSetChanged();
        }
    };
}


