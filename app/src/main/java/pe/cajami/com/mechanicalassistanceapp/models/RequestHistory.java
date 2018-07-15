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
    private String nameProvider;
    private int scoreProvider;

    private double latitudeParent;
    private double longitudeParent;

    public RequestHistory() {
    }

    public RequestHistory(int idrequesthistory, int idrequest, Date date, String idstate, int idprovider, double latitude, double longitude, String nameProvider, int scoreProvider, double latitudeParent, double longitudeParent) {
        this.idrequesthistory = idrequesthistory;
        this.idrequest = idrequest;
        this.date = date;
        this.idstate = idstate;
        this.idprovider = idprovider;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nameProvider = nameProvider;
        this.scoreProvider = scoreProvider;
        this.latitudeParent = latitudeParent;
        this.longitudeParent = longitudeParent;
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

    public String getNameProvider() {
        return nameProvider;
    }

    public RequestHistory setNameProvider(String nameProvider) {
        this.nameProvider = nameProvider;
        return this;
    }

    public int getScoreProvider() {
        return scoreProvider;
    }

    public RequestHistory setScoreProvider(int scoreProvider) {
        this.scoreProvider = scoreProvider;
        return this;
    }

    public double getLatitudeParent() {
        return latitudeParent;
    }

    public RequestHistory setLatitudeParent(double latitudeParent) {
        this.latitudeParent = latitudeParent;
        return this;
    }

    public double getLongitudeParent() {
        return longitudeParent;
    }

    public RequestHistory setLongitudeParent(double longitudeParent) {
        this.longitudeParent = longitudeParent;
        return this;
    }
}
