package it.unive.dais.bunnyteam.unfinitaly.app.storage;

import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import it.unive.dais.bunnyteam.unfinitaly.app.LoadingActivity;
import it.unive.dais.bunnyteam.unfinitaly.app.marker.MapMarkerList;
import it.unive.dais.bunnyteam.unfinitaly.app.marker.MapMarkerListVersioningHelper;

/**
 *
 * @author BunnyTeam, Università Ca' Foscari
 */

public class MapsItemIO {

    public static boolean isCached(Context context) {
        File cacheDir = new File(context.getCacheDir(), "files");
        cacheDir.mkdirs();
        File cacheFile = new File(cacheDir, "mapMarkers1.obj");
        if (cacheFile.exists())
            return true;
        else
            return false;
    }

    public void loadFromCsv(LoadingActivity loadingActivity, TextView tv_status, TextView tvCountLoad, ProgressBar progressBar, AVLoadingIndicatorView loadinggif){
        new CSVReader(loadingActivity, tv_status, tvCountLoad, progressBar, loadinggif).execute();
    }
    public static boolean readFromCache(Context context) throws IOException, ClassNotFoundException {
        File cacheDir = new File(context.getCacheDir(), "files");
        Log.i("ReadFromCache", "start");
        if (!cacheDir.exists())
            throw new IOException();
        File cachedFile = new File(cacheDir, "mapMarkers1.obj");
        FileInputStream is = new FileInputStream(cachedFile);
        ObjectInputStream oIs = new ObjectInputStream(is);
        Log.i("ReadFromCache", "reading");
        Object readed = oIs.readObject();
        Log.i("ReadFromCache", ""+((MapMarkerListVersioningHelper)readed).getIdVersion());
        Log.i("ReadFromCache", ""+MapMarkerList.getStaticIdVersion());
        if(((MapMarkerListVersioningHelper)readed).getIdVersion()!=MapMarkerList.getStaticIdVersion())
            return false;
        else if(readed instanceof MapMarkerList) {
            MapMarkerList.setInstance((MapMarkerList) readed);
            Log.i("ReadFromCache", "readed " + MapMarkerList.getInstance().getMapMarkers().size());
            return true;
        }else
            return false;
    }

    public static void saveToCache(MapMarkerList mmL, Context context) throws IOException {
        Log.i("Save to Cache", "start: size = " + mmL.getMapMarkers().size());
        if (mmL != null) {
            File cacheDir = new File(context.getCacheDir(), "files");
            if (!cacheDir.exists())
                cacheDir.mkdir();
            Log.i("Save to Cache", "saving");
            File cachedFile = new File(cacheDir, "mapMarkers1.obj");
            ObjectOutputStream Oos = new ObjectOutputStream(new FileOutputStream(cachedFile));
            Oos.writeObject(mmL);
        }
    }


}

