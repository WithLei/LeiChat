package com.android.renly.leichat.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.renly.leichat.Bean.Message;
import com.android.renly.leichat.Listener.OnChatItemClickListener;
import com.android.renly.leichat.R;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends BaseAdapter {
    private final Context context;
    private List<Message>msg = null;
    private OnChatItemClickListener listener;

    private static final boolean isSend = true;
    private static final boolean isRecieve = false;


    public ChatAdapter(Context context, List<Message> msg, OnChatItemClickListener listener) {
        this.context = context;
        if(msg == null)
            msg = new ArrayList<>(0);
        this.msg = msg;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return msg.size();
    }

    @Override
    public Object getItem(int i) {
        return msg.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
     final RecyclerView.ViewHolder viewHolder;
     final Message data = msg.get(i);
        if(view == null){
            viewHolder = new RecyclerView.ViewHolder(view);
            if(data.isSend()){
                //发送消息
                view = View.inflate(context, R.layout.item_send,null);
            }else{
                //收到消息
                view = View.inflate(context,R.layout.item_recieve,null);
            }
        }
        return view;
    }
}
