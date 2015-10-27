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

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button btnSignup = (Button) findViewById(R.id.signup_button);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignupAsyncTask asyncTask = new SignupAsyncTask();
                asyncTask.execute(
                        ((EditText) findViewById(R.id.signup_textName)).getText().toString(),
                        ((EditText) findViewById(R.id.signup_textPassword)).getText().toString(),
                        ((EditText) findViewById(R.id.signup_textURL)).getText().toString()
                );
            }
        });
    }

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
}
