package pe.cajami.com.mechanicalassistanceapp.models;

public class TypeUser {
    private int idtypeuser;
    private String name;
    private String description;

    public TypeUser() {
    }

    public TypeUser(int idtypeuser, String name, String description) {
        this.idtypeuser = idtypeuser;
        this.name = name;
        this.description = description;
    }

    public int getIdtypeuser() {
        return idtypeuser;
    }

    public void setIdtypeuser(int idtypeuser) {
        this.idtypeuser = idtypeuser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
