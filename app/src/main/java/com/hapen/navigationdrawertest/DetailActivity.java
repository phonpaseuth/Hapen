package com.hapen.navigationdrawertest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;

import com.dmallcott.dismissibleimageview.DismissibleImageView;


public class DetailActivity extends AppCompatActivity {


    public DismissibleImageView eventFlyer;
    public ImageView mCardView;
    public ImageView organizationLogo;
    public TextView organizationName;
    public TextView eventCategory;
    public TextView eventTitle;
    public TextView eventDate;
    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        eventFlyer = findViewById(R.id.card_event_flyer);
        mCardView = findViewById(R.id.cardview);
        eventTitle = findViewById(R.id.card_event_title);
        organizationName = findViewById(R.id.card_organization_name);





        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {

            eventFlyer.setImageResource(mBundle.getInt("Image"));
            //eventTitle.setText(mBundle.getString("Title"));
            //organizationName.setText(mBundle.getInt("Description"));



        }
    }
}
