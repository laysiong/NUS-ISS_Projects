package com.example.flippingcard;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

public class FetchImages {

     protected boolean downloadImage(String imgURL, File destFile){
         HttpsURLConnection conn = null;
         InputStream in = null;
         FileOutputStream out = null;

         try{
             //create network connection
             URL url = new URL(imgURL);
             conn = (HttpsURLConnection) url.openConnection();
             conn.setRequestMethod("GET");

             if(conn.getResponseCode() != HttpURLConnection.HTTP_OK){
                 return false;
             }

             in = conn.getInputStream();
             out = new FileOutputStream(destFile);

             byte[] buf = new byte[4096];
             int bytesRead;
             while((bytesRead = in.read(buf)) != -1){
                 out.write(buf,0,bytesRead);
             }
             return true;
         }catch (Exception e){
             e.printStackTrace();
             return false;
         }finally {
              try{
                  if(in != null) in.close();
                  if(out !=null) out.close();
              }catch (Exception e){
                  e.printStackTrace();
              }
              if(conn != null){
                  conn.disconnect();
              }
         }

     }

}


