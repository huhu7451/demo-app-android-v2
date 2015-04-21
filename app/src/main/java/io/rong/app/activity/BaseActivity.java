package io.rong.app.activity;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import io.rong.app.R;

public abstract class BaseActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);// 使得音量键控制媒体声音
        getSupportActionBar().setLogo(R.drawable.de_bar_logo);
        setContentView(setContentViewResId());
        initView();
        initData();

    }

    protected <T extends View> T getViewById(int id) {
        return (T) findViewById(id);
    }

    protected abstract int setContentViewResId();

    protected abstract void initView();

    protected abstract void initData();
}
