package com.android.renly.leichat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.leichat.Activity.ChatActivity;
import com.android.renly.leichat.Common.ROBOT;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends FragmentActivity {

    @BindView(R.id.tv_test)
    TextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        InitData();
    }

    private void InitData() {
        String url = ROBOT.URL;
        //http://www.tuling123.com/openapi/api
        RequestParams params = new RequestParams();
        params.put("key", ROBOT.API_KEY);
        params.put("info", "今天天气怎么样");
        params.put("loc", "杭州市");
        params.put("userid", "123456");

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode != 200) {
                    Toast.makeText(MainActivity.this, "返回数据失败", Toast.LENGTH_SHORT).show();
                }
                String response = new String(responseBody);
                JSONObject jsonObject = JSON.parseObject(response);
                int code = jsonObject.getInteger("code");
                String text = jsonObject.getString("text");
                Log.e("print", text);
                //返回code == 100000成功，返回code == 40001失败
                if (code == 100000)
                    doSuccess(text);
                else if (code == 40001) {
                    Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(MainActivity.this, "联网失败", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void doSuccess(final String text) {
        tvTest.setText(text);
    }

    @OnClick(R.id.tv_test)
    public void onViewClicked() {
        startActivity(new Intent(MainActivity.this, ChatActivity.class));
    }
}
