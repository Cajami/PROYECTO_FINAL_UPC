package pe.cajami.com.mechanicalassistanceapp.models;

import com.orm.SugarRecord;

public class Customer extends SugarRecord {
    private int idcustomer;
    private int itypedocument;
    private String nrodocumento;
    private String name;
    private String address;
    private int iddistrict;
    private String phone;
    private String email;
    private int iduser;

    public Customer() {
    }

    public Customer(int idcustomer, int itypedocument, String nrodocumento, String name, String address, int iddistrict, String phone, String email, int iduser) {
        this.idcustomer = idcustomer;
        this.itypedocument = itypedocument;
        this.nrodocumento = nrodocumento;
        this.name = name;
        this.address = address;
        this.iddistrict = iddistrict;
        this.phone = phone;
        this.email = email;
        this.iduser = iduser;
    }

    public int getIdcustomer() {
        return idcustomer;
    }

    public Customer setIdcustomer(int idcustomer) {
        this.idcustomer = idcustomer;
        return this;
    }

    public int getItypedocument() {
        return itypedocument;
    }

    public Customer setItypedocument(int itypedocument) {
        this.itypedocument = itypedocument;
        return this;
    }

    public String getNrodocumento() {
        return nrodocumento;
    }

    public Customer setNrodocumento(String nrodocumento) {
        this.nrodocumento = nrodocumento;
        return this;
    }

    public String getName() {
        return name;
    }

    public Customer setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Customer setAddress(String address) {
        this.address = address;
        return this;
    }

    public int getIddistrict() {
        return iddistrict;
    }

    public Customer setIddistrict(int iddistrict) {
        this.iddistrict = iddistrict;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Customer setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Customer setEmail(String email) {
        this.email = email;
        return this;
    }

    public int getIduser() {
        return iduser;
    }

    public Customer setIduser(int iduser) {
        this.iduser = iduser;
        return this;
    }
}
