package com.dreamwalker.sleeper.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dreamwalker.sleeper.Model.ChatModel;
import com.dreamwalker.sleeper.R;
import com.github.library.bubbleview.BubbleTextView;

import java.util.List;

/**
 * Created by 2E313JCP on 2017-10-14.
 */

public class ChatAdapter extends BaseAdapter{


    private List<ChatModel> chatList;
    private Context context;
    private LayoutInflater layoutInflater;

    public ChatAdapter(List<ChatModel> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (convertView == null){
            if (!chatList.get(position).isSend()){
                view = layoutInflater.inflate(R.layout.chat_item_recv, null);
            }else {
                view = layoutInflater.inflate(R.layout.chat_item_send, null);
                TextView timeText = (TextView)view.findViewById(R.id.timeText);
                timeText.setText(chatList.get(position).chatTime);
            }
        }

        BubbleTextView bubbleTextView = (BubbleTextView)view.findViewById(R.id.bubbleChat);
        bubbleTextView.setText(chatList.get(position).chatMessage);

        return view;
    }
}
