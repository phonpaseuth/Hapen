package com.hapen.navigationdrawertest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.content.Intent;
import android.view.inputmethod.EditorInfo;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;


import com.dmallcott.dismissibleimageview.DismissibleImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import android.view.MenuInflater;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;




import java.util.ArrayList;
import java.util.Collections;


public class FragmentDiscovery extends Fragment {


//public class FragmentHome extends Fragment {



    RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    //private RecyclerView mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<CardItem> cardItem;
    private ArrayList<String> followingList;
    DatabaseReference ref;
    ProgressDialog progress;
    RecyclerAdapter cc;
    View v;
    private ArrayList<CardItem> mArrayList = new ArrayList<>();
    private ArrayList<String> mDatakey = new ArrayList<>();
    DatabaseReference mRef;
    FirebaseDatabase mFirebaseDatabase;
    SharedPreferences mSharedPref;
    Spinner orgSpinner;








    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_discover, container, false);
        setHasOptionsMenu(true);





        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v=view;

        init();
        progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Syncing");
        progress.setCancelable(false);
        progress.show();
        loaddata();

        mSharedPref = getActivity().getSharedPreferences("SortSettings", Context.MODE_PRIVATE);


        //Spinners

        // Categories spinner
        Spinner spinner1 = (Spinner) v.findViewById(R.id.spinner_categories);


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //String text = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();

                if (position == 0){
                    loaddata();
                }
                else if (position == 1){
                    sortbyevent();
                }
                else if (position == 2){
                    sortbyorganization();
                }
                else {
                    String text = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                this.getActivity(), R.array.spinner_categories,
                android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner1.setAdapter(adapter1);




        // Organization spinner


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fDatabaseRoot = database.getReference();
        fDatabaseRoot.child("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final ArrayList<String> orgNameList = new ArrayList<String>();
                orgNameList.add("All Organizations");
                for (DataSnapshot addressSnapshot: dataSnapshot.getChildren()) {
                    String orgName = addressSnapshot.child("organization").getValue(String.class);
                    if (orgName != null) {
                        orgNameList.add(orgName);
                    }
                }



                Spinner spinner2 = (Spinner) v.findViewById(R.id.spinner_organizations);
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, orgNameList);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinner2.setAdapter(areasAdapter);


            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final Spinner spinner2 = (Spinner) v.findViewById(R.id.spinner_organizations);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {


                if (position == 3 || position == 2 || position == 1 ){

                    String item = spinner2.getSelectedItem().toString();
                    loadOrg(item);
                }
                else {
                    loaddata();
                }
                //String text = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




//WELCOME20-0dae803d





    }



    public void onCreate(@Nullable Bundle savedInstanceState){
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);


    }


    private void init() {

        mRecyclerView = v.findViewById(R.id.recycler_home);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cardItem = new ArrayList<>();
        //cc = new RecyclerAdapter(mArrayList);

        cc = new RecyclerAdapter(getContext(), cardItem);

        //cc = new RecyclerAdapter(mArrayList);
        mRecyclerView.setAdapter(cc);

    }

    private void loaddata(){

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("events");

        //mFirebaseDatabase = FirebaseDatabase.getInstance();
        //mRef = mFirebaseDatabase.getReference("Post");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cardItem.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    CardItem post = snapshot.getValue(CardItem.class);
                    cardItem.add(post);


                }

                cc.notifyDataSetChanged();
                progress.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







    }


    private void firebaseSearch(final String searchText) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("events");

        mRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cardItem.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    CardItem post = snapshot.getValue(CardItem.class);


                    if (post.getEventTitle().toLowerCase().contains(searchText.toLowerCase()) || post.getOrganizationName().toLowerCase().contains(searchText.toLowerCase())){

                        cardItem.add(post);
                    }


                }

                cc = new RecyclerAdapter(getActivity(), cardItem);

                cc.notifyDataSetChanged();
                mRecyclerView.setAdapter(cc);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }



    //SEARCH VIEW
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);

                return true;
            }
        });
    }


    private void sortbyorganization(){

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("events");
        Query firebaseSearchQuery = mRef.orderByChild("organization");
        //Query firebaseSearchQuery = mRef.orderByKey().startAt(query).endAt(query + "\uf8ff");;

        //mFirebaseDatabase = FirebaseDatabase.getInstance();
        //mRef = mFirebaseDatabase.getReference("Post");
        firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cardItem.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    CardItem post = snapshot.getValue(CardItem.class);
                    cardItem.add(post);


                }

                cc.notifyDataSetChanged();
                progress.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void loadOrg(String name){

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("events");
        Query firebaseSearchQuery = mRef.orderByChild("organization").equalTo(name);
        //Query firebaseSearchQuery = mRef.orderByKey().startAt(query).endAt(query + "\uf8ff");;

        //mFirebaseDatabase = FirebaseDatabase.getInstance();
        //mRef = mFirebaseDatabase.getReference("Post");
        firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cardItem.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    CardItem post = snapshot.getValue(CardItem.class);
                    cardItem.add(post);


                }

                cc.notifyDataSetChanged();
                progress.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void sortbyevent(){

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("events");
        Query firebaseSearchQuery = mRef.orderByChild("title");
        //Query firebaseSearchQuery = mRef.orderByKey().startAt(query).endAt(query + "\uf8ff");;

        //mFirebaseDatabase = FirebaseDatabase.getInstance();
        //mRef = mFirebaseDatabase.getReference("Post");
        firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cardItem.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    CardItem post = snapshot.getValue(CardItem.class);
                    cardItem.add(post);


                }

                cc.notifyDataSetChanged();
                progress.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }




}

