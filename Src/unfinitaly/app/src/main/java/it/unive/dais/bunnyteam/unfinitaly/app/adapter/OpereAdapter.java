package it.unive.dais.bunnyteam.unfinitaly.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

import it.unive.dais.bunnyteam.unfinitaly.app.R;
import it.unive.dais.bunnyteam.unfinitaly.app.opere.Commento;
import it.unive.dais.bunnyteam.unfinitaly.app.storage.FirebaseUtilities;

/**
 * Created by Enrico on 19/03/2018.
 */

public class OpereAdapter extends RecyclerView.Adapter<OpereAdapter.MyViewHolder>{
    private List<Commento> commentoList;
    private AdapterView.OnItemClickListener onItemClickListener;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nome_commento, data_commento, testo_commento;

        public MyViewHolder(View view) {
            super(view);
            nome_commento = (TextView) view.findViewById(R.id.nome_commento);
            data_commento = (TextView) view.findViewById(R.id.data_commento);
            testo_commento = (TextView) view.findViewById(R.id.testo_commento);
        }
    }

    public OpereAdapter(List<Commento> commentoList) {
        this.commentoList = commentoList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.commenti_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Commento commento = commentoList.get(position);
        holder.nome_commento.setText(commento.getNome_utente());
        holder.data_commento.setText(commento.getData_commento());
        holder.testo_commento.setText(commento.getTesto_commento());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirebaseUtilities.getIstance().isLogged()){
                    Log.d("NOME UTENTE",holder.nome_commento.getText().toString());
                    Log.d("ID COMMENTO",commento.getId_utente().toString());
                    Log.d("ID USER",FirebaseUtilities.getIstance().getIdUtente());
                    if(commento.getId_utente().toString().equals(FirebaseUtilities.getIstance().getIdUtente()))
                        Log.d("USER","ALLOWED TO CHANGE COMMENT");
                }
                else
                    Log.d("TOUCH COMMENTO","UTENTE SLOGGATO");
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentoList.size();
    }
}
