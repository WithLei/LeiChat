package com.android.renly.leichat.Common;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.leichat.Adapter.ChatAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class AIRobot {
    private Context context;
    private ChatAdapter ChatAdapter;
    private static final String url = ROBOT.URL;
    private RequestParams params;
    private AsyncHttpClient client;
    public String reply = null;

    private RecyclerView rv;

    final String imgReply = "http://m.qpic.cn/psb?/V13Hh3Xy2wrWJw/F30x9B4k6qFpl6acDl66bAQ1Lg2BQrvWWP7.SQ8s8uI!/b/dAgBAAAAAAAA&bo=UAFYAVABWAEDCSw!&rf=viewer_4";
    private static final boolean isSend = true;
    private static final boolean isRecieve = false;
    //回传
    private static final int GET_REPLY = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_REPLY:
                    reply = msg.getData().getString("content");
                    ChatAdapter.addData(new com.android.renly.leichat.Bean.Message("AIRobot",imgReply,reply,isRecieve));
                    rv.scrollToPosition(ChatAdapter.getItemCount()-1);
                    Log.e("TAG",reply);
                    break;
            }
        }
    };

    public AIRobot(Context context) {
        this.context = context;
    }

    public AIRobot(Context context,ChatAdapter ChatAdapter){
        this.context = context;
        this.ChatAdapter = ChatAdapter;
    }

    public void getReply(String content) {
        params = new RequestParams();
        params.put("key", ROBOT.API_KEY);
        params.put("info", content);
        params.put("loc", "杭州市");
        params.put("userid", "123456");

        client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode != 200) {
                    Toast.makeText(context, "返回数据失败", Toast.LENGTH_SHORT).show();
                }
                String response = new String(responseBody);
                JSONObject jsonObject = JSON.parseObject(response);
                int code = jsonObject.getInteger("code");
                reply = jsonObject.getString("text");
                Message msg = Message.obtain();
                msg.what = GET_REPLY;
                Bundle bundle = new Bundle();
                bundle.putString("content",reply);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
//        return reply;
    }

    public void setRV(RecyclerView rv){
        this.rv = rv;
    }
}
