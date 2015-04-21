package io.rong.app.activity;

import android.view.MenuItem;

import io.rong.app.R;

/**
 * Created by Bob on 2015/4/9.
 */
public class DeBlackListActivity  extends BaseActivity{
    @Override
    protected int setContentViewResId() {
        return R.layout.de_fr_black;
    }

    @Override
    protected void initView() {
        getSupportActionBar().setTitle(R.string.the_blacklist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
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
