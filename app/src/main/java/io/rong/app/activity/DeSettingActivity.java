package io.rong.app.activity;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import io.rong.app.R;
import io.rong.imlib.model.Conversation;
import me.add1.exception.BaseException;
import me.add1.network.AbstractHttpRequest;

/**
 * Created by Administrator on 2015/4/19.
 */
public class DeSettingActivity extends BaseApiActivity {

    private static final String TAG = DeSettingActivity.class.getSimpleName();
    private String targetId;
    private String targetIds;
    private Conversation.ConversationType mConversationType;
    @Override
    public void onCallApiSuccess(AbstractHttpRequest request, Object obj) {
        
    }

    @Override
    public void onCallApiFailure(AbstractHttpRequest request, BaseException e) {

    }

    @Override
    protected int setContentViewResId() {
        return R.layout.de_ac_friend_setting;
    }

    @Override
    protected void initView() {
        getSupportActionBar().setTitle(R.string.de_actionbar_set_conversation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        Intent intent = getIntent();
        if (intent.getData() != null) {
            targetId = intent.getData().getQueryParameter("targetId");
            targetIds = intent.getData().getQueryParameter("targetIds");
            final String delimiter = intent.getData().getQueryParameter("delimiter");

            if (targetId != null) {
                mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase());
            } else if (targetIds != null)
                mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase());
//                mConversationType = Conversation.ConversationType.valueOf(intent.getData().getQueryParameter("type").toUpperCase());
            Log.e(TAG, "----  targetId----:" + targetId + ",targetIds----" + targetIds + ",mConversationType--" + mConversationType);


        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
