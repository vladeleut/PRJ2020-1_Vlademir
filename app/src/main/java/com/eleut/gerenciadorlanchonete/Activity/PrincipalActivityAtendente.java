package com.eleut.gerenciadorlanchonete.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.eleut.gerenciadorlanchonete.R;
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
        }else if(id == R.id.action_ver_meu_perfil){
            verPerfil();
        }else if(id == R.id.action_atend_abre_cardapio){
            abreCardapio();
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

    private void abreCardapio(){
        Intent intent = new Intent(PrincipalActivityAtendente.this, CardapioActivity.class);
        startActivity(intent);
        finish();
    }

    private void verPerfil(){
        Intent intent = new Intent(PrincipalActivityAtendente.this, MeuPerfilActivity.class);
        startActivity(intent);
        finish();
    }

}