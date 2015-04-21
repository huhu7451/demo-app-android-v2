package io.rong.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import io.rong.app.DemoContext;
import io.rong.app.R;
import io.rong.app.adapter.DeNewFriendListAdapter;
import io.rong.app.message.DeAgreedFriendRequestMessage;
import io.rong.app.model.ApiResult;
import io.rong.app.model.Friends;
import io.rong.app.model.Status;
import io.rong.app.ui.LoadingDialog;
import io.rong.app.ui.WinToast;
import io.rong.app.utils.DeConstants;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import me.add1.exception.BaseException;
import me.add1.network.AbstractHttpRequest;
import me.add1.network.ApiCallback;

/**
 * Created by Bob on 2015/3/26.
 */
public class DeNewFriendListActivity extends BaseApiActivity implements Handler.Callback {

    private static final String TAG = DeNewFriendListActivity.class.getSimpleName();
    private AbstractHttpRequest<Friends> getFriendHttpRequest;
    private AbstractHttpRequest<Status> mRequestFriendHttpRequest;

    private ListView mNewFriendList;
    private DeNewFriendListAdapter adapter;
    private List<ApiResult> mResultList;
    private LoadingDialog mDialog;
    private Handler mHandler;

    @Override
    protected int setContentViewResId() {
        return R.layout.de_ac_new_friendlist;
    }

    @Override
    protected void initView() {
        getSupportActionBar().setTitle(R.string.de_new_friends);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        mNewFriendList = (ListView) findViewById(R.id.de_new_friend_list);
        mDialog = new LoadingDialog(this);
        mResultList = new ArrayList<>();
        mHandler = new Handler(this);


        if (DemoContext.getInstance() != null) {
            getFriendHttpRequest = DemoContext.getInstance().getDemoApi().getNewFriendlist(this);
            if (mDialog != null && !mDialog.isShowing()) {
                mDialog.show();
            }
        }


        Intent in = new Intent();
        in.setAction(MainActivity.ACTION_DMEO_RECEIVE_MESSAGE);
        in.putExtra("has_message", false);
        sendBroadcast(in);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onCallApiSuccess(AbstractHttpRequest request, Object obj) {
        if (getFriendHttpRequest == request) {
            if (mDialog != null)
                mDialog.dismiss();

            if (obj instanceof Friends) {
                final Friends friends = (Friends) obj;
                Log.e("", "------onCallApiSuccess-user.getCode()--" + friends.getCode());
                if (friends.getCode() == 200) {
                    if (friends.getResult().size() != 0) {
                        Log.e("", "------onCallApiSuccess-user.getCode() == 200)-----" + friends.getResult().get(0).getId().toString());
                        for (int i = 0; i < friends.getResult().size(); i++) {
                            mResultList.add(friends.getResult().get(i));
                        }
                        adapter = new DeNewFriendListAdapter(mResultList, DeNewFriendListActivity.this);
                        mNewFriendList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        adapter.setOnItemButtonClick(mOnItemButtonClick);
                    }
                }
            }
        } else if (mRequestFriendHttpRequest == request) {
            Log.e("", "0415----- mRequestFriendHttpRequest):");
        }


    }

    DeNewFriendListAdapter.OnItemButtonClick mOnItemButtonClick = new DeNewFriendListAdapter.OnItemButtonClick() {

        @Override
        public boolean onButtonClick(final int position, View view, int status) {
            switch (status) {
                case 1://好友

                    break;
                case 2://请求添加

                    break;
                case 3://请求被添加
                    mResultList.get(position).getId();

                    if (DemoContext.getInstance() != null)
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mRequestFriendHttpRequest = DemoContext.getInstance().getDemoApi().processRequestFriend(mResultList.get(position).getId(), "1", new ApiCallback<Status>() {
                                    @Override
                                    public void onComplete(AbstractHttpRequest<Status> statusAbstractHttpRequest, Status status) {
                                        ArrayList<UserInfo> friendreslist = new ArrayList<UserInfo>();
                                        UserInfo info = new UserInfo(mResultList.get(position).getId(), mResultList.get(position).getUsername(), mResultList.get(position).getPortrait() == null ? null : Uri.parse(mResultList.get(position).getPortrait()));
                                        if (DemoContext.getInstance() != null) {
                                            friendreslist = DemoContext.getInstance().getFriends();
                                            friendreslist.add(info);
                                            DemoContext.getInstance().setFriends(friendreslist);

                                            ApiResult apiResult = mResultList.get(position);
                                            apiResult.setStatus(1);
                                            mResultList.set(position, mResultList.get(position));

                                            Message mess = Message.obtain();
                                            mess.obj = mResultList;
                                            mess.what = 1;
                                            mHandler.sendMessage(mess);
                                        }

                                        sendMessage(mResultList.get(position).getId());

                                    }

                                    @Override
                                    public void onFailure(AbstractHttpRequest<Status> statusAbstractHttpRequest, BaseException e) {

                                    }
                                });
                            }
                        });


                    break;
                case 4://请求被拒绝

                    break;
                case 5://我被对方删除

                    break;
            }

            return false;
        }
    };


    @Override
    public void onCallApiFailure(AbstractHttpRequest request, BaseException e) {
        if (getFriendHttpRequest == request) {
            if (mDialog != null)
                mDialog.dismiss();
            WinToast.toast(this, "获取失败");
        }
    }


    /**
     * 添加好友成功后，向对方发送一条消息
     *
     * @param id 对方id
     */
    private void sendMessage(String id) {
        final DeAgreedFriendRequestMessage message = new DeAgreedFriendRequestMessage(id, "agree");
        if (DemoContext.getInstance() != null) {
            UserInfo userInfo = DemoContext.getInstance().getUserInfoById(id);
            message.setUserInfo(userInfo);
            if (RongIM.getInstance() != null) {
                RongIM.getInstance().getRongClient().sendMessage(Conversation.ConversationType.PRIVATE, id, message, null, new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onError(Integer messageId, RongIMClient.ErrorCode e) {
                        Log.e(TAG, DeConstants.DEBUG + "------DeAgreedFriendRequestMessage----onError--");
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        Log.e(TAG, DeConstants.DEBUG + "------DeAgreedFriendRequestMessage----onSuccess--" + message.getMessage());

                    }
                });
            }
        }
    }

    private void updateAdapter(List<ApiResult> mResultList) {
        if (adapter != null) {
            adapter = null;
        }

        adapter = new DeNewFriendListAdapter(mResultList, DeNewFriendListActivity.this);
        mNewFriendList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemButtonClick(mOnItemButtonClick);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.de_conversation_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == DeConstants.SEARCH_REQUESTCODE) {

            if (adapter != null) {
                adapter = null;
                mResultList.clear();
            }

            if (DemoContext.getInstance() != null) {
                getFriendHttpRequest = DemoContext.getInstance().getDemoApi().getNewFriendlist(this);

            }
            if (mDialog != null && !mDialog.isShowing()) {
                mDialog.show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.icon:
                Intent intent = new Intent(DeNewFriendListActivity.this, DeSearchFriendActivity.class);
                startActivityForResult(intent, DeConstants.FRIENDLIST_REQUESTCODE);
                break;

            case android.R.id.home:
                finish();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                Log.e("", DeConstants.DEBUG + "0417--------handleMessage------+++");
                mResultList = (List<ApiResult>) msg.obj;
                updateAdapter(mResultList);


                break;
        }


        return false;
    }

    @Override
    protected void onDestroy() {

        if (adapter != null) {
            adapter = null;
        }
        super.onDestroy();
    }
}
