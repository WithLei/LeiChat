package com.android.renly.leichat.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.renly.leichat.Adapter.ChatAdapter;
import com.android.renly.leichat.Adapter.mLinearLayoutManager;
import com.android.renly.leichat.Bean.User;
import com.android.renly.leichat.Common.BaseActivity;
import com.android.renly.leichat.Common.NetConfig;
import com.android.renly.leichat.DB.MySQLiteOpenHelper;
import com.android.renly.leichat.R;
import com.android.renly.leichat.UIUtils.PagerSlidingTabStrip;
import com.rockerhieu.emojicon.EmojiconEditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends BaseActivity implements Runnable {
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.toolbox_btn_send)
    Button toolboxBtnSend;
    @BindView(R.id.toolbox_btn_face)
    CheckBox toolboxBtnFace;
    @BindView(R.id.toolbox_btn_more)
    CheckBox toolboxBtnMore;
    @BindView(R.id.toolbox_et_message)
    EmojiconEditText toolboxEtMessage;
    @BindView(R.id.messageToolBox)
    RelativeLayout messageToolBox;
    @BindView(R.id.toolbox_pagers_face)
    ViewPager toolboxPagersFace;
    @BindView(R.id.toolbox_tabs)
    PagerSlidingTabStrip toolboxTabs;
    @BindView(R.id.toolbox_layout_face)
    RelativeLayout toolboxLayoutFace;
    @BindView(R.id.iv_title_info)
    ImageView ivTitleInfo;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.rv_chat_item)
    RecyclerView rvChatItem;
    @BindView(R.id.iv_main_headImg)
    CircleImageView ivMainHeadImg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    private Unbinder unbinder;
    private ChatAdapter ChatAdapter;
    private List<com.android.renly.leichat.Bean.Message> msgs;
    private static final boolean isSend = true;
    private static final boolean isRecieve = false;

    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        initView();
        initData();
        initList();
        //启动线程，连接服务器，并用死循环守候，接收服务器发送过来的数据
        new Thread(this).start();
        //发空包通知服务端
        handler.sendEmptyMessageDelayed(EMPTY_MESSAGE,1000);
    }

    private Intent intent;
    private String toUserAvater;
    private String fromUserAvater;
    private String toUserName;
    private String fromUserName;

    private void initView() {
        intent = getIntent();
        toUserName = intent.getExtras().get("toUserName").toString();
        toUserAvater = intent.getExtras().get("toUserAvater").toString();
        tvTitleName.setText(toUserName);
        ivMainHeadImg.setVisibility(View.GONE);

        SharedPreferences sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        fromUserName = sp.getString("userName", "");
        fromUserAvater = sp.getString("headPhoto","");
    }

    private void initData() {
        msgs = new ArrayList<>();
        msgs.add(new com.android.renly.leichat.Bean.Message(fromUserName, fromUserAvater, "测试发送", isSend, 1));
        msgs.add(new com.android.renly.leichat.Bean.Message(toUserName, toUserAvater, "测试回复", isRecieve, 1));

        mySQLiteOpenHelper = MySQLiteOpenHelper.getInstance(this);
        synchronized (mySQLiteOpenHelper){
            db = mySQLiteOpenHelper.getWritableDatabase();
            if (!db.isOpen()) {
                db = mySQLiteOpenHelper.getReadableDatabase();
            }
            db.beginTransaction();
            //开启查询
            Cursor cursor = db.query(MySQLiteOpenHelper.TABLE_Message,null,null,null,null,null,null);

            //判断游标是否为空
            if(cursor.moveToFirst()){
                //游历游标
                do{
                    //发送的消息
                    if(cursor.getString(cursor.getColumnIndex("M_fromUserId")).equals(fromUserName) &&
                            cursor.getString(cursor.getColumnIndex("M_toUserId")).equals(toUserName)){
                        String content = cursor.getString(cursor.getColumnIndex("M_content"));
                        msgs.add(new com.android.renly.leichat.Bean.Message(fromUserName,fromUserAvater,content,isSend,1));
                    }
                    //接受到的消息
                    if(cursor.getString(cursor.getColumnIndex("M_fromUserId")).equals(toUserName) &&
                            cursor.getString(cursor.getColumnIndex("M_toUserId")).equals(fromUserName)){
                        String content = cursor.getString(cursor.getColumnIndex("M_content"));
                        msgs.add(new com.android.renly.leichat.Bean.Message(toUserName,toUserAvater,content,isRecieve,1));
                    }
                }while (cursor.moveToNext());
            }
            cursor.close();
            db.endTransaction();
//            db.close();
//            db = null;
        }
//        mySQLiteOpenHelper.close();
    }

    private static final int WHAT_REQUEST_SUCCESS = 1;
    private static final int WHAT_REQUEST_ERROR = 2;
    private static final int SCORLLTOBOTTOM = 3;
    private static final int GET_MESSAGE = 4;
    private static final int EMPTY_MESSAGE = 5;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case WHAT_REQUEST_SUCCESS:
                    rvChatItem.setAdapter(ChatAdapter);
                    break;
                case WHAT_REQUEST_ERROR:
                    Toast.makeText(ChatActivity.this, "加载数据失败", Toast.LENGTH_LONG).show();
                    break;
                case SCORLLTOBOTTOM:
                    rvChatItem.scrollToPosition(ChatAdapter.getItemCount() - 1);
                    break;
                case GET_MESSAGE:
                    ChatAdapter.addData(new com.android.renly.leichat.Bean.Message(toUserName, toUserAvater, (String) message.obj, isRecieve, 1));
                    insertMsgDB((String) message.obj,toUserName,fromUserName);
                    break;
                case EMPTY_MESSAGE:
                    if (socket != null)
                        ChatActivity.this.sendEmptyMessage();
                    else
                        Toast.makeText(ChatActivity.this,"还未连接服务器",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private SQLiteDatabase db;

    private void insertMsgDB(String content,String fromUserName, String toUserName){
//        mySQLiteOpenHelper = MySQLiteOpenHelper.getInstance(this);
        synchronized (mySQLiteOpenHelper){
            db = mySQLiteOpenHelper.getWritableDatabase();
            if (!db.isOpen()) {
                db = mySQLiteOpenHelper.getReadableDatabase();
            }
            db.beginTransaction();

            db.execSQL(insertSql(content,fromUserName,toUserName));

            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            db = null;
        }
        mySQLiteOpenHelper.close();
    }

    private String insertSql(String content, String fromUserName, String toUserName) {
        return "insert into " + MySQLiteOpenHelper.TABLE_Message + "(M_content, M_fromUserId, M_toUserId, M_Time)" +
                "values('" + content + "','" + fromUserName + "','" + toUserName + "','" + new Date(System.currentTimeMillis()) + "')";
    }

    private void initList() {
        ChatAdapter = new ChatAdapter(msgs, ChatActivity.this,this);
        mLinearLayoutManager layoutManager = new mLinearLayoutManager(ChatActivity.this);
        layoutManager.setScrollEnabled(true);
        rvChatItem.setLayoutManager(layoutManager);
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

    @OnClick({R.id.iv_title_back, R.id.iv_title_info, R.id.rl_title, R.id.toolbox_btn_send, R.id.toolbox_btn_face, R.id.toolbox_btn_more, R.id.toolbox_et_message,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                //返回键
                finish();
                break;
            case R.id.iv_title_info:
                //好友用户信息界面
                startActivity(new Intent(ChatActivity.this, UserInfoActivity.class));
                break;
            case R.id.rl_title:
                toolboxLayoutFace.setVisibility(View.GONE);
                hideInputKeyboard();
                break;
            case R.id.toolbox_btn_send:
                //发送按钮
                if (!toolboxEtMessage.getText().toString().isEmpty())
                    sendMessage();
                scorllToBottom();
                break;
            case R.id.toolbox_btn_face:
                //表情按钮
                hideInputKeyboard();
                toolboxLayoutFace.setVisibility(View.VISIBLE);
                break;
            case R.id.toolbox_btn_more:
                //更多按钮
                toolboxLayoutFace.setVisibility(View.VISIBLE);
                hideInputKeyboard();
                break;
            case R.id.toolbox_et_message:
                //输入框
                toolboxLayoutFace.setVisibility(View.GONE);
                scorllToBottom();
                break;
        }
    }

    /*
   点击发送消息
    */
    private void sendMessage() {
        final String msg = toolboxEtMessage.getText().toString();
        com.android.renly.leichat.Bean.Message sendMsg = null;
        if (msg != null) {
            if (socket.isConnected()) {//如果服务器连接
                if (!socket.isOutputShutdown()) {//如果输出流没有断开
                    sendMsg = new com.android.renly.leichat.Bean.Message(fromUserName, fromUserAvater, msg, isSend, com.android.renly.leichat.Bean.Message.MSG_STATE_SUCCESS);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String jsonMsg = toJsonMessage(msg);
                            out.println(jsonMsg);//点击按钮发送消息
                        }
                    }).start();
                } else {//输出流断开
                    sendMsg = new com.android.renly.leichat.Bean.Message(fromUserName, fromUserAvater, msg, isSend, com.android.renly.leichat.Bean.Message.MSG_STATE_FAIL);
                }
            } else {//服务器连接未成功
                sendMsg = new com.android.renly.leichat.Bean.Message(fromUserName, fromUserAvater, msg, isSend, com.android.renly.leichat.Bean.Message.MSG_STATE_FAIL);
            }
        } else
            Toast.makeText(ChatActivity.this, "请输入文本内容", Toast.LENGTH_SHORT).show();
        ChatAdapter.addData(sendMsg);
        insertMsgDB(msg,fromUserName,toUserName);
        toolboxEtMessage.setText("");
    }

    /**
     * 发送空包通知服务器
     */
    private void sendEmptyMessage() {
            if (socket.isConnected()) {//如果服务器连接
                if (!socket.isOutputShutdown()) {//如果输出流没有断开
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String msg = toEmptyFastJson();
                            out.println(msg);//发送空包
                        }
                    }).start();
                }
            }
    }

    /**
     * 转换Json数据
     */
    private String toJsonMessage(String msg){
        com.android.renly.leichat.Bean.Message jsonMsg = new com.android.renly.leichat.Bean.Message();
        if(!toUserName.equals("群聊(3)"))
            jsonMsg.setForm("single");
        else
            jsonMsg.setForm("multi");
        jsonMsg.setContent(msg);
        jsonMsg.setToUserId(toUserName);
        jsonMsg.setFromUserId(fromUserName);

        return JSON.toJSONString(jsonMsg);
    }

    //对message进行序列化 成为json数据
    private String toEmptyFastJson(){
        com.android.renly.leichat.Bean.Message msg = new com.android.renly.leichat.Bean.Message();
        msg.setForm("test");
        msg.setFromUserId(fromUserName);
        return JSON.toJSONString(msg);
    }

    public void hideInputKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

        scorllToBottom();
    }

    //滑动到recycler最低端
    private void scorllToBottom() {
        handler.sendEmptyMessageDelayed(SCORLLTOBOTTOM, 400);
    }

    /**
     * 连接服务器
     */
    private void connection() {
        try {
            socket = new Socket(NetConfig.Host, NetConfig.PORT);//连接服务器
            in = new BufferedReader(new InputStreamReader(socket
                    .getInputStream()));//接收消息的流对象
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream())), true);//发送消息的流对象
        } catch (IOException ex) {
            ex.printStackTrace();
            ShowDialog("连接服务器失败：" + ex.getMessage());
        }
    }

    /**
     * 如果连接出现异常，弹出AlertDialog！
     */
    public void ShowDialog(String msg) {
        new AlertDialog.Builder(this).setTitle("通知").setMessage(msg)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    /**
     * 读取服务器发来的信息，并通过Handler发给UI线程
     */
    public void run() {
        connection();// 连接到服务器
        try {
            while (true) {//死循环守护，监控服务器发来的消息
                if (!socket.isClosed()) {//如果服务器没有关闭
                    if (socket.isConnected()) {//连接正常
                        if (!socket.isInputShutdown()) {//如果输入流没有断开
                            String getLine;
                            if ((getLine = in.readLine()) != null) {//读取接收的信息
                                getLine += "\n";
                                Message message = new Message();
                                message.obj = getLine;
                                message.what = GET_MESSAGE;
                                handler.sendMessage(message);//通知UI更新
                                scorllToBottom();
                            } else {

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(out != null)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    out.println("exit");//点击按钮发送消息
                }
            }).start();
        unbinder.unbind();
        //清除所有handler未处理的消息
        handler.removeCallbacksAndMessages(null);
    }
}
