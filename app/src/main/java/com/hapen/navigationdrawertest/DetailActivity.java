package com.hapen.navigationdrawertest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;

import com.dmallcott.dismissibleimageview.DismissibleImageView;


public class DetailActivity extends AppCompatActivity {


    public DismissibleImageView eventFlyer;
    public ImageView mCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        eventFlyer = findViewById(R.id.card_event_flyer);
        mCardView = findViewById(R.id.cardview);


        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            eventFlyer.setImageResource(mBundle.getInt("Image"));

        }
    }
}
