package com.example.newpost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

import io.grpc.InternalNotifyOnServerBuild;

public class UploadActivity extends AppCompatActivity {
    Bitmap selectedImage;
    EditText commentText;
    Button uploadButton;
    ImageView imageView;
    Uri imageData;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    UUID uuid;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    String comment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
       commentText=findViewById(R.id.commentText);
        uploadButton = findViewById(R.id.uploadButton);
        imageView = findViewById(R.id.imageView);


        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
    }

    public void uploadClick(View view) {
        if (imageData !=null )
        {
                uuid = UUID.randomUUID();
                final String imageName = "images/" + uuid + ".jpg";
                storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(UploadActivity.this, "Saved", Toast.LENGTH_LONG).show();
                        final Intent intent = new Intent(UploadActivity.this, FeedAvtivity.class);
                        startActivity(intent);
                        finish();

                        //Download URL
                        StorageReference newStorageReference = FirebaseStorage.getInstance().getReference(imageName);
                        newStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();

                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                String email = firebaseUser.getEmail();

                                comment = commentText.getText().toString();
                                //Database

                                HashMap<String,Object> postData = new HashMap<>();
                                postData.put("downloadUrl", downloadUrl);
                                postData.put("email", email);
                                postData.put("comment", comment);
                                postData.put("date", FieldValue.serverTimestamp());

                                firebaseFirestore.collection("Post").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Intent intent1 = new Intent(UploadActivity.this, FeedAvtivity.class);
                                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent1);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UploadActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG);
                                    }
                                });

                            }
                        });
                    }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG);
                    }
                });
            }

        if(imageData ==null){
            Toast.makeText(UploadActivity.this, "Enter Picture", Toast.LENGTH_LONG).show();
        }
    }

    public void imageClick(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
            else {
                Toast.makeText(UploadActivity.this, "NOT ALLOWED", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageData = data.getData();

      try{
          if(Build.VERSION.SDK_INT>=28){
              ImageDecoder.Source source=ImageDecoder.createSource(this.getContentResolver(),imageData);
              selectedImage=ImageDecoder.decodeBitmap(source);
              imageView.setImageBitmap(selectedImage);
          }else{
              selectedImage=MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
              imageView.setImageBitmap(selectedImage);
          }

      }
      catch (Exception e){
          e.printStackTrace();
      }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

