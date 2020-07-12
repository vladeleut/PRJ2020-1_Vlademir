package com.eleut.gerenciadorlanchonete.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.eleut.gerenciadorlanchonete.Classes.Usuario;
import com.eleut.gerenciadorlanchonete.DAO.ConfigFirebase;
import com.eleut.gerenciadorlanchonete.Helpers.Preferences;
import com.eleut.gerenciadorlanchonete.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private BootstrapEditText email;
    private BootstrapEditText senha1;
    private BootstrapEditText senha2;
    private BootstrapEditText nome;
    private RadioButton rbAdmin;
    private RadioButton rbAtend;
    private BootstrapButton btnCadastrar;
    private BootstrapButton btnCancelar;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        email   = (BootstrapEditText) findViewById(R.id.edtCadEmail);
        senha1  = (BootstrapEditText) findViewById(R.id.edtCadSenha);
        senha2  = (BootstrapEditText) findViewById(R.id.edtCadSenhaConf);
        nome    = (BootstrapEditText) findViewById(R.id.edtCadNome);
        rbAdmin = (RadioButton) findViewById(R.id.rbAdmin);
        rbAtend = (RadioButton) findViewById(R.id.rbAtend);
        btnCadastrar    = (BootstrapButton) findViewById(R.id.btnCadastrar);
        btnCancelar     = (BootstrapButton) findViewById(R.id.btnCancelar);

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
