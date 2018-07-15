package pe.cajami.com.mechanicalassistanceapp.models;

import com.orm.SugarRecord;

import java.util.Date;

public class Favorite extends SugarRecord {

    private int idfavorite;
    private int idprovider;
    private Date date;
    private int idcustomer;
    private String idstate;

    public Favorite() {
    }

    public Favorite(int idfavorite, int idprovider, Date date, int idcustomer, String idstate) {
        this.idfavorite = idfavorite;
        this.idprovider = idprovider;
        this.date = date;
        this.idcustomer = idcustomer;
        this.idstate = idstate;
    }

    public int getIdfavorite() {
        return idfavorite;
    }

    public Favorite setIdfavorite(int idfavorite) {
        this.idfavorite = idfavorite;
        return this;
    }

    public int getIdprovider() {
        return idprovider;
    }

    public Favorite setIdprovider(int idprovider) {
        this.idprovider = idprovider;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Favorite setDate(Date date) {
        this.date = date;
        return this;
    }

    public int getIdcustomer() {
        return idcustomer;
    }

    public Favorite setIdcustomer(int idcustomer) {
        this.idcustomer = idcustomer;
        return this;
    }

    public String getIdstate() {
        return idstate;
    }

    public Favorite setIdstate(String idstate) {
        this.idstate = idstate;
        return this;
    }
}
