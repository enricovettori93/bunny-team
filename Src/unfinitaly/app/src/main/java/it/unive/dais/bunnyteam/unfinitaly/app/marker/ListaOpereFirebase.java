package it.unive.dais.bunnyteam.unfinitaly.app.marker;

import android.graphics.Path;

import java.util.ArrayList;
import java.util.List;

import it.unive.dais.bunnyteam.unfinitaly.app.entities.HashMapRegioni;

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
