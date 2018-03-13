package it.unive.dais.bunnyteam.unfinitaly.app.factory;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Enrico on 13/03/2018.
 */
@IgnoreExtraProperties
public class OperaFirebase {
    public String ambito_oggettivo;
    public String ambito_soggettivo;
    public String anno_decisione_attuazione;
    public String categoria;
    public String causa;
    public String codice_categoria;
    public String codice_fiscale;
    public String codice_sotto_settore;
    public String copertura_finanziaria;
    public String costo_prog;
    public String cpv;
    public String cup;
    public String data_assegnazione_cup;
    public String data_chiusura_cup;
    public String denominazione_stazione_appaltante;
    public String descrizione;
    public String dimensione_unita_misura;
    public String dimensione_valore;
    public String finanziamento_assegnato;
    public String finanziamento_di_prog;
    public String id;
    public String importo_oneri;
    public String importo_ultimo_qe;
    public String importo_ultimo_qe_approvato;
    public String indirizzo;
    public String lat;
    public String lng;
    public String natura_opera;
    public String oneri_necessari_per_ultimazione_lavori;
    public String percentage;
    public String progetto_cumulativo;
    public String regione;
    public String sottosettore;
    public String sponsorizzato;
    public String strutture_coinvolte;
    public String tipologia_cup;
    public String tipologia_opera_incompiuta;
    public String title;

    public OperaFirebase(){}

    public OperaFirebase(String ambito_oggettivo, String ambito_soggettivo, String anno_decisione_attuazione, String categoria, String causa, String codice_categoria, String codice_fiscale, String codice_sotto_settore, String copertura_finanziaria, String costo_prog, String cpv, String cup, String data_assegnazione_cup, String data_chiusura_cup, String denominazione_stazione_appaltante, String descrizione, String dimensione_unita_misura, String dimensione_valore, String finanziamento_assegnato, String finanziamento_di_prog, String id, String importo_oneri, String importo_ultimo_qe, String importo_ultimo_qe_approvato, String indirizzo, String lat, String lng, String natura_opera, String oneri_necessari_per_ultimazione_lavori, String percentage, String progetto_cumulativo, String regione, String sottosettore, String sponsorizzato, String strutture_coinvolte, String tipologia_cup, String tipologia_opera_incompiuta, String title) {
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
    }

    @Override
    public String toString() {
        return "OperaFirebase{" +
                "ambito_oggettivo='" + ambito_oggettivo + '\'' +
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
                '}';
    }
}
