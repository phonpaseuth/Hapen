package com.hapen.navigationdrawertest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FragmentProfile extends Fragment {


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_profile, container, false);




        ImageView profile = (ImageView) view.findViewById(R.id.profile);
        Glide.with(this).load(user.getPhotoUrl()).into(profile);

        // Set the current user's email to the left panel
        TextView useremail = (TextView) view.findViewById(R.id.useremail);
        useremail.setText("Email: " +user.getEmail());

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(user.getDisplayName());





        return view;
    }
}
