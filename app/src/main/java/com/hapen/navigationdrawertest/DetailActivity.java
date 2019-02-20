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
    public TextView eventDescription;
    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // match the data to the layout's id
        mCardView = findViewById(R.id.cardview);
        organizationLogo = findViewById(R.id.organization_logo);
        organizationName = findViewById(R.id.organization_name);
        eventCategory = findViewById(R.id.category);
        eventDate = findViewById(R.id.date);
        eventTitle = findViewById(R.id.title);
        eventDescription = findViewById(R.id.description);
        eventFlyer = findViewById(R.id.flyer);





        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            setTitle(mBundle.getString("Title"));
            organizationLogo.setImageResource(mBundle.getInt("Organization Logo"));
            organizationName.setText(mBundle.getString("Organization Name"));
            eventCategory.setText(mBundle.getString("Category"));
            eventDate.setText(mBundle.getString("Date"));
            eventTitle.setText(mBundle.getString("Title"));
            eventDescription.setText(mBundle.getString("Description"));
            eventFlyer.setImageResource(mBundle.getInt("Image"));
        }
    }
}
