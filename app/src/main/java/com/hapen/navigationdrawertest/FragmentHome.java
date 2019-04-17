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


public class FragmentHome extends Fragment {


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


        View v = inflater.inflate(R.layout.fragment_home, container, false);
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






 /**
public class FragmentHome extends Fragment {


//public class FragmentHome extends Fragment {


    RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    //private RecyclerView mAdapter;
    LinearLayoutManager mLayoutManager;
    private ArrayList<CardItem> cardItem;
    private ArrayList<String> followingList;
    DatabaseReference ref;
    ProgressDialog progress;
    //RecyclerAdapter cc;
    ViewHolder cc;
    Activity context;
    View v;
    private ArrayList<CardItem> mArrayList = new ArrayList<>();
    private ArrayList<String> mDatakey = new ArrayList<>();
    DatabaseReference mRef;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseRecyclerAdapter<CardItem, ViewHolder> FirebaseRecyclerAdapter;
    FirebaseRecyclerOptions<CardItem> options;
    SharedPreferences mSharedPref; //for saving sort settings

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);


        return v;
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;

        //init();
        // init
        cardItem = new ArrayList<>();
        mRecyclerView = v.findViewById(R.id.recycler_home);
        //mRecyclerView.setHasFixedSize(true);


        //cc = new RecyclerAdapter(mArrayList);

        //cc = new RecyclerAdapter(getContext(), cardItem);

        //cc = new RecyclerAdapter(mArrayList);
        //mRecyclerView.setAdapter(cc);


        //set adapter to recyclerview
        //mRecyclerView.setAdapter(firebaseRecyclerAdapter);


        //send Query to FirebaseDatabase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Post");

        options = new FirebaseRecyclerOptions.Builder<CardItem>().setQuery(mRef, CardItem.class).build();


        FirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CardItem, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull CardItem model) {

                holder.setDetails(getContext(), model.getEventTitle(), model.getOrganizationLogo(), model.getOrganizationName()
                        , model.getEventCategory(), model.getEventDate(), model.getEventFlyer());
            }


            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                //ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);

                ViewHolder viewHolder = new ViewHolder(itemView);
                //item click listener


                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        //Views
                        ImageView organizationLogo = view.findViewById(R.id.card_organization_logo);
                        TextView organizationName = view.findViewById(R.id.card_organization_name);
                        TextView eventCategory = view.findViewById(R.id.card_category);
                        TextView eventTitle = view.findViewById(R.id.card_event_title);
                        TextView eventDate = view.findViewById(R.id.card_event_date);
                        DismissibleImageView eventFlyer = view.findViewById(R.id.card_event_flyer);
                        //TextView eventDescription = view.findViewById(R.id.card_event_desc);
                        //mCardView = view.findViewById(R.id.cardview);

                        //get data from views
                        String mOrganizationName = organizationName.getText().toString();
                        //String mEventDescription = eventDecription.getText().toString();
                        String mEventCategory = eventCategory.getText().toString();
                        String mEventTitle = eventTitle.getText().toString();
                        String mEventDate = eventDate.getText().toString();
                        Drawable mEventFlyer = eventFlyer.getDrawable();
                        Bitmap mBitmap = ((BitmapDrawable) mEventFlyer).getBitmap();
                        Drawable mOrganizationLog = organizationLogo.getDrawable();
                        Bitmap mBitmap2 = ((BitmapDrawable) mOrganizationLog).getBitmap();


                        //pass this data to new activity
                        Intent intent = new Intent(view.getContext(), DetailActivity.class);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        mBitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                        byte[] bytes = stream.toByteArray();
                        intent.putExtra("Image", bytes); //put bitmap image as array of bytes
                        intent.putExtra("Title", mEventTitle); // put title
                        intent.putExtra("Organization Name", mOrganizationName); // put title
                        intent.putExtra("Category", mEventCategory); // put title
                        intent.putExtra("Date", mEventDate); //put description
                        mBitmap2.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                        intent.putExtra("Organization Logo", bytes); //put bitmap image as array of bytes
                        //intent.putExtra("Description", mArrayList.get(viewHolder.getAdapterPosition()).getEventDescription());

                        startActivity(intent);

                    }


                    public void onItemLongClick(View view, int position) {
                        //TODO do your own implementaion on long item click
                    }
                });

                return viewHolder;
            }


        };
        FirebaseRecyclerAdapter.notifyDataSetChanged();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        FirebaseRecyclerAdapter.startListening();

        //set adapter to recyclerview
        mRecyclerView.setAdapter(FirebaseRecyclerAdapter);
        //loaddata
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();


        mSharedPref = this.getActivity().getSharedPreferences("SortSettings", Context.MODE_PRIVATE);
        String mSorting = mSharedPref.getString("Sort", "newest"); //where if no settingsis selected newest will be default

        if (mSorting.equals("newest")) {
            mLayoutManager = new LinearLayoutManager(context);
            //this will load the items from bottom means newest first
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
        } else if (mSorting.equals("oldest")) {
            mLayoutManager = new LinearLayoutManager(context);
            //this will load the items from bottom means oldest first
            mLayoutManager.setReverseLayout(false);
            mLayoutManager.setStackFromEnd(false);
        }


        //Spinners
        // Categories spinner
        Spinner spinner1 = (Spinner) v.findViewById(R.id.spinner_categories);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
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
        Spinner spinner2 = (Spinner) v.findViewById(R.id.spinner_organizations);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this.getActivity(), R.array.spinner_organizations,
                android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner2.setAdapter(adapter2);


    }


    private void init() {

        mRecyclerView = v.findViewById(R.id.recycler_home);
        mRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        cardItem = new ArrayList<>();
        //cc = new RecyclerAdapter(mArrayList);

        //cc = new RecyclerAdapter(getContext(), cardItem);

        //cc = new RecyclerAdapter(mArrayList);
        //mRecyclerView.setAdapter(cc);


        //set adapter to recyclerview
        //mRecyclerView.setAdapter(firebaseRecyclerAdapter);

        //send Query to FirebaseDatabase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Post");
        //DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Post");


    }


    private void firebaseSearch(final String searchText) {

        //convert string entered in SearchView to lowercase

        String query = searchText.toLowerCase();

        //Query firebaseSearchQuery = mRef.orderByChild("eventTitle").equalTo(query);
        //
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //mRef = mFirebaseDatabase.getReference("Post");
        mRef = mFirebaseDatabase.getReference().child("Post");
        Query firebaseSearchQuery = mRef.orderByChild("organizationName").startAt(query).endAt(query + "\uf8ff");
        //Query firebaseSearchQuery = mRef.orderByKey().startAt(query).endAt(query + "\uf8ff");;

        options = new FirebaseRecyclerOptions.Builder<CardItem>().setQuery(firebaseSearchQuery, CardItem.class).build();


        FirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CardItem, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull CardItem model) {

                holder.setDetails(getActivity().getApplicationContext(), model.getEventTitle(), model.getOrganizationLogo(), model.getOrganizationName()
                        , model.getEventCategory(), model.getEventDate(), model.getEventFlyer());
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                //ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);

                ViewHolder viewHolder = new ViewHolder(itemView);


                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        //Views
                        ImageView organizationLogo = view.findViewById(R.id.card_organization_logo);
                        TextView organizationName = view.findViewById(R.id.card_organization_name);
                        TextView eventCategory = view.findViewById(R.id.card_category);
                        TextView eventTitle = view.findViewById(R.id.card_event_title);
                        TextView eventDate = view.findViewById(R.id.card_event_date);
                        DismissibleImageView eventFlyer = view.findViewById(R.id.card_event_flyer);
                        //TextView eventDecription = view.findViewById(R.id.card_event_d);
                        //mCardView = view.findViewById(R.id.cardview);

                        //get data from views
                        String mOrganizationName = organizationName.getText().toString();
                        //String mEventDescription = eventDecription.getText().toString();
                        String mEventCategory = eventCategory.getText().toString();
                        String mEventTitle = eventTitle.getText().toString();
                        String mEventDate = eventDate.getText().toString();
                        Drawable mEventFlyer = eventFlyer.getDrawable();
                        Bitmap mBitmap = ((BitmapDrawable) mEventFlyer).getBitmap();
                        Drawable mOrganizationLog = organizationLogo.getDrawable();
                        Bitmap mBitmap2 = ((BitmapDrawable) mOrganizationLog).getBitmap();


                        //pass this data to new activity
                        //Intent intent = new Intent(context, DetailActivity.class);
                        Intent intent = new Intent(view.getContext(), DetailActivity.class);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        mBitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                        byte[] bytes = stream.toByteArray();
                        intent.putExtra("Image", bytes); //put bitmap image as array of bytes
                        intent.putExtra("Title", mEventTitle); // put title
                        intent.putExtra("Organization Name", mOrganizationName); // put title
                        intent.putExtra("Category", mEventCategory); // put title
                        intent.putExtra("Date", mEventDate); //put description
                        mBitmap2.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                        intent.putExtra("Organization Logo", bytes); //put bitmap image as array of bytes

                        startActivity(intent);

                    }

                    public void onItemLongClick(View view, int position) {
                        //TODO do your own implementaion on long item click
                    }
                });

                return viewHolder;
            }


        };
        FirebaseRecyclerAdapter.notifyDataSetChanged();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        FirebaseRecyclerAdapter.startListening();

        //set adapter to recyclerview
        mRecyclerView.setAdapter(FirebaseRecyclerAdapter);
    }

    public void onStart() {
        super.onStart();

        if (FirebaseRecyclerAdapter != null) {
            FirebaseRecyclerAdapter.startListening();
        }


    }


    //SEARCH VIEW
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        //final SearchView searchView = (SearchView) searchItem.getActionView();
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //SearchView searchView = (SearchView) searchItem.getActionView();
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                //((RecyclerAdapter)mRecyclerView.getAdapter()).getFilter().filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //newText = searchView.getQuery().toString();

                //String query = newText.toLowerCase();
                firebaseSearch(newText);


                return false;
            }
        });
        //return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //handle other action bar item clicks here
        if (id == R.id.action_sort) {
            //display alert dialog to choose sorting
            showSortDialog();
            return true;
        }

            return super.onOptionsItemSelected(item);
    }


        private void showSortDialog () {
            //options to display in dialog
            String[] sortOptions = {" Newest", " Oldest"};
            //create alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Sort by") //set title
                    .setIcon(R.drawable.ic_action_sort) //set icon
                    .setItems(sortOptions, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position of the selected item
                            // 0 means "Newest" and 1 means "oldest"
                            if (which == 0) {
                                //sort by newest
                                //Edit our shared preferences
                                SharedPreferences.Editor editor = mSharedPref.edit();
                                editor.putString("Sort", "newest"); //where 'Sort' is key & 'newest' is value
                                editor.apply(); // apply/save the value in our shared preferences
                                getActivity().recreate(); //restart activity to take effect
                            } else if (which == 1) {
                                {
                                    //sort by oldest
                                    //Edit our shared preferences
                                    SharedPreferences.Editor editor = mSharedPref.edit();
                                    editor.putString("Sort", "oldest"); //where 'Sort' is key & 'oldest' is value
                                    editor.apply(); // apply/save the value in our shared preferences
                                    getActivity().recreate(); //restart activity to take effect
                                }
                            }
                        }
                    });
            builder.show();
        }


    }

    **/


/**
public class FragmentHome extends Fragment {


//public class FragmentHome extends Fragment {



    RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    //private RecyclerView mAdapter;
    LinearLayoutManager mLayoutManager;
    private ArrayList<CardItem> cardItem;
    private ArrayList<String> followingList;
    DatabaseReference ref;
    ProgressDialog progress;
    //RecyclerAdapter cc;
    ViewHolder cc;
    Activity context;
    View v;
    private ArrayList<CardItem> mArrayList = new ArrayList<>();
    private ArrayList<String> mDatakey = new ArrayList<>();
    DatabaseReference mRef;
    FirebaseDatabase mFirebaseDatabase;


    /**********
    @Override
    public void onStart() {
        super.onStart();

        //Query firebaseSearchQuery = mRef.orderByChild("eventTitle").startAt(query).endAt(query + "\uf8ff");



        //DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Post");
        FirebaseRecyclerAdapter<CardItem, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<CardItem, ViewHolder>(
                        CardItem.class,
                        R.layout.card_item,
                        ViewHolder.class,
                        mRef
                ){
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, CardItem model, int position) {
                        viewHolder.setDetails(context.getApplicationContext(), model.getEventTitle(), model.getOrganizationLogo(), model.getOrganizationName()
                                ,model.getEventCategory(), model.getEventDate(),model.getEventFlyer());

                    }
                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                //Views
                                ImageView organizationLogo = view.findViewById(R.id.card_organization_logo);
                                TextView organizationName = view.findViewById(R.id.card_organization_name);
                                TextView eventCategory = view.findViewById(R.id.card_category);
                                TextView eventTitle = view.findViewById(R.id.card_event_title);
                                TextView eventDate = view.findViewById(R.id.card_event_date);
                                DismissibleImageView eventFlyer = view.findViewById(R.id.card_event_flyer);
                                //TextView eventDecription = view.findViewById(R.id.card_event_d);
                                //mCardView = view.findViewById(R.id.cardview);

                                //get data from views
                                String mOrganizationName = organizationName.getText().toString();
                                //String mEventDescription = eventDecription.getText().toString();
                                String mEventCategory = eventCategory.getText().toString();
                                String mEventTitile = eventTitle.getText().toString();
                                String mEventDate = eventDate.getText().toString();
                                Drawable mEventFlyer = eventFlyer.getDrawable();
                                Bitmap mBitmap = ((BitmapDrawable) mEventFlyer).getBitmap();
                                Drawable mOrganizationLog = organizationLogo.getDrawable();
                                Bitmap mBitmap2 = ((BitmapDrawable) mOrganizationLog).getBitmap();




                                //pass this data to new activity
                                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] bytes = stream.toByteArray();
                                intent.putExtra("Image", bytes); //put bitmap image as array of bytes
                                intent.putExtra("Title", mEventTitile); // put title
                                intent.putExtra("Organization Name", mOrganizationName); // put title
                                intent.putExtra("Category", mEventCategory); // put title
                                intent.putExtra("Date", mEventDate); //put description
                                mBitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                intent.putExtra("Organization Logo", bytes); //put bitmap image as array of bytes

                                startActivity(intent);

                            }


                            public void onItemLongClick(View view, int position) {
                                //TODO do your own implementaion on long item click
                            }
                        });

                        //progress.dismiss();
                        return viewHolder;
                    }

                };

        //set adapter to recyclerview
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    ************/
/**

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);





        return v;
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v=view;

        //init();
        // init
        cardItem = new ArrayList<>();
        mRecyclerView = v.findViewById(R.id.recycler_home);
        //mRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        //cc = new RecyclerAdapter(mArrayList);

        //cc = new RecyclerAdapter(getContext(), cardItem);

        //cc = new RecyclerAdapter(mArrayList);
        //mRecyclerView.setAdapter(cc);


        //set adapter to recyclerview
        //mRecyclerView.setAdapter(firebaseRecyclerAdapter);

        //send Query to FirebaseDatabase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Post");
        //DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Post");


        //progress = new ProgressDialog(getActivity());
        //progress.setTitle("Loading");
        //progress.setMessage("Syncing");
        //progress.setCancelable(false);
        //progress.show();
        //loaddata();
        // loaddata
        //DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Post");
        //Query firebaseSearchQuery = mRef.orderByChild("eventTitle");
        FirebaseRecyclerAdapter<CardItem, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<CardItem, ViewHolder>(
                        CardItem.class,
                        R.layout.card_item,
                        ViewHolder.class,
                        mRef
                )

                {
                    @Override
                    protected void populateViewHolder(final ViewHolder viewHolder, CardItem model, int position) {

                        viewHolder.setDetails(getContext(), model.getEventTitle(), model.getOrganizationLogo(), model.getOrganizationName()
                                ,model.getEventCategory(), model.getEventDate(),model.getEventFlyer());


/***********

                        mRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //cardItem.clear();


                                if (dataSnapshot.exists()){
                                    final String eventTitle = dataSnapshot.child("eventTitle").getValue().toString();
                                    final String organizationName = dataSnapshot.child("organizationName").getValue().toString();
                                    final String eventDate = dataSnapshot.child("eventDate").getValue().toString();
                                    final String eventCategory = dataSnapshot.child("eventCategory").getValue().toString();
                                    final String eventFlyer = dataSnapshot.child("eventDate").getValue().toString();
                                    final String organizationLogo = dataSnapshot.child("eventCategory").getValue().toString();


                                    viewHolder.setEventTitle(eventTitle);
                                    viewHolder.setOrganizationName(organizationName);
                                    viewHolder.setEventDate(eventDate);
                                    viewHolder.setEventCategory(eventCategory);
                                    viewHolder.setEventFlyer(getContext(), eventFlyer);
                                    viewHolder.setOrganizationLogo(getContext(), organizationLogo);

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
 *************/
/**

                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                //Views
                                ImageView organizationLogo = view.findViewById(R.id.card_organization_logo);
                                TextView organizationName = view.findViewById(R.id.card_organization_name);
                                TextView eventCategory = view.findViewById(R.id.card_category);
                                TextView eventTitle = view.findViewById(R.id.card_event_title);
                                TextView eventDate = view.findViewById(R.id.card_event_date);
                                DismissibleImageView eventFlyer = view.findViewById(R.id.card_event_flyer);
                                //TextView eventDescription = view.findViewById(R.id.card_event_desc);
                                //mCardView = view.findViewById(R.id.cardview);

                                //get data from views
                                String mOrganizationName = organizationName.getText().toString();
                                //String mEventDescription = eventDecription.getText().toString();
                                String mEventCategory = eventCategory.getText().toString();
                                String mEventTitle = eventTitle.getText().toString();
                                String mEventDate = eventDate.getText().toString();
                                Drawable mEventFlyer = eventFlyer.getDrawable();
                                Bitmap mBitmap = ((BitmapDrawable) mEventFlyer).getBitmap();
                                Drawable mOrganizationLog = organizationLogo.getDrawable();
                                Bitmap mBitmap2 = ((BitmapDrawable) mOrganizationLog).getBitmap();




                                //pass this data to new activity
                                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                mBitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                                byte[] bytes = stream.toByteArray();
                                intent.putExtra("Image", bytes); //put bitmap image as array of bytes
                                intent.putExtra("Title", mEventTitle); // put title
                                intent.putExtra("Organization Name", mOrganizationName); // put title
                                intent.putExtra("Category", mEventCategory); // put title
                                intent.putExtra("Date", mEventDate); //put description
                                mBitmap2.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                                intent.putExtra("Organization Logo", bytes); //put bitmap image as array of bytes
                                //intent.putExtra("Description", mArrayList.get(viewHolder.getAdapterPosition()).getEventDescription());

                                startActivity(intent);

                            }



                            public void onItemLongClick(View view, int position) {
                                //TODO do your own implementaion on long item click
                            }
                        });

                        return viewHolder;
                    }



                };
        firebaseRecyclerAdapter.notifyDataSetChanged();

        //set adapter to recyclerview
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        //loaddata




        //Spinners
        // Categories spinner
        Spinner spinner1 = (Spinner) v.findViewById(R.id.spinner_categories);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
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
        Spinner spinner2 = (Spinner) v.findViewById(R.id.spinner_organizations);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this.getActivity(), R.array.spinner_organizations,
                android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner2.setAdapter(adapter2);







    }






    private void init() {

        mRecyclerView = v.findViewById(R.id.recycler_home);
        mRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        cardItem = new ArrayList<>();
        //cc = new RecyclerAdapter(mArrayList);

        //cc = new RecyclerAdapter(getContext(), cardItem);

        //cc = new RecyclerAdapter(mArrayList);
        //mRecyclerView.setAdapter(cc);


        //set adapter to recyclerview
        //mRecyclerView.setAdapter(firebaseRecyclerAdapter);

        //send Query to FirebaseDatabase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Post");
        //DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Post");


    }



    private void loaddata(){

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Post");
        Query firebaseSearchQuery = mRef.orderByChild("eventTitle");
        FirebaseRecyclerAdapter<CardItem, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<CardItem, ViewHolder>(
                        CardItem.class,
                        R.layout.card_item,
                        ViewHolder.class,
                        firebaseSearchQuery
                ){
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, CardItem model, int position) {
                        viewHolder.setDetails(getContext(), model.getEventTitle(), model.getOrganizationLogo(), model.getOrganizationName()
                                ,model.getEventCategory(), model.getEventDate(),model.getEventFlyer());

                    }

                };


        //set adapter to recyclerview
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);


    }
/*********
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Post");

        FirebaseRecyclerAdapter<CardItem, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<CardItem, ViewHolder>(
                        CardItem.class,
                        R.layout.card_item,
                        ViewHolder.class,
                        mRef
                ){
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, CardItem model, int position) {
                        viewHolder.setDetails(context.getApplicationContext(), model.getEventTitle(), model.getOrganizationLogo(), model.getOrganizationName()
                                ,model.getEventCategory(), model.getEventDate(),model.getEventFlyer());

                    }
                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                //Views
                                ImageView organizationLogo = view.findViewById(R.id.card_organization_logo);
                                TextView organizationName = view.findViewById(R.id.card_organization_name);
                                TextView eventCategory = view.findViewById(R.id.card_category);
                                TextView eventTitle = view.findViewById(R.id.card_event_title);
                                TextView eventDate = view.findViewById(R.id.card_event_date);
                                DismissibleImageView eventFlyer = view.findViewById(R.id.card_event_flyer);
                                //TextView eventDecription = view.findViewById(R.id.card_event_d);
                                //mCardView = view.findViewById(R.id.cardview);

                                //get data from views
                                String mOrganizationName = organizationName.getText().toString();
                                //String mEventDescription = eventDecription.getText().toString();
                                String mEventCategory = eventCategory.getText().toString();
                                String mEventTitile = eventTitle.getText().toString();
                                String mEventDate = eventDate.getText().toString();
                                Drawable mEventFlyer = eventFlyer.getDrawable();
                                Bitmap mBitmap = ((BitmapDrawable) mEventFlyer).getBitmap();
                                Drawable mOrganizationLog = organizationLogo.getDrawable();
                                Bitmap mBitmap2 = ((BitmapDrawable) mOrganizationLog).getBitmap();




                                //pass this data to new activity
                                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] bytes = stream.toByteArray();
                                intent.putExtra("Image", bytes); //put bitmap image as array of bytes
                                intent.putExtra("Title", mEventTitile); // put title
                                intent.putExtra("Organization Name", mOrganizationName); // put title
                                intent.putExtra("Category", mEventCategory); // put title
                                intent.putExtra("Date", mEventDate); //put description
                                mBitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                intent.putExtra("Organization Logo", bytes); //put bitmap image as array of bytes

                                startActivity(intent);

                            }


                            public void onItemLongClick(View view, int position) {
                                //TODO do your own implementaion on long item click
                            }
                        });

                        //progress.dismiss();
                        return viewHolder;
                    }

                };

        //set adapter to recyclerview
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);





        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cardItem.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    CardItem post = snapshot.getValue(CardItem.class);
                    cardItem.add(post);


                }

                //cc.\\\notifyDataSetChanged();
                //progress.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

*********/



/**

    private void firebaseSearch(final String searchText) {



        //DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Post");

        //convert string entered in SearchView to lowercase

        String query = searchText.toLowerCase();

        //Query firebaseSearchQuery = mRef.orderByChild("eventTitle").equalTo(query);
        //
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //mRef = mFirebaseDatabase.getReference("Post");
        mRef = mFirebaseDatabase.getReference().child("Post");
        Query firebaseSearchQuery = mRef.orderByChild("organizationName").startAt(query).endAt(query + "\uf8ff");
        //Query firebaseSearchQuery = mRef.orderByKey().startAt(query).endAt(query + "\uf8ff");;



        FirebaseRecyclerAdapter<CardItem, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<CardItem, ViewHolder>(
                        CardItem.class,
                        R.layout.card_item,
                        ViewHolder.class,
                        firebaseSearchQuery

                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, CardItem model, int position) {
                        viewHolder.setDetails(getContext(), model.getEventTitle(), model.getOrganizationLogo(), model.getOrganizationName()
                                ,model.getEventCategory(), model.getEventDate(),model.getEventFlyer());

                    }



                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                //Views
                                ImageView organizationLogo = view.findViewById(R.id.card_organization_logo);
                                TextView organizationName = view.findViewById(R.id.card_organization_name);
                                TextView eventCategory = view.findViewById(R.id.card_category);
                                TextView eventTitle = view.findViewById(R.id.card_event_title);
                                TextView eventDate = view.findViewById(R.id.card_event_date);
                                DismissibleImageView eventFlyer = view.findViewById(R.id.card_event_flyer);
                                //TextView eventDecription = view.findViewById(R.id.card_event_d);
                                //mCardView = view.findViewById(R.id.cardview);

                                //get data from views
                                String mOrganizationName = organizationName.getText().toString();
                                //String mEventDescription = eventDecription.getText().toString();
                                String mEventCategory = eventCategory.getText().toString();
                                String mEventTitle = eventTitle.getText().toString();
                                String mEventDate = eventDate.getText().toString();
                                Drawable mEventFlyer = eventFlyer.getDrawable();
                                Bitmap mBitmap = ((BitmapDrawable) mEventFlyer).getBitmap();
                                Drawable mOrganizationLog = organizationLogo.getDrawable();
                                Bitmap mBitmap2 = ((BitmapDrawable) mOrganizationLog).getBitmap();




                                //pass this data to new activity
                                //Intent intent = new Intent(context, DetailActivity.class);
                                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                mBitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                                byte[] bytes = stream.toByteArray();
                                intent.putExtra("Image", bytes); //put bitmap image as array of bytes
                                intent.putExtra("Title", mEventTitle); // put title
                                intent.putExtra("Organization Name", mOrganizationName); // put title
                                intent.putExtra("Category", mEventCategory); // put title
                                intent.putExtra("Date", mEventDate); //put description
                                mBitmap2.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                                intent.putExtra("Organization Logo", bytes); //put bitmap image as array of bytes

                                startActivity(intent);

                            }

                            public void onItemLongClick(View view, int position) {
                                //TODO do your own implementaion on long item click
                            }
                        });

                        return viewHolder;
                    }


                };
        firebaseRecyclerAdapter.notifyDataSetChanged();
        //set adapter to recyclerview
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }



    //SEARCH VIEW
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        //final SearchView searchView = (SearchView) searchItem.getActionView();
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //SearchView searchView = (SearchView) searchItem.getActionView();
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                //((RecyclerAdapter)mRecyclerView.getAdapter()).getFilter().filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //newText = searchView.getQuery().toString();

                //String query = newText.toLowerCase();
                firebaseSearch(newText);



                //Query firebaseSearchQuery = mRef.orderByChild("eventTitle").startAt(query).endAt("~");
                //

                /*********
                Query firebaseSearchQuery = mRef.orderByChild("eventTitle").startAt(query).endAt(query + "\uf8ff");
                //Query firebaseSearchQuery = mRef.orderByKey().startAt(query).endAt(query + "\uf8ff");
                //firebaseSearch(newText);



                FirebaseRecyclerAdapter<CardItem, ViewHolder> firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<CardItem, ViewHolder>(
                                CardItem.class,
                                R.layout.card_item,
                                ViewHolder.class,
                                firebaseSearchQuery

                        ){



                    //cov1 = cov1[~np.isnan(cov1)]
                            @Override
                            protected void populateViewHolder(final ViewHolder viewHolder, final CardItem model, int position) {



                                mRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            final String title = dataSnapshot.child("eventTitle").getValue(String.class);
                                            final String org = dataSnapshot.child("organizationName").getValue(String.class);
                                            final String category = dataSnapshot.child("eventCategory").getValue(String.class);
                                            final String date = dataSnapshot.child("eventDate").getValue(String.class);
                                            final String orglogo = dataSnapshot.child("organizationLogo").getValue(String.class);
                                            final String flyer = dataSnapshot.child("eventFlyer").getValue(String.class);

                                            //final String title = dataSnapshot.child("eventTitle").getValue(String.class).toString();
                                            //final String org = dataSnapshot.child("organizationName").getValue().toString();
                                            //final String category = dataSnapshot.child("eventCategory").getValue().toString();
                                            //final String date = dataSnapshot.child("eventDate").getValue().toString();
                                            //final String orglogo = dataSnapshot.child("organizationLogo").getValue().toString();
                                            //final String flyer = dataSnapshot.child("eventFlyer").getValue().toString();

                                            viewHolder.setDetails(getContext(),title, orglogo,org
                                                    ,category, date,flyer);


                                        }


                                        //cc.\\\notifyDataSetChanged();
                                        //progress.dismiss();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }





                        };
                firebaseRecyclerAdapter.notifyDataSetChanged();
                //set adapter to recyclerview
                mRecyclerView.setAdapter(firebaseRecyclerAdapter);


********/
/**
            //SEARCH VIEW



                            //firebaseUserSearch (searchText);

                //firebaseSearch(newText);
                //mAdapter.getFilter().filter(newText);
                //((RecyclerAdapter)mRecyclerView.getAdapter()).getFilter().filter(newText);
                //newText = searchView.getQuery().toString();
                //((RecyclerAdapter)mRecyclerView.getAdapter()).getFilter().filter(newText);

                //firebaseUserSearch (newText);


                return false;
            }
        });
        //return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }






            }

**/

/**


public class FragmentHome extends Fragment {


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









    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_home, container, false);
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


        //Spinners

        // Categories spinner
        Spinner spinner1 = (Spinner) v.findViewById(R.id.spinner_categories);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
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
        Spinner spinner2 = (Spinner) v.findViewById(R.id.spinner_organizations);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this.getActivity(), R.array.spinner_organizations,
                android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner2.setAdapter(adapter2);







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

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Post");
        /*********
        FirebaseRecyclerAdapter<CardItem, RecyclerAdapter.RecyclerViewHolder> firebaseRecyclerAdapter =



         new FirebaseRecyclerAdapter<CardItem, RecyclerAdapter.RecyclerViewHolder>(
         CardItem.class,
         R.layout.card_item,
         RecyclerAdapter.RecyclerViewHolder.class,
         mRef
         ){
        @Override
        protected void populateViewHolder(RecyclerAdapter.RecyclerViewHolder viewHolder, CardItem model, int position) {
        viewHolder.setOrganizationLogo(getContext(), model.getOrganizationLogo());
        viewHolder.setOrganizationName(model.getOrganizationName());
        viewHolder.setEventTitle(model.getEventTitle());
        viewHolder.setEventDate(model.getEventDate());
        viewHolder.setEventCategory(model.getEventCategory());
        viewHolder.setEventFlyer(getContext(), model.getEventFlyer());
        //.set

        }
        @Override
        public RecyclerAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerAdapter.RecyclerViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);


        viewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        Context context = view.getContext();

        //Views
        ImageView organizationLogo = view.findViewById(R.id.card_organization_logo);
        TextView organizationName = view.findViewById(R.id.card_organization_name);
        TextView eventCategory = view.findViewById(R.id.card_category);
        TextView eventTitle = view.findViewById(R.id.card_event_title);
        TextView eventDate = view.findViewById(R.id.card_event_date);
        ImageView eventFlyer = view.findViewById(R.id.card_event_flyer);
        //TextView eventDecription = view.findViewById(R.id.card_event_d);
        //mCardView = view.findViewById(R.id.cardview);

        //get data from views
        String mOrganizationName = organizationName.getText().toString();
        //String mEventDescription = eventDecription.getText().toString();
        String mEventCategory = eventCategory.getText().toString();
        String mEventTitile = eventTitle.getText().toString();
        String mEventDate = eventDate.getText().toString();
        Drawable mEventFlyer = eventFlyer.getDrawable();
        Bitmap mBitmap = ((BitmapDrawable) mEventFlyer).getBitmap();
        Drawable mOrganizationLog = organizationLogo.getDrawable();
        Bitmap mBitmap2 = ((BitmapDrawable) mOrganizationLog).getBitmap();




        //pass this data to new activity
        Intent intent = new Intent(context, DetailActivity.class);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        intent.putExtra("Image", bytes); //put bitmap image as array of bytes
        intent.putExtra("Title", mEventTitile); // put title
        intent.putExtra("Organization Name", mOrganizationName); // put title
        intent.putExtra("Category", mEventCategory); // put title
        intent.putExtra("Date", mEventDate); //put description
        mBitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream);
        intent.putExtra("Organization Logo", bytes); //put bitmap image as array of bytes

        startActivity(intent);
        progress.dismiss();

        }


        public void onItemLongClick(View view, int position) {
        //TODO do your own implementaion on long item click
        }
        });

        progress.dismiss();
        return viewHolder;
        }

        };

         //set adapter to recyclerview
         mRecyclerView.setAdapter(firebaseRecyclerAdapter);

**********/

/**
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




    private void firebaseSearch(String searchText) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Post");

        //convert string entered in SearchView to lowercase
        String query = searchText.toLowerCase();

        Query firebaseSearchQuery = mRef.orderByChild("eventTitle").startAt(query).endAt(query + "\uf8ff");



        FirebaseRecyclerAdapter<CardItem, RecyclerAdapter.RecyclerViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<CardItem, RecyclerAdapter.RecyclerViewHolder>(
                        CardItem.class,
                        R.layout.card_item,
                        RecyclerAdapter.RecyclerViewHolder.class,
                        firebaseSearchQuery
                ) {
                    @Override
                    protected void populateViewHolder(RecyclerAdapter.RecyclerViewHolder viewHolder, CardItem model, int position) {
                        viewHolder.setOrganizationLogo(getContext(), model.getOrganizationLogo());
                        viewHolder.setOrganizationName(model.getOrganizationName());
                        viewHolder.setEventTitle(model.getEventTitle());
                        viewHolder.setEventDate(model.getEventDate());
                        viewHolder.setEventCategory(model.getEventCategory());
                        viewHolder.setEventFlyer(getContext(), model.getEventFlyer());
                        //.set

                    }

                    @Override
                    public RecyclerAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                        RecyclerAdapter.RecyclerViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Context context = view.getContext();

                                //Views
                                ImageView organizationLogo = view.findViewById(R.id.card_organization_logo);
                                TextView organizationName = view.findViewById(R.id.card_organization_name);
                                TextView eventCategory = view.findViewById(R.id.card_category);
                                TextView eventTitle = view.findViewById(R.id.card_event_title);
                                TextView eventDate = view.findViewById(R.id.card_event_date);
                                ImageView eventFlyer = view.findViewById(R.id.card_event_flyer);
                                //TextView eventDecription = view.findViewById(R.id.card_event_d);
                                //mCardView = view.findViewById(R.id.cardview);

                                //get data from views
                                String mOrganizationName = organizationName.getText().toString();
                                //String mEventDescription = eventDecription.getText().toString();
                                String mEventCategory = eventCategory.getText().toString();
                                String mEventTitle = eventTitle.getText().toString();
                                String mEventDate = eventDate.getText().toString();
                                Drawable mEventFlyer = eventFlyer.getDrawable();
                                Bitmap mBitmap = ((BitmapDrawable) mEventFlyer).getBitmap();
                                Drawable mOrganizationLog = organizationLogo.getDrawable();
                                Bitmap mBitmap2 = ((BitmapDrawable) mOrganizationLog).getBitmap();




                                //pass this data to new activity
                                Intent intent = new Intent(context, DetailActivity.class);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] bytes = stream.toByteArray();
                                intent.putExtra("Image", bytes); //put bitmap image as array of bytes
                                intent.putExtra("Title", mEventTitle); // put title
                                intent.putExtra("Organization Name", mOrganizationName); // put title
                                intent.putExtra("Category", mEventCategory); // put title
                                intent.putExtra("Date", mEventDate); //put description
                                mBitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                intent.putExtra("Organization Logo", bytes); //put bitmap image as array of bytes

                                startActivity(intent);

                            }

                            public void onItemLongClick(View view, int position) {
                                //TODO do your own implementaion on long item click
                            }
                        });

                        return viewHolder;
                    }


                };

        //set adapter to recyclerview
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }



    //SEARCH VIEW
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //firebaseSearch(query);
                //((RecyclerAdapter)mRecyclerView.getAdapter()).getFilter().filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //firebaseSearch(newText);
                //mAdapter.getFilter().filter(newText);
                //((RecyclerAdapter)mRecyclerView.getAdapter()).getFilter().filter(newText);
                //newText = searchView.getQuery().toString();
                //((RecyclerAdapter)mRecyclerView.getAdapter()).getFilter().filter(newText);

                //firebaseSearch (newText);

                String query = newText.toLowerCase();
                Query firebaseSearchQuery = mRef.orderByChild("eventTitle").startAt(query).endAt(query + "\uf8ff");

                mRef.orderByChild("eventTitle").startAt(query).endAt(query + "\uf8ff").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        cardItem.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            CardItem post = snapshot.getValue(CardItem.class);
                            cardItem.add(post);


                        }

                        cc.notifyDataSetChanged();
                        //progress.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




                return true;
            }
        });
    }




}
**/