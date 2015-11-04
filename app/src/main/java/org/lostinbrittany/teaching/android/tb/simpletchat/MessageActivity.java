package org.lostinbrittany.teaching.android.tb.simpletchat;

import android.os.AsyncTask;
import org.lostinbrittany.teaching.android.tb.simpletchat.model.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import org.lostinbrittany.teaching.android.tb.simpletchat.tools.NetworkHelper;

import java.util.List;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            String token = extras.getString("token");
            if (null != token) {

                MessageListAsyncTask asyncTask = new MessageListAsyncTask();

                asyncTask.execute(token);
            }

        }

    }

    private class MessageListAsyncTask extends AsyncTask<String, Void, List<Message>> {

        @Override
        protected List<Message> doInBackground(String... params) {

            boolean networkAvailable = NetworkHelper.isInternetAvailable(getApplicationContext());
            Log.d("Available network?", Boolean.toString(networkAvailable));

            if (!networkAvailable) {
                return null;
            }
            String token = params[0];
            return NetworkHelper.messageList(token);
        }

        @Override
        protected void onPostExecute(List<Message> messages) {

            TextView messageList = (TextView) findViewById(R.id.message_list);
            StringBuilder sb = new StringBuilder();
            for (Message msg: messages) {
                sb.append("---------------------------\n");
                sb.append(new java.util.Date(msg.getDate())).append("\n")
                    .append(msg.getUsername()).append("\n")
                    .append(msg.getMessage()).append("\n");
            }
            messageList.setText(sb.toString());
        }
    }

}
