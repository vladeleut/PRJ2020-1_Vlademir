package com.example.prj_vlademir.Actitvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.prj_vlademir.Classes.Usuario;
import com.example.prj_vlademir.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PrincipalActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference ref;
    private TextView tipoUsuario;
    private Usuario usuario;
    private String tipoUsuarioEmail;
    private Menu menu1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        tipoUsuario = (TextView) findViewById(R.id.txtTipoUsuario);

        auth = FirebaseAuth.getInstance();
        //consultando o banco
        ref = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        this.menu1 = menu;
        //recebe o email do usuário que está logado
        String email = auth.getCurrentUser().getEmail().toString();

        //identifica qual nó queremos; //ordena por email independente da chave que ele gerou e pega o único igual ao q passamos
        ref.child("usuario").orderByChild("email").equalTo(email.toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){//percorre todos os resultados que no caso é um só
                    tipoUsuarioEmail = postSnapshot.child("tipoUsuario").getValue().toString();//dentro do email que queremos, pegamos o valor do tipo do usuário
                    tipoUsuario.setText(tipoUsuarioEmail);
                    menu1.clear();
                    if(tipoUsuarioEmail.equals("Administrador")){
                        getMenuInflater().inflate(R.menu.menu_admin, menu1);
                    }else if(tipoUsuarioEmail.equals("Atendente")){
                        getMenuInflater().inflate(R.menu.menu_atend, menu1);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_add_usuario){
            abrirTelaCadastroUsuario();
        }else if(id == R.id.action_admin_sair){
            deslogarUsuario();
        }else if(id == R.id.action_atend_sair){
            deslogarUsuario();
        }else if(id == R.id.action_insert_profile_pic_atend){
            updloadFotoPerfil();
        }

        return super.onOptionsItemSelected(item);
    }

    private void abrirTelaCadastroUsuario(){
        Intent intent = new Intent(PrincipalActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }

    private void deslogarUsuario(){
        auth.signOut();
        Intent intent = new Intent(PrincipalActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void updloadFotoPerfil(){
        Intent intent = new Intent(PrincipalActivity.this, UploadFotoActivity.class);
        startActivity(intent);
    }
}