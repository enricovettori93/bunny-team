package it.unive.dais.bunnyteam.unfinitaly.app;


import android.os.Bundle;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;


import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.Drawer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.unive.dais.bunnyteam.unfinitaly.app.adapter.OpereAdapter;
import it.unive.dais.bunnyteam.unfinitaly.app.opere.Commento;
import it.unive.dais.bunnyteam.unfinitaly.app.opere.OperaFirebase;
import it.unive.dais.bunnyteam.unfinitaly.app.storage.FirebaseUtilities;
import it.unive.dais.bunnyteam.unfinitaly.app.view.ProgressBarAnimation;

/**
 *
 * @author BunnyTeam, Università Ca' Foscari
 */
public class MarkerInfoActivity extends BaseActivity {
    private OperaFirebase thisMapMarker, passedMapMarker;
    private Button insert;
    private EditText commento;
    private TextView countChar;
    private Commento nuovo_commento;
    boolean statoLetturaFirebase;
    private DatabaseReference mDatabase;
    private RoundCornerProgressBar rc;
    private RecyclerView recyclerView;
    private OpereAdapter mAdapter;
    private List<Commento> commenti = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_info);
        rc = (RoundCornerProgressBar) findViewById(R.id.roundcorner);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        insert = (Button) findViewById(R.id.buttonInserisciCommento);
        countChar = (TextView) findViewById(R.id.textViewCountChar);
        commento = (EditText) findViewById(R.id.editTextCommento);
        commento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                countChar.setText(String.format("%s/500",commento.getText().toString().length()));
                if(commento.getText().toString().length() > 500)
                    Toast.makeText(getApplicationContext(),"Lunghezza massima raggiunta.",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
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
        mAdapter = new OpereAdapter(commenti,this);
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
            findViewById(R.id.loading_spinner).setVisibility(View.VISIBLE);
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
            findViewById(R.id.textViewCountChar).setVisibility(View.INVISIBLE);
            findViewById(R.id.editTextCommento).setVisibility(View.INVISIBLE);
        }
        else{
            //Ha finito
            findViewById(R.id.loading_spinner).setVisibility(View.INVISIBLE);
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

    public String getIdActive(){
        return thisMapMarker.getId_firebase();
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
            ((TextView)findViewById(R.id.ImportiSAL)).setText(String.format("%.2f €",Double.parseDouble(thisMapMarker.getImporto_sal())));
            ((TextView)findViewById(R.id.importiQE)).setText(String.format("%.2f €",Double.parseDouble(thisMapMarker.getImporto_ultimo_qe_approvato())));
            ((TextView)findViewById(R.id.tv_percentuale)).setText(thisMapMarker.getPercentage().replace(".",",")+"%");
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
                findViewById(R.id.textViewCountChar).setVisibility(View.VISIBLE);
                countChar.setText(String.format("%s/500",commento.getText().toString().length()));
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
                            if(commento.getText().toString().trim().isEmpty())
                                Toast.makeText(getApplicationContext(),"Testo del commento vuoto.",Toast.LENGTH_SHORT).show();
                            else{
                                Log.d("COMMENTO","ID FIREBASE"+thisMapMarker.getId_firebase());
                                nuovo_commento = new Commento(FirebaseUtilities.getIstance().getIdUtente(),FirebaseUtilities.getIstance().getNome(),commento.getText().toString().trim(),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
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
        //Prendo i commenti dal database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("opere").child(thisMapMarker.getId_firebase()).child("commenti");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() == 0){
                    //Non ho commenti
                    if(FirebaseUtilities.getIstance().isLogged() ? commenti.add(new Commento("","Nessun commento scritto.","Vuoi essere il primo a scrivere?","")) : commenti.add(new Commento("","Nessun commento scritto.","Esegui il login per commentare.","")));
                    Log.d("COMMENTI","NESSUNO");
                }
                else{
                    //Ho almeno un commento
                    for(DataSnapshot dato: dataSnapshot.getChildren()){
                        commenti.add(dato.getValue(Commento.class));
                    }
                }
                //Ordino i commenti in base alla data
                Collections.sort(commenti, new Comparator<Commento>() {
                    @Override
                    public int compare(Commento commento, Commento t1) {
                        long data1,data2;
                        try{
                            data1 = Long.parseLong(commento.getData_commento().replace("-","").replace(":","").replace(" ",""));
                            data2 = Long.parseLong(t1.getData_commento().replace("-","").replace(":","").replace(" ",""));
                            Log.d("SORT COMMENTI",""+data1+"_"+data2);
                            return (int)(data1 - data2);
                        }catch(Exception e) {
                            Log.e("MarkerInfo","CRASH " + e);
                            return 1;
                        }
                    }
                });
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Errore lettura nei commenti.",Toast.LENGTH_SHORT).show();
                Log.d("COMMENTI","ERRORE LETTURA");
            }
        });
    }
}

