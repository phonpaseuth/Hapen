package com.hapen.navigationdrawertest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;


public class FragmentUpload extends Fragment {

    Button chooseImg, uploadImg;
    EditText upload_title, upload_description;

    ImageView imgView;
    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    ProgressDialog pd;

    //creating reference to firebase storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://hapen-51939.appspot.com/");    //change the url according to your firebase app

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String fullName;


    public static class event {

        public String organization;
        public String category;
        public String title;
        public String description;
        public String date;
        public String url;
        // public String[] attendants = new String[0];

        public event(String eOrganization, String eCategory, String eTitle, String eDescription, String eDate, String eUrl) {
            organization = eOrganization;
            category = eCategory;
            title = eTitle;
            description = eDescription;
            date = eDate;   // https://stackoverflow.com/questions/10286204/the-right-json-date-format | https://stackoverflow.com/questions/37976468/saving-and-retrieving-date-in-firebase
            url = eUrl;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_upload, container, false);

        chooseImg = (Button)rootView.findViewById(R.id.chooseImg);
        uploadImg = (Button)rootView.findViewById(R.id.uploadImg);
        imgView = (ImageView)rootView.findViewById(R.id.imgView);
        upload_title = (EditText) rootView.findViewById(R.id.upload_title);
        upload_description = (EditText) rootView.findViewById(R.id.upload_description);

        pd = new ProgressDialog(getContext().getApplicationContext());
        pd.setMessage("Uploading....");

        chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });

        // Get uploader's info
        DatabaseReference userRef = database.getReference("users").child(user.getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fullName = dataSnapshot.child("fullName").getValue(String.class);
                System.out.print(fullName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filePath != null) {

                    final StorageReference childRef = storageRef.child(""+upload_title.getText());

                    //uploading the image
                    final UploadTask uploadTask = childRef.putFile(filePath);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            Toast.makeText(getContext(),"Upload Successfull",Toast.LENGTH_SHORT).show();

                            // Save the event and file path
                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }

                                    // Continue with the task to get the download URL
                                    return childRef.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        DatabaseReference myRef = database.getReference("events");
                                        DatabaseReference newRef = myRef.push();

                                        newRef.setValue(new event(
                                                fullName,
                                                "Scholarship",
                                                ""+upload_title.getText(),
                                                ""+upload_description.getText(),
                                                "Random date",
                                                downloadUri.toString()));
                                    } else {
                                        // Handle failures
                                        // ...
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getContext(),"Upload Failed -> " + e,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(getContext(),"Select an Image",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK&& data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), filePath);
                //Setting image to ImageView
                imgView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


}
