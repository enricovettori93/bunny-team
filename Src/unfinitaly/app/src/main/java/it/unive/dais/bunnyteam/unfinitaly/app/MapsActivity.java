/**
 * Questo package contiene le componenti Android riusabili.
 * Si tratta di classi che contengono già funzionalità base e possono essere riusate apportandovi modifiche
 */
package it.unive.dais.bunnyteam.unfinitaly.app;

import android.Manifest;
import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;

import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import it.unive.dais.bunnyteam.unfinitaly.app.cluster.CustomClusterManager;
import it.unive.dais.bunnyteam.unfinitaly.app.entities.HashMapRegioni;
import it.unive.dais.bunnyteam.unfinitaly.app.regioni.PolygonManager;
import it.unive.dais.bunnyteam.unfinitaly.app.opere.ListaOpereFirebase;
import it.unive.dais.bunnyteam.unfinitaly.app.opere.OperaFirebase;
import it.unive.dais.bunnyteam.unfinitaly.app.storage.FirebaseUtilities;

/**
 * Questa classe è la componente principale del toolkit: fornisce servizi primari per un'app basata su Google Maps, tra cui localizzazione, pulsanti
 * di navigazione, preferenze ed altro. Essa rappresenta un template che è una buona pratica riusabile per la scrittura di app, fungendo da base
 * solida, robusta e mantenibile.
 * Vengono rispettate le convenzioni e gli standard di qualità di Google per la scrittura di app Android; ogni pulsante, componente,
 * menu ecc. è definito in modo che sia facile riprodurne degli altri con caratteristiche diverse.
 * All'interno del codice ci sono dei commenti che aiutano il programmatore ad estendere questa app in maniera corretta, rispettando le convenzioni
 * e gli standard qualitativi.
 * Per scrivere una propria app è necessario modificare questa classe, aggiungendo campi, metodi e codice che svolge le funzionalità richieste.
 *
 * @author BunnyTeam and Alvise Spanò, Università Ca' Foscari
 */
public class MapsActivity extends BaseActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnCameraMoveStartedListener{

    //DICHIARAZIONE VARIABILI
    protected static final int REQUEST_CHECK_SETTINGS = 500;
    protected static final int PERMISSIONS_REQUEST_ACCESS_BOTH_LOCATION = 501;
    private static final String TAG = "MapsActivity";
    private LatLng posItaly = new LatLng(41.87, 12.56);;
    private boolean onBackPressed = false;
    protected boolean firstMapTouch = false;
    protected GoogleMap gMap;
    @Nullable
    protected LatLng currentPosition = null;
    @Nullable
    protected Marker hereMarker = null;
    private CustomClusterManager<OperaFirebase> mClusterManager;
    private ListaOpereFirebase mapMarkers = null;
    private View info;
    private ImageView list;
    SupportMapFragment mapFragment;
    Toolbar toolbar;
    /**
     * API per i servizi di localizzazione.
     */
    protected FusedLocationProviderClient fusedLocationClient;

    /**
     * Inizializza i campi d'istanza, imposta alcuni listener e svolge gran parte delle operazioni "globali" dell'activity.
     *
     * @param savedInstanceState bundle con lo stato dell'activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        // API per i servizi di localizzazione
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // inizializza la mappa asincronamente
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //Creo il drawer
        buildDrawer(toolbar);
        //Istanzio la mappa
        mapFragment.getMapAsync(this);
    }


    // ciclo di vita della app
    //

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("STATO","START");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("STATO","STOP");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d("STATO","RESTART");
        PolygonManager.getIstance().putPolygonRegion(gMap);
    }

    /**
     * Applica le impostazioni (preferenze) della mappa ad ogni chiamata.
     */
    @Override
    protected void onResume() {
        super.onResume();
        applyMapSettings();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Pulisce la mappa quando l'app viene distrutta.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        gMap.clear();
    }

