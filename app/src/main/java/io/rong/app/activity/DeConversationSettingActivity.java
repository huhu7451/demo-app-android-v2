package io.rong.app.activity;

import android.content.Intent;
import android.view.MenuItem;

import io.rong.app.R;
import me.add1.exception.BaseException;
import me.add1.network.AbstractHttpRequest;

/**
 * Created by Bob on 2015/3/27.
 */
public class DeConversationSettingActivity extends BaseApiActivity {
    private static final String TAG = DeConversationSettingActivity.class.getSimpleName();

    @Override
    public void onCallApiSuccess(AbstractHttpRequest request, Object obj) {

    }

    @Override
    public void onCallApiFailure(AbstractHttpRequest request, BaseException e) {

    }

    @Override
    protected int setContentViewResId() {
        return R.layout.de_ac_setting;
    }

    @Override
    protected void initView() {
        getSupportActionBar().setTitle(R.string.de_actionbar_set_conversation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        Intent intent = getIntent();
        if (intent.getData() != null) {
            final String targetId = intent.getData().getQueryParameter("targetId");
            final String targetIds = intent.getData().getQueryParameter("targetIds");
            final String delimiter = intent.getData().getQueryParameter("delimiter");
//        intent.getData().getQueryParameter("title")

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
