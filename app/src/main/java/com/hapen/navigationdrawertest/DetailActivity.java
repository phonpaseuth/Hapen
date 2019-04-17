package com.hapen.navigationdrawertest;

import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import com.squareup.picasso.Picasso;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

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

    Bitmap bitmap;
    Bitmap bitmap2;

    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;


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
/**
        //get data from intent
        byte[] bytes = getIntent().getByteArrayExtra("Image");
        String title = getIntent().getStringExtra("Title");
        String date = getIntent().getStringExtra("Date");
        String category = getIntent().getStringExtra("Category");
        String org = getIntent().getStringExtra("Organization Name");
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        byte[] bytes2 = getIntent().getByteArrayExtra("Organization Logo");
        Bitmap bmp2 = BitmapFactory.decodeByteArray(bytes2, 0, bytes.length);


        //set data to views
        eventTitle.setText(title);
        eventCategory.setText(category);
        eventDate.setText(date);
        organizationName.setText(org);
        eventFlyer.setImageBitmap(bmp);
        organizationLogo.setImageBitmap(bmp2);

        //get image from imageview as bitmap
        bitmap = ((BitmapDrawable)eventFlyer.getDrawable()).getBitmap();
        bitmap2 = ((BitmapDrawable)organizationLogo.getDrawable()).getBitmap();

**/


        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            setTitle(mBundle.getString("Title"));

            //organizationLogo.setImageResource(mBundle.getInt("Organization Logo"));
            String orgLogo = getIntent().getStringExtra("Organization Logo");
            Picasso.with(DetailActivity.this).load(orgLogo).into(organizationLogo);

            organizationName.setText(mBundle.getString("Organization Name"));
            eventCategory.setText(mBundle.getString("Category"));
            eventDate.setText(mBundle.getString("Date"));
            eventTitle.setText(mBundle.getString("Title"));
            eventDescription.setText(mBundle.getString("Description"));

            String image = getIntent().getStringExtra("Image");
            Picasso.with(DetailActivity.this).load(image).into(eventFlyer);
            //eventFlyer.setImageResource(mBundle.getInt("Image"));
            //Picasso.with(this).load(eventFlyer).into(eventFlyer);


        }

    }

    //handle onBackPressed(go to previous activity)
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
