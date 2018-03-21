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
        /*if(listaOpere.size() == 0){
            //Controllo che non sia 0, accade quando android libera risorse
            Log.d("LISTA OPERE","EMPTY SIZE " +listaOpere.size());
            FirebaseUtilities.getIstance().readFromFirebase(null);
            return finishLetturaOpereFromFirebase();
        }
        else{*/
            Log.d("LISTA OPERE","ON MEMORY, SIZE " + listaOpere.size());
            return listaOpere;
        //}
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
