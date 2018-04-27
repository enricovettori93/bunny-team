package it.unive.dais.bunnyteam.unfinitaly.app.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import it.unive.dais.bunnyteam.unfinitaly.app.MapsActivity;
import it.unive.dais.bunnyteam.unfinitaly.app.MarkerInfoActivity;
import it.unive.dais.bunnyteam.unfinitaly.app.R;
import it.unive.dais.bunnyteam.unfinitaly.app.opere.Commento;
import it.unive.dais.bunnyteam.unfinitaly.app.opere.OperaFirebase;
import it.unive.dais.bunnyteam.unfinitaly.app.storage.FirebaseUtilities;

/**
 * Created by Enrico on 19/03/2018.
 */

public class OpereAdapter extends RecyclerView.Adapter<OpereAdapter.MyViewHolder>{
    private List<Commento> commentoList;
    private AdapterView.OnItemClickListener onItemClickListener;
    private MarkerInfoActivity context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nome_commento, data_commento, testo_commento;
        public ImageView delete_commento;
        public MyViewHolder(View view) {
            super(view);
            nome_commento = (TextView) view.findViewById(R.id.nome_commento);
            data_commento = (TextView) view.findViewById(R.id.data_commento);
            testo_commento = (TextView) view.findViewById(R.id.testo_commento);
            delete_commento = (ImageView) view.findViewById(R.id.imageDeleteComment);
        }
    }

    public OpereAdapter(List<Commento> commentoList, MarkerInfoActivity context) {
        this.commentoList = commentoList;
        this.context = context;
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
        holder.delete_commento.setVisibility(View.INVISIBLE);
        //Controllo se l'utente sia loggato
        if(FirebaseUtilities.getIstance().isLogged()) {
            if (commento.getId_utente().toString().equals(FirebaseUtilities.getIstance().getIdUtente())) {
                //Se è loggato ed è il proprietario del commento, allora rendo visibile il cestino e associo il listener
                holder.delete_commento.setVisibility(View.VISIBLE);
                holder.delete_commento.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(context)
                                .setTitle("Cancellare il commento?")
                                .setMessage("Attenzione, l'azione è irreversibile.")
                                .setPositiveButton("Procedi", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String childCommentName = holder.nome_commento.getText().toString()+"_"+holder.data_commento.getText().toString().replace(":","").replace(" ","_");
                                        Log.d("ID_OPERA",context.getIdActive());
                                        Log.d("CHILD DA ELIMINARE",childCommentName);
                                        DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("opere").child(context.getIdActive()).child("commenti").child(childCommentName);
                                        data.removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                Toast.makeText(context.getApplicationContext(),"Operazione riuscita",Toast.LENGTH_SHORT).show();
                                             }
                                        });
                                    }
                                })
                                .setNegativeButton("Indietro", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //nothing
                                    }
                                }).create();
                        dialog.show();
                        Log.d("USER", "ALLOWED TO DELETE COMMENT");
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return commentoList.size();
    }
}
