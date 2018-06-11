package com.android.renly.leichat.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.renly.leichat.Activity.AIChatActivity;
import com.android.renly.leichat.Activity.ChatActivity;
import com.android.renly.leichat.Adapter.FriendsAdapter;
import com.android.renly.leichat.Adapter.mLinearLayoutManager;
import com.android.renly.leichat.Bean.User;
import com.android.renly.leichat.Common.BaseFragment;
import com.android.renly.leichat.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FriendsFragment extends BaseFragment {
    @BindView(R.id.rv_friends)
    RecyclerView rvFriends;
    Unbinder unbinder;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_friends;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        initData();
        initList();
        initListener();
        return rootView;
    }

    private void initListener() {
        adapter.setOnItemClickListener(new FriendsAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = null;
                if(position == 0) {
                    intent = new Intent(getActivity(), AIChatActivity.class);
                }else{
                    intent = new Intent(getActivity(), ChatActivity.class);
                }
                intent.putExtra("name",friends.get(position).getName());
                intent.putExtra("img",friends.get(position).getHeadPhoto());
                startActivity(intent);
//                Toast.makeText(getContext(), "Click:姓名：" + friends.get(position).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(int position) {
                Intent intent = null;
                if(position == 0) {
                    intent = new Intent(getActivity(), AIChatActivity.class);
                }else{
                    intent = new Intent(getActivity(), ChatActivity.class);
                }
                intent.putExtra("name",friends.get(position).getName());
                intent.putExtra("img",friends.get(position).getHeadPhoto());
                startActivity(intent);
//                Toast.makeText(getContext(), "LongClick:姓名：" + friends.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private User user;
    private List<User>friends;
    private FriendsAdapter adapter;

    private void initList() {
        rvFriends.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        adapter = new FriendsAdapter(user,getContext());
        mLinearLayoutManager layoutManager = new mLinearLayoutManager(getContext());
        layoutManager.setScrollEnabled(true);
        rvFriends.setLayoutManager(layoutManager);
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

    private void initData() {
        friends = new ArrayList<>();
        String img = "http://m.qpic.cn/psb?/V13Hh3Xy2wrWJw/F30x9B4k6qFpl6acDl66bAQ1Lg2BQrvWWP7.SQ8s8uI!/b/dAgBAAAAAAAA&bo=UAFYAVABWAEDCSw!&rf=viewer_4";
        String img1 = "http://m.qpic.cn/psb?/V13Hh3Xy2wrWJw/ZVU219Y5gp2VhDelSYRNr6hA1l3KxRL*UZqj9Bks0VU!/b/dDEBAAAAAAAA&bo=WAJZAlgCWQIRCT4!&rf=viewer_4";
        String img2 = "http://m.qpic.cn/psb?/V13Hh3Xy2wrWJw/hkAN7ZOEiRPDuiQq.ax0IjNFCFaV70x6mr48jjYUhR8!/b/dFkAAAAAAAAA&bo=RAJEAkQCRAIRCT4!&rf=viewer_4";
        String img3 = "http://m.qpic.cn/psb?/V13Hh3Xy2wrWJw/LbTBuD2at8fsZ0q3g7*Ek9ROXDTzCAvecSYwj1nfBYU!/b/dDABAAAAAAAA&bo=LAEsASwBLAERCT4!&rf=viewer_4";
        String img4 = "http://m.qpic.cn/psb?/V13Hh3Xy2wrWJw/Ejo*xAMmPTTStXnvRK.U5xKTxc5uK7vDWGoUUzyN0rs!/b/dC4BAAAAAAAA&bo=gAKAAoACgAIRGS4!&rf=viewer_4";
        String img5 = "http://m.qpic.cn/psb?/V13Hh3Xy2wrWJw/3NHDgZN9AAxy3RuyvCQGdkjsSKL6n5707Wl5hqY1S5U!/b/dDIBAAAAAAAA&bo=JgImAiYCJgIRCT4!&rf=viewer_4";

        friends.add(new User("AI机器人",img));
        for(int i = 0;i < 5;i++){
            friends.add(new User("喜羊羊",img1));
            friends.add(new User("美羊羊",img2));
            friends.add(new User("懒羊羊",img3));
            friends.add(new User("沸羊羊",img4));
            friends.add(new User("慢羊羊",img5));
        }

        user = new User();
        user.setFriends(friends);
    }

    private static final int WHAT_REQUEST_SUCCESS = 1;
    private static final int WHAT_REQUEST_ERROR = 2;
    private static final int SCORLLTOBOTTOM = 3;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case WHAT_REQUEST_SUCCESS:
                    rvFriends.setAdapter(adapter);
                    break;
                case WHAT_REQUEST_ERROR:
                    Toast.makeText(getContext(), "加载数据失败", Toast.LENGTH_LONG).show();
                    break;
                case SCORLLTOBOTTOM :
                    rvFriends.scrollToPosition(adapter.getItemCount()-1);
                    break;
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
