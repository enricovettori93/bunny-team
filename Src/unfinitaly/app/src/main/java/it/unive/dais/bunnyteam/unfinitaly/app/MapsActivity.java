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
import android.graphics.Color;
import android.location.Location;
import android.media.Image;
import android.net.Uri;

import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import android.widget.ImageView;
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
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;

import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mikepenz.materialdrawer.model.ContainerDrawerItem;

import java.io.IOException;
import java.util.ArrayList;

import it.unive.dais.bunnyteam.unfinitaly.app.cluster.CustomClusterManager;
import it.unive.dais.bunnyteam.unfinitaly.app.entities.HashMapRegioni;
import it.unive.dais.bunnyteam.unfinitaly.app.factory.RegioniFactory;
import it.unive.dais.bunnyteam.unfinitaly.app.marker.ListaOpereFirebase;
import it.unive.dais.bunnyteam.unfinitaly.app.marker.MapMarker;
import it.unive.dais.bunnyteam.unfinitaly.app.marker.MapMarkerList;
import it.unive.dais.bunnyteam.unfinitaly.app.marker.OperaFirebase;

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
    private Polygon abruzzo, basilicata, campania, calabria, emilia, friuli, lazio, liguria, lombardia, marche, molise, piemonte, puglia, sardegna, sicilia, toscana, trentino, umbria, valleaosta, veneto;
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
        mapMarkers = ListaOpereFirebase.getIstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        buildDrawer(toolbar);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        // API per i servizi di localizzazione
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // inizializza la mappa asincronamente
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    // ciclo di vita della app
    //

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Applica le impostazioni (preferenze) della mappa ad ogni chiamata.
     */
    @Override
    protected void onResume() {
        super.onResume();
        //TODO: rileggere i dati da firebase
        applyMapSettings();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Maps", "OnPause");
    }

    /**
     * Pulisce la mappa quando l'app viene distrutta.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        gMap.clear();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Maps", "on Restart");
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
        gMap = googleMap;
        mClusterManager = new CustomClusterManager<>(this, googleMap);

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
                Log.d("zoom",""+gMap.getCameraPosition().zoom);
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

        /*prepare the cluster*/
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        googleMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setMapMarkerList(mapMarkers);
        mClusterManager.cluster();

        list = (ImageView)findViewById(R.id.button_list);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<OperaFirebase> activemarkers;
                String[] stringmarkers;
                activemarkers = mClusterManager.getActiveMarkers();
                stringmarkers = new String[activemarkers.size()];
                for(int i=0;i<activemarkers.size();i++)
                    stringmarkers[i]= "Categoria: " +((OperaFirebase)activemarkers.toArray()[i]).getCategoria()+"\n"+((OperaFirebase)activemarkers.toArray()[i]).getTipologia_cup();
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
        //googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.bunnyteam2_map));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posItaly, 5));
        updateCurrentPosition();
        // TODO: riattivare dopo aver fixato -> createOverlay();
        createOverlay();
        activateHeatmap();
        createPolygonMap();
    }

    /*
     * Setta al valore booleano status la proprietà visible dei poligoni
     */
    public void setVisibilityPolygon(boolean status){
        abruzzo.setVisible(status);
        basilicata.setVisible(status);
        campania.setVisible(status);
        calabria.setVisible(status);
        emilia.setVisible(status);
        friuli.setVisible(status);
        lazio.setVisible(status);
        liguria.setVisible(status);
        lombardia.setVisible(status);
        marche.setVisible(status);
        molise.setVisible(status);
        piemonte.setVisible(status);
        puglia.setVisible(status);
        sardegna.setVisible(status);
        sicilia.setVisible(status);
        toscana.setVisible(status);
        trentino.setVisible(status);
        umbria.setVisible(status);
        valleaosta.setVisible(status);
        veneto.setVisible(status);
    }

    /*
     * Creo i poligoni nella mappa
     */
    public void createPolygonMap(){
        Log.d("POLYGON MAP","SETTING");
        abruzzo = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Abruzzo"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Abruzzo")));
        basilicata = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Basilicata"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Basilicata")));
        campania = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Campania"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Campania")));
        calabria = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Calabria"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Calabria")));
        emilia = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Emilia-Romagna"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Emilia-Romagna")));
        friuli = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Friuli-Venezia Giulia"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Friuli-Venezia Giulia")));
        lazio = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Lazio"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Lazio")));
        liguria = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Liguria"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Liguria")));
        lombardia = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Lombardia"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Lombardia")));
        marche = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Marche"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Marche")));
        molise = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Molise"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Molise")));
        piemonte = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Piemonte"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Piemonte")));
        puglia = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Puglia"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Puglia")));
        sardegna = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Sardegna"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Sardegna")));
        sicilia = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Sicilia"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Sicilia")));
        toscana = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Toscana"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Toscana")));
        trentino = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Trentino-Alto Adige"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Trentino-Alto Adige")));
        umbria = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Umbria"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Umbria")));
        valleaosta = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Valle d'Aosta"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Valle d'Aosta")));
        veneto = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Veneto"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Veneto")));
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
        else{
            if(findViewById(R.id.marker_window).getVisibility()==View.VISIBLE)
                findViewById(R.id.marker_window).setVisibility(View.INVISIBLE);
            else if(onBackPressed){
                /*è stato premuto una volta. Lo ripremiamo, quindi dovremmo uscire*/
                Intent intent = new Intent(getApplicationContext(), LoadingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
            }
            else{
                onBackPressed=true;
                Toast.makeText(this, R.string.maps_onmapbackpress, Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed=false;
                    }
                }, 2000);
            }
        }
    }

    public CustomClusterManager getClusterManager(){
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

