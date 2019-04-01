package com.hapen.navigationdrawertest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class FragmentUpload extends Fragment {

    Button chooseImg, uploadImg;
    EditText editTextName;

    ImageView imgView;
    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    ProgressDialog pd;


    //creating reference to firebase storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://hapen-51939.appspot.com/");    //change the url according to your firebase app

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_upload, container, false);


        chooseImg = (Button)rootView.findViewById(R.id.chooseImg);
        uploadImg = (Button)rootView.findViewById(R.id.uploadImg);
        imgView = (ImageView)rootView.findViewById(R.id.imgView);
        editTextName = (EditText) rootView.findViewById(R.id.editText);


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

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filePath != null) {

                    StorageReference childRef = storageRef.child(""+editTextName.getText());

                    //uploading the image
                    UploadTask uploadTask = childRef.putFile(filePath);


                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            Toast.makeText(getContext(),"Upload Successfull",Toast.LENGTH_SHORT).show();
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