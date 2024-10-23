package edu.nus.adproject.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CountryService {
    public List<String> getCountries() {
        HttpURLConnection conn = null;
        try {
            URL url = new URL("https://restcountries.com/v3.1/all");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            String result = convertStreamToString(inputStream);

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            final List<String> countryNames = parseCountries(result);
            return countryNames;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private List<String> parseCountries(String jsCountries) {
        List<String> countryNames = new ArrayList<>();
        try {
            JSONArray countriesArray = new JSONArray(jsCountries);

            for (int i = 0; i < countriesArray.length(); i++) {
                JSONObject countryObject = countriesArray.getJSONObject(i);
                String countryName = countryObject.getJSONObject("name").getString("common");
                countryNames.add(countryName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return countryNames;
    }
}
