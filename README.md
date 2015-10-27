## Simple Tchat

This project is a teaching support for an introductory course to Android
development.

The tutorial relies on the use of the [Git](http://git-scm.com/) versioning system for source code management. You don't need to know anything about Git to follow the tutorial other than how to install and run a few git commands.


### Step-00: Create project

You begin a new project, `SimpleTchat`.

![New project](./img/001.png)

You choose to begin with an empty activity.

![Add empty activity](./img/002.png)

You call the activity `SignupActivity`.

![Customize activity](./img/003.png)

And you have the new project in the IDE.

![Project in Android Studio](./img/004.png)


### Step-01: Create the activities

Reset the workspace to step-01.

```
git checkout -f step-01
```

In this step you're going to create the three main Activities for the SimpleTchat:
`SignupActivity`, `SigninActivity` and `MessageActivity`:

![Activities](./img/005.png)

For each Actitity you create the UI widgets, you give them unique ID and you
externalize all the strings.

![SignupActivity](./img/004.png)

Don't forget to tweak the manifest to mark `SigninActivity` as the main (launching)
one.

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="org.lostinbrittany.teaching.android.tb.simpletchat" >

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/AppTheme" >
        <activity android:name=".SignupActivity" >
        </activity>
        <activity android:name=".SigninActivity" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity android:name=".MessageActivity" >
        </activity>
    </application>

</manifest>

```


### Step-02: Plug the SignupActivity

Reset the workspace to step-02.

```
git checkout -f step-02
```

You begin by placing a listener at `SigninActivity`'s `Signup` button and inside
it launching an Intent to change the Activity to the `SignupActivity`.

```java
public class SigninActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Button btnSignup = (Button)findViewById(R.id.signin_button_signup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

    }
}
```  

Then you give your application internet permissions:

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="org.lostinbrittany.teaching.android.tb.simpletchat" >

    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />

    <application [...]
```

And add a `NetworkHelper` class that will help you dealing with network.
You give it a `isInternetAvailable` method to verify network state:

```java
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
```

A method to read the HTTP response:

```java
// Reads an InputStream and converts it to a String.
private static String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
    int ch;
    StringBuffer sb = new StringBuffer();
    while ((ch = stream.read()) != -1) {
        sb.append((char) ch);
    }

    Reader reader = null;
    reader = new InputStreamReader(stream, "UTF-8");

    while ((ch = reader.read()) != -1) {
        sb.append((char) ch);
    }
    return  sb.toString();
}
```

And a method `signup` to build a post request to the service URL.

Let's see again the details of the service:

> Base URL : http://cesi.cleverapps.io/
> POST /signup
> Description : Register a new user.
> Parameters
> - `username`	(string)	User login.
> - `pwd`	(string)	Password for this user.
> - `urlPhoto`	(string)	url to a picture for this user.

You're going to do it by creating a HttpURLConnection, and adding the parameters
to it:

```java
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
                .append("urlPhoto=").append(URLEncoder.encode(urlPhoto, "UTF-8");

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


        return readIt(conn.getInputStream());

    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
}
```

Then, in the `SignupActivity`, add a listener to the button that creates an AsyncTask that calls the service.
When it gets the answer, it deals with it and, if correct, it launches an intent to `SigninActivity`.

```java
private class SignupAsyncTask extends AsyncTask<String, Void, String> {

  @Override
  protected String doInBackground(String... params) {

    boolean networkAvailable = NetworkHelper.isInternetAvailable(getApplicationContext());
    Log.d("Available network?", Boolean.toString(networkAvailable));

    if (!networkAvailable) {
      return null;
    }
    String username = params[0];
    String password = params[1];
    String urlPhoto = params[2];

    return NetworkHelper.signup(username,password,urlPhoto);
  }

  @Override
  protected void onPostExecute(String result) {
    if (null != result) {
      Log.d("AsyncTask result", result);
      Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
    } else {
      Log.d("AsyncTask", "Finished without error");
      Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
      intent.putExtra("message", "User signed up");
      startActivity(intent);
    }
  }
}
```

## Step-04 Do the sign-in

Reset the workspace to step-03.

```
git checkout -f step-03
```

Add a `signin` method `NetworkHelper`. Remember:

> Base URL : http://cesi.cleverapps.io/
> POST /signin
> Description : Login to the app.
> Parameters
> - `username`	(string)	User login.
> - `pwd`	(string)	Password for this user.
> Output
> - token (in JSON format)

You'll need to parse the answer to recover the token.


```java
public static String signin(String username, String password, String urlPhoto) {
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
            return readIt(conn.getErrorStream());
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
```

Now in the `SigninActivity` you must create an AsyncTask et call this method, and
deal with the result...

```java
private class SigninAsyncTask extends AsyncTask<String, Void, String> {

     @Override
     protected String doInBackground(String... params) {

         boolean networkAvailable = NetworkHelper.isInternetAvailable(getApplicationContext());
         Log.d("Available network?", Boolean.toString(networkAvailable));

         if (!networkAvailable) {
             return null;
         }
         String username = params[0];
         String password = params[1];

         return NetworkHelper.signin(username, password);
     }

     @Override
     protected void onPostExecute(String result) {
         if (null == result) {
             Log.d("AsyncTask result", "null");
             return;
         }
         if (result.startsWith("Error:"))  {
             Log.d("AsyncTask result", result);
             Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
         } else {
             Log.d("AsyncTask", "Finished without error");
             Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
             intent.putExtra("message", "User signed in");
             intent.putExtra("token", result);
             startActivity(intent);
         }
     }
 }
```
