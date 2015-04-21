package io.rong.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import io.rong.app.DemoContext;
import io.rong.app.R;
import io.rong.app.fragment.DeFriendMultiChoiceFragment;
import io.rong.app.ui.WinToast;
import io.rong.imkit.RongIM;
import io.rong.imkit.common.RongConst;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.fragment.SubConversationListFragment;
import io.rong.imkit.fragment.UriFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;

/**
 * Created by Bob on 2015/3/27.
 */
public class DemoActivity extends BaseActivity {

    private static final String TAG = DemoActivity.class.getSimpleName();
    private String targetId;
    private String targetIds;
    private String mDiscussionId;
    private Conversation.ConversationType mConversationType;
    private boolean isSubList = false;

    @Override
    protected int setContentViewResId() {
        return R.layout.de_activity;
    }

    @Override
    protected void initView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);

        Intent intent = getIntent();

        if (intent != null) {
            Fragment fragment = null;

            if (intent.getExtras() != null && intent.getExtras().containsKey(RongConst.EXTRA.CONTENT)) {
                String fragmentName = intent.getExtras().getString(RongConst.EXTRA.CONTENT);
                fragment = Fragment.instantiate(this, fragmentName);
            } else if (intent.getData() != null) {
                if (intent.getData().getPathSegments().get(0).equals("conversation")) {
                    if (intent.getData().getLastPathSegment().equals("system")) {
//                        String fragmentName = MessageListFragment.class.getCanonicalName();
//                        fragment = Fragment.instantiate(this, fragmentName);
                        startActivity(new Intent(DemoActivity.this, DeNewFriendListActivity.class));
                        finish();
                    } else {
                        String fragmentName = ConversationFragment.class.getCanonicalName();
                        fragment = Fragment.instantiate(this, fragmentName);
                    }
                } else if (intent.getData().getLastPathSegment().equals("conversationlist")) {
                    String fragmentName = ConversationListFragment.class.getCanonicalName();
                    fragment = Fragment.instantiate(this, fragmentName);
                } else if (intent.getData().getLastPathSegment().equals("subconversationlist")) {
                    String fragmentName = SubConversationListFragment.class.getCanonicalName();
                    fragment = Fragment.instantiate(this, fragmentName);
                    isSubList = true;
                } else if (intent.getData().getPathSegments().get(0).equals("friend")) {
                    String fragmentName = DeFriendMultiChoiceFragment.class.getCanonicalName();
                    fragment = Fragment.instantiate(this, fragmentName);
                    ActionBar actionBar = getSupportActionBar();
                    actionBar.hide();//隐藏ActionBar
                }

                targetId = intent.getData().getQueryParameter("targetId");
                targetIds = intent.getData().getQueryParameter("targetIds");
                mDiscussionId = intent.getData().getQueryParameter("discussionId");
                if (targetId != null) {
                    mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase());
                } else if (targetIds != null)
                    mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase());
