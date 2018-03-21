package it.unive.dais.bunnyteam.unfinitaly.app.marker;

import android.graphics.Path;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import it.unive.dais.bunnyteam.unfinitaly.app.entities.HashMapRegioni;
import it.unive.dais.bunnyteam.unfinitaly.app.storage.FirebaseUtilities;

/**
 * Created by Enrico on 13/03/2018.
 */

public class ListaOpereFirebase {
    private ArrayList<OperaFirebase> listaOpere = new ArrayList<>();
    private static ListaOpereFirebase istanza = new ListaOpereFirebase();
    private boolean isFirstTime = true;
    private ListaOpereFirebase(){}

    public static ListaOpereFirebase getIstance(){
        return istanza;
    }

    public ArrayList<OperaFirebase> getListaOpere(){
        if(listaOpere == null){
            //Controllo che non sia null, accade quando android libera risorse
            Log.d("LISTA OPERE","NULL");
            FirebaseUtilities.getIstance().readFromFirebase(null);
            return finishLetturaOpereFromFirebase();
        }
        else{
            Log.d("LISTA OPERE","ON MEMORY");
            return listaOpere;
        }
    }

    public ArrayList<OperaFirebase> finishLetturaOpereFromFirebase(){
        return listaOpere;
    }

    public void setPercentageRegioni(){
        if(isFirstTime = true){
            for(OperaFirebase mm: listaOpere){
                HashMapRegioni.getIstance().addUnitRegione(mm.getRegione());
            }
        }
        else
            isFirstTime = false;
    }
}
