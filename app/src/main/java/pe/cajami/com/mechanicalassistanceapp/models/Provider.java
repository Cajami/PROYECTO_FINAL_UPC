package pe.cajami.com.mechanicalassistanceapp.models;

import com.orm.SugarRecord;

public class Provider extends SugarRecord {

    private int idprovider;
    private int itypedocument;
    private String nrodocument;
    private String name;
    private String address;
    private int iddistrict;
    private String contact;
    private String phone;
    private String email;
    private String web;
    private double longitude;
    private double latitude;
    private int score;
    private String schedule;
    private int iduser;

    public Provider() {
    }

    public Provider(int idprovider, int itypedocument, String nrodocument, String name, String address, int iddistrict, String contact, String phone, String email, String web, double longitude, double latitude, int score, String schedule, int iduser) {
        this.idprovider = idprovider;
        this.itypedocument = itypedocument;
        this.nrodocument = nrodocument;
        this.name = name;
        this.address = address;
        this.iddistrict = iddistrict;
        this.contact = contact;
        this.phone = phone;
        this.email = email;
        this.web = web;
        this.longitude = longitude;
        this.latitude = latitude;
        this.score = score;
        this.schedule = schedule;
        this.iduser = iduser;
    }

    public int getIdprovider() {
        return idprovider;
    }

    public Provider setIdprovider(int idprovider) {
        this.idprovider = idprovider;
        return this;
    }

    public int getItypedocument() {
        return itypedocument;
    }

    public Provider setItypedocument(int itypedocument) {
        this.itypedocument = itypedocument;
        return this;
    }

    public String getNrodocument() {
        return nrodocument;
    }

    public Provider setNrodocument(String nrodocument) {
        this.nrodocument = nrodocument;
        return this;
    }

    public String getName() {
        return name;
    }

    public Provider setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Provider setAddress(String address) {
        this.address = address;
        return this;
    }

    public int getIddistrict() {
        return iddistrict;
    }

    public Provider setIddistrict(int iddistrict) {
        this.iddistrict = iddistrict;
        return this;
    }

    public String getContact() {
        return contact;
    }

    public Provider setContact(String contact) {
        this.contact = contact;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Provider setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Provider setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getWeb() {
        return web;
    }

    public Provider setWeb(String web) {
        this.web = web;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public Provider setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public Provider setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public int getScore() {
        return score;
    }

    public Provider setScore(int score) {
        this.score = score;
        return this;
    }

    public String getSchedule() {
        return schedule;
    }

    public Provider setSchedule(String schedule) {
        this.schedule = schedule;
        return this;
    }

    public int getIduser() {
        return iduser;
    }

    public Provider setIduser(int iduser) {
        this.iduser = iduser;
        return this;
    }
}
