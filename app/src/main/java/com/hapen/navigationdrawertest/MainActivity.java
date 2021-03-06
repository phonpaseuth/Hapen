package com.hapen.navigationdrawertest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean zoomOut =  false;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<CardItem> mArrayList;
    private RecyclerView mRecyclerView;
    DatabaseReference ref;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Actionbar
        ActionBar actionBar = getSupportActionBar();
        //set title
        //Action Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set the initial state to the home page
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragmentHome()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
            setTitle("Home");
        }

        /*
        // Write to database
        DatabaseReference myRef = database.getReference("message/test");
        myRef.setValue("Hello, Hell!");

        // Read from database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Database error: " + databaseError);
            }
        });
        */
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);

        //MenuItem menuItem = menu.findItem(R.id.search);
        //SearchView searchView = (SearchView) menuItem.getActionView();
        //searchView.setOnQueryTextListener(this);


        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.main, menu);

        ImageView profile = (ImageView) findViewById(R.id.PP);
        Glide.with(this).load(user.getPhotoUrl()).into(profile);

        // Set the current user's email to the left panel
        TextView useremail = (TextView) findViewById(R.id.nav_email);
        useremail.setText(user.getEmail());

        TextView name = (TextView) findViewById(R.id.navfullname);
        name.setText(user.getDisplayName());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragmentHome()).commit();
            setTitle("Home");
        } else if (id == R.id.nav_discover) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragmentDiscovery()).commit();
            setTitle("Discover");
        } else if (id == R.id.nav_saved) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragmentSaved()).commit();
            setTitle("Saved");


        } else if (id == R.id.nav_organizations) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragmentOrganizations()).commit();
            setTitle("Organizations");
        } else if (id == R.id.nav_categories) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragmentCategories()).commit();
            setTitle("Categories");
        } else if (id == R.id.nav_upload) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragmentUpload()).commit();
            setTitle("Upload");
        } else if (id == R.id.nav_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragmentProfile()).commit();
            setTitle("Profile");
        } else if (id == R.id.nav_log_out) {

            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this,StartActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}



