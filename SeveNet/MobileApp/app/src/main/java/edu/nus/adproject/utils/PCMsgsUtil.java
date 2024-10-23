package edu.nus.adproject.utils;

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
import java.util.Locale;

public class PCMsgsUtil {

    public interface MessageCallback {
        void onSuccess();
        void onFailure();
    }

    // Method to post a message
    public static void postMessage(String endpoint, Integer postId, String content, int userId, MessageCallback callback) {
        new PostMessageTask(callback).execute(endpoint, postId, content, userId);
    }

    // AsyncTask to handle posting the message
    private static class PostMessageTask extends AsyncTask<Object, Void, Boolean> {
        private final MessageCallback callback;

        PostMessageTask(MessageCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            String endpoint = (String) params[0];
            Integer postId = (Integer) params[1];
            String content = (String) params[2];
            int userId = (int) params[3];

            // Build the JSON object with the message content
            JSONObject jsonParam = new JSONObject();
            try {
                jsonParam.put("imageUrl", JSONObject.NULL);
                jsonParam.put("content", content);
                if (postId != null) {
                    jsonParam.put("sourceId", postId);
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                jsonParam.put("timeStamp", sdf.format(new Date()));
                jsonParam.put("visibility", true);
                jsonParam.put("status", "show");

                JSONObject userObject = new JSONObject();
                userObject.put("id", userId);
                jsonParam.put("user", userObject);

                JSONObject tagObject = new JSONObject();
                tagObject.put("tag", "none");
                tagObject.put("remark", JSONObject.NULL);
                jsonParam.put("tag", tagObject);

                // Execute the HTTP request
                return executePostRequest(endpoint, jsonParam);
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

        // Helper method to execute POST requests
        private boolean executePostRequest(String endpoint, JSONObject jsonParam) {
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

    // Method to like a message
    public static void likeMessage(String endpoint, int messageId, MessageCallback callback) {
        new LikeMessageTask(callback).execute(endpoint, messageId);
    }

    // AsyncTask to handle liking the message
    private static class LikeMessageTask extends AsyncTask<Object, Void, Boolean> {
        private final MessageCallback callback;

        LikeMessageTask(MessageCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            String endpoint = (String) params[0];
            int messageId = (int) params[1];

            // Build the JSON object with the like details
            JSONObject jsonParam = new JSONObject();
            try {
                jsonParam.put("id", messageId);

                // Execute the HTTP request
                return executePostRequest(endpoint, jsonParam);
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

        // Helper method to execute POST requests
        private boolean executePostRequest(String endpoint, JSONObject jsonParam) {
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