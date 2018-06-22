package com.android.renly.leichat.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
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

import com.android.renly.leichat.Adapter.ChatAdapter;
import com.android.renly.leichat.Adapter.mLinearLayoutManager;
import com.android.renly.leichat.Bean.Message;
import com.android.renly.leichat.Common.AIRobot;
import com.android.renly.leichat.Common.BaseActivity;
import com.android.renly.leichat.DB.MySQLiteOpenHelper;
import com.android.renly.leichat.R;
import com.android.renly.leichat.UIUtils.PagerSlidingTabStrip;
import com.rockerhieu.emojicon.EmojiconEditText;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class AIChatActivity extends BaseActivity {
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_info)
    ImageView ivTitleInfo;
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
    //下方隐藏菜单
    @BindView(R.id.toolbox_layout_face)
    RelativeLayout toolboxLayoutFace;
    @BindView(R.id.rv_chat_item)
    RecyclerView rvChatItem;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.iv_main_headImg)
    CircleImageView ivMainHeadImg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    private Unbinder unbinder;
    private String toUserAvater;
    private String fromUserName;
    private String fromUserAvater;
    private String toUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
//        rlTitle.bringToFront();
        initView();
        initData();
        initList();
    }

    private void initView() {
        Intent intent = getIntent();
        toUserName = intent.getExtras().get("toUserName").toString();
        toUserAvater = intent.getExtras().get("toUserAvater").toString();
        tvTitleName.setText(toUserName);
        ivMainHeadImg.setVisibility(View.GONE);

        SharedPreferences sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        fromUserName = sp.getString("userName", "");
        fromUserAvater = sp.getString("headPhoto","");
    }

    private List<Message> msgs;
    private static final boolean isSend = true;
    private static final boolean isRecieve = false;

    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private SQLiteDatabase db;

    private void initData() {
        msgs = new ArrayList<>();
        String img = "http://m.qpic.cn/psb?/V13Hh3Xy2wrWJw/ZVU219Y5gp2VhDelSYRNr6hA1l3KxRL*UZqj9Bks0VU!/b/dDEBAAAAAAAA&bo=WAJZAlgCWQIRCT4!&rf=viewer_4";
        msgs.add(new Message(fromUserName, img, "测试发送", isSend, Message.MSG_STATE_SUCCESS));
        msgs.add(new Message(toUserName, toUserAvater, "测试回复", isRecieve, Message.MSG_STATE_SUCCESS));

        mySQLiteOpenHelper = MySQLiteOpenHelper.getInstance(this);
        synchronized (mySQLiteOpenHelper){
            db = mySQLiteOpenHelper.getWritableDatabase();
            if (!db.isOpen())
                db = mySQLiteOpenHelper.getReadableDatabase();

            db.beginTransaction();
            //开启查询
            Cursor cursor = db.query(MySQLiteOpenHelper.TABLE_Message,null,null,null,null,null,null);

            //判断游标是否为空
            if(cursor.moveToFirst()){
                //游历游标
                do{
                    //发送的消息
                    if(cursor.getString(cursor.getColumnIndex("M_fromUserID")).equals(fromUserName) &&
                            cursor.getString(cursor.getColumnIndex("M_toUserID")).equals(toUserName)){
                        String content = cursor.getString(cursor.getColumnIndex("M_content"));
                        msgs.add(new com.android.renly.leichat.Bean.Message(fromUserName,fromUserAvater,content,isSend,1));
                    }
                    //接受到的消息
                    if(cursor.getString(cursor.getColumnIndex("M_fromUserID")).equals(toUserName) &&
                            cursor.getString(cursor.getColumnIndex("M_toUserID")).equals(fromUserName)){
                        String content = cursor.getString(cursor.getColumnIndex("M_content"));
                        msgs.add(new com.android.renly.leichat.Bean.Message(toUserName,toUserAvater,content,isRecieve,1));
                    }
                }while (cursor.moveToNext());
            }
            cursor.close();
            db.endTransaction();
        }
    }

    private void initList() {
        ChatAdapter = new ChatAdapter(msgs, AIChatActivity.this,this);
        mLinearLayoutManager layoutManager = new mLinearLayoutManager(AIChatActivity.this);
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

    private static final int WHAT_REQUEST_SUCCESS = 1;
    private static final int WHAT_REQUEST_ERROR = 2;
    private static final int SCORLLTOBOTTOM = 3;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case WHAT_REQUEST_SUCCESS:
                    rvChatItem.setAdapter(ChatAdapter);
                    break;
                case WHAT_REQUEST_ERROR:
                    Toast.makeText(AIChatActivity.this, "加载数据失败", Toast.LENGTH_LONG).show();
                    break;
                case SCORLLTOBOTTOM:
                    rvChatItem.scrollToPosition(ChatAdapter.getItemCount() - 1);
                    break;
            }
        }
    };

    public ChatAdapter ChatAdapter;

    @OnClick({R.id.iv_title_back, R.id.iv_title_info, R.id.toolbox_btn_send, R.id.toolbox_btn_face, R.id.toolbox_btn_more, R.id.toolbox_et_message, R.id.toolbox_layout_face, R.id.rl_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                //返回键
                finish();
                break;
            case R.id.iv_title_info:
                //好友用户信息界面
                startActivity(new Intent(AIChatActivity.this, UserInfoActivity.class));
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
            case R.id.rl_title:
                toolboxLayoutFace.setVisibility(View.GONE);
                hideInputKeyboard();
                break;
        }
    }

    public void hideInputKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

        scorllToBottom();
    }

    private void insertMsgDB(String content,String fromUserName, String toUserName){
        synchronized (mySQLiteOpenHelper){

            db.beginTransaction();

            db.execSQL(insertSql(content,fromUserName,toUserName));

            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    private String insertSql(String content, String fromUserName, String toUserName) {
        return "insert into " + MySQLiteOpenHelper.TABLE_Message + "(M_content, M_fromUserId, M_toUserId, M_Time)" +
                "values('" + content + "','" + fromUserName + "','" + toUserName + "','" + new Date(System.currentTimeMillis()) + "')";
    }

    /*
    点击发送消息
     */
    private AIRobot aiRobot;

    private void sendMessage() {
        final String msg = toolboxEtMessage.getText().toString();
        final String img = "http://m.qpic.cn/psb?/V13Hh3Xy2wrWJw/ZVU219Y5gp2VhDelSYRNr6hA1l3KxRL*UZqj9Bks0VU!/b/dDEBAAAAAAAA&bo=WAJZAlgCWQIRCT4!&rf=viewer_4";
        if (msg != null) {
            ChatAdapter.addData(new Message(fromUserName, img, msg, isSend, Message.MSG_STATE_SUCCESS));
            insertMsgDB(msg,fromUserName,toUserName);
            aiRobot = new AIRobot(AIChatActivity.this, ChatAdapter);
            aiRobot.setRV(rvChatItem);
            aiRobot.getReply(msg);
        } else
            Toast.makeText(AIChatActivity.this, "请输入文本内容", Toast.LENGTH_SHORT).show();
        toolboxEtMessage.setText("");
    }

    //滑动到recycler最低端
    private void scorllToBottom() {
        handler.sendEmptyMessageDelayed(SCORLLTOBOTTOM, 400);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        //清除所有handler未处理的消息
        handler.removeCallbacksAndMessages(null);
    }
}
