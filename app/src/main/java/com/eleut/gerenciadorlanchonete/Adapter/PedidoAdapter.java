package com.eleut.gerenciadorlanchonete.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eleut.gerenciadorlanchonete.Activity.MainActivity;
import com.eleut.gerenciadorlanchonete.Activity.PedidoActivity;
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

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.ViewHolder> {

    private List<Pedido> mPedidoList;
    private Context context;
    private DatabaseReference databaseRef;
    private List<Pedido> pedidos;
    private Pedido todosPedidos;

    public PedidoAdapter(List<Pedido> l, Context c){
        context = c;
        mPedidoList = l;
    }

    @Override
    public PedidoAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_todos_pedidos, viewGroup, false);
        return new PedidoAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PedidoAdapter.ViewHolder holder, int position) {
        final Pedido item = mPedidoList.get(position);
        pedidos = new ArrayList<>();

        databaseRef = FirebaseDatabase.getInstance().getReference();
        databaseRef.child("pedidos").orderByChild("keyPedido").equalTo(item.getKeyPedido()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pedidos.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    todosPedidos = postSnapshot.getValue(Pedido.class);
                    pedidos.add(todosPedidos);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.txtNomeCliPedido.setText(item.getNomeCliPedido());
        holder.txtDescPedidoAberto.setText("Descrição: " + item.getDescPedido());
        holder.txtDatetimeAberturaPedido.setText("Aberto em "+item.getAberturaPedido());
        holder.txtSituacaoPedido.setText(item.getSituacaoPedido());
        //holder.txtKeyPedido.setText(item.getKeyPedido());


        holder.linearLayoutPedidoAberto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(PedidoAdapter.this, "It Works!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPedidoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView txtNomeCliPedido;
        protected TextView txtDescPedidoAberto;
        protected TextView txtDatetimeAberturaPedido;
        protected TextView txtSituacaoPedido;
        //protected TextView txtKeyPedido;
        protected LinearLayout linearLayoutPedidoAberto;

        public ViewHolder (View itemView){
            super(itemView);
            txtNomeCliPedido = (TextView)itemView.findViewById(R.id.txtNomeCliPedido);
            txtDescPedidoAberto = (TextView)itemView.findViewById(R.id.txtDescPedidoAberto);
            txtDatetimeAberturaPedido = (TextView)itemView.findViewById(R.id.txtDatetimeAberturaPedido);
            txtSituacaoPedido = (TextView)itemView.findViewById(R.id.txtSituacaoPedido);
            //txtKeyPedido = (TextView)itemView.findViewById(R.id.txtKeyPedido);
            linearLayoutPedidoAberto = (LinearLayout)itemView.findViewById(R.id.linearLayoutPedidoAberto);
        }

    }

}
