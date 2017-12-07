package org.lostinbrittany.teaching.android.tb.simpletchat;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.lostinbrittany.teaching.android.tb.simpletchat.tools.NetworkHelper;

public class SigninActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            String message = extras.getString("message");
            if (null != message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        }

        Button btnSignup = (Button)findViewById(R.id.signin_button_signup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

        Button btnSignin = (Button)findViewById(R.id.signin_button_signin);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SigninAsyncTask asyncTask = new SigninAsyncTask();
                asyncTask.execute(
                        ((EditText) findViewById(R.id.signin_textName)).getText().toString(),
                        ((EditText) findViewById(R.id.signin_textPassword)).getText().toString()
                );
            }
        });

    }

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
                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                intent.putExtra("message", "User signed in");
                intent.putExtra("token", result);
                startActivity(intent);
            }
        }
    }
}
