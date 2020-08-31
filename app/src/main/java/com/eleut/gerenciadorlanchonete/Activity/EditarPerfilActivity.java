package com.eleut.gerenciadorlanchonete.Activity;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.eleut.gerenciadorlanchonete.Classes.Usuario;
import com.eleut.gerenciadorlanchonete.DAO.ConfigFirebase;
import com.eleut.gerenciadorlanchonete.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class EditarPerfilActivity extends AppCompatActivity {

    private BootstrapEditText edtEditNome;
    private BootstrapEditText edtEditSenha;
    private BootstrapEditText edtEditSenhaConf;
    private RadioButton rbEditAdmin;
    private RadioButton rbEditAtend;
    private BootstrapButton btnEditGravar;
    private BootstrapButton btnEditCancelar;

    private String txtBundleOrigem = "";
    private String txtBundleNome = "";
    private String txtBundleEmail = "";
    private String txtBundleTipo = "";
    private String txtBundleKeyUsuario = "";

    private DatabaseReference ref;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        edtEditNome = (BootstrapEditText)findViewById(R.id.edtEditNome);
        edtEditSenha = (BootstrapEditText)findViewById(R.id.edtEditSenha);
        edtEditSenhaConf = (BootstrapEditText)findViewById(R.id.edtCadSenhaConf);

        rbEditAdmin = (RadioButton) findViewById(R.id.rbEditAdmin);
        rbEditAtend = (RadioButton)findViewById(R.id.rbEditAtend);

        btnEditGravar = (BootstrapButton)findViewById(R.id.btnEditGravar);
        btnEditCancelar = (BootstrapButton)findViewById(R.id.btnEditCancelar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        txtBundleOrigem = bundle.getString("origem");
        if(txtBundleOrigem.equals("editarUsuario")){
            txtBundleNome = bundle.getString("nome");
            txtBundleEmail = bundle.getString("email");

            txtBundleTipo = bundle.getString("tipo");
            txtBundleKeyUsuario = bundle.getString("keyUsuario");

            edtEditNome.setText(txtBundleNome.toString());
            if(txtBundleTipo.equals("Administrador")){
                rbEditAdmin.setChecked(true);
            }else if(txtBundleTipo.equals("Atendente")){
                rbEditAtend.setChecked(true);
            }
        }

        btnEditGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtEditSenha.getText().toString().equals(edtEditSenhaConf.getText().toString())){
                    Usuario u = new Usuario();
                    u.setNome(edtEditNome.getText().toString());
                    u.setSenha(edtEditSenha.getText().toString());
                    u.setEmail(txtBundleEmail);

                    if(rbEditAdmin.isChecked()){
                        u.setTipoUsuario("Adminstrador");
                    }else if(rbEditAtend.isChecked()){
                        u.setTipoUsuario("Atendente");
                    }
                    u.setKeyUsuario(txtBundleKeyUsuario);
                    atualizaDados(u);

                }else{
                    Toast.makeText(EditarPerfilActivity.this, "As senhas não correspondem", Toast.LENGTH_LONG);
                }
            }
        });

        btnEditCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelar();
            }
        });

    }

    private void cancelar(){
        Intent intent = new Intent(EditarPerfilActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean atualizaDados(final Usuario usuario){
        btnEditGravar.setEnabled(false);
        try{
            ref = ConfigFirebase.getFirebase().child("usuario");
            atualizarSenha(usuario.getSenha());
            ref.child(txtBundleKeyUsuario).setValue(usuario);
            Toast.makeText(EditarPerfilActivity.this, "Os dados foram atualizados", Toast.LENGTH_LONG);

        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    private void atualizarSenha(String novaSenha){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(novaSenha)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("NOVA SENHA ATUALIZADA", "Senha atualizada com sucesso");
                        }
                    }
                });
    }

}
