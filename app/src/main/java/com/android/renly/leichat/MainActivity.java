package com.android.renly.leichat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.leichat.Common.BaseActivity;
import com.android.renly.leichat.Fragment.FriendsFragment;
import com.android.renly.leichat.Fragment.MineFragment;
import com.android.renly.leichat.Fragment.MsgFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity {
    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.iv_main_bottom_msg)
    ImageView ivMainBottomMsg;
    @BindView(R.id.tv_main_bottom_msg)
    TextView tvMainBottomMsg;
    @BindView(R.id.ll_main_bottom_msg)
    LinearLayout llMainBottomMsg;
    @BindView(R.id.iv_main_bottom_friends)
    ImageView ivMainBottomFriends;
    @BindView(R.id.tv_main_bottom_friends)
    TextView tvMainBottomFriends;
    @BindView(R.id.ll_main_bottom_friends)
    LinearLayout llMainBottomFriends;
    @BindView(R.id.iv_main_bottom_mine)
    ImageView ivMainBottomMine;
    @BindView(R.id.tv_main_bottom_mine)
    TextView tvMainBottomMine;
    @BindView(R.id.ll_main_bottom_mine)
    LinearLayout llMainBottomMine;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_info)
    ImageView ivTitleInfo;


    private Unbinder unbinder;
    private FragmentTransaction transaction;

    private boolean needQuit = false;

    private static final int WHAT_RESET_BACK = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_RESET_BACK:
                    needQuit = false;
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        initView();
        setSelect(0);
    }

    private void initView() {
        ivTitleBack.setVisibility(View.GONE);
        ivTitleInfo.setImageResource(R.drawable.addpeople);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    private MsgFragment msgFragment;
    private FriendsFragment friendsFragment;
    private MineFragment mineFragment;

    public void setSelect(int select) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        //隐藏所有fragment的显示
        hideFragments();
        switch (select) {
            case 0:
                if (msgFragment == null) {
                    msgFragment = new MsgFragment();
                    transaction.add(R.id.fl_main, msgFragment);
                }
                transaction.show(msgFragment);

                //改变图片颜色和文字颜色
                ivMainBottomMsg.setImageResource(R.drawable.interactivered);
                tvMainBottomMsg.setTextColor(getResources().getColor(R.color.red));
                tvTitleName.setText("消息");
                break;
            case 1:
                if (friendsFragment == null) {
                    friendsFragment = new FriendsFragment();
                    transaction.add(R.id.fl_main, friendsFragment);
                }
                transaction.show(friendsFragment);

                //改变图片颜色和文字颜色
                ivMainBottomFriends.setImageResource(R.drawable.addressred);
                tvMainBottomFriends.setTextColor(getResources().getColor(R.color.red));
                tvTitleName.setText("好友");
                break;
            case 2:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    transaction.add(R.id.fl_main, mineFragment);
                }
                transaction.show(mineFragment);

                //改变图片颜色和文字颜色
                ivMainBottomMine.setImageResource(R.drawable.mine_fill);
                tvMainBottomMine.setTextColor(getResources().getColor(R.color.red));
                tvTitleName.setText("我");
                break;
        }
        //提交事务
        transaction.commit();
    }

    private void hideFragments() {
        ivMainBottomMsg.setImageResource(R.drawable.interactive);
        ivMainBottomFriends.setImageResource(R.drawable.address);
        ivMainBottomMine.setImageResource(R.drawable.mine);
        tvMainBottomMsg.setTextColor(getResources().getColor(R.color.bottom_unselect));
        tvMainBottomFriends.setTextColor(getResources().getColor(R.color.bottom_unselect));
        tvMainBottomMine.setTextColor(getResources().getColor(R.color.bottom_unselect));

        if (msgFragment != null) {
            transaction.hide(msgFragment);
        }

        if (friendsFragment != null) {
            transaction.hide(friendsFragment);
        }

        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !needQuit) {
            Toast.makeText(this, "再点击一次退出当前应用", Toast.LENGTH_SHORT).show();
            needQuit = true;
            //发送延迟消息
            handler.sendEmptyMessageDelayed(WHAT_RESET_BACK, 2000);
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_main_bottom_msg, R.id.ll_main_bottom_friends, R.id.ll_main_bottom_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_main_bottom_msg:
                setSelect(0);
                break;
            case R.id.ll_main_bottom_friends:
                setSelect(1);
                break;
            case R.id.ll_main_bottom_mine:
                setSelect(2);
                break;
        }
    }
}
