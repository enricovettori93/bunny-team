package it.unive.dais.bunnyteam.unfinitaly.app.cluster;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.unive.dais.bunnyteam.unfinitaly.app.AccountActivity;
import it.unive.dais.bunnyteam.unfinitaly.app.ActivityNewAccount;
import it.unive.dais.bunnyteam.unfinitaly.app.opere.ListaOpereFirebase;
import it.unive.dais.bunnyteam.unfinitaly.app.MapsActivity;
import it.unive.dais.bunnyteam.unfinitaly.app.R;
import it.unive.dais.bunnyteam.unfinitaly.app.opere.OperaFirebase;

/**
 *
 * @author BunnyTeam, Università Ca' Foscari
 */

public class CustomClusterManager<T extends ClusterItem> extends ClusterManager<OperaFirebase> implements GoogleMap.OnCameraIdleListener{

    private ListaOpereFirebase mapMarkers = null;
    private ArrayList<OperaFirebase> mapMarkersActive = null;
    private Context context;
    private GoogleMap map;
    private boolean flagregione = false;
    private boolean flagtipo = false;

    public CustomClusterManager(final Context context, GoogleMap map) {
        super(context, map);
        this.context = context;
        this.map = map;
        setOnClusterClickListener(getDefaultOnClusterClickListener());
        setOnClusterItemClickListener(new OnClusterItemClickListener<OperaFirebase>() {
            @Override
            public boolean onClusterItemClick(final OperaFirebase mapMarker) {
                if(((Activity)context).findViewById(R.id.marker_window).getVisibility()==View.VISIBLE)
                    ((Activity)context).findViewById(R.id.marker_window).setVisibility(View.INVISIBLE);
                ((MapsActivity)context).getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(mapMarker.getPosition(), 13), new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        showMarker();
                    }

                    @Override
                    public void onCancel() {
                        showMarker();
                    }

                    private void showMarker(){
                        String title = mapMarker.getCategoria();
                        String snippet = mapMarker.getTitle();
                        if(title.length()>100){
                            title = title.substring(0,99) + "...";
                        }
                        if(snippet.length()>100){
                            snippet = snippet.substring(0,99) + "...";
                        }
                        ((TextView)((Activity)context).findViewById(R.id.titleMarker)).setText(title);
                        ((TextView)((Activity)context).findViewById(R.id.snippetMarker)).setText(snippet);
                        ((Activity)context).findViewById(R.id.floatingActionButtonList).setVisibility(View.INVISIBLE);
                        ((Activity)context).findViewById(R.id.marker_window).setVisibility(View.VISIBLE);
                    }
                });
                View info;
                info = ((Activity)context).findViewById(R.id.marker_window);
                info.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        ((MapsActivity) context).showMarkerInfo(mapMarker);
                    }
                });
                View navigate;
                navigate = ((Activity)context).findViewById(R.id.navigate);
                navigate.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        ((MapsActivity)context).updateCurrentPosition();
                        LatLng app = ((MapsActivity)context).getCurrentPosition();
                        if (app != null)
                            ((MapsActivity)context).navigate(app,mapMarker.getPosition());
                        else
                            Toast.makeText(context, R.string.msg_error_gps, Toast.LENGTH_SHORT).show();
                    }
                });
                ((Activity)context).findViewById(R.id.position).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((MapsActivity)context).getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(mapMarker.getPosition(), 13));
                    }
                });
                return true;
            };
            });
        setOnClusterItemInfoWindowClickListener(getDefaultOnClusterItemInfoWindowClickListener());
        setRenderer(new ClusterRenderer<>(context, map, this));
    }


    public void resetMarkers(){
        clearItems();
        addItems(mapMarkers.getListaOpere());
        cluster();
    }

    protected void clearMarkers(){
        clearItems();
        cluster();
    }

    public void resetFlags(){
        flagregione = false;
        flagtipo = false;
    }

    public void setFlagRegion(boolean status){
        flagregione = status;
    }

    public void setFlagTipo(boolean status){
        flagtipo = status;
    }

    public ArrayList<String> getAllMarkerCategory(){
        ArrayList<String> allCategory = new ArrayList<>();
        for(OperaFirebase mM: mapMarkers.getListaOpere())
            if(!allCategory.contains(mM.getCategoria()))
                allCategory.add(mM.getCategoria());
        return allCategory;
    }

    public int countMarkerByCategory(String category) {
        int i=0;
        for(OperaFirebase mM: mapMarkers.getListaOpere())
            if(mM.getCategoria().equals(category))
                i++;
        return i;
    }

    public int countMarkerByRegion(String region){
        int i=0;
        for(OperaFirebase mM: mapMarkers.getListaOpere())
            if(mM.getRegione().equals(region))
                i++;
        return i;
    }

    public void showRegions(ArrayList<String> regions) {
        clearItems();
        mapMarkersActive = new ArrayList<>();
        for(OperaFirebase mM: mapMarkers.getListaOpere())
            if(regions.contains(mM.getRegione())){
                addItem(mM);
                mapMarkersActive.add(mM);
            }
        flagregione = true;
        cluster();
    }

    public void showCategory(ArrayList<String> selectedCategory) {
        clearItems();
        mapMarkersActive = new ArrayList<>();
        for(OperaFirebase mM: mapMarkers.getListaOpere())
            if(selectedCategory.contains(mM.getCategoria())) {
                addItem(mM);
                mapMarkersActive.add(mM);
            }
        cluster();
    }

    public OnClusterClickListener<OperaFirebase> getDefaultOnClusterClickListener(){
        return new ClusterManager.OnClusterClickListener<OperaFirebase>() {
            @Override
            public boolean onClusterClick(Cluster<OperaFirebase> cluster) {
                ((Activity)context).findViewById(R.id.marker_window).setVisibility(View.INVISIBLE);
                final String[] stringclusterlista = new String[cluster.getSize()];
                final Collection<OperaFirebase> clusterlist = cluster.getItems();
                Log.d("Grandezza",""+cluster.getSize());
                for(int i=0;i<clusterlist.size();i++){
                    stringclusterlista[i]= "Categoria: " +((OperaFirebase)clusterlist.toArray()[i]).getCategoria()+"\nTipologia CUP: "+((OperaFirebase)clusterlist.toArray()[i]).getTipologia_cup();
                }
                AlertDialog dialog = new AlertDialog.Builder(context,R.style.MyAlertDialogTheme)
                        .setTitle(R.string.clustertitle)
                        .setSingleChoiceItems(stringclusterlista, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if(context instanceof MapsActivity)
                                    ((MapsActivity)context).showMarkerInfo((OperaFirebase) clusterlist.toArray()[id]);
                            }
                        }).setNegativeButton(R.string.msg_back, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).create();
                dialog.show();
                return false;
            }
        };
    }

    @Override
    public void setOnClusterItemClickListener(OnClusterItemClickListener<OperaFirebase> listener) {
        super.setOnClusterItemClickListener(listener);
    }

    @Override
    public void onCameraIdle() {
        float[] result;
        result = ((MapsActivity)context).checkDistanceCamera(((MapsActivity)context).getMap().getCameraPosition());
        if (result[0] > 1000000){
            ((MapsActivity)context).animateOnItaly();
        }
        cluster();
    }

    public OnClusterItemInfoWindowClickListener<OperaFirebase> getDefaultOnClusterItemInfoWindowClickListener() {
        return new ClusterManager.OnClusterItemInfoWindowClickListener<OperaFirebase>() {
            @Override
            public void onClusterItemInfoWindowClick(OperaFirebase mapMarker) {
                if (context instanceof MapsActivity)
                    ((MapsActivity) context).showMarkerInfo(mapMarker);
            }
        };
    }
    public void setMapMarkerList(ListaOpereFirebase mM){
        this.mapMarkers = mM;
        addItems(mM.getListaOpere());
        cluster();
    }
    public void setPercentageRenderer(){
        setRenderer(new PercentageClusterRenderer<>(context,map,this));
        resetMarkers();
    }
    public void unsetPercentageRender(){
        setRenderer(new ClusterRenderer<>(context,map,this));
        resetMarkers();
    }
    public List<LatLng> getCoordList(){
        ArrayList<LatLng> lL = new ArrayList<>();
        for(OperaFirebase mM: mapMarkers.getListaOpere()){
            lL.add(mM.getPosition());
        }
        return lL;
    }
    public ArrayList<OperaFirebase> getActiveMarkers(){
        return mapMarkersActive;
    }
}
