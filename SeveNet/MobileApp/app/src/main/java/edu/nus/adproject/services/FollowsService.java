package edu.nus.adproject.services;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.nus.adproject.models.User;
import edu.nus.adproject.utils.ApiConfig;

public class FollowsService {
    private static final String BASE_URL = ApiConfig.getBaseUrl()+"/user/";

    public List<User> getFollowersOrFollowing(int userId, boolean isFollowing) {
        List<User> userlist = new ArrayList<>();
        HttpURLConnection conn = null;

        String apiUrl = isFollowing
                ? BASE_URL + userId + "/followings"
                : BASE_URL + userId + "/followers";
        Log.d("FollowsService", "Calling API: " + isFollowing);

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            Log.d("FollowsService", "API response: " + result.toString());

            // Parse the result to a list of User objects
            JSONArray jsonArray = new JSONArray(result.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject userObject = jsonArray.getJSONObject(i);

                // Assuming User class has a constructor that accepts these parameters
                User user = new User();
                user.setId(userObject.getInt("id"));
                user.setUsername(userObject.getString("username"));
                user.setName(userObject.getString("name"));
                userlist.add(user);
            }
            Log.d("FollowsService", "Parsed " + userlist.size() + " users");

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("FollowsService", "Error fetching data", e);

            return null;
        }finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return userlist;
    }

    // Follow a user
    public boolean followUser(int currentUserId, int followUserId) {
        String apiUrl = BASE_URL + currentUserId + "/follow/" + followUserId;
        return sendPostRequest(apiUrl);
    }

    public boolean unfollowUser(int currentUserId, int followUserId) {
        String apiUrl = BASE_URL + currentUserId + "/unfollow/" + followUserId;
        return sendDeleteRequest(apiUrl);
    }

    public boolean isfollower(int currentUserId, int followUserId) {
        String apiUrl = BASE_URL + currentUserId + "/isfollower/" + followUserId;
        return sendGetRequest(apiUrl);
    }

    public Integer followingCount(int userId){
        String apiUrl = BASE_URL + userId +"/followingsCount";
        return sendGetIntRequest(apiUrl);
    }

    public Integer followerCount(int userId){
        String apiUrl = BASE_URL + userId +"/followersCount";
        return sendGetIntRequest(apiUrl);
    }


    private boolean sendPostRequest(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            int responseCode = connection.getResponseCode();
            Log.d("FollowsService", "POST request to " + apiUrl + " responded with " + responseCode);

            connection.disconnect();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean sendDeleteRequest(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");

            int responseCode = connection.getResponseCode();
            Log.d("FollowsService", "DELETE request to " + apiUrl + " responded with " + responseCode);

            connection.disconnect();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean sendGetRequest(String apiUrl) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            reader.close();
            conn.disconnect();

            return Boolean.valueOf(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }


    private Integer sendGetIntRequest(String apiUrl) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            reader.close();
            conn.disconnect();

            return Integer.valueOf(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

}
