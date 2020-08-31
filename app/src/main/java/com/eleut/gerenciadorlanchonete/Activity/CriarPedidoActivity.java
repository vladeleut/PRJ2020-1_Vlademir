package com.eleut.gerenciadorlanchonete.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.eleut.gerenciadorlanchonete.Classes.Pedido;
import com.eleut.gerenciadorlanchonete.DAO.ConfigFirebase;
import com.eleut.gerenciadorlanchonete.R;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CriarPedidoActivity extends AppCompatActivity {

    private BootstrapEditText descPedido;
    private BootstrapEditText nomeCliPedido;

    private BootstrapButton btnCriarPedido;
    private BootstrapButton btnCancelar;

    private DatabaseReference ref;

    private Pedido pedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_pedido);

        descPedido      = (BootstrapEditText) findViewById(R.id.edtDescPedido);
        nomeCliPedido   = (BootstrapEditText) findViewById(R.id.edtNomeCliPedido);

        btnCriarPedido  = (BootstrapButton) findViewById(R.id.btnCriarPedido);
        btnCancelar     = (BootstrapButton) findViewById(R.id.btnCancelarPedido);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CriarPedidoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnCriarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nomeCliPedido.getText().toString().equals("") & descPedido.getText().toString().equals("")){
                    Toast.makeText(CriarPedidoActivity.this, "Insira alguma descrição ou nome", Toast.LENGTH_LONG).show();
                }else{
                    pedido = new Pedido();
                    pedido.setNomeCliPedido(nomeCliPedido.getText().toString());
                    pedido.setDescPedido(descPedido.getText().toString());
                    pedido.setSituacaoPedido("Aberto");

                    inserePedido(pedido);
                    abreTelaPedidos();
                }

            }
        });

    }

    public boolean inserePedido(Pedido p){
        try{
            ref = ConfigFirebase.getFirebase().child("pedidos");
            String key = ref.push().getKey();
            p.setKeyPedido(key);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            p.setAberturaPedido(sdf.format(Calendar.getInstance().getTime()));
            ref.child(key).setValue(p);
            Toast.makeText(CriarPedidoActivity.this, "Pedido criado com sucesso!", Toast.LENGTH_SHORT).show();
            return true;
        }catch (Exception e){
            Toast.makeText(CriarPedidoActivity.this, "Erro ao criar o pedido", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }

    public void abreTelaPedidos(){
        //Intent intent = new Intent()
        finish();
    }
}
