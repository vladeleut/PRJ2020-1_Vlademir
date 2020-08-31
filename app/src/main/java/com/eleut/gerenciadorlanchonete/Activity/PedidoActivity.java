package com.eleut.gerenciadorlanchonete.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.eleut.gerenciadorlanchonete.R;

public class PedidoActivity extends AppCompatActivity {

    private BootstrapButton btnCancelaPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        btnCancelaPedido = (BootstrapButton)findViewById(R.id.btnPedidoCancela);

        btnCancelaPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltaTela();
            }
        });
    }

    private void voltaTela(){
        Intent intent = new Intent(PedidoActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
