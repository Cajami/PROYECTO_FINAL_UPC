package pe.cajami.com.mechanicalassistanceapp.models;

import com.orm.SugarRecord;

import java.util.Date;

public class Request extends SugarRecord {
    private int idrequest;
    private Date date;
    private int idcar;
    private String idstate;
    private String details;
    private int idflaw;
    private String description;

    private String district;
    private String address;
    private double latitude;
    private double longitude;

    public Request() {
    }

    public Request(int idrequest, Date date, int idcar, String idstate, String details, int idflaw, String description, String district, String address, double latitude, double longitude) {
        this.idrequest = idrequest;
        this.date = date;
        this.idcar = idcar;
        this.idstate = idstate;
        this.details = details;
        this.idflaw = idflaw;
        this.description = description;
        this.district = district;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getIdrequest() {
        return idrequest;
    }

    public Request setIdrequest(int idrequest) {
        this.idrequest = idrequest;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Request setDate(Date date) {
        this.date = date;
        return this;
    }

    public int getIdcar() {
        return idcar;
    }

    public Request setIdcar(int idcar) {
        this.idcar = idcar;
        return this;
    }

    public String getIdstate() {
        return idstate;
    }

    public Request setIdstate(String idstate) {
        this.idstate = idstate;
        return this;
    }

    public String getDetails() {
        return details;
    }

    public Request setDetails(String details) {
        this.details = details;
        return this;
    }

    public int getIdflaw() {
        return idflaw;
    }

    public Request setIdflaw(int idflaw) {
        this.idflaw = idflaw;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Request setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDistrict() {
        return district;
    }

    public Request setDistrict(String district) {
        this.district = district;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Request setAddress(String address) {
        this.address = address;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public Request setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public Request setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }
}
