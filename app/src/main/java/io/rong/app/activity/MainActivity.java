package io.rong.app.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import io.rong.app.DemoContext;
import io.rong.app.R;
import io.rong.app.fragment.DeChatroomListFragment;
import io.rong.app.fragment.DeCustomerFragment;
import io.rong.app.fragment.DeGroupListFragment;
import io.rong.app.model.Friends;
import io.rong.app.ui.LoadingDialog;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import me.add1.exception.BaseException;
import me.add1.network.AbstractHttpRequest;

public class MainActivity extends BaseApiActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, ActionBar.OnMenuVisibilityListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String ACTION_DMEO_RECEIVE_MESSAGE = "action_demo_receive_message";
    public static final String ACTION_DMEO_AGREE_REQUEST = "action_demo_agree_request";
    private RelativeLayout mMainConversationLiner;
    private RelativeLayout mMainGroupLiner;
    private RelativeLayout mMainChatroomLiner;
    private RelativeLayout mMainCustomerLiner;

    /**
     * 聊天室的fragment
     */
    private Fragment mChatroomFragment = null;

    /**
     * 客服的fragment
     */
    private Fragment mCustomerFragment = null;
    /**
     * 会话列表的fragment
     */
    private Fragment mConversationFragment = null;
    /**
     * 群组的fragment
     */
    private Fragment mGroupListFragment = null;
    /**
     * 会话TextView
     */
    private TextView mMainConversationTv;
    /**
     * 群组TextView
     */
    private TextView mMainGroupTv;

    private TextView mUnreadNumView;
    /**
     * 聊天室TextView
     */
    private TextView mMainChatroomTv;
    /**
     * 客服TextView
     */
    private TextView mMainCustomerTv;

    private FragmentManager mFragmentManager;


    private ViewPager mViewPager;
    /**
     * 下划线
     */
    private ImageView mMainSelectImg;

    private DemoFragmentPagerAdapter mDemoFragmentPagerAdapter;

    private LayoutInflater mInflater;
    /**
     * 下划线长度
     */
    int indicatorWidth;
    private LinearLayout mMainShow;

    private boolean hasNewFriends = false;
    private Menu mMenu;
    private ReceiveMessageBroadcastReciver mBroadcastReciver;
    private LoadingDialog mDialog;
    //    private AbstractHttpRequest<Friends> getUserInfoHttpRequest;
    private AbstractHttpRequest<Friends> getFriendsHttpRequest;
    private int mNetNum = 0;

    @Override
    protected int setContentViewResId() {
        return R.layout.de_ac_main;
    }

    @Override
    protected void initView() {
        mFragmentManager = getSupportFragmentManager();
        getSupportActionBar().setTitle(R.string.main_name);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm); // 获取屏幕信息
        indicatorWidth = dm.widthPixels / 4;// 指示器宽度为屏幕宽度的4/1

        mMainShow = (LinearLayout) findViewById(R.id.main_show);
        mMainConversationLiner = (RelativeLayout) findViewById(R.id.main_conversation_liner);
        mMainGroupLiner = (RelativeLayout) findViewById(R.id.main_group_liner);
        mMainChatroomLiner = (RelativeLayout) findViewById(R.id.main_chatroom_liner);
        mMainCustomerLiner = (RelativeLayout) findViewById(R.id.main_customer_liner);
        mMainConversationTv = (TextView) findViewById(R.id.main_conversation_tv);
        mMainGroupTv = (TextView) findViewById(R.id.main_group_tv);
        mMainChatroomTv = (TextView) findViewById(R.id.main_chatroom_tv);
        mMainCustomerTv = (TextView) findViewById(R.id.main_customer_tv);
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        mMainSelectImg = (ImageView) findViewById(R.id.main_switch_img);
        mUnreadNumView = (TextView) findViewById(R.id.de_num);

        ViewGroup.LayoutParams cursor_Params = mMainSelectImg.getLayoutParams();
        cursor_Params.width = indicatorWidth;// 初始化滑动下标的宽
        mMainSelectImg.setLayoutParams(cursor_Params);
        // 获取布局填充器
        mInflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);

    }


    @Override
    protected void initData() {
        mMainChatroomLiner.setOnClickListener(this);
        mMainConversationLiner.setOnClickListener(this);
        mMainGroupLiner.setOnClickListener(this);
        mMainCustomerLiner.setOnClickListener(this);
        mDemoFragmentPagerAdapter = new DemoFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mDemoFragmentPagerAdapter);
        mViewPager.setOnPageChangeListener(this);
        mDialog = new LoadingDialog(this);
        //发起获取好友列表的http请求  (注：非融云SDK接口，是demo接口)
        if (DemoContext.getInstance() != null) {

//            getUserInfoHttpRequest = DemoContext.getInstance().getDemoApi().getFriends(MainActivity.this);

            getFriendsHttpRequest = DemoContext.getInstance().getDemoApi().getNewFriendlist(MainActivity.this);
            if (mDialog != null && !mDialog.isShowing()) {
                mDialog.show();
            }
        }

        final Conversation.ConversationType[] conversationTypes = {Conversation.ConversationType.PRIVATE, Conversation.ConversationType.DISCUSSION,
                Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM, Conversation.ConversationType.CHATROOM,
                Conversation.ConversationType.PUBLICSERVICE, Conversation.ConversationType.APPSERVICE};

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RongIM.getInstance().setOnReceiveUnreadMessageCountListener(mCountListener, conversationTypes);
            }
        }, 500);


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DMEO_RECEIVE_MESSAGE);
        if (mBroadcastReciver == null) {
            mBroadcastReciver = new ReceiveMessageBroadcastReciver();
        }
        this.registerReceiver(mBroadcastReciver, intentFilter);

    }

    public RongIM.OnReceiveUnreadMessageCountListener mCountListener = new RongIM.OnReceiveUnreadMessageCountListener() {
        @Override
        public void onMessageIncreased(int count) {
            if (count == 0) {
                mUnreadNumView.setVisibility(View.GONE);
            } else if (count > 0 && count < 100) {
                mUnreadNumView.setVisibility(View.VISIBLE);
                mUnreadNumView.setText(count + "");
            } else {
                mUnreadNumView.setVisibility(View.VISIBLE);
                mUnreadNumView.setText(R.string.no_read_message);
            }
        }
    };


    @Override
    public void onMenuVisibilityChanged(boolean b) {

    }


    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {

        switch (i) {
            case 0:
                selectNavSelection(0);
                break;
            case 1:
                selectNavSelection(1);
                break;
            case 2:
                selectNavSelection(2);
                break;
            case 3:
                selectNavSelection(3);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


    private class DemoFragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

        public DemoFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            switch (i) {
                case 0:
                    mMainConversationTv.setTextColor(getResources().getColor(R.color.de_title_bg));
//TODO
                    if (mConversationFragment == null) {
                        ConversationListFragment listFragment = ConversationListFragment.getInstance();
                        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                                .appendPath("conversationlist")
                                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")
                                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false") //设置私聊会话是否聚合显示
                                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false") //设置私聊会话是否聚合显示
                                .appendQueryParameter(Conversation.ConversationType.PUBLICSERVICE.getName(), "false")
                                .appendQueryParameter(Conversation.ConversationType.APPSERVICE.getName(), "false")
                                .build();
                        listFragment.initFragment(uri);
                        fragment = listFragment;
                    } else {
                        fragment = mConversationFragment;
                    }
                    break;
                case 1:
                    if (mGroupListFragment == null) {
                        mGroupListFragment = new DeGroupListFragment();
                    }

                    fragment = mGroupListFragment;

                    break;

                case 2:
                    if (mChatroomFragment == null) {
                        fragment = new DeChatroomListFragment();
                    } else {
                        fragment = mChatroomFragment;
                    }
                    break;
                case 3:
                    if (mCustomerFragment == null) {
                        fragment = new DeCustomerFragment();
                    } else {
                        fragment = mCustomerFragment;
                    }
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    private void selectNavSelection(int index) {
        clearSelection();
        switch (index) {
            case 0:
                mMainConversationTv.setTextColor(getResources().getColor(R.color.de_title_bg));
                TranslateAnimation animation = new TranslateAnimation(0, 0,
                        0f, 0f);
                animation.setInterpolator(new LinearInterpolator());
                animation.setDuration(100);
                animation.setFillAfter(true);
                mMainSelectImg.startAnimation(animation);

                break;
            case 1:
                mMainGroupTv.setTextColor(getResources().getColor(R.color.de_title_bg));
                TranslateAnimation animation1 = new TranslateAnimation(
                        indicatorWidth, indicatorWidth,
                        0f, 0f);
                animation1.setInterpolator(new LinearInterpolator());
                animation1.setDuration(100);
                animation1.setFillAfter(true);
                mMainSelectImg.startAnimation(animation1);

                break;
            case 2:
                mMainChatroomTv.setTextColor(getResources().getColor(R.color.de_title_bg));
                TranslateAnimation animation2 = new TranslateAnimation(
                        2 * indicatorWidth, indicatorWidth * 2,
                        0f, 0f);
                animation2.setInterpolator(new LinearInterpolator());
                animation2.setDuration(100);
                animation2.setFillAfter(true);
                mMainSelectImg.startAnimation(animation2);

                break;
            case 3:
                mMainCustomerTv.setTextColor(getResources().getColor(R.color.de_title_bg));
                TranslateAnimation animation3 = new TranslateAnimation(
                        3 * indicatorWidth, indicatorWidth * 3,
                        0f, 0f);
                animation3.setInterpolator(new LinearInterpolator());
                animation3.setDuration(100);
                animation3.setFillAfter(true);
                mMainSelectImg.startAnimation(animation3);
                break;
        }
    }

    private void clearSelection() {
        mMainConversationTv.setTextColor(getResources().getColor(R.color.black_textview));
        mMainGroupTv.setTextColor(getResources().getColor(R.color.black_textview));
        mMainChatroomTv.setTextColor(getResources().getColor(R.color.black_textview));
        mMainCustomerTv.setTextColor(getResources().getColor(R.color.black_textview));
    }

    private class ReceiveMessageBroadcastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_DMEO_RECEIVE_MESSAGE)) {
                hasNewFriends = intent.getBooleanExtra("has_message", false);
                supportInvalidateOptionsMenu();
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_conversation_liner:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.main_group_liner:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.main_chatroom_liner:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.main_customer_liner:
                mViewPager.setCurrentItem(3);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        this.mMenu = menu;
        inflater.inflate(R.menu.de_main_menu, menu);
        if (hasNewFriends) {
            mMenu.getItem(0).setIcon(getResources().getDrawable(R.drawable.de_ic_add_hasmessage));
            mMenu.getItem(0).getSubMenu().getItem(2).setIcon(getResources().getDrawable(R.drawable.de_btn_main_contacts_select));
        } else {
            mMenu.getItem(0).setIcon(getResources().getDrawable(R.drawable.de_ic_add));
            mMenu.getItem(0).getSubMenu().getItem(2).setIcon(getResources().getDrawable(R.drawable.de_btn_main_contacts));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item1://发起聊天
                startActivity(new Intent(this, DeFriendListActivity.class));
                break;
            case R.id.add_item2://选择群组
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startSubConversationList(this, Conversation.ConversationType.GROUP);
                }

                break;
            case R.id.add_item3://通讯录
                startActivity(new Intent(MainActivity.this, DeAdressListActivity.class));
                break;
            case R.id.set_item1://我的账号
                startActivity(new Intent(MainActivity.this, MyAccountActivity.class));
                break;
            case R.id.set_item2://新消息提醒
                startActivity(new Intent(MainActivity.this, NewMessageRemindActivity.class));
                break;
            case R.id.set_item3://隐私
                startActivity(new Intent(MainActivity.this, PrivacyActivity.class));
                break;
            case R.id.set_item4://关于融云
                startActivity(new Intent(MainActivity.this, AboutRongCloudActivity.class));
                break;
            case R.id.set_item5://退出
                if (RongIM.getInstance() != null) RongIM.getInstance().disconnect(false);
                Process.killProcess(Process.myPid());
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCallApiSuccess(AbstractHttpRequest request, Object obj) {

        if (getFriendsHttpRequest == request) {
            if (obj instanceof Friends) {
                final Friends friends = (Friends) obj;
                if (friends.getCode() == 200) {
                    ArrayList<UserInfo> friendreslut = new ArrayList<UserInfo>();

                    for (int i = 0; i < friends.getResult().size(); i++) {
                        if (friends.getResult().get(i).getStatus() == 1 || friends.getResult().get(i).getStatus() == 3 || friends.getResult().get(i).getStatus() == 5) {
                            UserInfo info = new UserInfo(String.valueOf(friends.getResult().get(i).getId()), friends.getResult().get(i).getUsername(), friends.getResult().get(i).getPortrait() == null ? null : Uri.parse(friends.getResult().get(i).getPortrait()));
                            friendreslut.add(info);
                        }
                    }
                    if (DemoContext.getInstance() != null)

                        DemoContext.getInstance().setFriends(friendreslut);

                    if (mDialog != null)
                        mDialog.dismiss();

                }
            }

        }
    }

    @Override
    public void onCallApiFailure(AbstractHttpRequest request, BaseException e) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {


            final AlertDialog.Builder alterDialog = new AlertDialog.Builder(this);
            alterDialog.setMessage("确定退出应用？");
            alterDialog.setCancelable(true);

            alterDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (RongIM.getInstance() != null) RongIM.getInstance().disconnect(true);
                    android.os.Process.killProcess(Process.myPid());

                }
            });
            alterDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alterDialog.show();
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        if (mBroadcastReciver != null) {
            this.unregisterReceiver(mBroadcastReciver);
        }
        super.onDestroy();
    }

}
