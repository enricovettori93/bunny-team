package it.unive.dais.bunnyteam.unfinitaly.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import java.util.Map;

import it.unive.dais.bunnyteam.unfinitaly.lib.util.UnexpectedException;


/**
 * Activity che rappresenta la schermata delle impostazioni accessibile tramite il menu.
 * Contiene metodi e utilità per la manipolazione rapida e sicura delle preferenze.
 * Per aggiungere impostazioni all'app, è necessario aggiungere qui ulteriore codice, riproducendo pattern e convenzioni simili
 * a quelle implementate.
 *
 * @author Alvise Spanò, Università Ca' Foscari
 */
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener, AppCompatCallback{
    /**
     * Costante di tipo stringa che indica la chiave dello stile della mappa nell'XML delle preferenze.
     */
    public static final String KEY_MAP_STYLE = "pref_mapStyle";
    /**
     * Costante di tipo stringa che indica la chiave della soglia di zoom nell'XML delle preferenze.
     */
    public static final String KEY_ZOOM_THRESHOLD = "pref_zoomThreshold";

    /**
     * Getter dell'impostazione relativa allo stile della mappa.
     *
     * @param ctx oggetto di tipo Context.
     * @return l'intero
     */
    public static int getMapStyle(Context ctx) {
        return getMapStyle(PreferenceManager.getDefaultSharedPreferences(ctx));
    }

    /**
     * Getter dell'impostazione relativa allo stile della mappa.
     *
     * @param sp oggetto SharedPreferences da cui vengono estratte le impostazioni.
     * @return ritorna la costante intera che rappresenta lo stile della mappa.
     * @see GoogleMap
     */
    public static int getMapStyle(SharedPreferences sp) {
        int n = Integer.parseInt(sp.getString(KEY_MAP_STYLE, "0"));
        switch (n) {
            case 0:
                return GoogleMap.MAP_TYPE_NORMAL;
            case 1:
                return GoogleMap.MAP_TYPE_SATELLITE;
            case 2:
                return GoogleMap.MAP_TYPE_TERRAIN;
            case 3:
                return GoogleMap.MAP_TYPE_HYBRID;
            default:
                throw new UnexpectedException(String.format("undefined map style value: %d", n));
        }
    }

    /**
     * Questo metodo viene chiamato quando questa activity parte.
     *
     * @param savedInstanceState stato dell'activity salvato precedentemente (opzionale).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        addPreferencesFromResource(R.xml.preferences);
        updateAllSummaries();
        AppCompatDelegate delegate = AppCompatDelegate.create(this, this);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        //Creo e sistemo la toolbar
        delegate.onCreate(savedInstanceState);
        delegate.getSupportActionBar();
        delegate.setSupportActionBar(toolbar);
        toolbar.setTitle("Impostazioni");
        delegate.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        delegate.getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Chiamato quando si seleziona una voce del menu.
     *
     * @param item oggetto che rappresenta la voce del menu cliccata.
     * @return ritorna true per continuare a chiamare altre callback; false altrimenti.
     * @see Activity#onOptionsItemSelected(MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("SETTINGS","MENU ITEM "+item.toString());
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Metodo proprietario che aggiorna tutti i summary delle impostazioni.
     * I summary sono le brevi descrizioni sotto ciascuna voce cliccabile, e devono mostrare
     * l'impostazione attuale.
     * @see SettingsActivity#updateSummaryWithActiveValue(Preference, String)
     */
    protected void updateAllSummaries() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // trucco per aggiornare i summary automagicamente
        for (Map.Entry<String, ?> e : sp.getAll().entrySet()) {
            onSharedPreferenceChanged(sp, e.getKey());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * Chiamato quando viene modificata una impostazione.
     * Questo è il metodo principale dove fare qualcosa quando una impostazione viene cambiata dall'utente.
     * Tutti i comportamenti che scattano alla modifica di una preferenza vanno in qualche modo messi qui.
     * Nel template qui fornito non ci sono comportamenti di questa categoria, ma le preferenze vengono ispezionate
     * esplicitamente alla bisogna tramite i getter forniti.
     * Questo metodo si occupa anche del tenere aggiornati i summary.
     *
     * @param sp l'oggetto SharedPreference.
     * @param k la chiave delle preferenze che è stata modificata.
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sp, String k) {
        Preference p = findPreference(k);
        switch (k) {
            case KEY_MAP_STYLE: {
                int n = getMapStyle(sp);
                String s;
                switch (n) {
                    case GoogleMap.MAP_TYPE_NORMAL:
                        s = getString(R.string.menu_mapStyle_normal);
                        break;
                    case GoogleMap.MAP_TYPE_SATELLITE:
                        s = getString(R.string.menu_mapStyle_satellite);
                        break;
                    case GoogleMap.MAP_TYPE_TERRAIN:
                        s = getString(R.string.menu_mapStyle_terrain);
                        break;
                    case GoogleMap.MAP_TYPE_HYBRID:
                        s = getString(R.string.menu_mapStyle_hybrid);
                        break;
                    default:
                        throw new UnexpectedException(String.format("undefined map style value: %d", n));
                }
                updateSummaryWithActiveValue(p, s);
                break;
            }
        }
    }

    /**
     * Metodo proprietario che aggiorna il summary di una impostazione data.
     * I summary sono le brevi descrizioni sotto ciascuna voce cliccabile, e devono mostrare
     * l'impostazione attuale.
     * Questo metodo renderizza correttamente tale summary.
     *
     * @param p l'oggetto di tipo Preference.
     * @param s la stringa da mostrare come valore attualmente attivo nel summary.
     */
    private void updateSummaryWithActiveValue(Preference p, String s) {
        String[] ss = p.getSummary().toString().split("\\.");
        p.setSummary(String.format("%s. (%s)", ss[0], s));
    }

    /**
     * Lasciata vuota.
     * @see AppCompatCallback#onSupportActionModeStarted(ActionMode)
     * @param mode action mode.
     */
    @Override
    public void onSupportActionModeStarted(ActionMode mode) {

    }

    /**
     * Lasciata vuota.
     * @see AppCompatCallback#onSupportActionModeFinished(ActionMode)
     * @param mode action mode.
     */
    @Override
    public void onSupportActionModeFinished(ActionMode mode) {

    }

    /**
     * Lasciata vuota.
     * @see AppCompatCallback#onWindowStartingSupportActionMode(ActionMode.Callback)
     * @param callback callback passata dal sistema.
     */
    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }
}
