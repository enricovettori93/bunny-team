package it.unive.dais.bunnyteam.unfinitaly.app.marker;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Enrico on 13/03/2018.
 */
@IgnoreExtraProperties
public class OperaFirebase implements ClusterItem, Serializable {
    String ambito_oggettivo;
    String ambito_soggettivo;
    String anno_decisione_attuazione;
    String categoria;
    String causa;
    String codice_categoria;
    String codice_fiscale;
    String codice_sotto_settore;
    String copertura_finanziaria;
    String costo_prog;
    String cpv;
    String cup;
    String data_assegnazione_cup;
    String data_chiusura_cup;
    String denominazione_stazione_appaltante;
    String descrizione;
    String dimensione_unita_misura;
    String dimensione_valore;
    String finanziamento_assegnato;
    String finanziamento_di_prog;
    String id;
    String importo_oneri;
    String importo_sal;
    String importo_ultimo_qe;
    String importo_ultimo_qe_approvato;
    String indirizzo;
    String lat;
    String lng;
    String natura_opera;
    String oneri_necessari_per_ultimazione_lavori;
    String percentage;
    String progetto_cumulativo;
    String regione;
    String sottosettore;
    String sponsorizzato;
    String strutture_coinvolte;
    String tipologia_cup;
    String tipologia_opera_incompiuta;
    String title;
    String id_firebase;
    Map<String,Object> commenti = new HashMap<>();

    public OperaFirebase(){}

    public OperaFirebase(String ambito_oggettivo, String ambito_soggettivo, String anno_decisione_attuazione, String categoria, String causa, String codice_categoria, String codice_fiscale, String codice_sotto_settore, String copertura_finanziaria, String costo_prog, String cpv, String cup, String data_assegnazione_cup, String data_chiusura_cup, String denominazione_stazione_appaltante, String descrizione, String dimensione_unita_misura, String dimensione_valore, String finanziamento_assegnato, String finanziamento_di_prog, String id, String importo_oneri, String importo_sal, String importo_ultimo_qe, String importo_ultimo_qe_approvato, String indirizzo, String lat, String lng, String natura_opera, String oneri_necessari_per_ultimazione_lavori, String percentage, String progetto_cumulativo, String regione, String sottosettore, String sponsorizzato, String strutture_coinvolte, String tipologia_cup, String tipologia_opera_incompiuta, String title, Map commenti) {
        this.ambito_oggettivo = ambito_oggettivo;
        this.ambito_soggettivo = ambito_soggettivo;
        this.anno_decisione_attuazione = anno_decisione_attuazione;
        this.categoria = categoria;
        this.causa = causa;
        this.codice_categoria = codice_categoria;
        this.codice_fiscale = codice_fiscale;
        this.codice_sotto_settore = codice_sotto_settore;
        this.copertura_finanziaria = copertura_finanziaria;
        this.costo_prog = costo_prog;
        this.cpv = cpv;
        this.cup = cup;
        this.data_assegnazione_cup = data_assegnazione_cup;
        this.data_chiusura_cup = data_chiusura_cup;
        this.denominazione_stazione_appaltante = denominazione_stazione_appaltante;
        this.descrizione = descrizione;
        this.dimensione_unita_misura = dimensione_unita_misura;
        this.dimensione_valore = dimensione_valore;
        this.finanziamento_assegnato = finanziamento_assegnato;
        this.finanziamento_di_prog = finanziamento_di_prog;
        this.id = id;
        this.importo_oneri = importo_oneri;
        this.importo_sal = importo_sal;
        this.importo_ultimo_qe = importo_ultimo_qe;
        this.importo_ultimo_qe_approvato = importo_ultimo_qe_approvato;
        this.indirizzo = indirizzo;
        this.lat = lat;
        this.lng = lng;
        this.natura_opera = natura_opera;
        this.oneri_necessari_per_ultimazione_lavori = oneri_necessari_per_ultimazione_lavori;
        this.percentage = percentage;
        this.progetto_cumulativo = progetto_cumulativo;
        this.regione = regione;
        this.sottosettore = sottosettore;
        this.sponsorizzato = sponsorizzato;
        this.strutture_coinvolte = strutture_coinvolte;
        this.tipologia_cup = tipologia_cup;
        this.tipologia_opera_incompiuta = tipologia_opera_incompiuta;
        this.title = title;
        this.id_firebase = "";
        this.commenti = commenti;
    }

    public String getAmbito_oggettivo() {
        return ambito_oggettivo;
    }

