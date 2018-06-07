package com.android.renly.leichat.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.leichat.Adapter.ChatAdapter;
import com.android.renly.leichat.Bean.Message;
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initList();
        initListener();
        unbinder = ButterKnife.bind(this);
    }

    private List<Message>msgs;

    private void initData() {
        msgs = new ArrayList<>();
        msgs.add(new Message("renly",""));
    }

    private void initList() {
        chatAdapter = new ChatAdapter();
    }

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

    @OnClick({R.id.iv_title_back, R.id.iv_title_info, R.id.toolbox_btn_send, R.id.toolbox_btn_face, R.id.toolbox_btn_more, R.id.toolbox_et_message, R.id.toolbox_layout_face})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                //返回键
                finish();
                break;
            case R.id.iv_title_info:
                //好友用户信息界面
                startActivity(new Intent(ChatActivity.this,UserInfoAcitivity.class));
                break;
            case R.id.toolbox_btn_send:
                //发送按钮
                if (!toolboxEtMessage.getText().toString().isEmpty())
                    sendMessage();
                break;
            case R.id.toolbox_btn_face:
                //表情按钮
                break;
            case R.id.toolbox_btn_more:
                //更多按钮
                break;
            case R.id.toolbox_et_message:
                //输入框
                break;
        }
    }

    /*
    点击发送消息
     */
    private void sendMessage() {
        String msg = toolboxEtMessage.getText().toString();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
