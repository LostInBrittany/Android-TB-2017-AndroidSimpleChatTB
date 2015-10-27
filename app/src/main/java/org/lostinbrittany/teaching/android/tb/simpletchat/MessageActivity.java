package org.lostinbrittany.teaching.android.tb.simpletchat;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.lostinbrittany.teaching.android.tb.simpletchat.model.Message;
import org.lostinbrittany.teaching.android.tb.simpletchat.tools.NetworkHelper;

import java.util.List;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            String message = extras.getString("message");
            if (null != message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
            String token = extras.getString("token");
            if (null != token) {
                Toast.makeText(getApplicationContext(), "Token: "+token, Toast.LENGTH_LONG).show();
            }
        }

    }


    private class SigninAsyncTask extends AsyncTask<String, Void, List<Message>> {

        @Override
        protected List<Message> doInBackground(String... params) {

            boolean networkAvailable = NetworkHelper.isInternetAvailable(getApplicationContext());
            Log.d("Available network?", Boolean.toString(networkAvailable));

            if (!networkAvailable) {
                return null;
            }
            return NetworkHelper.getMessages(params[0]);
        }

        @Override
        protected void onPostExecute( List<Message> messages) {
            if (null == messages) {
                Log.d("AsyncTask result", "null");
                return;
            }
    }
}
