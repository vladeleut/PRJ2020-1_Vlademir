package com.example.prj_vlademir.Adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prj_vlademir.Classes.Produto;
import com.example.prj_vlademir.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CardapioAdapter extends RecyclerView.Adapter<CardapioAdapter.ViewHolder> {

    private List<Produto> mProdutoList;
    private Context context;
    private DatabaseReference databaseRef;
    private List<Produto> produtos;
    private Produto todosProdutos;

    public CardapioAdapter (List<Produto> l, Context c){
        context = c;
        mProdutoList = l;

    }

    @NonNull
    @Override
    public CardapioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_todos_produtos, viewGroup, false);
        return new CardapioAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardapioAdapter.ViewHolder holder, int position) {
        final Produto item = mProdutoList.get(position);
        produtos = new ArrayList<>();

        databaseRef = FirebaseDatabase.getInstance().getReference();
        databaseRef.child("cardapio").orderByChild("keyProduto").equalTo(item.getKeyProduto()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                produtos.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    todosProdutos = postSnapshot.getValue(Produto.class);
                    produtos.add(todosProdutos);
                    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                    final int height = (displayMetrics.heightPixels / 4);
                    final int width = (displayMetrics.widthPixels / 2) ;
                    Picasso.with(context).load(todosProdutos.getUrlImgProduto()).resize(width, height).centerCrop().into(holder.fotoProdutoCardapio);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.txtNomeProdutoCardapio.setText(item.getNomeProduto());
        holder.txtNomeProdutoCardapio.setText(item.getDescProduto());
        holder.txtNomeProdutoCardapio.setText(item.getPreco());
        holder.linearLayoutProdutoCardapio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mProdutoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtNomeProdutoCardapio;
        protected TextView txtDescProdutoCardapio;
        protected TextView txtPrecoProdutoCardapio;
        protected ImageView fotoProdutoCardapio;
        protected LinearLayout linearLayoutProdutoCardapio;

        public ViewHolder(View itemView){
            super(itemView);
            txtNomeProdutoCardapio = (TextView)itemView.findViewById(R.id.txtNomeProdCardapio);
            txtDescProdutoCardapio = (TextView)itemView.findViewById(R.id.txtDescProdCardapio);
            txtPrecoProdutoCardapio = (TextView)itemView.findViewById(R.id.txtPrecoProdCardapio);
            fotoProdutoCardapio = (ImageView)itemView.findViewById(R.id.fotoProdCardapio);
            linearLayoutProdutoCardapio = (LinearLayout)itemView.findViewById(R.id.linearLayoutPesqProdCardapio);
        }
    }
}
