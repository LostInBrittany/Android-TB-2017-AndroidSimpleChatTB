package org.lostinbrittany.teaching.android.tb.simpletchat;

import android.os.AsyncTask;

import org.lostinbrittany.teaching.android.tb.simpletchat.adapter.MessageAdapter;
import org.lostinbrittany.teaching.android.tb.simpletchat.model.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.lostinbrittany.teaching.android.tb.simpletchat.tools.NetworkHelper;

import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private MessageAdapter adapter;
    private String token;

    private void refreshMessages() {
        MessageListAsyncTask asyncTask = new MessageListAsyncTask();

        asyncTask.execute(token);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            token = extras.getString("token");
            if (null != token) {
                refreshMessages();
            }

        }

        Button btnSendMessage = (Button) findViewById(R.id.btnSendMessage);

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editMessage = (EditText) findViewById(R.id.editMessage);
                String message = editMessage.getText().toString();

                SendMessageAsyncTask asyncTask = new SendMessageAsyncTask();
                asyncTask.execute(message);

            }
        });

        Button btnRefresh = (Button) findViewById(R.id.btnRefresh);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            refreshMessages();
            }
        });

    }

    private class SendMessageAsyncTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            boolean networkAvailable = NetworkHelper.isInternetAvailable(getApplicationContext());
            Log.d("Available network?", Boolean.toString(networkAvailable));

            if (!networkAvailable) {
                return null;
            }
            String message = params[0];
            return NetworkHelper.sendMessage(message, token);
        }

        @Override
        protected void onPostExecute(Integer result) {

            if (200 == result) {
                EditText editMessage = (EditText) findViewById(R.id.editMessage);
                editMessage.setText("");

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
