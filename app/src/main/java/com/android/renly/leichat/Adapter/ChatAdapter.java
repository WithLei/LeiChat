package com.android.renly.leichat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.renly.leichat.Activity.AIChatActivity;
import com.android.renly.leichat.Activity.ChatActivity;
import com.android.renly.leichat.Activity.UserInfoActivity;
import com.android.renly.leichat.Bean.Message;
import com.android.renly.leichat.Listener.OnChatItemClickListener;
import com.android.renly.leichat.R;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.squareup.picasso.Picasso;

import java.util.List;


public  class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context context;
    private List<Message> msgList = null; //消息
    private OnChatItemClickListener listener;
    private ChatActivity chatActivity;
    private AIChatActivity aiChatActivity;
    private static enum ITEM_TYPE{
        ITEM_TYPE_RECIEVE,ITEM_TYPE_SEND;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView headPhoto;
        EmojiconTextView chat_item_content_text;
        ImageView failImg;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    收起软键盘
                    if(chatActivity != null)
                        chatActivity.hideInputKeyboard();
                    else
                        aiChatActivity.hideInputKeyboard();
                }
            });
            headPhoto = itemView.findViewById(R.id.chat_item_avatar);
            chat_item_content_text = itemView.findViewById(R.id.chat_item_content_text);
            headPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, UserInfoActivity.class);
                    context.startActivity(intent);
                }
            });
            failImg = itemView.findViewById(R.id.chat_item_fail);
        }
    }

    //AI构造器
    public ChatAdapter(List<Message>msg, Context context,AIChatActivity aiChatActivity){
        this.context = context;
        this.msgList = msg;
        this.aiChatActivity = aiChatActivity;
    }

    //普通聊天构造器
    public ChatAdapter(List<Message>msg, Context context, ChatActivity chatActivity){
        this.context = context;
        this.msgList = msg;
        this.chatActivity = chatActivity;
    }

    //加载item 的布局  创建ViewHolder实例
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        if(viewType == ITEM_TYPE.ITEM_TYPE_SEND.ordinal() )
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_send,parent,false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recieve,parent,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return msgList.get(position).isSend() ? ITEM_TYPE.ITEM_TYPE_SEND.ordinal() : ITEM_TYPE.ITEM_TYPE_RECIEVE.ordinal();
    }

    //对RecyclerView子项数据进行赋值
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Message message = msgList.get(position);
        //设置数据
        holder.chat_item_content_text.setText(message.getContent());
        Picasso.with(context).load(message.getUserAvater()).into(holder.headPhoto);
        if(message.getState() == Message.MSG_STATE_FAIL)
            holder.failImg.setVisibility(View.VISIBLE);
        else
            holder.failImg.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    public void setOnChatItemClickListener(OnChatItemClickListener listener){
        this.listener = listener;
    }

    public void addData(Message msg){
        msgList.add(getItemCount(),msg);
        refresh();
    }

    public void refresh(){
        notifyItemInserted(getItemCount());
    }
}