//                    mConversationType = Conversation.ConversationType.valueOf(intent.getData().getQueryParameter("type").toUpperCase());

                Log.e(TAG, "----demoacitivity  targetId----:" + targetId + ",targetIds----" + targetIds + ",mConversationType--" + mConversationType + ",mDiscussionId---" + mDiscussionId);
            }

            if (fragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.de_content, fragment);
                transaction.addToBackStack(null).commitAllowingStateLoss();
            }
        }
    }


    @Override
    protected void initData() {
        if (mConversationType != null) {
            if (mConversationType.toString().equals("PRIVATE")) {
                if (DemoContext.getInstance() != null)
                    getSupportActionBar().setTitle(DemoContext.getInstance().getUserNameByUserId(targetId));
            } else if (mConversationType.toString().equals("GROUP")) {
                if (DemoContext.getInstance() != null) {
                    getSupportActionBar().setTitle(DemoContext.getInstance().getGroupNameById(targetId));
                }
            } else if (mConversationType.toString().equals("DISCUSSION")) {
                if (targetId != null) {
                    RongIM.getInstance().getRongClient().getDiscussion(targetId, new RongIMClient.GetDiscussionCallback() {
                        @Override
                        public void onSuccess(Discussion discussion) {
                            getSupportActionBar().setTitle(discussion.getName());
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode e) {

                        }
                    });
                } else if (targetIds != null) {
                    setDiscussionName(targetIds);

                } else {

                    getSupportActionBar().setTitle("讨论组");
                }
            } else if (mConversationType.toString().equals("SYSTEM")) {

                getSupportActionBar().setTitle("系统会话类型");
            }else if(mConversationType.toString().equals("CHATROOM")){
                getSupportActionBar().setTitle("聊天室");
            }else if(mConversationType.toString().equals("CUSTOMER_SERVICE")){
                getSupportActionBar().setTitle("客服");
            }


        }

    }

    /**
     * set discussion name
     *
     * @param targetIds
     */
    private void setDiscussionName(String targetIds) {
        StringBuilder sb = new StringBuilder();
        getSupportActionBar().setTitle(targetIds);
        String[] ids = targetIds.split(",");
        if (DemoContext.getInstance() != null) {
            for (int i = 0; i < ids.length; i++) {
                DemoContext.getInstance().getUserNameByUserId(ids[i]);
                sb.append(DemoContext.getInstance().getUserNameByUserId(ids[i]));
                sb.append(",");
            }
            sb.append(DemoContext.getInstance().getSharedPreferences().getString("DEMO_USER_NAME", "0.0"));
        }

        getSupportActionBar().setTitle(sb);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);

        Fragment fragment = null;

        if (intent.getExtras() != null && intent.getExtras().containsKey(RongConst.EXTRA.CONTENT)) {
            String fragmentName = intent.getExtras().getString(RongConst.EXTRA.CONTENT);
            fragment = Fragment.instantiate(this, fragmentName);
        } else if (intent.getData() != null) {
            if (intent.getData().getPathSegments().get(0).equals("conversation")) {
                String fragmentName = ConversationFragment.class.getCanonicalName();
                fragment = Fragment.instantiate(this, fragmentName);
            } else if (intent.getData().getLastPathSegment().equals("conversationlist")) {
                String fragmentName = ConversationListFragment.class.getCanonicalName();
                fragment = Fragment.instantiate(this, fragmentName);
            } else if (intent.getData().getLastPathSegment().equals("subconversationlist")) {
                String fragmentName = SubConversationListFragment.class.getCanonicalName();
                fragment = Fragment.instantiate(this, fragmentName);

            }
        }

        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.de_content, fragment);
            transaction.addToBackStack(null).commitAllowingStateLoss();
        }
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            super.onBackPressed();
            this.finish();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.de_conversation_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.icon:
                if (mConversationType == null) {
                    return false;
                }
                if (mConversationType == Conversation.ConversationType.PUBLICSERVICE || mConversationType == Conversation.ConversationType.APPSERVICE) {
                    RongIM.getInstance().startPublicAccountInfo(this, mConversationType, targetId);
                } else {

                    if (!TextUtils.isEmpty(targetId)) {
                        Uri uri = Uri.parse("demo://" + getApplicationInfo().packageName).buildUpon().appendPath("conversationSetting")
                                .appendPath(mConversationType.getName()).appendQueryParameter("targetId", targetId).build();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(uri);
                        startActivity(intent);
                        Log.e(TAG, "00000000000----------targetId---uri.toString()--:" + uri.toString());
                    } else if (!TextUtils.isEmpty(targetIds)) {

                        UriFragment fragment = (UriFragment) getSupportFragmentManager().getFragments().get(0);
                        fragment.getUri();
                        targetId = fragment.getUri().getQueryParameter("targetId");


                        if (!TextUtils.isEmpty(targetId)) {
                            Uri uri = Uri.parse("demo://" + getApplicationInfo().packageName).buildUpon().appendPath("conversationSetting")
                                    .appendPath(mConversationType.getName()).appendQueryParameter("targetId", targetId).build();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(uri);
                            startActivity(intent);

                        } else {
                            WinToast.toast(DemoActivity.this, "讨论组尚未创建成功");

                        }
                    }
                }
                    break;

                    case android.R.id.home:
                        finish();

                        break;
                }

                return super.onOptionsItemSelected(item);
        }

}
