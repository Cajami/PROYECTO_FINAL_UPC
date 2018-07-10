package pe.cajami.com.mechanicalassistanceapp.models;

import com.orm.SugarRecord;

public class Flaw extends SugarRecord{
    private int idflaw;
    private String description;

    public Flaw() {
    }

    public Flaw(int idflaws, String description) {
        this.idflaw = idflaws;
        this.description = description;
    }

    public int getIdflaw() {
        return idflaw;
    }

    public Flaw setIdflaw(int idflaw) {
        this.idflaw = idflaw;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Flaw setDescription(String description) {
        this.description = description;
        return this;
    }
}
