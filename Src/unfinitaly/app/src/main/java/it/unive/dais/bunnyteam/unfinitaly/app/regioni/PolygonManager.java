package it.unive.dais.bunnyteam.unfinitaly.app.regioni;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import it.unive.dais.bunnyteam.unfinitaly.app.entities.HashMapRegioni;

/**
 * Created by Enrico on 14/03/2018.
 */

public class PolygonManager {
    //Variabili di classe
    private static PolygonManager istance = new PolygonManager();
    //NB: GLI ID DEI POLIGONI SONO CRESCENTI IN BASE A COME LI SI CREA
    private Polygon abruzzo, basilicata, campania, calabria, emilia, friuli, lazio, liguria, lombardia, marche, molise, piemonte, puglia, sardegna, sicilia, toscana, trentino, umbria, valleaosta, veneto;
    //Costruttore privato
    private PolygonManager(){}
    //Metodi di accesso
    public static PolygonManager getIstance(){return istance;}

    /**
     * Inserisce i poligoni nella GoogleMap
     * @param gMap: Mappa dove inserire i poligoni
     */
    public void putPolygonRegion(GoogleMap gMap){
        abruzzo = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Abruzzo"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Abruzzo")));
        basilicata = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Basilicata"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Basilicata")));
        campania = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Campania"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Campania")));
        calabria = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Calabria"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Calabria")));
        emilia = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Emilia-Romagna"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Emilia-Romagna")));
        friuli = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Friuli-Venezia Giulia"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Friuli-Venezia Giulia")));
        lazio = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Lazio"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Lazio")));
        liguria = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Liguria"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Liguria")));
        lombardia = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Lombardia"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Lombardia")));
        marche = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Marche"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Marche")));
        molise = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Molise"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Molise")));
        piemonte = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Piemonte"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Piemonte")));
        puglia = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Puglia"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Puglia")));
        sardegna = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Sardegna"))
                .visible(false)
                .strokeWidth(5)
                .clickable(true)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Sardegna")));
        sicilia = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Sicilia"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Sicilia")));
        toscana = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Toscana"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Toscana")));
        trentino = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Trentino-Alto Adige"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Trentino-Alto Adige")));
        umbria = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Umbria"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Umbria")));
        valleaosta = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Valle d'Aosta"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Valle d'Aosta")));
        veneto = gMap.addPolygon(new PolygonOptions()
                .add(RegioniFactory.getIstance().createRegione("Veneto"))
                .visible(false)
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(HashMapRegioni.getIstance().getColorByPercentage("Veneto")));
    }

    /**
     * Setta al valore booleano status la proprietà visible dei poligoni
     * @param status
     */
    public void setVisibilityPolygon(boolean status){
        abruzzo.setVisible(status);
        basilicata.setVisible(status);
        campania.setVisible(status);
        calabria.setVisible(status);
        emilia.setVisible(status);
        friuli.setVisible(status);
        lazio.setVisible(status);
        liguria.setVisible(status);
        lombardia.setVisible(status);
        marche.setVisible(status);
        molise.setVisible(status);
        piemonte.setVisible(status);
        puglia.setVisible(status);
        sardegna.setVisible(status);
        sicilia.setVisible(status);
        toscana.setVisible(status);
        trentino.setVisible(status);
        umbria.setVisible(status);
        valleaosta.setVisible(status);
        veneto.setVisible(status);
        setClickable(status);
    }

    /**
     * Setta al valore booleano status la proprietà clickable -> uguale a visible, viene chiamato da quel metodo
     * @param status
     */
    private void setClickable(boolean status){
        abruzzo.setClickable(status);
        basilicata.setClickable(status);
        campania.setClickable(status);
        calabria.setClickable(status);
        emilia.setClickable(status);
        friuli.setClickable(status);
        lazio.setClickable(status);
        liguria.setClickable(status);
        lombardia.setClickable(status);
        marche.setClickable(status);
        molise.setClickable(status);
        piemonte.setClickable(status);
        puglia.setClickable(status);
        sardegna.setClickable(status);
        sicilia.setClickable(status);
        toscana.setClickable(status);
        trentino.setClickable(status);
        umbria.setClickable(status);
        valleaosta.setClickable(status);
        veneto.setClickable(status);
    }

    /**
     * Ritorna il nome della regione in base all'ID passato
     * @param id
     * @return
     */
    public String getNomeRegioneById(String id){
        String ritorno;
        switch (id){
            case "pg0":
                ritorno =  "Abruzzo";
                break;
            case "pg1":
                ritorno =  "Basilicata";
                break;
            case "pg2":
                ritorno =  "Campania";
                break;
            case "pg3":
                ritorno =  "Calabria";
                break;
            case "pg4":
                ritorno =  "Emilia-Romagna";
                break;
            case "pg5":
                ritorno =  "Friuli-Venezia Giulia";
                break;
            case "pg6":
                ritorno =  "Lazio";
                break;
            case "pg7":
                ritorno =  "Liguria";
                break;
            case "pg8":
                ritorno =  "Lombardia";
                break;
            case "pg9":
                ritorno =  "Marche";
                break;
            case "pg10":
                ritorno =  "Molise";
                break;
            case "pg11":
                ritorno =  "Piemonte";
                break;
            case "pg12":
                ritorno =  "Puglia";
                break;
            case "pg13":
                ritorno =  "Sardegna";
                break;
            case "pg14":
                ritorno =  "Sicilia";
                break;
            case "pg15":
                ritorno =  "Toscana";
                break;
            case "pg16":
                ritorno =  "Trentino-Alto Adige";
                break;
            case "pg17":
                ritorno =  "Umbria";
                break;
            case "pg18":
                ritorno =  "Valle d'Aosta";
                break;
            case "pg19":
                ritorno =  "Veneto";
                break;
            default:
                ritorno =  "null";
                break;
        }
        return ritorno;
    }
}
