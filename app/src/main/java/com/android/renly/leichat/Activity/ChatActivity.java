package com.android.renly.leichat.Activity;

import android.content.Context;
import android.content.Intent;
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
import com.android.renly.leichat.Listener.OnChatItemClickListener;
import com.android.renly.leichat.R;
import com.android.renly.leichat.UIUtils.PagerSlidingTabStrip;
import com.rockerhieu.emojicon.EmojiconEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ChatActivity extends BaseActivity {
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        rlTitle.bringToFront();
        initData();
        initList();
        initListener();
    }

    private List<Message> msgs;
    private static final boolean isSend = true;
    private static final boolean isRecieve = false;

    private void initData() {
        msgs = new ArrayList<>();
        String img = "http://m.qpic.cn/psb?/V13Hh3Xy2wrWJw/ZVU219Y5gp2VhDelSYRNr6hA1l3KxRL*UZqj9Bks0VU!/b/dDEBAAAAAAAA&bo=WAJZAlgCWQIRCT4!&rf=viewer_4";
        String imgReply = "http://m.qpic.cn/psb?/V13Hh3Xy2wrWJw/hkAN7ZOEiRPDuiQq.ax0IjNFCFaV70x6mr48jjYUhR8!/b/dFkAAAAAAAAA&bo=RAJEAkQCRAIRCT4!&rf=viewer_4";
        msgs.add(new Message("renly", img, "今日夜色真美", isSend));
        msgs.add(new Message("rua", imgReply, "yep", isRecieve));
    }

    private void initList() {
        chatAdapter = new ChatAdapter(msgs, ChatActivity.this);
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

    private static final int WHAT_REQUEST_SUCCESS = 1;
    private static final int WHAT_REQUEST_ERROR = 2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case WHAT_REQUEST_SUCCESS:
                    rvChatItem.setAdapter(chatAdapter);
                    break;
                case WHAT_REQUEST_ERROR:
                    Toast.makeText(ChatActivity.this, "加载数据失败111", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    public ChatAdapter chatAdapter;

    private void initListener() {
        chatAdapter.setOnChatItemClickListener(new OnChatItemClickListener() {
            @Override
            public void onPhotoClick(int position) {
                Toast.makeText(ChatActivity.this, "onPhotoClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextClick(int position) {
                Toast.makeText(ChatActivity.this, "onTextClick", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.iv_title_back, R.id.iv_title_info, R.id.toolbox_btn_send, R.id.toolbox_btn_face, R.id.toolbox_btn_more, R.id.toolbox_et_message, R.id.toolbox_layout_face, R.id.rl_title, R.id.rv_chat_item})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                //返回键
                finish();
                break;
            case R.id.iv_title_info:
                //好友用户信息界面
                startActivity(new Intent(ChatActivity.this, UserInfoAcitivity.class));
                break;
            case R.id.toolbox_btn_send:
                //发送按钮
                if (!toolboxEtMessage.getText().toString().isEmpty())
                    sendMessage();
                break;
            case R.id.toolbox_btn_face:
                //表情按钮
                hideInputKeyboard();
                toolboxLayoutFace.setVisibility(View.VISIBLE);
                break;
            case R.id.toolbox_btn_more:
                //更多按钮
                hideInputKeyboard();
                toolboxLayoutFace.setVisibility(View.VISIBLE);
                break;
            case R.id.toolbox_et_message:
                //输入框
                toolboxLayoutFace.setVisibility(View.GONE);
                break;
            case R.id.rl_title:
                toolboxLayoutFace.setVisibility(View.GONE);
                hideInputKeyboard();
                break;
            case R.id.rv_chat_item:
                //recyclerView
                toolboxLayoutFace.setVisibility(View.GONE);
                hideInputKeyboard();
                break;
        }
    }

    private void hideInputKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    /*
    点击发送消息
     */
    private AIRobot aiRobot;
    private void sendMessage() {
        final String msg = toolboxEtMessage.getText().toString();
        final String img = "http://m.qpic.cn/psb?/V13Hh3Xy2wrWJw/ZVU219Y5gp2VhDelSYRNr6hA1l3KxRL*UZqj9Bks0VU!/b/dDEBAAAAAAAA&bo=WAJZAlgCWQIRCT4!&rf=viewer_4";
        if (msg != null){
            chatAdapter.addData(new Message("renly", img, msg, isSend));
            aiRobot = new AIRobot(ChatActivity.this,chatAdapter);
            aiRobot.getReply(msg);
        }
        else
            Toast.makeText(ChatActivity.this, "请输入文本内容", Toast.LENGTH_SHORT).show();
        toolboxEtMessage.setText("");
        rvChatItem.scrollToPosition(chatAdapter.getItemCount()-1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
