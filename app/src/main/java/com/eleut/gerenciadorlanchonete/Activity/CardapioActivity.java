package com.eleut.gerenciadorlanchonete.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.eleut.gerenciadorlanchonete.Adapter.CardapioAdapter;
import com.eleut.gerenciadorlanchonete.Classes.Produto;
import com.eleut.gerenciadorlanchonete.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CardapioActivity extends AppCompatActivity {

    private RecyclerView mRecyclerViewCardapio;
    private CardapioAdapter adapter;
    private List<Produto> produtos;
    private DatabaseReference databaseRef;
    private Produto todosProduto;
    private LinearLayoutManager mLayoutManagerTodosProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);


        mRecyclerViewCardapio = (RecyclerView)findViewById(R.id.recycleViewTodosProdutos);
        carregaTodosProdutos();

    }

    private void carregaTodosProdutos(){
        mRecyclerViewCardapio.setHasFixedSize(true);
        mLayoutManagerTodosProdutos = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewCardapio.setLayoutManager(mLayoutManagerTodosProdutos);
        produtos = new ArrayList<>();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        databaseRef.child("cardapio").orderByChild("keyProduto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    todosProduto = postSnapshot.getValue(Produto.class);
                    produtos.add(todosProduto);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter = new CardapioAdapter(produtos, this);
        mRecyclerViewCardapio.setAdapter(adapter);
    }
}