package org.lostinbrittany.teaching.android.tb.simpletchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

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
}
