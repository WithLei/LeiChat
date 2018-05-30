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

import com.android.renly.leichat.Common.BaseActivity;
import com.android.renly.leichat.R;
import com.android.renly.leichat.UIUtils.PagerSlidingTabStrip;
import com.rockerhieu.emojicon.EmojiconEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.toolbox_layout_face)
    RelativeLayout toolboxLayoutFace;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
//                startActivity(new Intent(ChatActivity.this,UserInfoAcitivity.class));
                break;
            case R.id.toolbox_btn_send:
                //发送按钮
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
            case R.id.toolbox_layout_face:
                //下方隐藏菜单
                break;
        }
    }
}
