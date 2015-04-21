package io.rong.app.activity;

import android.support.v4.app.Fragment;
import android.view.MenuItem;

import io.rong.app.R;
import io.rong.app.fragment.DeAdressListFragment;

/**
 * Created by Administrator on 2015/3/26.
 */
public class DeAdressListActivity extends BaseActionBarActivity  {
    private static final String TAG = DeAdressListActivity.class.getSimpleName();
    private Fragment mFragment;

    @Override
    protected int setContentViewResId() {
        return R.layout.de_ac_address_fragment;
    }

    @Override
    protected void initView() {
        getSupportActionBar().setTitle(R.string.add_contacts);
        mFragment = new DeAdressListFragment();


    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
