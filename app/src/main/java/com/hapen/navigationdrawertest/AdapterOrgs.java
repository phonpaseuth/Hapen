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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import android.widget.Button;

import java.util.List;


public class AdapterOrgs extends RecyclerView.Adapter<AdapterOrgs.MyHolder> {

    Context context;
    List<OrgItem> orgList;

    private FirebaseUser firebaseUser;


    public AdapterOrgs(Context context, List<OrgItem> orgList){
        this.context = context;
        this.orgList = orgList;
    }



    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate
        View view = LayoutInflater.from(context).inflate(R.layout.row_orgs, viewGroup, false);


        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, int i) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final OrgItem org = orgList.get(i);

        myHolder.btn_follow.setVisibility(View.VISIBLE);


        final String orgImage = orgList.get(i).getImage();
        final String orgName = orgList.get(i).getName1();
        myHolder.mNameTv.setText(orgName);
        try{
            Picasso.with(context).load(orgImage)
                    .placeholder(R.drawable.ic_default_img)
                    .into(myHolder.mProfileTv);
        }
        catch (Exception e){

        }

        //isFollowing(org.getId(),myHolder.btn_follow);
/**
        if (org.getId().equals(firebaseUser.getUid())){
            myHolder.btn_follow.setVisibility(View.GONE);
        }
 **/
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+orgName, Toast.LENGTH_SHORT).show();

            }
        });


        myHolder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myHolder.btn_follow.getText().toString().equals("follow")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(org.getId()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(org.getId())
                            .child("followers").child(firebaseUser.getUid()).setValue(true);
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(org.getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(org.getId())
                            .child("followers").child(firebaseUser.getUid()).removeValue();


                }
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
        public Button btn_follow;



        public MyHolder(@NonNull View itemView){
            super(itemView);

            mProfileTv =  itemView.findViewById(R.id.profileTv);
            mNameTv =  itemView.findViewById(R.id.nameTv);
            btn_follow = itemView.findViewById(R.id.btn_follow);

        }
    }


    public void isFollowing(final String userid, final Button button){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userid).exists()){
                    button.setText("following");
                }
                else{
                    button.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

//409 -n 347 2667
