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

    public static String getFlaws() {
        return BASE_URL + "getFlaws";
    }

    public static String getBrands() {
        return BASE_URL + "getBrands";
    }

    public static String editCustomer() {
        return urlApi() + "editCustomer";
    }

    public static String editProvider() {
        return urlApi() + "editProvider";
    }

    public static String insertRequests() {
        return urlApi() + "insertRequests";
    }

    public static String insertRequestsHistory() {
        return urlApi() + "insertEmergencyFreeHistorial";
    }

    public static String getEmergenciesFree() {
        return urlApi() + "getEmergencyFree";
    }

    public static String getEmergencyFreeHistorial() {
        return urlApi() + "getEmergencyFreeHistorial";
    }


    public static String getEmergencyProviders() {
        return urlApi() + "getEmergencyProviders";
    }

    public static String setEmergencyToProviders() {
        return urlApi() + "setEmergencyToProviders";
    }

    public static String setEmergencyFinishs() {
        return urlApi() + "setEmergencyFinishs";
    }

    public static String getEmergencyHistories() {
        return urlApi() + "getEmergencyHistories";
    }





}
