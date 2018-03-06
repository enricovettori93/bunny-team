package it.unive.dais.bunnyteam.unfinitaly.app.entities;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by Enrico on 06/03/2018.
 */

public class HashMapRegioni {
    private static HashMap<String, Integer> percentualeRegioni = new HashMap<>();
    private static HashMapRegioni singleton = new HashMapRegioni();
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
        return singleton;
    }

    public void addUnitRegione(String regione){
        percentualeRegioni.put(regione,percentualeRegioni.get(regione)+1);
        totale++;
    }

    public double getPercentualeRegione(String regione){
        return (100*percentualeRegioni.get(regione))/totale;
    }

    public void debugPrintPercentage(){
        int countregionetot = 0;
        for(String key : percentualeRegioni.keySet()){
            countregionetot += percentualeRegioni.get(key);
            Log.d("REGIONE: " + key," PERCENTUALE: "+ getPercentualeRegione(key) + " OPERE: " + percentualeRegioni.get(key) + " TOTALE: " + totale);
        }
        Log.d("TOTALE OPERE REGIONI:",""+countregionetot);
    }
}
