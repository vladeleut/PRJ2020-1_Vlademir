package com.eleut.gerenciadorlanchonete.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eleut.gerenciadorlanchonete.Classes.Produto;
import com.eleut.gerenciadorlanchonete.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CardapioAdapter extends RecyclerView.Adapter<CardapioAdapter.ViewHolder> {

    private List<Produto> mCardapioList;
    private Context context;
    private DatabaseReference databaseRef;
    private List<Produto> produtos;
    private Produto todosProdutos;

    public CardapioAdapter(List<Produto> l, Context c){
        context = c;
        mCardapioList = l;

    }

    @Override
    public CardapioAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_todos_produtos, viewGroup, false);
        return new CardapioAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CardapioAdapter.ViewHolder holder, int position) {
        final Produto item = mCardapioList.get(position);
        produtos = new ArrayList<>();

        databaseRef = FirebaseDatabase.getInstance().getReference();
        databaseRef.child("produtos").orderByChild("keyProduto").equalTo(item.getKeyProduto()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                produtos.clear();

                for(DataSnapshot posttSnapshot : dataSnapshot.getChildren()){
                    todosProdutos = posttSnapshot.getValue(Produto.class);
                    produtos.add(todosProdutos);

                    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                    final int height = (displayMetrics.heightPixels / 5);
                    final int width = (displayMetrics.widthPixels / 3);

                    Picasso.get().load(todosProdutos.getUrlImagem()).resize(width, height).centerInside().into(holder.fotoProdutoCardapio);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.txtNomeProdCardapio.setText(item.getNomeProduto());
        holder.txtDescProdCardapio.setText(item.getDescProduto());
        holder.txtPrecoProdCardapio.setText(item.getPreco());
        holder.txtDispProduto.setText(item.getDisponibilidade());

        holder.linearLayoutProdutosCardapio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mCardapioList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView txtNomeProdCardapio;
        protected TextView txtDescProdCardapio;
        protected TextView txtPrecoProdCardapio;
        protected TextView txtDispProduto;
        protected ImageView fotoProdutoCardapio;
        protected LinearLayout linearLayoutProdutosCardapio;

        public ViewHolder (View itemView){
            super(itemView);
            txtNomeProdCardapio = (TextView)itemView.findViewById(R.id.txtNomeProdCardapio);
            txtDescProdCardapio = (TextView)itemView.findViewById(R.id.txtDescProdCardapio);
            txtPrecoProdCardapio = (TextView)itemView.findViewById(R.id.txtPrecoProdCardapio);
            txtDispProduto = (TextView)itemView.findViewById(R.id.txtDispProduto);
            fotoProdutoCardapio = (ImageView)itemView.findViewById(R.id.fotoProdCardapio);
            linearLayoutProdutosCardapio = (LinearLayout)itemView.findViewById(R.id.linearLayoutProdCardapio);
        }

    }
}
