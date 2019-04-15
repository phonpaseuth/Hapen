package com.hapen.navigationdrawertest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;


public class AdapterOrgs extends RecyclerView.Adapter<AdapterOrgs.MyHolder> {

    Context context;
    List<OrgItem> orgList;



    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate
        View view = LayoutInflater.from(context).inflate(R.layout.row_orgs, viewGroup);


        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {

        String orgImage = orgList.get(i).getImage();
        final String orgName = orgList.get(i).getName1();

        myHolder.mNameTv.setText(orgName);
        try{
            Picasso.with(context).load(orgImage)
                    .placeholder(R.drawable.ic_default_img)
                    .into(myHolder.mProfileTv);
        }
        catch (Exception e){

        }

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+orgName, Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return orgList.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        ImageView mProfileTv;
        TextView mNameTv;



        public MyHolder(@NonNull View itemView){
            super(itemView);

            mProfileTv =  itemView.findViewById(R.id.profileTv);
            mNameTv =  itemView.findViewById(R.id.nameTv);

        }
    }
}
