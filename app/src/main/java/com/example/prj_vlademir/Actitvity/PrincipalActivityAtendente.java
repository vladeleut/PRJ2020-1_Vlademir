package com.example.prj_vlademir.Actitvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.prj_vlademir.R;
import com.google.firebase.auth.FirebaseAuth;

public class PrincipalActivityAtendente extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_atendente);

        auth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_atend, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_atend_sair){
            deslogarUsuario();
        }else if(id == R.id.action_insert_profile_pic_atend){
            updloadFotoPerfil();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario(){
        auth.signOut();
        Intent intent = new Intent(PrincipalActivityAtendente.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void updloadFotoPerfil(){
        Intent intent = new Intent(PrincipalActivityAtendente.this, UploadFotoActivity.class);
        startActivity(intent);
    }
}