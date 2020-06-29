package com.example.prj_vlademir.Actitvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prj_vlademir.Classes.Usuario;
import com.example.prj_vlademir.DAO.ConfigFirebase;
import com.example.prj_vlademir.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText txtEmail;
    private EditText txtSenha;
    private Button btnLogin;
    private Usuario user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtEmail = (EditText) findViewById(R.id.edtEmail);
        txtSenha = (EditText) findViewById(R.id.edtSenha);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        if(usuarioLogado()){
            Intent intentMinhaConta = new Intent(MainActivity.this, PrincipalActivity.class);
            abrirNovaActivity(intentMinhaConta);
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
                    abrirTelaPrincipal();
                    Toast.makeText(MainActivity.this, "O Login foi efetuado com sucesso", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Usuário ou senha inválidos. Tente novamente.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void  abrirTelaPrincipal(){
        Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);
        startActivity(intent);
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

}
