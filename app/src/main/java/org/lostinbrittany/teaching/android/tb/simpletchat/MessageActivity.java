package org.lostinbrittany.teaching.android.tb.simpletchat;

import android.os.AsyncTask;

import org.lostinbrittany.teaching.android.tb.simpletchat.adapter.MessageAdapter;
import org.lostinbrittany.teaching.android.tb.simpletchat.model.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.lostinbrittany.teaching.android.tb.simpletchat.tools.NetworkHelper;

import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private MessageAdapter adapter;

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

            for (Message msg: messages.toArray(new Message[0])) {
                Log.d("message", msg.getUsername());
            }

            adapter = new MessageAdapter(getApplicationContext(),messages.toArray(new Message[0]));

            ListView listView = (ListView) findViewById(R.id.message_list);
            listView.setAdapter(adapter);
        }
    }

}
