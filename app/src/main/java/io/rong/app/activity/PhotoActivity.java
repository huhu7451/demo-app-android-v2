package io.rong.app.activity;

import android.net.Uri;

import io.rong.app.R;
import io.rong.imkit.tools.PhotoFragment;

/**
 * Created by DragonJ on 15/4/13.
 */
public class PhotoActivity extends BaseActionBarActivity {
    PhotoFragment mPhotoFragment;

    @Override
    protected int setContentViewResId() {
        return R.layout.de_ac_photo;
    }

    @Override
    protected void initView() {
        mPhotoFragment =(PhotoFragment) getSupportFragmentManager().getFragments().get(0);
    }

    @Override
    protected void initData() {
        Uri uri = getIntent().getParcelableExtra("photo");

        if (uri != null)
            mPhotoFragment.initPhoto(uri);
    }
}
