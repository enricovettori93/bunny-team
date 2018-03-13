package it.unive.dais.bunnyteam.unfinitaly.app.marker;

import android.graphics.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Enrico on 13/03/2018.
 */

public class ListaOpereFirebase {
    private ArrayList<OperaFirebase> listaOpere = new ArrayList<>();
    private static ListaOpereFirebase istanza = new ListaOpereFirebase();
    private ListaOpereFirebase(){}
    public static ListaOpereFirebase getIstance(){
        return istanza;
    }
    public ArrayList<OperaFirebase> getListaOpere(){
        return listaOpere;
    }
}
