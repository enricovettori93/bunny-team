package it.unive.dais.bunnyteam.unfinitaly.app.entities;

/**
 * Created by Enrico on 16/03/2018.
 */

public class Commento {
    //Variabili per il commento
    String id_utente, nome_utente, testo_commento, data_commento;

    //Costruttori
    public Commento(){}
    public Commento(String id_utente, String nome_utente, String testo_commento, String data_commento) {
        this.id_utente = id_utente;
        this.nome_utente = nome_utente;
        this.testo_commento = testo_commento;
        this.data_commento = data_commento;
    }

    //Getter e Setter
    public String getId_utente() {
        return id_utente;
    }
    public void setId_utente(String id_utente) {
        this.id_utente = id_utente;
    }
    public String getNome_utente() {
        return nome_utente;
    }
    public void setNome_utente(String nome_utente) {
        this.nome_utente = nome_utente;
    }
    public String getTesto_commento() {
        return testo_commento;
    }
    public void setTesto_commento(String testo_commento) {
        this.testo_commento = testo_commento;
    }
    public String getData_commento() {
        return data_commento;
    }
    public void setData_commento(String data_commento) {
        this.data_commento = data_commento;
    }
}
