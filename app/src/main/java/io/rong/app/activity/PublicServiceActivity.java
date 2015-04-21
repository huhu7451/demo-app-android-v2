package io.rong.app.activity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import io.rong.app.R;

/**
 * Created by rongdev on 15/4/10.
 */
public class PublicServiceActivity extends BaseActivity {

    @Override
    protected int setContentViewResId() {
        return R.layout.de_pub_list;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.de_pa_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bar_add:
                Intent intent = new Intent(PublicServiceActivity.this, PublicServiceSearchActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
