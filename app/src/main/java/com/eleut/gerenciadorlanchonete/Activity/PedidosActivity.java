package com.eleut.gerenciadorlanchonete.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.eleut.gerenciadorlanchonete.Adapter.PedidoAdapter;
import com.eleut.gerenciadorlanchonete.Adapter.PedidoAdapter;
import com.eleut.gerenciadorlanchonete.Classes.Pedido;
import com.eleut.gerenciadorlanchonete.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PedidosActivity extends AppCompatActivity {

    private RecyclerView mRecyclerViewPedidos;
    private PedidoAdapter adapter;
    private List<Pedido> pedidos;
    private DatabaseReference databaseRef;
    private Pedido todosPedidos;
    private LinearLayoutManager mLayoutManagerTodosPedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);


        mRecyclerViewPedidos = (RecyclerView)findViewById(R.id.recycleViewTodosPedidos);
        carregaTodosPedidos();

    }

    private void carregaTodosPedidos(){
        mRecyclerViewPedidos.setHasFixedSize(true);
        mLayoutManagerTodosPedidos = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewPedidos.setLayoutManager(mLayoutManagerTodosPedidos);
        pedidos = new ArrayList<>();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        databaseRef.child("pedidos").orderByChild("keyPedido").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    todosPedidos = postSnapshot.getValue(Pedido.class);
                    pedidos.add(todosPedidos);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        adapter = new PedidoAdapter(pedidos, this);
        mRecyclerViewPedidos.setAdapter(adapter);
    }
}