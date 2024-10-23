package edu.nus.adproject.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.nus.adproject.R;
import edu.nus.adproject.fragments.CommentListFragment;
import edu.nus.adproject.models.Comment;
import edu.nus.adproject.models.PCMsg;
import edu.nus.adproject.models.User;
import edu.nus.adproject.utils.ApiConfig;

public class PostsService {

    private static final String BASE_URL = ApiConfig.getBaseUrl()+"/pcmsgs/";


    public PCMsg fetchPostsById(int postId) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(BASE_URL + "findPostDetailById/" + postId);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            // Parse the response into a JSON object
            JSONObject jsonObject = new JSONObject(result.toString());
            JSONObject pcmsg = jsonObject.getJSONObject("pcmsg");

            // Extract the relevant data for the post
            int id = pcmsg.getInt("id");
            int userId = pcmsg.getJSONObject("user_id").getInt("id");
            String username = pcmsg.getJSONObject("user_id").getString("username");
            String content = pcmsg.getString("content");
            String timestamp = pcmsg.getString("timeStamp");

            // Create the PCMsg object and populate it
            PCMsg post = new PCMsg();
            post.setId(id);
            post.setContent(content);
            post.setTimeStamp(timestamp);

            // Create and set the User object
            User user = new User();
            user.setId(userId);
            user.setUsername(username);
            post.setUser(user);

            // Inside fetchPostsById method
            if (jsonObject.has("comments")) {
                JSONArray commentsArray = jsonObject.getJSONArray("comments");
                List<Comment> commentsList = new ArrayList<>();

                for (int i = 0; i < commentsArray.length(); i++) {
                    JSONObject commentObject = commentsArray.getJSONObject(i);
                    Comment comment = new Comment();
                    comment.setId(commentObject.getInt("id"));
                    comment.setContent(commentObject.getString("content"));
                    comment.setTimeStamp(commentObject.getString("timeStamp"));
                    comment.setStatus(commentObject.getString("status"));

                    // Extract and set the user associated with the comment
                    JSONObject commentUserObject = commentObject.getJSONObject("user_id");
                    User commentUser = new User();
                    commentUser.setId(commentUserObject.getInt("id"));
                    commentUser.setUsername(commentUserObject.getString("username"));
                    comment.setuser_name(commentUser.getUsername());

                    // Add the comment to the list
                    commentsList.add(comment);
                }

                // Associate the comments with the post
                post.setCommentList(commentsList);
            } else {
                post.setCommentList(new ArrayList<>());  // Initialize an empty list if no comments
            }

