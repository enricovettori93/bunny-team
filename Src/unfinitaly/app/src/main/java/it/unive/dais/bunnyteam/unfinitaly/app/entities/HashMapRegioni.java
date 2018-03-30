package it.unive.dais.bunnyteam.unfinitaly.app.entities;

import android.graphics.Color;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Enrico on 06/03/2018.
 */

public class HashMapRegioni {
    private static HashMap<String, Integer> percentualeRegioni;
    private static HashMapRegioni singleton;
    private static int totale;

    private HashMapRegioni(){
        percentualeRegioni.put("Abruzzo",0);
        percentualeRegioni.put("Basilicata",0);
        percentualeRegioni.put("Calabria",0);
        percentualeRegioni.put("Campania",0);
        percentualeRegioni.put("Emilia-Romagna",0);
        percentualeRegioni.put("Friuli-Venezia Giulia",0);
        percentualeRegioni.put("Lazio",0);
        percentualeRegioni.put("Liguria",0);
        percentualeRegioni.put("Lombardia",0);
        percentualeRegioni.put("Marche",0);
        percentualeRegioni.put("Molise",0);
        percentualeRegioni.put("Piemonte",0);
        percentualeRegioni.put("Puglia",0);
        percentualeRegioni.put("Sardegna",0);
        percentualeRegioni.put("Sicilia",0);
        percentualeRegioni.put("Toscana",0);
        percentualeRegioni.put("Trentino-Alto Adige",0);
        percentualeRegioni.put("Umbria",0);
        percentualeRegioni.put("Valle d'Aosta",0);
        percentualeRegioni.put("Veneto",0);
        totale = 0;
    }

    public static HashMapRegioni getIstance(){
        if(singleton != null)
            return singleton;
        else{
            percentualeRegioni = new HashMap<>();
            singleton = new HashMapRegioni();
            return singleton;
        }
    }

    public void addUnitRegione(String regione){
        percentualeRegioni.put(regione,percentualeRegioni.get(regione)+1);
        totale++;
    }

    public double getPercentualeRegione(String regione){
        try {
            return (100*percentualeRegioni.get(regione))/totale;
        }catch(Exception e){
            Log.e("ERRORE",""+e);
            return 0;
        }
    }

    public int getOpereRegione(String regione){
        try {
            return percentualeRegioni.get(regione);
        }catch(Exception e){
            Log.e("ERRORE",""+e);
            return 0;
        }
    }

    public int getColorByPercentage(String regione){
        double app;
        app = getPercentualeRegione(regione);
        if(app <= 2){
            return 0xffccff99;
        }
        if(app > 2 && app <= 5){
            return 0xffffff99;
        }
        if(app > 5 && app <= 20){
            return 0xffff9933;
        }
        if(app > 20){
            return 0xffff0000;
        }
        //Qui non ci arrivo mai
        return 0xffff0000;
    }

    public void debugPrintPercentage(){
        int countregionetot = 0;
        double countpercentage = 0;
        for(String key : percentualeRegioni.keySet()){
            countregionetot += percentualeRegioni.get(key);
            countpercentage += getPercentualeRegione(key);
            Log.d("REGIONE: " + key," PERCENTUALE: "+ getPercentualeRegione(key) + " OPERE: " + percentualeRegioni.get(key) + " TOTALE: " + totale);
        }
        Log.d("TOTALE OPERE",""+countregionetot);
        Log.d("TOTALE PERC OPERE",""+countpercentage);
    }
}
