package com.eleut.gerenciadorlanchonete.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.eleut.gerenciadorlanchonete.Classes.Usuario;
import com.eleut.gerenciadorlanchonete.DAO.ConfigFirebase;
import com.eleut.gerenciadorlanchonete.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MeuPerfilActivity extends AppCompatActivity {

    private TextView txtNome;
    private TextView txtEmail;
    private TextView txtTipo;
    private BootstrapButton btnEditar;
    private BootstrapButton btnExcluir;
    private BootstrapButton btnCancelar;

    private FirebaseAuth auth;
    private DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meu_perfil);
        auth = FirebaseAuth.getInstance();
        ref = ConfigFirebase.getFirebase();

        txtNome = (TextView)findViewById(R.id.txtNomeUsuario);
        txtEmail = (TextView)findViewById(R.id.txtEmailUsuario);
        txtTipo= (TextView)findViewById(R.id.txtTipoUsuario);
        btnEditar = (BootstrapButton)findViewById(R.id.btnEditar);
        btnCancelar = (BootstrapButton)findViewById(R.id.btnCancelar);
        btnExcluir = (BootstrapButton)findViewById(R.id.btnExcluirConta);

        String emailLoggedUser = auth.getCurrentUser().getEmail();

        ref.child("usuario").orderByChild("email").equalTo(emailLoggedUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Usuario user = postSnapshot.getValue(Usuario.class);
                    txtNome.setText(user.getNome());
                    txtEmail.setText(user.getEmail());
                    txtTipo.setText(user.getTipoUsuario());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelar();
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abreDialogConfirmaExclusao();
            }
        });

    }

    private void cancelar(){
        Intent intent = new Intent(MeuPerfilActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void excluirUsuario(){
        String emailLoggedUser = auth.getCurrentUser().getEmail();
        ref = ConfigFirebase.getFirebase();
        ref.child("usuario").orderByChild("email").equalTo(emailLoggedUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    final Usuario user = postSnapshot.getValue(Usuario.class);
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d("Usuário Excluído", "User deleted successfully");
                                Toast.makeText(MeuPerfilActivity.this, "O usuário foi excluído", Toast.LENGTH_LONG);
                                ref = ConfigFirebase.getFirebase();
                                ref.child("usuario").child(user.getKeyUsuario()).removeValue();

                                auth.signOut();
                                abrirTelaLogin();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void abrirTelaLogin(){
        Intent intent = new Intent(MeuPerfilActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void abreDialogConfirmaExclusao(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert_excluir);
        final BootstrapButton btnSim = (BootstrapButton)dialog.findViewById(R.id.btnSim);
        final BootstrapButton btnNao = (BootstrapButton)dialog.findViewById(R.id.btnNao);

        btnSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                excluirUsuario();
                dialog.dismiss();
            }
        });

        btnNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
