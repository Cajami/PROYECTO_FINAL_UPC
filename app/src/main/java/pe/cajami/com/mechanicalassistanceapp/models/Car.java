package pe.cajami.com.mechanicalassistanceapp.models;

import com.orm.SugarRecord;

public class Car extends SugarRecord {
    private int idcar;
    private int idbrand;
    private String model;
    private int year;
    private int idcustomer;

    public Car() {
    }

    public Car(int idcar, int idbrand, String model, int year, int idcustomer) {
        this.idcar = idcar;
        this.idbrand = idbrand;
        this.model = model;
        this.year = year;
        this.idcustomer = idcustomer;
    }

    public int getIdcar() {
        return idcar;
    }

    public Car setIdcar(int idcar) {
        this.idcar = idcar;
        return this;
    }

    public int getIdbrand() {
        return idbrand;
    }

    public Car setIdbrand(int idbrand) {
        this.idbrand = idbrand;
        return this;
    }

    public String getModel() {
        return model;
    }

    public Car setModel(String model) {
        this.model = model;
        return this;
    }

    public int getYear() {
        return year;
    }

    public Car setYear(int year) {
        this.year = year;
        return this;
    }

    public int getIdcustomer() {
        return idcustomer;
    }

    public Car setIdcustomer(int idcustomer) {
        this.idcustomer = idcustomer;
        return this;
    }
}
