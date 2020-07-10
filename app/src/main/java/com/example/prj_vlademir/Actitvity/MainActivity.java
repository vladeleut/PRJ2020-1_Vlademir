package com.example.prj_vlademir.Actitvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.prj_vlademir.Classes.Usuario;
import com.example.prj_vlademir.DAO.ConfigFirebase;
import com.example.prj_vlademir.Helper.Preferences;
import com.example.prj_vlademir.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private BootstrapEditText txtEmail;
    private BootstrapEditText txtSenha;
    private BootstrapButton btnLogin;
    private Usuario user;

    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtEmail = (BootstrapEditText) findViewById(R.id.edtEmail);
        txtSenha = (BootstrapEditText) findViewById(R.id.edtSenha);
        btnLogin = (BootstrapButton) findViewById(R.id.btnLogin);

        databaseRef = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        if(usuarioLogado()){
            String email = auth.getCurrentUser().getEmail().toString();
            abrirTelaPrincipal(email);
        }else{
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!txtEmail.getText().toString().equals("") && !txtSenha.getText().toString().equals("")){
                        user = new Usuario();
                        user.setEmail(txtEmail.getText().toString());
                        user.setSenha(txtSenha.getText().toString());
                        validarLogin();
                    }else{
                        Toast.makeText(MainActivity.this, "Preencha os campos de E-mail e senha", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    private void validarLogin(){
        auth = ConfigFirebase.getFirebaseAuth();
        auth.signInWithEmailAndPassword(user.getEmail().toString(), user.getSenha().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    abrirTelaPrincipal(user.getEmail());
                    Preferences preferences = new Preferences(MainActivity.this);
                    preferences.saveUserPreferences(user.getEmail(), user.getSenha());
                    Toast.makeText(MainActivity.this, "O Login foi efetuado com sucesso", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Usuário ou senha inválidos. Tente novamente.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void  abrirTelaPrincipal(String emailUsuario){
        String email = auth.getCurrentUser().getEmail().toString();
        databaseRef.child("usuario").orderByChild("email").equalTo(email.toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    String tipoUsuarioEmail = postSnapshot.child("tipoUsuario").getValue().toString();
                    if (tipoUsuarioEmail.equals("Administrador")){
                        Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(tipoUsuarioEmail.equals("Atendente")){
                        Intent intent = new Intent(MainActivity.this, PrincipalActivityAtendente.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public Boolean usuarioLogado(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            return true;
        }else{
            return false;
        }
    }

    public void abrirNovaActivity(Intent intent){
        startActivity(intent);
    }

    public void permission(){
        int PERMISSION_ALL = 1;
        String [] PERMISSION = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, PERMISSION, PERMISSION_ALL);
    }

}
