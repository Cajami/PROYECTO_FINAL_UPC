package pe.cajami.com.mechanicalassistanceapp.models;

import com.orm.SugarRecord;

public class District extends SugarRecord {
    public int iddistrict;
    public String name;

    public District() {
    }

    public District(int iddistrict, String name) {
        this.iddistrict = iddistrict;
        this.name = name;
    }

    public int getIddistrict() {
        return iddistrict;
    }

    public District setIddistrict(int iddistrict) {
        this.iddistrict = iddistrict;
        return this;
    }

    public String getName() {
        return name;
    }

    public District setName(String name) {
        this.name = name;
        return this;
    }
}
