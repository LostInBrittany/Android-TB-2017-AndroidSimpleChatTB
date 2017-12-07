package org.lostinbrittany.teaching.android.tb.simpletchat.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class NetworkHelper {

    public static boolean isInternetAvailable(Context context) {
        try {
            ConnectivityManager cm
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        } catch (Exception e) {
            Log.e("HelloWorld", "Error on checking internet:", e);

        }
        //default allowed to access internet
        return true;
    }

    // Reads an InputStream and converts it to a String.
    private static String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        int ch;
        StringBuffer sb = new StringBuffer();
        /*
        while ((ch = stream.read()) != -1) {
            sb.append((char) ch);
        }
        Log.d("Read Response", sb.toString());
        */
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");

        while ((ch = reader.read()) != -1) {
            sb.append((char) ch);
        }
        Log.d("Read Response", "Begin");
        if (sb.length() > 0) {
            return sb.toString();
        }
        return null;
    }


    public static final String BASE_URL = "http://cesi.cleverapps.io";
    public static final String SIGNUP_SERVICE = "/signup";



    public static String signup(String username, String password, String urlPhoto) {
        try {
            URL url = new URL(BASE_URL+SIGNUP_SERVICE);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);

            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            StringBuilder params = new StringBuilder();
            params.append("username=").append(URLEncoder.encode(username, "UTF-8")).append("&")
                    .append("pwd=").append(URLEncoder.encode(password, "UTF-8")).append("&")
                    .append("urlPhoto=").append(URLEncoder.encode(urlPhoto, "UTF-8"));

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(params.toString());
            writer.flush();
            writer.close();
            os.close();

            conn.connect();

            int response = conn.getResponseCode();
            Log.d("NetworkHelper", "The response code is: " + response);

            if (response >= 400) {
                return readIt(conn.getErrorStream());
            }
            return readIt(conn.getInputStream());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