            // Return the fetched PCMsg object
            return post;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // Close the BufferedReader and disconnect the HttpURLConnection
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }




    public List<PCMsg> getPosts(int userId) {
        List<PCMsg> posts = new ArrayList<>();
        HttpURLConnection conn = null;

        try {
            URL url = new URL(BASE_URL + "findAllFollowingPostsAndNotDeletedByUserId/" + userId);
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
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Create PCMsg object from JSON
                PCMsg post = new PCMsg();
                post.setId(jsonObject.getInt("id"));
                post.setImageUrl(jsonObject.optString("imageUrl", null));
                post.setContent(jsonObject.optString("content", null));
                //post.setSourceId(jsonObject.optInt("sourceId", -1));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                post.setTimeStamp(jsonObject.optString("timeStamp", null));

                post.setVisibility(jsonObject.getBoolean("visibility"));
                post.setStatus(jsonObject.optString("status", null));

                // Handle nested JSON objects for user and tag if present
                if (jsonObject.has("user_id")) {
                    JSONObject userJson = jsonObject.getJSONObject("user_id");
                    User user = new User();
                    user.setId(userJson.getInt("id")); // Assuming User has id property
                    user.setName(userJson.optString("name", null)); // Adjust fields as needed
                    post.setUser(user);
                }


                // Add the PCMsg object to the list
                posts.add(post);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return posts;
    }

    public int countLikesByPCMsgId(int postId) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(BASE_URL + "countLikesByPCMsgId/" + postId);
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

            return Integer.parseInt(result.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int countAllCommentsByPostId(int postId) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(BASE_URL + "countAllCommentsByPostId/" + postId);
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

            return Integer.parseInt(result.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public List<PCMsg> getHotPosts() {
        List<PCMsg> posts = new ArrayList<>();
        HttpURLConnection conn = null;

        try {
            URL url = new URL(BASE_URL + "findHotPosts");
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
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Create PCMsg object from JSON
                PCMsg post = new PCMsg();
                post.setId(jsonObject.getInt("id"));
                post.setImageUrl(jsonObject.optString("imageUrl", null));
                post.setContent(jsonObject.optString("content", null));
                //post.setSourceId(jsonObject.optInt("sourceId", -1));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                post.setTimeStamp(jsonObject.optString("timeStamp", null));

                post.setVisibility(jsonObject.getBoolean("visibility"));
                post.setStatus(jsonObject.optString("status", null));

                // Handle nested JSON objects for user and tag if present
                if (jsonObject.has("user_id")) {
                    JSONObject userJson = jsonObject.getJSONObject("user_id");
                    User user = new User();
                    user.setId(userJson.getInt("id")); // Assuming User has id property
                    user.setName(userJson.optString("name", null)); // Adjust fields as needed
                    post.setUser(user);
                }

                posts.add(post);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return posts;
    }

    public List<PCMsg> getUserPosts(int postId) {
        List<PCMsg> posts = new ArrayList<>();
        HttpURLConnection conn = null;

        try {
            URL url = new URL(BASE_URL + "findAllPostsByUserId/"+postId);
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
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Create PCMsg object from JSON
                PCMsg post = new PCMsg();
                post.setId(jsonObject.getInt("id"));
                post.setImageUrl(jsonObject.optString("imageUrl", null));
                post.setContent(jsonObject.optString("content", null));
                //post.setSourceId(jsonObject.optInt("sourceId", -1));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                post.setTimeStamp(jsonObject.optString("timeStamp", null));

                post.setVisibility(jsonObject.getBoolean("visibility"));
                post.setStatus(jsonObject.optString("status", null));

                // Handle nested JSON objects for user and tag if present
                if (jsonObject.has("user_id")) {
                    JSONObject userJson = jsonObject.getJSONObject("user_id");
                    User user = new User();
                    user.setId(userJson.getInt("id")); // Assuming User has id property
                    user.setName(userJson.optString("name", null)); // Adjust fields as needed
                    post.setUser(user);
                }


                posts.add(post);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return posts;
    }

    //Toggle like
    public Boolean togglelike (int userId, int postId) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(BASE_URL +"likeOrUnlikePCMsg/" + userId + "/" + postId);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");

            int responseCode = conn.getResponseCode();
            conn.disconnect();

            return responseCode == HttpURLConnection.HTTP_OK;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    //return boolean to see if user likes the post/commets
    public Boolean CheckUserLikeTask(int userId,  int postId){

        boolean results;
        HttpURLConnection conn = null;
        try {
            //URL url = new URL("http://ec2-18-233-108-144.compute-1.amazonaws.com:8080/api/pcmsgs/" + postId + "/likes/" + userId);
            URL url = new URL(ApiConfig.getBaseUrl() + "/pcmsgs/" + postId + "/likes/" + userId);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = new BufferedInputStream(conn.getInputStream());
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Parse the result as a Boolean
                    results = Boolean.parseBoolean(result.toString().trim());
                }
            } else {
                // Handle other response codes (e.g., 404, 500)
                System.err.println("Server returned error: " + responseCode);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return results;
    }



    public static class DeletePostTask extends AsyncTask<Integer, Void, Boolean> {
        private Context context;

        public DeletePostTask(Context context) {
            this.context = context.getApplicationContext();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            int postId = params[0];
            return deleteByPostId(postId);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(context, "Post deleted successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to delete post.", Toast.LENGTH_SHORT).show();
            }
        }

        private boolean  deleteByPostId(int postId) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(BASE_URL + "delete/" + postId);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");

                int responseCode = conn.getResponseCode();
                Log.d("DeletePostTask", "Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                    // The server has successfully processed the request
                    return true;
                } else {
                    // The server returned an error
                    Log.e("DeletePostTask", "Failed to delete post. Server responded with code: " + responseCode);
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

    public static class HidePostTask extends AsyncTask<Integer, Void, Boolean> {
        private Context context;

        public HidePostTask(Context context) {
            this.context = context.getApplicationContext();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            int postId = params[0];
            return hideByPostId(postId);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(context, "Post deleted successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to delete post.", Toast.LENGTH_SHORT).show();
            }
        }

        private boolean hideByPostId(int postId) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(BASE_URL + "hide/" + postId);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");


                int responseCode = conn.getResponseCode();
                Log.d("hidePostTask", "Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                    // The server has successfully processed the request
                    return true;
                } else {
                    // The server returned an error
                    Log.e("hidePostTask", "Failed to delete post. Server responded with code: " + responseCode);
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
