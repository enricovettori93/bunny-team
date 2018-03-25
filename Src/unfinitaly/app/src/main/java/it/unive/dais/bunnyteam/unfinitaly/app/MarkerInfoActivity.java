package it.unive.dais.bunnyteam.unfinitaly.app;


import android.content.Intent;
import android.os.Bundle;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;


import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.Drawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.unive.dais.bunnyteam.unfinitaly.app.adapter.OpereAdapter;
import it.unive.dais.bunnyteam.unfinitaly.app.entities.Commento;
import it.unive.dais.bunnyteam.unfinitaly.app.marker.MapMarker;
import it.unive.dais.bunnyteam.unfinitaly.app.marker.OperaFirebase;
import it.unive.dais.bunnyteam.unfinitaly.app.storage.FirebaseUtilities;
import it.unive.dais.bunnyteam.unfinitaly.app.view.ProgressBarAnimation;

/**
 *
 * @author BunnyTeam, Università Ca' Foscari
 */
public class MarkerInfoActivity extends BaseActivity {
    OperaFirebase thisMapMarker;
    OperaFirebase passedMapMarker;
    Button insert;
    EditText commento;
    Commento nuovo_commento;
    boolean statoLetturaFirebase;
    private DatabaseReference mDatabase;
    RoundCornerProgressBar rc;
    RecyclerView recyclerView;
    OpereAdapter mAdapter;
    List<Object> commenti_appoggio;
    List<Commento> commenti = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_info);
        rc = (RoundCornerProgressBar) findViewById(R.id.roundcorner);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        insert = (Button) findViewById(R.id.buttonInserisciCommento);
        commento = (EditText) findViewById(R.id.editTextCommento);
        buildDrawer(toolbar);
        toolbar.setTitle("Informazioni opera");
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawer.setOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
            @Override
            public boolean onNavigationClickListener(View clickedView) {
                onBackPressed();
                return true;
            }
        });
        //Setting recycleview commenti
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewCommenti);
        mAdapter = new OpereAdapter(commenti);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        //Leggo il marker
        passedMapMarker = (OperaFirebase) getIntent().getSerializableExtra("MapMarker");
        stillLoading(true);
        statoLetturaFirebase = FirebaseUtilities.getIstance().readOperaSingolaFromFirebase(this, passedMapMarker);
    }

    /**
     * Setta la visibilità delle textview in base allo stato del caricamento
     * @param loading: indica lo stato del caricamento
     */
    public void stillLoading(boolean loading){
        if(loading){
            //Sta caricando
            findViewById(R.id.imgmarker).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView6).setVisibility(View.INVISIBLE);
            findViewById(R.id.tv_categoria).setVisibility(View.INVISIBLE);
            findViewById(R.id.tv_pubblicata_da).setVisibility(View.INVISIBLE);
            findViewById(R.id.tv_sottosettore).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView8).setVisibility(View.INVISIBLE);
            findViewById(R.id.tv_cup).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView10).setVisibility(View.INVISIBLE);
            findViewById(R.id.tv_tipo_cup).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView12).setVisibility(View.INVISIBLE);
            findViewById(R.id.tv_descrizione).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView14).setVisibility(View.INVISIBLE);
            findViewById(R.id.tv_fallimento).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView16).setVisibility(View.INVISIBLE);
            findViewById(R.id.tv_percentuale).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView4).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView5).setVisibility(View.INVISIBLE);
            findViewById(R.id.ImportiSAL).setVisibility(View.INVISIBLE);
            findViewById(R.id.importiQE).setVisibility(View.INVISIBLE);
            findViewById(R.id.roundcorner).setVisibility(View.INVISIBLE);
            findViewById(R.id.buttonInserisciCommento).setVisibility(View.INVISIBLE);
            findViewById(R.id.editTextCommento).setVisibility(View.INVISIBLE);
        }
        else{
            //Ha finito
            findViewById(R.id.aviInfo).setVisibility(View.INVISIBLE);
            findViewById(R.id.imgmarker).setVisibility(View.VISIBLE);
            findViewById(R.id.textView).setVisibility(View.VISIBLE);
            findViewById(R.id.textView6).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_categoria).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_pubblicata_da).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_sottosettore).setVisibility(View.VISIBLE);
            findViewById(R.id.textView8).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_cup).setVisibility(View.VISIBLE);
            findViewById(R.id.textView10).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_tipo_cup).setVisibility(View.VISIBLE);
            findViewById(R.id.textView12).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_descrizione).setVisibility(View.VISIBLE);
            findViewById(R.id.textView14).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_fallimento).setVisibility(View.VISIBLE);
            findViewById(R.id.textView16).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_percentuale).setVisibility(View.VISIBLE);
            findViewById(R.id.textView4).setVisibility(View.VISIBLE);
            findViewById(R.id.textView5).setVisibility(View.VISIBLE);
            findViewById(R.id.ImportiSAL).setVisibility(View.VISIBLE);
            findViewById(R.id.importiQE).setVisibility(View.VISIBLE);
            findViewById(R.id.roundcorner).setVisibility(View.VISIBLE);
        }
    }

    /**
     * Riprende l'esecuzione del codice quando viene letta un'opera dal DB quando si entra in questa activity
     * @param operaLetta: opera che viene letta dal DB quando si entra nell'activity
     */
    public void resumeAfterLoadingFirebase(OperaFirebase operaLetta){
        if(statoLetturaFirebase){
            stillLoading(false);
            thisMapMarker = operaLetta;
            ((TextView)findViewById(R.id.tv_categoria)).setText(thisMapMarker.getCategoria());
            ((TextView)findViewById(R.id.tv_pubblicata_da)).setText(thisMapMarker.getRegione());
            ((TextView)findViewById(R.id.tv_sottosettore)).setText(thisMapMarker.getSottosettore());
            ((TextView)findViewById(R.id.tv_cup)).setText(thisMapMarker.getCup());
            ((TextView)findViewById(R.id.tv_tipo_cup)).setText(thisMapMarker.getTipologia_cup());
            ((TextView)findViewById(R.id.tv_descrizione)).setText(thisMapMarker.getTitle()+" ID_FB: "+thisMapMarker.getId_firebase());
            ((TextView)findViewById(R.id.tv_fallimento)).setText(thisMapMarker.getCausa());
            String sal = String.format("%.2f €",Double.parseDouble(thisMapMarker.getImporto_sal()));
            String qe = String.format("%.2f €",Double.parseDouble(thisMapMarker.getImporto_ultimo_qe()));
            ((TextView)findViewById(R.id.ImportiSAL)).setText(qe);
            ((TextView)findViewById(R.id.importiQE)).setText(sal);
            String percentage = thisMapMarker.getPercentage().replace(".",",")+"%";
            ((TextView)findViewById(R.id.tv_percentuale)).setText(percentage);
            final ProgressBarAnimation mProgressAnimation = new ProgressBarAnimation(rc, 1500);
            rc.setMax(100);
            mProgressAnimation.setProgress((int)Double.parseDouble(thisMapMarker.getPercentage()));
            //Setto i commenti
            setCommenti();
            //Guardo se l'utente è loggato, in caso affermativo gli si da la possibilità di inserire un commento
            mDatabase = FirebaseDatabase.getInstance().getReference();
            if(FirebaseUtilities.getIstance().isLogged()){
                findViewById(R.id.buttonInserisciCommento).setVisibility(View.VISIBLE);
                findViewById(R.id.editTextCommento).setVisibility(View.VISIBLE);
                commento.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        commento.setFocusable(true);
                        commento.setFocusableInTouchMode(true);
                        return false;
                    }
                });
                insert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mDatabase == null){
                            Toast.makeText(getApplicationContext(),"Errore durante la connessione al database",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(commento.getText().equals(""))
                                Toast.makeText(getApplicationContext(),"Testo del commento vuoto.",Toast.LENGTH_SHORT).show();
                            else{
                                Log.d("COMMENTO","ID FIREBASE"+thisMapMarker.getId_firebase());
                                nuovo_commento = new Commento(FirebaseUtilities.getIstance().getIdUtente(),FirebaseUtilities.getIstance().getNome(),commento.getText().toString().replace(",","^^^"),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                                FirebaseDatabase.getInstance().getReference().child("opere").child(thisMapMarker.getId_firebase()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Log.d("DATI CAMBIATI","INSERITO COMMENTO");
                                        Log.d("TOSTRING",dataSnapshot.toString());
                                        commento.setText("");
                                        commento.setHint("Inserisci un commento");
                                        Toast.makeText(getApplicationContext(),"Commento inserito", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(getApplicationContext(),"Errore durante l'inserimento del commento",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                mDatabase.child("opere").child(thisMapMarker.getId_firebase()).child("commenti").child(FirebaseUtilities.getIstance().getNome()+"_"+new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.getDefault()).format(new Date())).setValue(nuovo_commento);
                            }
                        }
                    }
                });
            }
            else{
                //Bottone invisibile, utente non loggato
                insert.setVisibility(View.INVISIBLE);
                commento.setVisibility(View.INVISIBLE);
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Errore durante la lettura nel DB",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Metodo per andare a visualizzare i commenti del mapMarker
     */
    private void setCommenti(){
        commenti.clear();
        if(thisMapMarker.getCommenti().size() > 0){
            commenti_appoggio = new ArrayList<>(thisMapMarker.getCommenti().values());
            Log.d("TOSTRING",""+commenti_appoggio.toString());
            Log.d("COMMENTI","NUMERO:"+thisMapMarker.getCommenti().size());
            for(Object o:commenti_appoggio){
                Log.d("OGGETTO_COMMENTO",o.toString());
                try{
                    //Parser creato al volo di prova, non so come cazzo risolvere al momento
                    String[] splitvirgola = o.toString().substring(1,o.toString().length()-1).split(",");
                    Log.d("VIRGOLA",splitvirgola[0]+"_"+splitvirgola[1]+"_"+splitvirgola[2]+"_"+splitvirgola[3]);

                    String[] data_commento = splitvirgola[0].split("=");
                    Log.d("DATA",data_commento[0]+"_"+data_commento[1]);

                    String[] testo_commento = splitvirgola[1].replace("^^^",",").split("=");
                    Log.d("TESTO",testo_commento[0]+"_"+testo_commento[1]);

                    String[] id_utente = splitvirgola[2].split("=");
                    Log.d("ID",id_utente[0]+"_"+id_utente[1]);

                    String[] nome_utente = splitvirgola[3].split("=");
                    Log.d("NOME",nome_utente[0]+"_"+nome_utente[1]);

                    commenti.add(new Commento(id_utente[1],nome_utente[1],testo_commento[1],data_commento[1]));
                }catch(ArrayIndexOutOfBoundsException e){
                    commenti.add(new Commento("","Errore lettura del commento.","",""));
                }
            }
        }
        else{
            commenti.add(new Commento("","Nessun commento disponibile","",""));
            Log.d("COMMENTI","NESSUNO");
        }
        if(commenti.size()>1){
            //Ordino in base alla data
            Collections.sort(commenti, new Comparator<Commento>() {
                @Override
                public int compare(Commento commento, Commento t1) {
                    long data1,data2;
                    data1 = Long.parseLong(commento.getData_commento().replace("-","").replace(":","").replace(" ",""));
                    data2 = Long.parseLong(t1.getData_commento().replace("-","").replace(":","").replace(" ",""));
                    Log.d("SORT COMMENTI",""+data1+"_"+data2);
                    return (int)(data1 - data2);
                }
            });
        }
        mAdapter.notifyDataSetChanged();
    }
}

