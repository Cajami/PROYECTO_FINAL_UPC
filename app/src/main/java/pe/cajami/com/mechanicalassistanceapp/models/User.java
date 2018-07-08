package pe.cajami.com.mechanicalassistanceapp.models;

import com.orm.SugarRecord;

public class User extends SugarRecord {
    private int iduser;
    private String name;
    private String password;
    private int idtypeuser;

    public User() {
    }

    public User(int iduser, String name, String password, int idtypeuser) {
        this.iduser = iduser;
        this.name = name;
        this.password = password;
        this.idtypeuser = idtypeuser;
    }

    public int getIduser() {
        return iduser;
    }

    public User setIduser(int iduser) {
        this.iduser = iduser;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public int getIdtypeuser() {
        return idtypeuser;
    }

    public User setIdtypeuser(int idtypeuser) {
        this.idtypeuser = idtypeuser;
        return this;
    }
}
