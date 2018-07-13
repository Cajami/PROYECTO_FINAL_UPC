package pe.cajami.com.mechanicalassistanceapp.models;

import com.orm.SugarRecord;

import java.util.Date;

public class RequestHistory extends SugarRecord {
    private int idrequesthistory;
    private int idrequest;
    private Date date;
    private String idstate;
    private int idprovider;
    private double latitude;
    private double longitude;

    public RequestHistory() {
    }

    public RequestHistory(int idrequesthistory, int idrequest, Date date, String idstate, int idprovider, double latitude, double longitude) {
        this.idrequesthistory = idrequesthistory;
        this.idrequest = idrequest;
        this.date = date;
        this.idstate = idstate;
        this.idprovider = idprovider;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getIdrequesthistory() {
        return idrequesthistory;
    }

    public RequestHistory setIdrequesthistory(int idrequesthistory) {
        this.idrequesthistory = idrequesthistory;
        return this;
    }

    public int getIdrequest() {
        return idrequest;
    }

    public RequestHistory setIdrequest(int idrequest) {
        this.idrequest = idrequest;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public RequestHistory setDate(Date date) {
        this.date = date;
        return this;
    }

    public String getIdstate() {
        return idstate;
    }

    public RequestHistory setIdstate(String idstate) {
        this.idstate = idstate;
        return this;
    }

    public int getIdprovider() {
        return idprovider;
    }

    public RequestHistory setIdprovider(int idprovider) {
        this.idprovider = idprovider;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public RequestHistory setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public RequestHistory setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }
}
