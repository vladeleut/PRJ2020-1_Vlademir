package com.example.prj_vlademir.Actitvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prj_vlademir.CadastroProdutoActivity;
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

    private LinearLayout linearLayoutAddProdutos;
    private LinearLayout linearLayoutTotalVendido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        linearLayoutAddProdutos = (LinearLayout)findViewById(R.id.linnerLayoutAddProduto);
        linearLayoutTotalVendido = (LinearLayout)findViewById(R.id.linnerLayoutTotalPedido);

        linearLayoutAddProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaCadastroProdutos();
            }
        });

        auth = FirebaseAuth.getInstance();
        //consultando o banco
        ref = FirebaseDatabase.getInstance().getReference();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_add_usuario){
            abrirTelaCadastroUsuario();
        }else if(id == R.id.action_admin_sair){
            deslogarUsuario();
        }
        return super.onOptionsItemSelected(item);
    }

    private void abrirTelaCadastroProdutos(){
        Intent intent = new Intent(PrincipalActivity.this, CadastroProdutoActivity.class);
        startActivity(intent);
    }

    private void abrirTelaCadastroUsuario(){
        Intent intent = new Intent(PrincipalActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }

    private void abreCardapio(){
        Intent intent = new Intent(PrincipalActivity.this, CardapioActivity.class);
        startActivity(intent);
    }

    private void deslogarUsuario(){
        auth.signOut();
        Intent intent = new Intent(PrincipalActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}