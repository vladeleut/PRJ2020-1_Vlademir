package com.example.prj_vlademir;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.prj_vlademir.Actitvity.CadastroUsuarioActivity;
import com.example.prj_vlademir.Actitvity.PrincipalActivity;
import com.example.prj_vlademir.Actitvity.PrincipalActivityAtendente;
import com.example.prj_vlademir.Classes.Produto;
import com.example.prj_vlademir.Classes.Usuario;
import com.example.prj_vlademir.DAO.ConfigFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroProdutoActivity extends AppCompatActivity {

    private BootstrapEditText nome;
    private BootstrapEditText desc;
    private BootstrapEditText preco;
    private BootstrapEditText codigo;
    private RadioButton rbProdDisp;
    private RadioButton rbProdIndisp;
    private BootstrapButton btnCadastrar;
    private BootstrapButton btnCancelar;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Produto prod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_produto);

        nome    = (BootstrapEditText) findViewById(R.id.edtCadNomeProduto);
        codigo    = (BootstrapEditText) findViewById(R.id.edtCadCodigoProduto);
        preco    = (BootstrapEditText) findViewById(R.id.edtCadPrecoProduto);
        desc    = (BootstrapEditText) findViewById(R.id.edtCadDescProduto);
        rbProdDisp = (RadioButton) findViewById(R.id.rbProdDisponivel);
        rbProdIndisp = (RadioButton) findViewById(R.id.rbProdIndisponivel);
        btnCadastrar    = (BootstrapButton) findViewById(R.id.btnCadastrarProduto);
        btnCancelar     = (BootstrapButton) findViewById(R.id.btnCancelarProduto);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prod = new Produto();
                prod.setNomeProduto(nome.getText().toString());
                prod.setDescProduto(desc.getText().toString());
                prod.setPreco(preco.getText().toString());
                prod.setKeyProduto(codigo.getText().toString());
                if (rbProdDisp.isChecked()) {
                    prod.setDisponibilidade("Sim");
                } else if (rbProdIndisp.isChecked()) {
                    prod.setDisponibilidade("Não");
                }
                cadastrarProduto(prod);
                finish();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastroProdutoActivity.this, PrincipalActivityAtendente.class);
                startActivity(intent);
            }
        });
    }
    private Boolean cadastrarProduto(Produto p){
        try{
            reference = ConfigFirebase.getFirebase().child("produtos");
            reference.push().setValue(prod);
            Toast.makeText(CadastroProdutoActivity.this, "Produto cadastrado com sucesso", Toast.LENGTH_SHORT).show();
            return true;
        }catch (Exception e){
            Toast.makeText(CadastroProdutoActivity.this, "Erro ao gravar o Produto", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }
}