    /**
     * Quando arriva un Intent viene eseguito questo metodo.
     * Può essere esteso e modificato secondo le necessità.
     *
     * @see Activity#onActivityResult(int, int, Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(intent);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // inserire codice qui
                        break;
                    case Activity.RESULT_CANCELED:
                        // o qui
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    /**
     * Questo metodo viene chiamato quando viene richiesto un permesso.
     * Si tratta di un pattern asincrono che va gestito come qui impostato: per gestire nuovi permessi, qualora dovesse essere necessario,
     * è possibile riprodurre un comportamento simile a quello già implementato.
     *
     * @param requestCode  codice di richiesta.
     * @param permissions  array con i permessi richiesti.
     * @param grantResults array con l'azione da intraprende per ciascun dei permessi richiesti.
     * @see Activity#onRequestPermissionsResult(int, String[], int[])
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_BOTH_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "permissions granted: ACCESS_FINE_LOCATION + ACCESS_COARSE_LOCATION");
                } else {
                    Log.e(TAG, "permissions not granted: ACCESS_FINE_LOCATION + ACCESS_COARSE_LOCATION");
                    Snackbar.make(this.findViewById(R.id.map), R.string.msg_permissions_not_granted, Snackbar.LENGTH_LONG);
                }
            }
        }
    }

    // onConnection callbacks
    //
    //

    /**
     * Viene chiamata quando i servizi di localizzazione sono attivi.
     * @param bundle il bundle passato da Android.
     * @see com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks#onConnected(Bundle)
     */
    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "location service connected");
    }

    /**
     * Viene chiamata quando i servizi di localizzazione sono sospesi.
     * @param i un intero che rappresenta il codice della causa della sospenzione.
     * @see com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks#onConnectionSuspended(int)
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "location service connection suspended");
        Toast.makeText(this, R.string.conn_suspended, Toast.LENGTH_LONG).show();
    }

    /**
     * Viene chiamata quando la connessione ai servizi di localizzazione viene persa.
     *
     * @param connectionResult oggetto che rappresenta il risultato della connessione, con le cause della disconnessione ed altre informazioni.
     * @see com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener#onConnectionFailed(ConnectionResult)
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "location service connection lost");
        Toast.makeText(this, R.string.conn_failed, Toast.LENGTH_LONG).show();
    }

    /**
     * Chiamare questo metodo per aggiornare la posizione corrente del GPS.
     * Si tratta di un metodo proprietario, che non ridefinisce alcun metodo della superclasse né implementa alcuna interfaccia:
     * aggiorna asincronamente la posizione corrente del dispositivo mobile.
     */
    public void updateCurrentPosition() {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "requiring permission");
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_BOTH_LOCATION);
        } else {
            Log.d(TAG, "permission granted");
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location loc) {
                            if (loc != null) {
                                currentPosition = new LatLng(loc.getLatitude(), loc.getLongitude());
                                Log.i(TAG, "current position updated");
                            }
                        }
                    });
        }
    }

    public LatLng getCurrentPosition(){
        if(currentPosition == null) {
            return null;
        }
        else
            return currentPosition;
    }

    /**
     * Viene chiamato quando si clicca sulla mappa
     *
     * @param latLng la posizione del click.
     */
    @Override
    public void onMapClick(LatLng latLng) {
        if(findViewById(R.id.marker_window).getVisibility() == View.VISIBLE){
            findViewById(R.id.marker_window).setVisibility(View.INVISIBLE);
        }else{
            if (!firstMapTouch) {
                Toast.makeText(getApplicationContext(), R.string.maps_onmapclick, Toast.LENGTH_SHORT).show();
                firstMapTouch = true;
            }
        }
    }


    /**
     * Viene chiamato quando si clicca a lungo sulla mappa (long click).
     *
     * @param latLng la posizione del click.
     */
    @Override
    public void onMapLongClick(LatLng latLng) {
        Toast.makeText(getApplicationContext(),R.string.maps_onmaplongclick,Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(i);
            }
        }, 700);
    }

    /**
     * Viene chiamato quando si muove la camera.
     * Attenzione: solamente al momento in cui la camera comincia a muoversi, non durante tutta la durata del movimento.
     *
     * @param reason
     */
    @Override
    public void onCameraMoveStarted(int reason) {}

    /*
     * Metodo per controllare la distanza tra la camera e l'italia
     */
    public float[] checkDistanceCamera(CameraPosition posCamera){
        float[] results = new float[1];
        Location.distanceBetween(posItaly.latitude,posItaly.longitude,posCamera.target.latitude,posCamera.target.longitude,results);
        return results;
    }

    /**
     * Questo metodo è molto importante: esso viene invocato dal sistema quando la mappa è pronta.
     * Il parametro è l'oggetto di tipo GoogleMap pronto all'uso, che viene immediatamente assegnato ad un campo interno della
     * classe.
     *
     * @param googleMap oggetto di tipo GoogleMap.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Prendo la lista di opere
        mapMarkers = ListaOpereFirebase.getIstance();
        //Controllo che non sia empty l'array di opere
        if(mapMarkers.getListaOpere().size() == 0){
            Log.d("LISTA OPERE","VUOTA");
            FirebaseUtilities.getIstance().readFromFirebase(this,googleMap);
        }
        else{
            prepareMap(googleMap);
        }
    }

    /**
     * Metodo per preparare la mappa una volta che si ha letto i dati dal DB e la mappa è pronta
     * @param googleMap
     */
    public void prepareMap(GoogleMap googleMap){
        //Setto la mappa
        gMap = googleMap;

        /*prepare the cluster*/
        prepareCluster(googleMap);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_BOTH_LOCATION);
        } else {
            gMap.setMyLocationEnabled(true);
        }

        gMap.setOnMapClickListener(this);
        gMap.setOnMapLongClickListener(this);
        gMap.setOnCameraMoveStartedListener(this);
        gMap.setOnMarkerClickListener(mClusterManager);
        gMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener(){
            @Override
            public void onCameraMove() {
                if(findViewById(R.id.marker_window).getVisibility() == View.VISIBLE && gMap.getCameraPosition().zoom < 8.2){
                    findViewById(R.id.marker_window).setVisibility(View.INVISIBLE);
                }
            }
        });
        gMap.setOnMyLocationButtonClickListener(
                new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        gpsCheck();
                        return false;
                    }
                });
        gMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        gMap.getUiSettings().setMapToolbarEnabled(false);
        gMap.getUiSettings().setZoomGesturesEnabled(true);
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        gMap.getUiSettings().setCompassEnabled(true);
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setMapToolbarEnabled(true);

        list = (ImageView)findViewById(R.id.button_list);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<OperaFirebase> activemarkers;
                String[] stringmarkers;
                activemarkers = mClusterManager.getActiveMarkers();
                stringmarkers = new String[activemarkers.size()];
                for(int i=0;i<activemarkers.size();i++)
                    stringmarkers[i]= "Categoria: " +((OperaFirebase)activemarkers.toArray()[i]).getCategoria()+"\nTipologia CUP: "+((OperaFirebase)activemarkers.toArray()[i]).getTipologia_cup();
                Log.d("SIZE LIST",""+activemarkers.size());
                final AlertDialog alert = new AlertDialog.Builder(thisActivity)
                        .setTitle("Elementi attivi")
                        .setSingleChoiceItems(stringmarkers, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                showMarkerInfo((OperaFirebase)activemarkers.toArray()[id]);
                            }
                        })
                        .setNegativeButton(R.string.msg_back, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .create();
                alert.show();
            }
        });

        //Inserisco le % di opere nelle varie regioni
        ListaOpereFirebase.getIstance().setPercentageRegioni();
        //HashMapRegioni.getIstance().debugPrintPercentage();
        //Applico le varie impostazioni alla mappa
        applyMapSettings();
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posItaly, 5));
        gMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                Log.d("POLYGON", PolygonManager.getIstance().getNomeRegioneById(polygon.getId()));
                /*Controllo se il banner dell'opera è aperto, se si, significa che l'utente non vuole vedere il popup della regione ma chiudere
                il banner dell'opera*/
                if (findViewById(R.id.marker_window).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.marker_window).setVisibility(View.INVISIBLE);
                }
                else{
                    String nomeRegione;
                    TextView nomeregione,opereRegione,percentualeOpere;
                    nomeRegione = PolygonManager.getIstance().getNomeRegioneById(polygon.getId());

                    final AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                    LayoutInflater inflater = thisActivity.getLayoutInflater();
                    View mView = inflater.inflate(R.layout.region_dialog,null);
                    builder.setView(mView)
                            .setNegativeButton("Indietro", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {}
                            });
                    AlertDialog dialog = builder.create();
                    //Puglio le textview dopo la create
                    nomeregione = (TextView)mView.findViewById(R.id.textNomeRegione);
                    opereRegione = (TextView)mView.findViewById(R.id.textTotaleOpere);
                    percentualeOpere =(TextView)mView.findViewById(R.id.textPercOpere);
                    nomeregione.setText("Regione: " + nomeRegione);
                    opereRegione.setText("Numero opere: " + Integer.toString(HashMapRegioni.getIstance().getOpereRegione(nomeRegione)));
                    percentualeOpere.setText("Percentuale rispetto al totale: " + Double.toString(HashMapRegioni.getIstance().getPercentualeRegione(nomeRegione))+"%");
                    //Show del alert dialog
                    dialog.show();
                }
            }
        });
        //Setto le ultime cosine
        updateCurrentPosition();
        createOverlay();
        activateHeatmap();
        createPolygonMap();
    }

    /**
     * Prepara il cluster sul parametro googleMap
     * @param googleMap
     */
    private void prepareCluster(GoogleMap googleMap){
        mClusterManager = new CustomClusterManager<>(this, googleMap);
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        googleMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setMapMarkerList(mapMarkers);
        mClusterManager.cluster();
    }

    /*
     * Creo i poligoni nella mappa
     */
    public void createPolygonMap(){
        Log.d("POLYGON MAP","SETTING");
        PolygonManager.getIstance().putPolygonRegion(gMap);
    }

    /**
     * Metodo proprietario che forza l'applicazione le impostazioni (o preferenze) che riguardano la mappa.
     */
    protected void applyMapSettings() {
        if (gMap != null) {
            gMap.setMapType(SettingsActivity.getMapStyle(this));
        }
    }

    public void animateOnItaly(){
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posItaly, 5));
    }

    /**
     * Naviga dalla posizione from alla posizione to chiamando il navigatore di Google.
     *
     * @param from posizione iniziale.
     * @param to   posizione finale.
     */
    public void navigate(@NonNull LatLng from, @NonNull LatLng to) {
        Intent navigation = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + from.latitude + "," + from.longitude + "&daddr=" + to.latitude + "," + to.longitude + ""));
        navigation.setPackage("com.google.android.apps.maps");
        Log.i(TAG, String.format("starting navigation from %s to %s", from, to));
        startActivity(navigation);
    }

    /**
     * Controlla lo stato del GPS e dei servizi di localizzazione, comportandosi di conseguenza.
     * Viene usata durante l'inizializzazione ed in altri casi speciali.
     */
    protected void gpsCheck() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(MapsActivity.this).addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    /*
     * Quando l'utente fa click sul popup del marker, si apre la scheda relativa
     */
    public void showMarkerInfo(OperaFirebase mapMarker){
        Intent i = new Intent(this, MarkerInfoActivity.class);
        i.putExtra("MapMarker", mapMarker);
        startActivity(i);
    }

    public void onBackPressed(){
        if(drawer.isDrawerOpen())
            drawer.closeDrawer();
        else {
            if (findViewById(R.id.marker_window).getVisibility() == View.VISIBLE) {
                findViewById(R.id.marker_window).setVisibility(View.INVISIBLE);
            }
            else{
                if (onBackPressed) {
                    Intent intent = new Intent(getApplicationContext(), LoadingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
                    startActivity(intent);
                } else {
                    onBackPressed = true;
                    Toast.makeText(this, R.string.maps_onmapbackpress, Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onBackPressed = false;
                        }
                    }, 2000);
                }
            }
        }
    }

    public CustomClusterManager getClusterManager(){
        Log.d("CLUSTER MANAGER","RETURN CLUSTER MANAGER");
        return mClusterManager;
    }

    public GoogleMap getMap(){
        return gMap;
    }

    /*
     * Setta al valore booleano visibility il pulsante che mostra la lista di opere
     */
    public void setIconListVisibility(boolean visibility){
        ImageView icon = (ImageView)findViewById(R.id.button_list);
        if(visibility == true)
            icon.setVisibility(View.VISIBLE);
        else
            icon.setVisibility(View.INVISIBLE);
    }
}

