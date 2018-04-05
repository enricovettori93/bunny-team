package it.unive.dais.bunnyteam.unfinitaly.app.opere;

import android.util.Log;

import java.util.ArrayList;

import it.unive.dais.bunnyteam.unfinitaly.app.entities.HashMapRegioni;

/**
 * Created by Enrico on 13/03/2018.
 */

public class ListaOpereFirebase {
    private ArrayList<OperaFirebase> listaOpere = new ArrayList<>();
    private static ListaOpereFirebase istanza;
    private boolean isFirstTime = true;
    private ListaOpereFirebase(){}

    public static ListaOpereFirebase getIstance(){
        Log.d("ISTANZA",""+istanza);
        if(istanza == null)
            istanza = new ListaOpereFirebase();
        return istanza;
    }

    public ArrayList<OperaFirebase> getListaOpere(){
        return listaOpere;
    }

    public ArrayList<OperaFirebase> finishLetturaOpereFromFirebase(){
        return listaOpere;
    }

    public void setPercentageRegioni(){
        Log.d("FIRST TIME REG",""+isFirstTime);
        if(isFirstTime == true){
            isFirstTime = false;
            for(OperaFirebase mm: listaOpere){
                HashMapRegioni.getIstance().addUnitRegione(mm.getRegione());
            }
        }
    }
}
