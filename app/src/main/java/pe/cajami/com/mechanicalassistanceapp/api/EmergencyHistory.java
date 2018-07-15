package pe.cajami.com.mechanicalassistanceapp.api;

import java.util.Date;

public class EmergencyHistory {
    public int idrequest;
    private int idRequestHistory;

    private Date date;
    private Date datefinish;
    private int idcustomer;
    private int idprovider;

    private String idflaw;
    private String detalleFlaw;

    private String nameProvider;
    private int score;

    private String district;

    public EmergencyHistory() {
    }

    public int getIdrequest() {
        return idrequest;
    }

    public EmergencyHistory setIdrequest(int idrequest) {
        this.idrequest = idrequest;
        return this;
    }

    public int getIdRequestHistory() {
        return idRequestHistory;
    }

    public EmergencyHistory setIdRequestHistory(int idRequestHistory) {
        this.idRequestHistory = idRequestHistory;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public EmergencyHistory setDate(Date date) {
        this.date = date;
        return this;
    }

    public Date getDatefinish() {
        return datefinish;
    }

    public EmergencyHistory setDatefinish(Date datefinish) {
        this.datefinish = datefinish;
        return this;
    }

    public int getIdcustomer() {
        return idcustomer;
    }

    public EmergencyHistory setIdcustomer(int idcustomer) {
        this.idcustomer = idcustomer;
        return this;
    }

    public int getIdprovider() {
        return idprovider;
    }

    public EmergencyHistory setIdprovider(int idprovider) {
        this.idprovider = idprovider;
        return this;
    }

    public String getIdflaw() {
        return idflaw;
    }

    public EmergencyHistory setIdflaw(String idflaw) {
        this.idflaw = idflaw;
        return this;
    }

    public String getDetalleFlaw() {
        return detalleFlaw;
    }

    public EmergencyHistory setDetalleFlaw(String detalleFlaw) {
        this.detalleFlaw = detalleFlaw;
        return this;
    }

    public String getNameProvider() {
        return nameProvider;
    }

    public EmergencyHistory setNameProvider(String nameProvider) {
        this.nameProvider = nameProvider;
        return this;
    }

    public int getScore() {
        return score;
    }

    public EmergencyHistory setScore(int score) {
        this.score = score;
        return this;
    }

    public String getDistrict() {
        return district;
    }

    public EmergencyHistory setDistrict(String district) {
        this.district = district;
        return this;
    }
}
