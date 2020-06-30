package com.example.prj_vlademir.Actitvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.prj_vlademir.Classes.Usuario;
import com.example.prj_vlademir.DAO.ConfigFirebase;
import com.example.prj_vlademir.Helper.Preferences;
import com.example.prj_vlademir.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroUsuarioActivity extends AppCompatActivity {
    private EditText email;
    private EditText senha1;
    private EditText senha2;
    private EditText nome;
    private RadioButton rbAdmin;
    private RadioButton rbAtend;
    private Button btnCadastrar;
    private Button btnCancelar;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Usuario user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        email   = (EditText) findViewById(R.id.edtCadEmail);
        senha1  = (EditText) findViewById(R.id.edtCadSenha);
        senha2  = (EditText) findViewById(R.id.edtCadSenhaConf);
        nome    = (EditText) findViewById(R.id.edtCadNome);
        rbAdmin = (RadioButton) findViewById(R.id.rbAdmin);
        rbAtend = (RadioButton) findViewById(R.id.rbAtend);
        btnCadastrar    = (Button) findViewById(R.id.btnCadastrar);
        btnCancelar     = (Button) findViewById(R.id.btnCancelar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(senha1.getText().toString().equals(senha2.getText().toString())){
                    user = new Usuario();
                    user.setEmail(email.getText().toString());
                    user.setSenha(senha1.getText().toString());
                    user.setNome(nome.getText().toString());

                    if(rbAdmin.isChecked()){
                        user.setTipoUsuario("Administrador");
                    }else if(rbAtend.isChecked()){
                        user.setTipoUsuario("Atendente");
                    }
                    cadastrarUsuario();
                }else{
                    Toast.makeText(CadastroUsuarioActivity.this, "As senhas não são iguais", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastroUsuarioActivity.this, PrincipalActivity.class);
                startActivity(intent);
            }
        });
    }

    private void cadastrarUsuario(){
        auth = ConfigFirebase.getFirebaseAuth();
        auth.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    insereUsuario(user);
                    finish();

                    //desloga o usuário que o firebase logou ao cadastrar
                    auth.signOut();
                    abreTelaPrincipal();
                }else{
                    String erroExcecao = "";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        erroExcecao = "A senha precisa conter no mínimo 8 caracteres e ser composta por letras e números";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcecao = "O E-mail digitado é inválido";
                    }catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o cadastro :( ";
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroUsuarioActivity.this, "Erro "+erroExcecao, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private Boolean insereUsuario(Usuario u){
        try{
            reference = ConfigFirebase.getFirebase().child("usuario");
            reference.push().setValue(user);
            Toast.makeText(CadastroUsuarioActivity.this, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();
            return true;
        }catch (Exception e){
            Toast.makeText(CadastroUsuarioActivity.this, "Erro ao gravar o Usuário", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }

    private void abreTelaPrincipal(){
        auth = ConfigFirebase.getFirebaseAuth();
        Preferences preferences = new Preferences(CadastroUsuarioActivity.this);
        auth.signInWithEmailAndPassword(preferences.getLoggedUserEmail(), preferences.getLoggedUserPasswd()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(CadastroUsuarioActivity.this, PrincipalActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(CadastroUsuarioActivity.this, "Falha!", Toast.LENGTH_LONG).show();
                    auth.signOut();
                    Intent intent = new Intent(CadastroUsuarioActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }
}
