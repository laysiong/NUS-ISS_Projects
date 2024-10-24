package com.example.flippingcard;



import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetURLHtml {

    HttpURLConnection connection = null;
    InputStream in = null;

  protected String fetchHtmlContent(String imgURL) throws IOException {

        StringBuilder htmlContent = new StringBuilder();
        URL url = new URL(imgURL);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0"); // Set User-Agent

        try {
            in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                htmlContent.append(line).append("\n");
            }

        }catch (Exception e){
            //Log.d("get html","unable to grab html content");
            return "Error";
        }
        finally {
            connection.disconnect(); // Close the connection
        }

        return htmlContent.toString();
    }

    protected List<String> extractImageUrls(String htmlContent, String imgURL) {
        List<String> imageUrls = new ArrayList<>();
        Pattern pattern = Pattern.compile("<img[^>]+src=[\"']([^\"']+\\.(jpg|png))[\"'][^>]*>", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(htmlContent);

        while (matcher.find()) {
            String imageUrl = matcher.group(1);
            if (!imageUrl.startsWith("http")) {
                imageUrl = imgURL + imageUrl; // Handle relative URLs
            }
            imageUrls.add(imageUrl);
        }
        return imageUrls;
    }
}
