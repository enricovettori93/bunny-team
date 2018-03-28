package it.unive.dais.bunnyteam.unfinitaly.app;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.mikepenz.materialdrawer.*;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;

import java.util.ArrayList;
import java.util.Collections;

import it.unive.dais.bunnyteam.unfinitaly.app.entities.User;
import it.unive.dais.bunnyteam.unfinitaly.app.regioni.PolygonManager;
import it.unive.dais.bunnyteam.unfinitaly.app.storage.FirebaseUtilities;
import it.unive.dais.bunnyteam.unfinitaly.app.testing.TestFirebase;

/**
 *
 * @author BunnyTeam, Università Ca' Foscari
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Drawer drawer;
    protected Activity thisActivity;
    protected ArrayList<Integer> selectedRegionsItems = new ArrayList<>();
    protected ArrayList<Integer> selectedCategoriesItems = new ArrayList<>();
    boolean resetfilter;
    Uri imageprofile;
    TileOverlay mOverlay;
    HeatmapTileProvider mProvider;
    PrimaryDrawerItem user,informazioni,impostazioni,reset,regione,categoria,mappa;
    PrimaryDrawerItem testing;
    SwitchDrawerItem percentuale,distribuzione,percentualeRegione;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void buildDrawer(Toolbar toolbar) {
        thisActivity = this;
        resetfilter = false;
        setSupportActionBar(toolbar);
        AccountHeader headerResult;
        if(!FirebaseUtilities.getIstance().isLogged()){
            //Utente non loggato
            headerResult = new AccountHeaderBuilder()
                    .withActivity(this)
                    .withSelectionListEnabledForSingleProfile(false)
                    .withHeaderBackground(R.drawable.background)
                    .withProfileImagesVisible(false)
                    .addProfiles(
                            new ProfileDrawerItem().withName(R.string.menu_account_nologin).withEmail(R.string.menu_email_nologin)
                    )
                    .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                        @Override
                        public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                            startInfoActivity();
                            return false;
                        }
                    })
                    .build();
        }
        else{
            //Utente loggato
            imageprofile = FirebaseUtilities.getIstance().getFotoProfilo();
            Log.d("URI IMAGE:",""+imageprofile);
            if(imageprofile != null){
                //L'immagine profilo è presente
                //CODICE CHE FA COSE DA SOLO PER INSERIRE L'IMMAGINE PROFILO
                DrawerImageLoader.init(new AbstractDrawerImageLoader() {
                    @Override
                    public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                        Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
                    }

                    @Override
                    public void cancel(ImageView imageView) {
                        Glide.clear(imageView);
                    }
                });
                headerResult = new AccountHeaderBuilder()
                        .withActivity(this)
                        .withSelectionListEnabledForSingleProfile(false)
                        .withHeaderBackground(R.drawable.background)
                        .addProfiles(
                                new ProfileDrawerItem().withName(User.getIstance().getName()).withEmail(User.getIstance().getEmail()).withIcon(imageprofile)
                        )
                        .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                            @Override
                            public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                                startAccountActivity();
                                return false;
                            }
                        })
                        .build();
            }
            else{
                //L'immagine profilo non è presente
                headerResult = new AccountHeaderBuilder()
                        .withActivity(this)
                        .withSelectionListEnabledForSingleProfile(false)
                        .withHeaderBackground(R.drawable.background)
                        .addProfiles(
                                new ProfileDrawerItem().withName(User.getIstance().getName()).withEmail(User.getIstance().getEmail()).withIcon(R.drawable.ic_account_circle_black_24dp)
                        )
                        .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                            @Override
                            public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                                startAccountActivity();
                                return false;
                            }
                        })
                        .build();
            }
        }

        //Creazione voci di menu
        user = new PrimaryDrawerItem().withIdentifier(10).withName(R.string.menu_user).withIcon(R.drawable.ic_account_circle_black_24dp);
        informazioni = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.menu_informazioni).withIcon(R.drawable.info);
        impostazioni = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.menu_impostazioni).withIcon(R.drawable.settings);
        testing = new PrimaryDrawerItem().withIdentifier(99).withName("Test");
        user.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                if(FirebaseUtilities.getIstance().isLogged()){
                    if(thisActivity instanceof AccountActivity)
                        drawer.setSelection(-1);
                    else
                        startAccountActivity();
                    return false;
                }
                else{
                    startLoginActivity();
                    drawer.setSelection(-1);
                    return false;
                }
            }
        });
        informazioni.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                startInfoActivity();
                drawer.setSelection(-1);
                return false;
            }
        });
        impostazioni.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                startSettingsActivity();
                drawer.setSelection(-1);
                return false;
            }
        });
        testing.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                Intent i = new Intent(getApplicationContext(), TestFirebase.class);
                startActivity(i);
                return false;
            }
        });
        if (this instanceof MapsActivity) {
            //CREO I PULSANTI
            reset = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.menu_reset_filtri).withIcon(R.drawable.unset);
            regione = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.menu_regione).withIcon(R.drawable.regione);
            categoria = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.menu_categoria).withIcon(R.drawable.categoria);
            percentuale = new SwitchDrawerItem().withIdentifier(4).withName(R.string.menu_percentuale).withIcon(R.drawable.percentage);
            distribuzione = new SwitchDrawerItem().withIdentifier(5).withName(R.string.menu_distribuzione).withIcon(R.drawable.distribuzione).withChecked(true);
            percentualeRegione = new SwitchDrawerItem().withIdentifier(6).withName(R.string.menu_percentuale_regione).withIcon(R.drawable.ic_dashboard_black_24dp).withChecked(false);
            //Associazione listener alle varie voci
            reset.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                    ((MapsActivity)thisActivity).getClusterManager().resetMarkers();
                    if(percentuale.isChecked()){
                        percentuale.withChecked(false);
                        drawer.updateItem(percentuale);
                        ((MapsActivity)thisActivity).getClusterManager().unsetPercentageRender();
                    }
                    if(distribuzione.isChecked()){
                        distribuzione.withChecked(false);
                        drawer.updateItem(distribuzione);
                        mOverlay.setVisible(false);
                    }
                    resetfilter = true;
                    ((MapsActivity)thisActivity).getClusterManager().resetFlags();
                    ((MapsActivity)thisActivity).setIconListVisibility(false);
                    drawer.setSelection(-1);
                    return false;
                }
            });
            regione.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                    drawer.setSelection(-1);
                    showAlertDialogRegions();
                    return false;
                }
            });
            categoria.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                    drawer.setSelection(-1);
                    showAlertDialogCategory();
                    return false;
                }
            });
            percentuale.withOnCheckedChangeListener(new OnCheckedChangeListener() {
                public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        ((MapsActivity)thisActivity).getClusterManager().setPercentageRenderer();
                    }
                    else{
                        ((MapsActivity)thisActivity).getClusterManager().unsetPercentageRender();
                    }
                    drawer.setSelection(-1);
                }
            });
            distribuzione.withOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        if(percentualeRegione.isChecked()){
                            percentualeRegione.withChecked(false);
                            drawer.updateItem(percentualeRegione);
                            PolygonManager.getIstance().setVisibilityPolygon(false);
                        }
                        mOverlay.setVisible(true);
                    }
                    else{
                        mOverlay.setVisible(false);
                    }
                    drawer.setSelection(-1);
                }
            });
            percentualeRegione.withOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        if(distribuzione.isChecked()){
                            distribuzione.withChecked(false);
                            drawer.updateItem(distribuzione);
                            mOverlay.setVisible(false);
                        }
                        PolygonManager.getIstance().setVisibilityPolygon(true);
                    }
                    else
                        PolygonManager.getIstance().setVisibilityPolygon(false);
                    drawer.setSelection(-1);
                }
            });

            drawer = new com.mikepenz.materialdrawer.DrawerBuilder()
                    .withActivity(this)
                    .withAccountHeader(headerResult)
                    .withToolbar(toolbar)
                    .withSelectedItem(-1)
                    .addDrawerItems(
                           user,new DividerDrawerItem(),reset, regione, categoria, percentuale, percentualeRegione, distribuzione, new DividerDrawerItem(), informazioni, impostazioni, new DividerDrawerItem(),testing
                    )
                    .build();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
        } else {
            mappa = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.menu_mappa).withIcon(R.drawable.maps);
            mappa.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                    startMapsActivity();
                    return false;
                }
            });
            drawer = new com.mikepenz.materialdrawer.DrawerBuilder()
                    .withActivity(this)
                    .withAccountHeader(headerResult)
                    .withToolbar(toolbar)
                    .withSelectedItem(-1)
                    .addDrawerItems(
                            user,new DividerDrawerItem(),mappa, new DividerDrawerItem(), informazioni, impostazioni, new DividerDrawerItem()
                    )
                    .build();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
        }
    }

    public void createOverlay(){
        //Voglio che mProvider sia un sigleton, di conseguenza anche mOverlay sarà singleton
        if(mProvider == null){
            mProvider = new HeatmapTileProvider.Builder()
                    .data(((MapsActivity) thisActivity).getClusterManager().getCoordList())
                    .build();
            mOverlay = ((MapsActivity) thisActivity).getMap().addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        }
    }

    private void startLoginActivity(){
        Intent i = new Intent(this,LoginActivity.class);
        i.putExtra("Activity","Base");
        startActivity(i);
    }

    private void startSettingsActivity() {
        Intent intent_info = new Intent(this, SettingsActivity.class);
        startActivity(intent_info);
        //overridePendingTransition(R.xml.slide_up_info, R.xml.no_change);
    }

    private void startInfoActivity() {
        if (!(this instanceof InfoActivity)) {
            Intent intent_info = new Intent(this, InfoActivity.class);
            startActivity(intent_info);
            //overridePendingTransition(R.xml.slide_up_info, R.xml.no_change);
        }
    }

    public void startMapsActivity() {
        if (!(this instanceof MapsActivity)) {
            Intent maps_info = new Intent(this, MapsActivity.class);
            startActivity(maps_info);
        }
    }

    private void startAccountActivity(){
        Intent account = new Intent(this,AccountActivity.class);
        startActivity(account);
    }

    protected void activateHeatmap(){
        //ABILITO L'OVERLAY PER LA HEATMAP
        mOverlay.setVisible(true);
        distribuzione.withChecked(true);
    }

    private void showAlertDialogRegions() {
        if (thisActivity instanceof MapsActivity) {
            final String[] allRegions = getResources().getStringArray(R.array.regions);
            final String[] allRegionsWithNumbers = new String[allRegions.length];
            final ArrayList<String> selectedRegions = new ArrayList<>();
            for (int i = 0; i < allRegionsWithNumbers.length; i++)
                allRegionsWithNumbers[i] = allRegions[i] + " (" + ((MapsActivity) thisActivity).getClusterManager().countMarkerByRegion(allRegions[i]) + ")";
            final boolean[] selectedReg = new boolean[allRegions.length];
            if(resetfilter){
                selectedRegionsItems = new ArrayList<>();
                resetfilter = false;
            }else{
                for(int i : selectedRegionsItems)
                    selectedReg[i] = true;
            }
            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Scegli le regioni")
                    .setMultiChoiceItems(allRegionsWithNumbers, selectedReg , new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                            if (isChecked) {
                                selectedRegionsItems.add(indexSelected);
                            } else if (selectedRegionsItems.contains(indexSelected)) {
                                selectedRegionsItems.remove(Integer.valueOf(indexSelected));
                            }
                        }
                    })
                    .setPositiveButton(R.string.msg_ok,null)
                    .setNegativeButton(R.string.msg_back, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(selectedRegionsItems.size()==0)
                        Toast.makeText(getApplicationContext(),R.string.selezionelista,Toast.LENGTH_SHORT).show();
                    else{
                        //DISABILITO I FILTRI COMBINATI
                        selectedCategoriesItems = new ArrayList<>();
                        for (int number : selectedRegionsItems)
                            selectedRegions.add(allRegions[number]);
                        ((MapsActivity) thisActivity).getClusterManager().showRegions(selectedRegions);
                        ((MapsActivity)thisActivity).getClusterManager().setFlagRegion(true);
                        ((MapsActivity)thisActivity).animateOnItaly();
                        ((MapsActivity)thisActivity).setIconListVisibility(true);
                        dialog.dismiss();
                    }
                }
            });
        }
    }
    private void showAlertDialogCategory(){
        if(thisActivity instanceof MapsActivity) {
            final ArrayList<String> allCategory = ((MapsActivity)thisActivity).getClusterManager().getAllMarkerCategory();
            Collections.sort(allCategory);
            final String[] allCategoryWithNumbers = new String[allCategory.size()];
            for(int i=0; i<allCategoryWithNumbers.length; i++)
                allCategoryWithNumbers[i] = allCategory.get(i)+" ("+((MapsActivity)thisActivity).getClusterManager().countMarkerByCategory(allCategory.get(i))+")";
            final ArrayList<String> selectedCategory = new ArrayList<>();
            final boolean[] selectedCat = new boolean[allCategory.size()];
            if (resetfilter){
                selectedCategoriesItems = new ArrayList<>();
                resetfilter = false;
            }else{
                for(int i : selectedCategoriesItems)
                    selectedCat[i] = true;
            }
            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Scegli le categorie")
                    .setMultiChoiceItems(allCategoryWithNumbers, selectedCat, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                            if (isChecked) {
                                selectedCategoriesItems.add(indexSelected);
                            } else if (selectedCategoriesItems.contains(indexSelected)) {
                                selectedCategoriesItems.remove(Integer.valueOf(indexSelected));
                            }
                        }
                    })
                    .setPositiveButton(R.string.msg_ok,null)
                    .setNegativeButton(R.string.msg_back, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if(selectedCategoriesItems.size()==0)
                        Toast.makeText(getApplicationContext(),R.string.selezionelista,Toast.LENGTH_SHORT).show();
                    else{
                        //DISABILITO I FILTRI COMBINATI
                        selectedRegionsItems = new ArrayList<>();
                        for (int number : selectedCategoriesItems)
                            selectedCategory.add(allCategory.get(number));
                        ((MapsActivity)thisActivity).getClusterManager().showCategory(selectedCategory);
                        ((MapsActivity)thisActivity).getClusterManager().setFlagTipo(true);
                        ((MapsActivity)thisActivity).animateOnItaly();
                        ((MapsActivity)thisActivity).setIconListVisibility(true);
                        dialog.dismiss();
                    }
                }
            });
        }
    }
}
