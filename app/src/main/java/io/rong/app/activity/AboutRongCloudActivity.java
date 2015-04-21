package io.rong.app.activity;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import io.rong.app.R;

/**
 * Created by Administrator on 2015/3/3.
 */
public class AboutRongCloudActivity extends BaseActionBarActivity implements View.OnClickListener {

    /**
     * 更新日志
     */
    @SuppressWarnings("FieldCanBeLocal")
    private RelativeLayout mUpdateLog;
    /**
     * 更能介绍
     */
    @SuppressWarnings("FieldCanBeLocal")
    private RelativeLayout mFunctionIntroduce;
    /**
     * 开发者文档
     */
    @SuppressWarnings("FieldCanBeLocal")
    private RelativeLayout mDVDocument;
    /**
     * 官方网站
     */
    @SuppressWarnings("FieldCanBeLocal")
    private RelativeLayout mRongCloudWeb;
    /**
     * 版本更新
     */
    @SuppressWarnings("FieldCanBeLocal")
    private RelativeLayout mVersionUpdate;

    @Override
    protected int setContentViewResId() {
        return R.layout.de_ac_about_rongcloud;
    }

    @Override
    protected void initView() {
        getSupportActionBar().setTitle(R.string.set_rongcloud);
        mUpdateLog = (RelativeLayout) findViewById(R.id.rl_update_log);
        mFunctionIntroduce = (RelativeLayout) findViewById(R.id.rl_function_introduce);
        mDVDocument = (RelativeLayout) findViewById(R.id.rl_dv_document);
        mRongCloudWeb = (RelativeLayout) findViewById(R.id.rl_rongcloud_web);
        mVersionUpdate = (RelativeLayout) findViewById(R.id.rl_new_version_update);

        mUpdateLog.setOnClickListener(this);
        mFunctionIntroduce.setOnClickListener(this);
        mDVDocument.setOnClickListener(this);
        mRongCloudWeb.setOnClickListener(this);
        mVersionUpdate.setOnClickListener(this);

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_update_log://更新日志
                startActivity(new Intent(AboutRongCloudActivity.this,UpdateLogActivity.class));
                break;

            case R.id.rl_function_introduce://功能介绍
                startActivity(new Intent(AboutRongCloudActivity.this,FunctionIntroducedActivity.class));
                break;
            case R.id.rl_dv_document://开发者文档
                startActivity(new Intent(AboutRongCloudActivity.this,DocumentActivity.class));
                break;
            case R.id.rl_rongcloud_web://官方网站
                startActivity(new Intent(AboutRongCloudActivity.this,RongWebActivity.class));
                break;
            case R.id.rl_new_version_update://版本更新

                break;
        }
    }

}
