package com.android.renly.leichat.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.android.renly.leichat.Common.BaseActivity;
import com.android.renly.leichat.MainActivity;
import com.android.renly.leichat.R;

public class WelcomeActivity extends Activity {

    private static final int JUMP_TO_MAIN = 1;
    private static final int JUMP_TO_LOGIN = 2;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case JUMP_TO_MAIN:
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    break;
                case JUMP_TO_LOGIN:
                    startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                    break;
            }
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去掉窗口标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏顶部的状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        //查看本地是否有用户的登录信息
        SharedPreferences sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String name = sp.getString("username", "");
        if(TextUtils.isEmpty(name))
            handler.sendEmptyMessageDelayed(JUMP_TO_LOGIN,5000);
        else
            handler.sendEmptyMessageDelayed(JUMP_TO_MAIN,5000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
