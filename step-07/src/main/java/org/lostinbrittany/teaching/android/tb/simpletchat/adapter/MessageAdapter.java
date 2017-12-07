package org.lostinbrittany.teaching.android.tb.simpletchat.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.lostinbrittany.teaching.android.tb.simpletchat.R;
import org.lostinbrittany.teaching.android.tb.simpletchat.model.Message;

public class MessageAdapter  extends ArrayAdapter<Message> {

    private final Context context;
    private Message[] messages;

    public MessageAdapter(Context context) {
        super(context, -1);
        this.context = context;
    }

    public MessageAdapter(Context context, Message[] messages) {
        super(context, -1, messages);
        this.context = context;
        this.messages  = messages;
    }

    public void changeMessages(Message[] messages) {
        this.messages = messages;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("message", ""+position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_message, parent, false);

        TextView username = (TextView) rowView.findViewById(R.id.msg_user);
        TextView date = (TextView) rowView.findViewById(R.id.msg_date);
        TextView message = (TextView) rowView.findViewById(R.id.msg_message);

        username.setText(messages[position].getUsername());
        date.setText(new java.util.Date(messages[position].getDate()).toString());
        message.setText(messages[position].getMessage());

        return rowView;
    }
}
