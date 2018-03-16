package it.unive.dais.bunnyteam.unfinitaly.app;


import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    Button insert;
    EditText commento;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_info);
        RoundCornerProgressBar rc = (RoundCornerProgressBar) findViewById(R.id.roundcorner);
        final Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        insert = (Button)findViewById(R.id.buttonInserisciCommento);
        commento = (EditText)findViewById(R.id.editTextCommento);
        buildDrawer(toolbar);
        toolbar.setTitle("Informazioni opera");
        thisMapMarker = (OperaFirebase)getIntent().getSerializableExtra("MapMarker");
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
        String percentage = thisMapMarker.getPercentage()+"%";
        ((TextView)findViewById(R.id.tv_percentuale)).setText(percentage);
        final ProgressBarAnimation mProgressAnimation = new ProgressBarAnimation(rc, 1500);
        rc.setMax(100);
        mProgressAnimation.setProgress((int)Double.parseDouble(thisMapMarker.getPercentage()));
        final LatLng coordMapM = thisMapMarker.getPosition();
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawer.setOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
            @Override
            public boolean onNavigationClickListener(View clickedView) {
                onBackPressed();
                return true;
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(FirebaseUtilities.getIstance().isLogged()){
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
                            Commento nuovo_commento = new Commento(FirebaseUtilities.getIstance().getIdUtente(),FirebaseUtilities.getIstance().getNome(),commento.getText().toString());
                            FirebaseDatabase.getInstance().getReference().child("opere").child(thisMapMarker.getId_firebase()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.d("DATI CAMBIATI","INSERITO COMMENTO");
                                    Log.d("TOSTRING",dataSnapshot.toString());
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
}

