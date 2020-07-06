package com.example.prj_vlademir.Actitvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.prj_vlademir.DAO.ConfigFirebase;
import com.example.prj_vlademir.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class UploadFotoActivity extends AppCompatActivity {
    private Button btnInsertProfilePic;
    private Button btnCancelProfilePic;
    private StorageReference storageReference;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private ImageView imageView;
    private String emailLoggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_foto);

        storageReference = ConfigFirebase.getFirebaseStorageReference();
        auth = ConfigFirebase.getFirebaseAuth();
        emailLoggedUser = auth.getCurrentUser().getEmail();

        imageView = (ImageView) findViewById(R.id.imgInsertProfilePic);

        loadDefaultPic();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"),123);
            }
        });

        btnInsertProfilePic = (Button) findViewById(R.id.btnInsertProfilePic);
        btnCancelProfilePic = (Button) findViewById(R.id.btnCancelProfilePic);

        btnInsertProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertPicture();
            }
        });
    }

    private void insertPicture(){//processo de pegar a foto bruta e transformar num bitmap
        StorageReference createPicRef = storageReference.child("userProfilePic/" + emailLoggedUser + ".jpg");
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();

        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
        byte [] data = byteArray.toByteArray();

        UploadTask uploadTask = createPicRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                loadDefaultPic();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        final int heigth = 300;
        final int width = 300;

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == 123){
                Uri selectedImg = data.getData();
                Picasso.with(UploadFotoActivity.this).load(selectedImg.toString()).resize(width, heigth).centerCrop().into(imageView);
            }
        }

    }

    private void loadDefaultPic(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://vladpjr-d0a60.appspot.com/missing_pic.png");

        final int heigth = 300;
        final int width = 300;

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(UploadFotoActivity.this).load(uri.toString()).resize(width, heigth).centerCrop().into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}