    public void setAmbito_oggettivo(String ambito_oggettivo) {
        this.ambito_oggettivo = ambito_oggettivo;
    }

    public String getAmbito_soggettivo() {
        return ambito_soggettivo;
    }

    public void setAmbito_soggettivo(String ambito_soggettivo) {
        this.ambito_soggettivo = ambito_soggettivo;
    }

    public String getAnno_decisione_attuazione() {
        return anno_decisione_attuazione;
    }

    public void setAnno_decisione_attuazione(String anno_decisione_attuazione) {
        this.anno_decisione_attuazione = anno_decisione_attuazione;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    public String getCodice_categoria() {
        return codice_categoria;
    }

    public void setCodice_categoria(String codice_categoria) {
        this.codice_categoria = codice_categoria;
    }

    public String getCodice_fiscale() {
        return codice_fiscale;
    }

    public void setCodice_fiscale(String codice_fiscale) {
        this.codice_fiscale = codice_fiscale;
    }

    public String getCodice_sotto_settore() {
        return codice_sotto_settore;
    }

    public void setCodice_sotto_settore(String codice_sotto_settore) {
        this.codice_sotto_settore = codice_sotto_settore;
    }

    public String getCopertura_finanziaria() {
        return copertura_finanziaria;
    }

    public void setCopertura_finanziaria(String copertura_finanziaria) {
        this.copertura_finanziaria = copertura_finanziaria;
    }

    public String getCosto_prog() {
        return costo_prog;
    }

    public void setCosto_prog(String costo_prog) {
        this.costo_prog = costo_prog;
    }

    public String getCpv() {
        return cpv;
    }

    public void setCpv(String cpv) {
        this.cpv = cpv;
    }

    public String getCup() {
        return cup;
    }

    public void setCup(String cup) {
        this.cup = cup;
    }

    public String getData_assegnazione_cup() {
        return data_assegnazione_cup;
    }

    public void setData_assegnazione_cup(String data_assegnazione_cup) {
        this.data_assegnazione_cup = data_assegnazione_cup;
    }

    public String getData_chiusura_cup() {
        return data_chiusura_cup;
    }

    public void setData_chiusura_cup(String data_chiusura_cup) {
        this.data_chiusura_cup = data_chiusura_cup;
    }

    public String getDenominazione_stazione_appaltante() {
        return denominazione_stazione_appaltante;
    }

    public void setDenominazione_stazione_appaltante(String denominazione_stazione_appaltante) {
        this.denominazione_stazione_appaltante = denominazione_stazione_appaltante;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDimensione_unita_misura() {
        return dimensione_unita_misura;
    }

    public void setDimensione_unita_misura(String dimensione_unita_misura) {
        this.dimensione_unita_misura = dimensione_unita_misura;
    }

    public String getDimensione_valore() {
        return dimensione_valore;
    }

    public void setDimensione_valore(String dimensione_valore) {
        this.dimensione_valore = dimensione_valore;
    }

    public String getFinanziamento_assegnato() {
        return finanziamento_assegnato;
    }

    public void setFinanziamento_assegnato(String finanziamento_assegnato) {
        this.finanziamento_assegnato = finanziamento_assegnato;
    }

    public String getFinanziamento_di_prog() {
        return finanziamento_di_prog;
    }

    public void setFinanziamento_di_prog(String finanziamento_di_prog) {
        this.finanziamento_di_prog = finanziamento_di_prog;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImporto_oneri() {
        return importo_oneri;
    }

    public void setImporto_oneri(String importo_oneri) {
        this.importo_oneri = importo_oneri;
    }

    public String getImporto_ultimo_qe() {
        return importo_ultimo_qe;
    }

    public void setImporto_ultimo_qe(String importo_ultimo_qe) {
        this.importo_ultimo_qe = importo_ultimo_qe;
    }

    public String getImporto_ultimo_qe_approvato() {
        return importo_ultimo_qe_approvato;
    }

    public void setImporto_ultimo_qe_approvato(String importo_ultimo_qe_approvato) {
        this.importo_ultimo_qe_approvato = importo_ultimo_qe_approvato;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getNatura_opera() {
        return natura_opera;
    }

    public void setNatura_opera(String natura_opera) {
        this.natura_opera = natura_opera;
    }

    public String getOneri_necessari_per_ultimazione_lavori() {
        return oneri_necessari_per_ultimazione_lavori;
    }

    public void setOneri_necessari_per_ultimazione_lavori(String oneri_necessari_per_ultimazione_lavori) {
        this.oneri_necessari_per_ultimazione_lavori = oneri_necessari_per_ultimazione_lavori;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getProgetto_cumulativo() {
        return progetto_cumulativo;
    }

    public void setProgetto_cumulativo(String progetto_cumulativo) {
        this.progetto_cumulativo = progetto_cumulativo;
    }

    public String getRegione() {
        return regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }

    public String getSottosettore() {
        return sottosettore;
    }

    public void setSottosettore(String sottosettore) {
        this.sottosettore = sottosettore;
    }

    public String getSponsorizzato() {
        return sponsorizzato;
    }

    public void setSponsorizzato(String sponsorizzato) {
        this.sponsorizzato = sponsorizzato;
    }

    public String getStrutture_coinvolte() {
        return strutture_coinvolte;
    }

    public void setStrutture_coinvolte(String strutture_coinvolte) {
        this.strutture_coinvolte = strutture_coinvolte;
    }

    public String getTipologia_cup() {
        return tipologia_cup;
    }

    public void setTipologia_cup(String tipologia_cup) {
        this.tipologia_cup = tipologia_cup;
    }

    public String getTipologia_opera_incompiuta() {
        return tipologia_opera_incompiuta;
    }

    public void setTipologia_opera_incompiuta(String tipologia_opera_incompiuta) {
        this.tipologia_opera_incompiuta = tipologia_opera_incompiuta;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImporto_sal() {
        return importo_sal;
    }

    public void setImporto_sal(String importo_sal) {
        this.importo_sal = importo_sal;
    }

    public String getId_firebase() {
        return id_firebase;
    }

    public void setId_firebase(String id_firebase) {
        this.id_firebase = id_firebase;
    }

    @Override
    public String toString() {
        return "OperaFirebase{" +
                "ID_FIREBASE='" + id_firebase + '\'' +
                ", ambito_oggettivo='" + ambito_oggettivo + '\'' +
                ", ambito_soggettivo='" + ambito_soggettivo + '\'' +
                ", anno_decisione_attuazione='" + anno_decisione_attuazione + '\'' +
                ", categoria='" + categoria + '\'' +
                ", causa='" + causa + '\'' +
                ", codice_categoria='" + codice_categoria + '\'' +
                ", codice_fiscale='" + codice_fiscale + '\'' +
                ", codice_sotto_settore='" + codice_sotto_settore + '\'' +
                ", copertura_finanziaria='" + copertura_finanziaria + '\'' +
                ", costo_prog='" + costo_prog + '\'' +
                ", cpv='" + cpv + '\'' +
                ", cup='" + cup + '\'' +
                ", data_assegnazione_cup='" + data_assegnazione_cup + '\'' +
                ", data_chiusura_cup='" + data_chiusura_cup + '\'' +
                ", denominazione_stazione_appaltante='" + denominazione_stazione_appaltante + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", dimensione_unita_misura='" + dimensione_unita_misura + '\'' +
                ", dimensione_valore='" + dimensione_valore + '\'' +
                ", finanziamento_assegnato='" + finanziamento_assegnato + '\'' +
                ", finanziamento_di_prog='" + finanziamento_di_prog + '\'' +
                ", id='" + id + '\'' +
                ", importo_oneri='" + importo_oneri + '\'' +
                ", importo_ultimo_qe='" + importo_ultimo_qe + '\'' +
                ", importo_ultimo_qe_approvato='" + importo_ultimo_qe_approvato + '\'' +
                ", indirizzo='" + indirizzo + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", natura_opera='" + natura_opera + '\'' +
                ", oneri_necessari_per_ultimazione_lavori='" + oneri_necessari_per_ultimazione_lavori + '\'' +
                ", percentage='" + percentage + '\'' +
                ", progetto_cumulativo='" + progetto_cumulativo + '\'' +
                ", regione='" + regione + '\'' +
                ", sottosettore='" + sottosettore + '\'' +
                ", sponsorizzato='" + sponsorizzato + '\'' +
                ", strutture_coinvolte='" + strutture_coinvolte + '\'' +
                ", tipologia_cup='" + tipologia_cup + '\'' +
                ", tipologia_opera_incompiuta='" + tipologia_opera_incompiuta + '\'' +
                ", title='" + title + '\'' +
                ", commenti: \n" + commenti.toString() +
                '}';
    }
    @Override
    public String getSnippet() {
        return getTitle() + " " + getDescrizione();
    }
    @Override
    public LatLng getPosition() {
        return new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
    }
}
