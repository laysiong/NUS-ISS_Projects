package edu.nus.adproject.services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.nus.adproject.models.Label;
import edu.nus.adproject.utils.ApiConfig;

public class LabelService {

    private static final String BASE_URL = ApiConfig.getBaseUrl()+"/label/";

    public List<Label> getLabel(){
        List<Label> labelist = new ArrayList<>();
        HttpURLConnection conn = null;

        try{
            URL url = new URL(BASE_URL + "findAll");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            JSONArray jsonArray = new JSONArray(result.toString());
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                //Create Report Objectc from JSON
                Label Labelform = new Label();
                Labelform.setId(jsonObject.getInt("id"));
                Labelform.setLabel(jsonObject.getString("label"));

                labelist.add(Labelform);
            }


        }catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return labelist;

    }


}
