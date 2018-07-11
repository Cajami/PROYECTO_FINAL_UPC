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

    public Request() {
    }

    public Request(int idrequest, Date date, int idcar, String idstate, String details, int idflaw) {
        this.idrequest = idrequest;
        this.date = date;
        this.idcar = idcar;
        this.idstate = idstate;
        this.details = details;
        this.idflaw = idflaw;
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
}
