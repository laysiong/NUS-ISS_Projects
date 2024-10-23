package edu.nus.adproject.services;

import android.util.Log;

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

import edu.nus.adproject.models.Comment;
import edu.nus.adproject.utils.ApiConfig;

public class CommentService {
    private static final String BASE_URL = ApiConfig.getBaseUrl()+"/pcmsgs/";

    public List<Comment> fetchChildCommentsFromAPI(int commentId) {
        HttpURLConnection conn = null;
        List<Comment> comments = new ArrayList<>();
        try {
            URL url = new URL(BASE_URL + commentId + "/children");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            // Parse the response into Comment objects
            JSONArray jsonArray = new JSONArray(result.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Comment comment = new Comment();

                if (jsonObject.has("id") && !jsonObject.getString("id").isEmpty()) {
                    int id = jsonObject.getInt("id");
                    comment.setId(id);
                } else {
                    // Log error or handle missing ID
                    Log.e("Comment Parsing", "Invalid or missing ID for comment.");
                    return null; // or handle it appropriately
                }

                // Handle other fields
                JSONObject userObject = jsonObject.getJSONObject("user_id");
                String commentUsername = userObject.getString("username");
                int userId = userObject.getInt("id");

                String commentContent = jsonObject.getString("content");
                String commentTimestampStr = jsonObject.getString("timeStamp");
                String commentStatus = jsonObject.getString("status");

                // Set other comment fields
                comment.setUser_id(userId);
                comment.setuser_name(commentUsername);
                comment.setContent(commentContent);
                comment.setTimeStamp(commentTimestampStr);
                comment.setStatus(commentStatus);

                comments.add(comment);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return comments;
    }
}
