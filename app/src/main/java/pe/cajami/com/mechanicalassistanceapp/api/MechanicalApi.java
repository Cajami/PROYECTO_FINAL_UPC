package pe.cajami.com.mechanicalassistanceapp.api;

public class MechanicalApi {
    //private static String BASE_URL = "https://mechanical-assistance-cajamisoft.c9users.io/";
    private static String BASE_URL = "http://192.168.1.6:3000/";


    public static String loginApi() {
        return BASE_URL + "autenticate";
    }

    public static String registerUsers() {
        return BASE_URL + "registerUser";
    }

    private static String urlApi() {
        return BASE_URL + "api/";
    }

    public static String getTypesDocuments() {
        return BASE_URL + "getTypeDocuments";
    }

    public static String getTypesUers() {
        return BASE_URL + "getTypeUsers";
    }

    public static String getDistrict() {
        return BASE_URL + "getDistrict";
    }

}
