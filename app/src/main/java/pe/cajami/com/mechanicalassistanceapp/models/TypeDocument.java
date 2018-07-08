package pe.cajami.com.mechanicalassistanceapp.models;

import com.orm.SugarRecord;

public class TypeDocument extends SugarRecord {
    int idtypedocument;
    String description;

    public TypeDocument() {
    }

    public int getIdtypedocument() {
        return idtypedocument;
    }

    public TypeDocument(int idtypedocument, String description) {
        this.idtypedocument = idtypedocument;
        this.description = description;
    }

    public void setIdtypedocument(int idtypedocument) {
        this.idtypedocument = idtypedocument;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
