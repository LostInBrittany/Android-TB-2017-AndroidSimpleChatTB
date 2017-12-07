package org.lostinbrittany.teaching.android.tb.simpletchat.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lostinbrittany.teaching.android.tb.simpletchat.model.Message;

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
import java.util.LinkedList;
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


    public static final String BASE_URL = "http://lostinbrittany-simple-chat.cleverapps.io";
    public static final String SIGNUP_SERVICE = "/signup";
    public static final String SIGNIN_SERVICE = "/signin";
    public static final String MESSAGE_SERVICE = "/messages";

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

    public static String signin(String username, String password) {
        try {
            URL url = new URL(BASE_URL+SIGNIN_SERVICE);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);

            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            StringBuilder params = new StringBuilder();
            params.append("username=").append(URLEncoder.encode(username, "UTF-8")).append("&")
                    .append("pwd=").append(URLEncoder.encode(password, "UTF-8"));

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
                return "Error: "+readIt(conn.getErrorStream());
            }
            String responseText = readIt(conn.getInputStream());
            return new JSONObject(responseText).optString("token");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int sendMessage(String message, String token) {
        try {
            URL url = new URL(BASE_URL+MESSAGE_SERVICE);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);

            conn.setRequestMethod("POST");
            conn.setDoInput(true);

            conn.setRequestProperty("token", token);

            StringBuilder params = new StringBuilder();
            params.append("message=").append(URLEncoder.encode(message, "UTF-8"));

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(params.toString());
            writer.flush();
            writer.close();
            os.close();

            conn.connect();

            int response = conn.getResponseCode();
            Log.d("NetworkHelper", "The sendMessage response code is: " + response);

            return response;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;

    }

    public static List<Message> messageList(String token) {
        try {
            URL url = new URL(BASE_URL+MESSAGE_SERVICE);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);

            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.setRequestProperty("token", token);

            conn.connect();

            int response = conn.getResponseCode();
            Log.d("NetworkHelper", "The response code is: " + response);

            if (response >= 400) {
                Log.e("getMessages", "Error: "+readIt(conn.getErrorStream()));
            }
            String responseText = readIt(conn.getInputStream());
            return getMessageFromJSON(responseText);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Message> getMessageFromJSON(String json) {
        List<Message> messages = new LinkedList<>();
        JSONArray array = null;
        try {
            array = new JSONArray(json);
            JSONObject obj;
            Message msg;
            for(int i=0; i < array.length(); i++){
                obj = array.getJSONObject(i);
                msg = new Message(
                        obj.optLong("date"),
                        obj.optString("username"),
                        obj.optString("message") );
                messages.add(msg);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messages;
    }
}
