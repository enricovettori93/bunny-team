package it.unive.dais.bunnyteam.unfinitaly.app.slider;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import it.unive.dais.bunnyteam.unfinitaly.app.LoadingActivity;
import it.unive.dais.bunnyteam.unfinitaly.app.R;
import it.unive.dais.bunnyteam.unfinitaly.app.marker.ListaOpereFirebase;
import it.unive.dais.bunnyteam.unfinitaly.app.marker.MapMarkerList;
import it.unive.dais.bunnyteam.unfinitaly.app.storage.FirebaseUtilities;
import it.unive.dais.bunnyteam.unfinitaly.app.storage.MapsItemIO;

/**
 *
 * @author BunnyTeam, Università Ca' Foscari
 */

public class CustomSliderLoading extends CustomSlider {
    private TextView tv_status;
    private TextView tvCountLoad;
    private ProgressBar progressBar;
    private LoadingActivity loadAct;
    private AVLoadingIndicatorView loadinggif;
    private static final String ARG_LAYOUT_RES_ID = "layoutResId";
    private View v;

    public static CustomSliderLoading newInstance(int layoutResId, LoadingActivity loadAct) {
        CustomSliderLoading sampleSlide = new CustomSliderLoading();
        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        sampleSlide.setArguments(args);
        sampleSlide.loadAct = loadAct;
        return sampleSlide;
    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(v!=null)
            return v;
        else
            return inflater.inflate(layoutResId, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (v == null) {
            v = view;
            super.onViewCreated(view, savedInstanceState);
            tv_status = (TextView) view.findViewById(R.id.tv_status2);
            tv_status.setText("Apertura file...");
            tvCountLoad = (TextView) view.findViewById(R.id.tvCountLoad2);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);
            loadinggif = (AVLoadingIndicatorView) view.findViewById(R.id.avi2);
            FirebaseUtilities.getIstance().readFromFirebase();
            loadAct.startMapsActivity();
            /*if (MapMarkerList.getInstance().getMapMarkers().size() == 0) {
                try {
                    if (MapsItemIO.isCached(loadAct)) {
                        Log.i("loading", "is on cache!");
                        if (!(MapMarkerList.getInstance().loadFromCache(loadAct)))
                            MapMarkerList.getInstance().loadFromCsv(loadAct, tv_status, tvCountLoad, progressBar,loadinggif);
                        else {
                            Log.i("loading", "starting app!");
                            loadAct.startMapsActivity();
                        }
                    } else
                        MapMarkerList.getInstance().loadFromCsv(loadAct, tv_status, tvCountLoad, progressBar, loadinggif);
                } catch (InterruptedException | IOException | ExecutionException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }*/
        }
    }
}
