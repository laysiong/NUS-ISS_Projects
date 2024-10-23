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

import edu.nus.adproject.models.Notification;
import edu.nus.adproject.models.NotificationStatus;
import edu.nus.adproject.utils.ApiConfig;

public class NotificationService {
    private static final String BASE_URL = ApiConfig.getBaseUrl()+"/notifications/";

    public List<Notification> getNoti(int userId) {
        List<Notification> notifications = new ArrayList<>();
        HttpURLConnection conn = null;

        try {
            URL url = new URL(BASE_URL + "findAllByUserId/" + userId);
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
                Notification notification = new Notification();
                notification.setId(jsonObject.getInt("id"));
                notification.setMessage(jsonObject.optString("message"));
                notification.setNotificationTime(jsonObject.optString("notificationTime"));
                notification.setTitle(jsonObject.optString("title"));

                String status = jsonObject.optString("notificationStatus");
                if (status.equalsIgnoreCase("Read")) {
                    notification.setNotificationStatus(NotificationStatus.Read);
                } else if (status.equalsIgnoreCase("Unread")) {
                    notification.setNotificationStatus(NotificationStatus.Unread);
                }

                notifications.add(notification);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return notifications;
    }
}
