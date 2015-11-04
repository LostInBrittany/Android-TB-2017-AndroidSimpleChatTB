package org.lostinbrittany.teaching.android.tb.simpletchat.adapter;

import android.app.Activity;
import android.content.Context;
import org.lostinbrittany.teaching.android.tb.simpletchat.model.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.lostinbrittany.teaching.android.tb.simpletchat.R;

import java.text.ParseException;
import java.util.List;

/**
 * Created by horacio on 27/10/15.
 */
public class MessageAdapter extends BaseAdapter {


    private final Context context;

    public MessageAdapter(Context ctx){
        this.context = ctx;
    }
    List<Message> messages;

    @Override
    public int getCount() {
        if (null != messages)
            return messages.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position,
                        View convertView,
                        ViewGroup parent) {

        // A ViewHolder keeps references to children views to avoid unneccessary calls
        // to findViewById() on each row.
        ViewHolder vh;

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            convertView = inflater.inflate(R.layout.item_message, parent, false);
            vh = new ViewHolder();
            vh.username = (TextView) convertView.findViewById(R.id.msg_user);
            vh.message = (TextView) convertView.findViewById(R.id.msg_message);
            vh.date = (TextView) convertView.findViewById(R.id.msg_date);
            convertView.setTag(vh);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            vh = (ViewHolder) convertView.getTag();
        }
        // Bind the data efficiently with the holder.
        vh.username.setText(messages.get(position).getAuthor());
        vh.message.setText(Html.fromHtml(messages.get(position).getMessage()));

        return convertView;
    }


    private class ViewHolder{
        TextView username;
        TextView message;
        TextView date;
    }



}
