package edu.nus.adproject.services;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.nus.adproject.utils.ApiConfig;
import edu.nus.adproject.utils.PCMsgsUtil;

public class ReportService {
    private static final String BASE_URL = ApiConfig.getBaseUrl()+"/reports/";

    public interface MessageCallback {
        void onSuccess();
        void onFailure();
    }

    public static void postReportTask(String type, Integer reportId, int reporterId, int labelId, String content, MessageCallback callback) {
        new postReportTask(callback).execute(type, reportId, reporterId, labelId, content);
    }

    private static class postReportTask extends AsyncTask<Object, Void, Boolean> {
        private final MessageCallback callback;

        postReportTask(MessageCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            String endpoint = BASE_URL + "create";
            String type = (String) params[0];
            Integer reportId = (Integer) params[1];
            int reporterId = (int) params[2];
            int labelId = (int) params[3];
            String content = (String) params[4];

            String prefixedReportId = "";
            if (type == "user") {
                prefixedReportId = "u"+ reportId;
            } else if (type == "post") {
                prefixedReportId = "p"+ reportId;
            } else if (type == "comment") {
                prefixedReportId = "c"+reportId;
            }


            // Create a SimpleDateFormat instance with ISO 8601 format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            // Format the current date into the desired format
            String formattedDate = sdf.format(new Date());

            // Build the JSON object with the message content
            JSONObject jsonParam = new JSONObject();
            try {
                JSONObject labelObject = new JSONObject();
                labelObject.put("id", labelId);

                JSONObject reportUserObject = new JSONObject();
                reportUserObject.put("id", reporterId); // Use "id" instead of "user_id"

                jsonParam.put("label", labelObject);
                jsonParam.put("reason", content);
                jsonParam.put("reportedId", prefixedReportId);
                jsonParam.put("reportUser", reportUserObject); // Use the nested object here
                jsonParam.put("reportDate", formattedDate);
                jsonParam.put("status", "Pending");

                Log.d("ReportService", "JSON Request: " + jsonParam.toString());

                return executeReportRequest(endpoint, jsonParam);
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                callback.onSuccess();
            } else {
                callback.onFailure();
            }
        }

        // To execute request
        private boolean executeReportRequest(String endpoint, JSONObject jsonParam) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(endpoint);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                OutputStream out = conn.getOutputStream();
                out.write(jsonParam.toString().getBytes());
                out.flush();
                out.close();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                    // Handle the response if needed
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();
                    // Process the response if needed
                    return true;
                } else {
                    // Log error or handle response code accordingly
                    Log.e("HTTP_POST", "Server returned response code: " + conn.getResponseCode());
                    return false;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
    }
}