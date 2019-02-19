package com.hapen.navigationdrawertest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;
//import android.view.Ca
import android.support.v7.app.AppCompatActivity;

import com.dmallcott.dismissibleimageview.DismissibleImageView;
import com.squareup.picasso.Picasso;






import java.util.List;

import com.github.chrisbanes.photoview.PhotoView;
import com.hapen.navigationdrawertest.R;
import com.squareup.picasso.Picasso;



import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private ArrayList<CardItem> mArrayList;
    private Context mContext;
    //ImageView imageView;
    boolean isImageFitToScreen = false;
    private boolean zoomOut =  false;
    private OnItemClickListener mListener;



    public interface OnItemClickListener{
        void onITemClick(int position);
    };

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
        //boolean zoomOut =  false;
    }

    RecyclerAdapter(Context mContext,ArrayList<CardItem> mArrayList) {
        this.mContext=mContext;
        this.mArrayList= mArrayList;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public ImageView organizationLogo;
        public TextView organizationName;
        public TextView eventCategory;
        public TextView eventTitle;
        public TextView eventDate;
        public DismissibleImageView eventFlyer;
        public CardView mCardView;
        private View mView;
        private boolean zoomOut =  false;



        public RecyclerViewHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);

            organizationLogo = itemView.findViewById(R.id.card_organization_logo);
            organizationName = itemView.findViewById(R.id.card_organization_name);
            eventCategory = itemView.findViewById(R.id.card_category);
            eventTitle = itemView.findViewById(R.id.card_event_title);
            eventDate = itemView.findViewById(R.id.card_event_date);
            eventFlyer = itemView.findViewById(R.id.card_event_flyer);
            mCardView = itemView.findViewById(R.id.cardview);
            mView = itemView;

           // DismissibleImageView dismissibleImageView = itemView.findViewById(R.id.card_event_flyer);

            //final ImageView imageView  = itemView.findViewById(R.id.card_event_flyer);
/*
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){

                        if(zoomOut) {
                            //Toast.makeText(getApplicationContext(), "NORMAL SIZE!", Toast.LENGTH_LONG).show();
                            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            imageView.setAdjustViewBounds(true);
                            zoomOut =false;
                        }else{
                            //Toast.makeText(getApplicationContext(), "FULLSCREEN!", Toast.LENGTH_LONG).show();
                            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            zoomOut = true;
                        }

                    }


                }
            });

            */



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

    public RecyclerAdapter(ArrayList<CardItem> arrayList) {
        mArrayList = arrayList;
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
        recyclerViewHolder.organizationLogo.setImageResource(currentItem.getOrganizationLogo());
        recyclerViewHolder.organizationName.setText(currentItem.getOrganizationName());
        recyclerViewHolder.eventCategory.setText(currentItem.getEventCategory());
        recyclerViewHolder.eventTitle.setText(currentItem.getEventTitle());
        recyclerViewHolder.eventTitle.setText(mArrayList.get(i).getEventTitle());
        recyclerViewHolder.eventDate.setText(currentItem.getEventDate());
        recyclerViewHolder.eventFlyer.setImageResource(currentItem.getEventFlyer());
        recyclerViewHolder.eventFlyer.setImageResource(mArrayList.get(i).getEventFlyer());

/*
        recyclerViewHolder.eventFlyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(zoomOut) {
                    //Toast.makeText(getApplicationContext(), "NORMAL SIZE!", Toast.LENGTH_LONG).show();
                    recyclerViewHolder.eventFlyer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    recyclerViewHolder.eventFlyer.setAdjustViewBounds(true);
                    zoomOut =false;
                }else{
                    //Toast.makeText(getApplicationContext(), "FULLSCREEN!", Toast.LENGTH_LONG).show();
                    recyclerViewHolder.eventFlyer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    recyclerViewHolder.eventFlyer.setScaleType(ImageView.ScaleType.FIT_XY);
                    zoomOut = true;
                }
            }
        });
        */


/*
        recyclerViewHolder.eventFlyer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isImageFitToScreen) {
                        isImageFitToScreen=false;
                        recyclerViewHolder.eventFlyer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        recyclerViewHolder.eventFlyer.setAdjustViewBounds(true);
                    }else{
                        isImageFitToScreen=true;
                        recyclerViewHolder.eventFlyer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        recyclerViewHolder.eventFlyer.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                }
            });
*/


            //recyclerViewHolder.setImage(currentItem, mArrayList.get(i).getEventFlyer());



/*
        recyclerViewHolder.eventFlyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);

                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                View mView = inflater.inflate(R.layout.dialog_custom_layout, null);
                PhotoView photoView = mView.findViewById(R.id.imageView);
                photoView.setImageResource(mArrayList.get(i).getEventFlyer());
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
*/

        /*


        recyclerViewHolder.eventFlyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent i = new Intent(context, FragmentHome.class);
                context.startActivity(i);
            }
        });
*/




        recyclerViewHolder.eventTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                Intent mIntent = new Intent(context, DetailActivity.class);
                mIntent.putExtra("Title", mArrayList.get(recyclerViewHolder.getAdapterPosition()).getOrganizationName());
                mIntent.putExtra("Description", mArrayList.get(recyclerViewHolder.getAdapterPosition()).getEventTitle());
                mIntent.putExtra("Image", mArrayList.get(recyclerViewHolder.getAdapterPosition()).getEventFlyer());

                context.startActivity(mIntent);




                //Intent mIntent = new Intent(mContext, DetailActivity.class);
                  //  mIntent.putExtra("Title", mArrayList.get(recyclerViewHolder.getAdapterPosition()).getOrganizationName());
                  //  mIntent.putExtra("Description", mArrayList.get(recyclerViewHolder.getAdapterPosition()).getEventTitle());
                  //  mIntent.putExtra("Image", mArrayList.get(recyclerViewHolder.getAdapterPosition()).getEventFlyer());
                  //  mContext.startActivity(mIntent);

            }
        });






    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }
}




