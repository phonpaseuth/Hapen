package com.hapen.navigationdrawertest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class
FragmentOrganizations extends Fragment {

    RecyclerView recyclerView;
    AdapterOrgs adapterOrgs;
    List<OrgItem> orgList;

    public FragmentOrganizations(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizations, container, false);

        recyclerView = view.findViewById(R.id.orgs_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        orgList = new ArrayList<>();
        //recyclerView.setAdapter(adapterOrgs);
        //cc = new RecyclerAdapter(getContext(), OrgList);


        getAllOrgs();
        

        return view;
    }

    private void getAllOrgs() {

        //FirebaseUser fOrg = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Organization");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orgList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    OrgItem orgItem = ds.getValue(OrgItem.class);

                    orgList.add(orgItem);


                    adapterOrgs = new AdapterOrgs(getActivity(), orgList);
                    recyclerView.setAdapter(adapterOrgs);


                }
                //adapterOrgs = new AdapterOrgs(getActivity(), orgList);
                //cc = new RecyclerAdapter(getActivity(), cardItem);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
