package com.android.renly.leichat.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.leichat.Activity.LoginActivity;
import com.android.renly.leichat.Activity.UserInfoActivity;
import com.android.renly.leichat.Common.BaseFragment;
import com.android.renly.leichat.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class MineFragment extends BaseFragment {
    @BindView(R.id.iv_mine_icon)
    CircleImageView ivMineIcon;
    @BindView(R.id.rl_mine_icon)
    RelativeLayout rlMineIcon;
    @BindView(R.id.tv_mine_name)
    TextView tvMineName;
    @BindView(R.id.rl_mine)
    RelativeLayout rlMine;
    Unbinder unbinder;
    @BindView(R.id.btn_logout)
    Button btnLogout;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_mine;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        initData();
        return rootView;
    }

    private void initData() {
        SharedPreferences sp = getContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String fromUserName = sp.getString("userName", "");
        tvMineName.setText(fromUserName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_mine_icon, R.id.btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_mine_icon:
                startActivity(new Intent(getContext(), UserInfoActivity.class));
                break;
            case R.id.btn_logout:
                startActivity(new Intent(getContext(), LoginActivity.class));
                SharedPreferences sharedPre = getContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPre.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(getContext(), "退出登陆", Toast.LENGTH_SHORT).show();
                getActivity().finish();
                break;
        }
    }
}
