package com.example.flippingcard;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloader
{
    private File dir;

    public ImageDownloader(File dir) {
        this.dir = dir;
    }

    protected boolean downloadImage(String imgURL, File destFile)
    {

        HttpURLConnection conn = null;
        InputStream in = null;
        FileOutputStream out = null;

        try {
            Thread.sleep(1000);

            // create network connection
            URL url = new URL(imgURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");   // optional: default is GET
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            // check HTTP response code
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                //Log.e("fail repsonse code","fail to get repsonsecode");
                return false;
            }

            // setup input and output streams
            in = conn.getInputStream();
            out = new FileOutputStream(destFile);

            // read in streaming bytes from server
            int contentLength = conn.getContentLength();
            byte[] buf = new byte[4096];
            int bytesRead;

            while ((bytesRead = in.read(buf)) != -1) {
                out.write(buf, 0, bytesRead);

            }

            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Log.d("interrupted", "stop image downloader");
            cleanupTemporaryFiles();
            return false;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private void cleanupTemporaryFiles() {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }
}
