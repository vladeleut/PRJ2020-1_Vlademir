package com.eleut.gerenciadorlanchonete.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.eleut.gerenciadorlanchonete.DAO.ConfigFirebase;
import com.eleut.gerenciadorlanchonete.R;
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
    private BootstrapButton btnInsertProfilePic;
    private BootstrapButton btnCancelProfilePic;
    private StorageReference storageReference;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private ImageView imageView;
    private String emailLoggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_foto_activiy);


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

        btnInsertProfilePic = (BootstrapButton) findViewById(R.id.btnInsertProfilePic);
        btnCancelProfilePic = (BootstrapButton) findViewById(R.id.btnCancelProfilePic);

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
                Toast.makeText(UploadFotoActivity.this, "Foto atualizada com sucesso!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        btnCancelProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadFotoActivity.this, PrincipalActivityAtendente.class);
                startActivity(intent);
                finish();
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
                Picasso.get().load(selectedImg.toString()).resize(width, heigth).centerCrop().into(imageView);
            }
        }

    }

    private void loadDefaultPic(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://gerenciadorlanchonete-b1776.appspot.com/userProfilePic/"+emailLoggedUser+".jpg");

        final int heigth = 300;
        final int width = 300;

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri.toString()).resize(width, heigth).centerCrop().into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}