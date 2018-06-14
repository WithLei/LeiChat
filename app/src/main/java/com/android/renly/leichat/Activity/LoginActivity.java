package com.android.renly.leichat.Activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.leichat.Common.BaseActivity;
import com.android.renly.leichat.MainActivity;
import com.android.renly.leichat.R;
import com.android.renly.leichat.UIUtils.DrawableTextView;
import com.android.renly.leichat.Utils.KeyboardWatcher;

public class LoginActivity extends BaseActivity implements View.OnClickListener, KeyboardWatcher.SoftKeyboardStateListener {
    private DrawableTextView logo;
    private EditText et_mobile;
    private EditText et_password;
    private ImageView iv_clean_phone;
    private ImageView clean_password;
    private ImageView iv_show_pwd;
    private Button btn_login;
    private TextView forget_password;
    private LinearLayout root;

    private int screenHeight = 0;//屏幕高度
    private float scale = 0.8f; //logo缩放比例
    private View body;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    private KeyboardWatcher keyboardWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        keyboardWatcher = new KeyboardWatcher(this.findViewById(Window.ID_ANDROID_CONTENT));
        keyboardWatcher.addSoftKeyboardStateListener(this);
        isLogin();
    }

    private void isLogin() {
        //查看本地是否有用户的登录信息
        SharedPreferences sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String name = sp.getString("userName", "");
        if(!TextUtils.isEmpty(name)){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private boolean flag = false;


    private void initView() {
        logo = findViewById(R.id.logo);
        et_mobile = findViewById(R.id.et_mobile);
        et_password = findViewById(R.id.et_password);
        iv_clean_phone = findViewById(R.id.iv_clean_phone);
        clean_password = findViewById(R.id.clean_password);
        iv_show_pwd = findViewById(R.id.iv_show_pwd);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        forget_password = findViewById(R.id.forget_password);
        body = findViewById(R.id.body);
        root = findViewById(R.id.root);
        screenHeight = getResources().getDisplayMetrics().heightPixels; //获取屏幕高度
        findViewById(R.id.close).setOnClickListener(this);
    }

    private void initListener() {
        iv_clean_phone.setOnClickListener(this);
        clean_password.setOnClickListener(this);
        iv_show_pwd.setOnClickListener(this);
        root.setOnClickListener(this);
        et_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && iv_clean_phone.getVisibility() == View.GONE) {
                    iv_clean_phone.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    iv_clean_phone.setVisibility(View.GONE);
                }
            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && clean_password.getVisibility() == View.GONE) {
                    clean_password.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    clean_password.setVisibility(View.GONE);
                }
                if (s.toString().isEmpty())
                    return;
                if (!s.toString().matches("[A-Za-z0-9]+")) {
                    String temp = s.toString();
                    Toast.makeText(LoginActivity.this, R.string.please_input_limit_pwd, Toast.LENGTH_SHORT).show();
                    s.delete(temp.length() - 1, temp.length());
                    et_password.setSelection(s.length());
                }
            }
        });
    }

    /**
     * 缩小
     *
     * @param view
     */
    public void zoomIn(final View view, float dist) {
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();
        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, scale);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, scale);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", 0.0f, -dist);

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX).with(mAnimatorScaleY);

        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();

    }

    /**
     * f放大
     *
     * @param view
     */
    public void zoomOut(final View view) {
        if (view.getTranslationY() == 0) {
            return;
        }
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();

        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", scale, 1.0f);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", scale, 1.0f);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), 0);

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX).with(mAnimatorScaleY);
        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();

    }

    @Override
    public void onSoftKeyboardOpened(int keyboardSize) {
        int[] location = new int[2];
        body.getLocationOnScreen(location); //获取body在屏幕中的坐标,控件左上角
        int x = location[0];
        int y = location[1];
        int bottom = screenHeight - (y + body.getHeight());
        if (keyboardSize > bottom) {
            ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(body, "translationY", 0.0f, -(keyboardSize - bottom));
            mAnimatorTranslateY.setDuration(300);
            mAnimatorTranslateY.setInterpolator(new AccelerateDecelerateInterpolator());
            mAnimatorTranslateY.start();
            zoomIn(logo, keyboardSize - bottom);

        }
    }

    @Override
    public void onSoftKeyboardClosed() {
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(body, "translationY", body.getTranslationY(), 0);
        mAnimatorTranslateY.setDuration(300);
        mAnimatorTranslateY.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorTranslateY.start();
        zoomOut(logo);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        keyboardWatcher.removeSoftKeyboardStateListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.close:
                finish();
                break;
            case R.id.btn_login:
                if(!TextUtils.isEmpty(et_mobile.getText())){
                    // 获取SharedPreferences对象
                    SharedPreferences sharedPre = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
                    // 获取Editor对象
                    SharedPreferences.Editor editor = sharedPre.edit();
                    // 设置参数
                    editor.putString("userName", et_mobile.getText().toString());
                    editor.apply();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(this, "请输入账号密码", Toast.LENGTH_SHORT).show();
                }
                
                break;
            case R.id.iv_clean_phone:
                et_mobile.setText("");
                break;
            case R.id.clean_password:
                et_password.setText("");
                break;
            case R.id.iv_show_pwd:
                if (flag == true) {
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    iv_show_pwd.setImageResource(R.drawable.pass_gone);
                    flag = false;
                } else {
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    iv_show_pwd.setImageResource(R.drawable.pass_visuable);
                    flag = true;
                }
                String pwd = et_password.getText().toString();
                if (!TextUtils.isEmpty(pwd))
                    et_password.setSelection(pwd.length());
                break;
            case R.id.root:
                hideInputKeyboard();
                break;
        }
    }

    public void hideInputKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }
}
