package pe.cajami.com.mechanicalassistanceapp.models;

import com.orm.SugarRecord;

public class Brand extends SugarRecord {
    private int idbrand;
    private String name;
    private String description;

    public Brand() {
    }

    public Brand(int idbrand, String name, String description) {
        this.idbrand = idbrand;
        this.name = name;
        this.description = description;
    }

    public int getIdbrand() {
        return idbrand;
    }

    public Brand setIdbrand(int idbrand) {
        this.idbrand = idbrand;
        return this;
    }

    public String getName() {
        return name;
    }

    public Brand setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Brand setDescription(String description) {
        this.description = description;
        return this;
    }
}
