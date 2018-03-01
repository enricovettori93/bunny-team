package it.unive.dais.bunnyteam.unfinitaly.app.cluster;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import it.unive.dais.bunnyteam.unfinitaly.app.marker.MapMarker;

/**
 *
 * @author BunnyTeam, Università Ca' Foscari
 */

public class PercentageClusterRenderer<T extends MapMarker> extends ClusterRenderer {
    public PercentageClusterRenderer(Context context, GoogleMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);
    }
    @Override
    protected void onBeforeClusterItemRendered(MapMarker item, MarkerOptions markerOptions) {
        double percentage =  item.getPercentage();
        if(percentage >= 0 && percentage < 25){
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }else{
            if(percentage >= 25 && percentage < 50){
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            }else{
                if(percentage >= 50 && percentage < 75){
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                }
                else{
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }
            }
        }
    }
}