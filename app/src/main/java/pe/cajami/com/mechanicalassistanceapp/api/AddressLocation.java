package pe.cajami.com.mechanicalassistanceapp.api;

public class AddressLocation {
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String knownName; // Only if available else return NULL

    public AddressLocation() {
        this.address = "";
        this.city = "";
        this.state = "";
        this.country = "";
        this.postalCode = "";
        this.knownName = "";
    }

    public AddressLocation(String address, String city, String state, String country, String postalCode, String knownName) {
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.knownName = knownName;
    }

    public String getAddress() {
        return address;
    }

    public AddressLocation setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getCity() {
        return city;
    }

    public AddressLocation setCity(String city) {
        this.city = city;
        return this;
    }

    public String getState() {
        return state;
    }

    public AddressLocation setState(String state) {
        this.state = state;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public AddressLocation setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public AddressLocation setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public String getKnownName() {
        return knownName;
    }

    public AddressLocation setKnownName(String knownName) {
        this.knownName = knownName;
        return this;
    }
}
