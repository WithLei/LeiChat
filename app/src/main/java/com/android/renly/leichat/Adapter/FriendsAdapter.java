package com.android.renly.leichat.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.renly.leichat.Bean.User;
import com.android.renly.leichat.R;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter {
    private List<User>friends;
    private User user;
    private Context context;

    public FriendsAdapter(User user,Context context) {
        this.context = context;
        this.user = user;
        this.friends = this.user.getFriends();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_item_friend ;
        TextView tv_item_friend ;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_item_friend = itemView.findViewById(R.id.iv_item_friend);
            tv_item_friend = itemView.findViewById(R.id.tv_item_friend);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friends,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User user = friends.get(position);
        //设置数据

    }

    @Override
    public int getItemCount() {
        return friends.size();
    }
}
