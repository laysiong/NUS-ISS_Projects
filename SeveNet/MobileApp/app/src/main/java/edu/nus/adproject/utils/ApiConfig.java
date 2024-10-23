package edu.nus.adproject.utils;

public class ApiConfig {


    public String Status;

    public static final String LOCAL_API_URL = "http://10.0.2.2:8080/api";
    public static final String PROD_API_URL = "http://ec2-18-233-108-144.compute-1.amazonaws.com:8080/api";


    public String BASE_URL;

    public ApiConfig() {
        BASE_URL = Status.equals("production") ? PROD_API_URL : LOCAL_API_URL;
    }

    public static String getBaseUrl() {
        String status = "development";  // You can change this as needed
        return status.equals("production") ? PROD_API_URL : LOCAL_API_URL;
    }

//    export const LOCAL_API_URL = "http://10.0.2.2:8080/api";
//    export const PROD_API_URL = "http://ec2-18-233-108-144.compute-1.amazonaws.com:8080/api";

}
