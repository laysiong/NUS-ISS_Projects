package edu.nus.adproject.services;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.nus.adproject.utils.ApiConfig;

public class RegisterService {
    public boolean registerUser(JSONObject jsonParam) {
        //String baseUrl = "http://ec2-18-233-108-144.compute-1.amazonaws.com:8080/api/user/register";
        String baseUrl = ApiConfig.getBaseUrl()+"/user/register";

        HttpURLConnection conn = null;
        try {
            URL url = new URL(baseUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            OutputStream out = conn.getOutputStream();
            out.write(jsonParam.toString().getBytes());
            out.flush();
            out.close();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return false;
    }
}
