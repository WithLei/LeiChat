package com.android.renly.leichat.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.renly.leichat.Activity.AIChatActivity;
import com.android.renly.leichat.Activity.ChatActivity;
import com.android.renly.leichat.Adapter.FriendsAdapter;
import com.android.renly.leichat.Adapter.mLinearLayoutManager;
import com.android.renly.leichat.Bean.User;
import com.android.renly.leichat.Common.BaseFragment;
import com.android.renly.leichat.DB.MySQLiteOpenHelper;
import com.android.renly.leichat.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FriendsFragment extends BaseFragment {
    @BindView(R.id.rv_friends)
    RecyclerView rvFriends;
    Unbinder unbinder;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_friends;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        initData();
        initList();
        initListener();
        return rootView;
    }

    private void initListener() {
        adapter.setOnItemClickListener(new FriendsAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = null;
                if(position == 0) {
                    intent = new Intent(getActivity(), AIChatActivity.class);
                }else{
                    intent = new Intent(getActivity(), ChatActivity.class);
                }
                intent.putExtra("toUserName",friends.get(position).getName());
                intent.putExtra("toUserAvater",friends.get(position).getHeadPhoto());
                startActivity(intent);
//                Toast.makeText(getContext(), "Click:姓名：" + friends.get(position).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(int position) {
                Intent intent = null;
                if(position == 0) {
                    intent = new Intent(getActivity(), AIChatActivity.class);
                }else{
                    intent = new Intent(getActivity(), ChatActivity.class);
                }
                intent.putExtra("toUserName",friends.get(position).getName());
                intent.putExtra("toUserAvater",friends.get(position).getHeadPhoto());
                startActivity(intent);
//                Toast.makeText(getContext(), "LongClick:姓名：" + friends.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private User user;
    private List<User>friends;
    private FriendsAdapter adapter;

    private void initList() {
        rvFriends.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        adapter = new FriendsAdapter(user,getContext());
        mLinearLayoutManager layoutManager = new mLinearLayoutManager(getContext());
        layoutManager.setScrollEnabled(true);
        rvFriends.setLayoutManager(layoutManager);
        new Thread() {
            @Override
            public void run() {
                try {
                    //在这里模拟读取json数据
                    handler.sendEmptyMessage(WHAT_REQUEST_SUCCESS);
                } catch (Exception e) {
                    handler.sendEmptyMessage(WHAT_REQUEST_ERROR);
                    Log.e("TAG", "加载数据失败1");
                }
            }
        }.start();
    }

    private String headPhoto;
    private void initData() {
        SharedPreferences sp = getContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        int userID = sp.getInt("id", 0);
        headPhoto = sp.getString("headPhoto","");

        friends = new ArrayList<>();
        String img = "http://m.qpic.cn/psb?/V13Hh3Xy2wrWJw/F30x9B4k6qFpl6acDl66bAQ1Lg2BQrvWWP7.SQ8s8uI!/b/dAgBAAAAAAAA&bo=UAFYAVABWAEDCSw!&rf=viewer_4";
        String img1 = "http://m.qpic.cn/psb?/V13Hh3Xy2wrWJw/6vgNbIdlwQn4SRm.gvtL2l0Q*4Xz0mY47Lr62GN2Vt4!/b/dDMBAAAAAAAA&bo=4AHgAQAAAAADByI!&rf=viewer_4";

        friends.add(new User("AI机器人",img, 10086));
        friends.add(new User("群聊(3)",img1, 10087));
        queryDB(userID);

        user = new User();
        user.setFriends(friends);
    }

    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private SQLiteDatabase db;
    private void queryDB(int userID) {
        mySQLiteOpenHelper = MySQLiteOpenHelper.getInstance(getContext());
        db = mySQLiteOpenHelper.getWritableDatabase();
        if(!db.isOpen())
            db = mySQLiteOpenHelper.getReadableDatabase();
        db.beginTransaction();

        synchronized (mySQLiteOpenHelper){
            //开启查询
            Cursor cursor_friends = db.query(MySQLiteOpenHelper.TABLE_FRIENDS, null, null, null, null, null, null);
            Cursor cursor_user = db.query(MySQLiteOpenHelper.TABLE_User, null, null, null, null, null, null);

            //判断游标是否为空
            if(cursor_friends.moveToFirst()){
                //游历游标
                do{
                    if(cursor_friends.getInt(cursor_friends.getColumnIndex("U_id")) == userID){
                        friends.add(idToUser(cursor_friends.getInt(cursor_friends.getColumnIndex("F_id")),cursor_user));
                    }
                }while(cursor_friends.moveToNext());
            }
        }

    }

    private User idToUser(int frindID, Cursor cursor) {
        if(cursor.moveToFirst()){
            do {
                if(cursor.getInt(cursor.getColumnIndex("U_id")) == frindID)
                    return new User(cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("headPhoto")),
                            cursor.getInt(cursor.getColumnIndex("U_id")));
            }while (cursor.moveToNext());
        }
        return null;
    }

    private static final int WHAT_REQUEST_SUCCESS = 1;
    private static final int WHAT_REQUEST_ERROR = 2;
    private static final int SCORLLTOBOTTOM = 3;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case WHAT_REQUEST_SUCCESS:
                    rvFriends.setAdapter(adapter);
                    break;
                case WHAT_REQUEST_ERROR:
                    Toast.makeText(getContext(), "加载数据失败", Toast.LENGTH_LONG).show();
                    break;
                case SCORLLTOBOTTOM :
                    rvFriends.scrollToPosition(adapter.getItemCount()-1);
                    break;
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
