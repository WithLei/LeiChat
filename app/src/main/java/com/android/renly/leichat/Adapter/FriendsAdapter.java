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
import com.squareup.picasso.Picasso;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {
    private List<User>friends;
    private User user;
    private Context context;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onClick( int position);
        void onLongClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this. mOnItemClickListener = onItemClickListener;
    }

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
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        User user = friends.get(position);
        //设置数据
        holder.tv_item_friend.setText(user.getName());;
        Picasso.with(context).load(user.getHeadPhoto()).into(holder.iv_item_friend);

        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

}
