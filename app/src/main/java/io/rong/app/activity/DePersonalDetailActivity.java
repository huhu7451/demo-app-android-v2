package io.rong.app.activity;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.rong.app.DemoContext;
import io.rong.app.R;
import io.rong.app.model.User;
import io.rong.app.ui.LoadingDialog;
import io.rong.app.ui.WinToast;
import io.rong.app.utils.DeConstants;
import me.add1.exception.BaseException;
import me.add1.network.AbstractHttpRequest;
import io.rong.imkit.widget.AsyncImageView ;


/**
 * Created by Bob on 2015/3/26.
 */
public class DePersonalDetailActivity extends BaseApiActivity implements View.OnClickListener {


    private AsyncImageView mFriendImg;
    private TextView mFriendName;
    private Button mAddFriend;
    private AbstractHttpRequest<User> mUserHttpRequest;
    private LoadingDialog mDialog;
    @Override
    protected int setContentViewResId() {
        return R.layout.de_ac_personal_detail;
    }

    @Override
    protected void initView() {
        getSupportActionBar().setTitle(R.string.public_add_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        mFriendImg = (AsyncImageView) findViewById(R.id.friend_adapter_img);
        mFriendName = (TextView) findViewById(R.id.de_name);
        mAddFriend = (Button) findViewById(R.id.de_add_friend);
    }

    @Override
    protected void initData() {
        mAddFriend.setOnClickListener(this);
        mDialog = new LoadingDialog(this);
        if (getIntent().getStringExtra("SEARCH_USERNAME") != null) {
            mFriendName.setText(getIntent().getStringExtra("SEARCH_USERNAME").toString());
        }

    }

    @Override
    public void onCallApiSuccess(AbstractHttpRequest request, Object obj) {
        if (mUserHttpRequest == request) {
            if (mDialog != null)
                mDialog.dismiss();
            final User user = (User) obj;
            if (user.getCode() == 200) {
                WinToast.toast(this,R.string.friend_send_success);
                Log.e("", "--------onCallApiSuccess----发送好友请求成功---------");
                Intent intent = new Intent();
                this.setResult( DeConstants.PERSONAL_REQUESTCODE, intent);

            }else if(user.getCode() == 301){
                WinToast.toast(this,R.string.friend_send);
            }
        }

    }

    @Override
    public void onCallApiFailure(AbstractHttpRequest request, BaseException e) {
        if (mUserHttpRequest == request) {
            if (mDialog != null)
                mDialog.dismiss();
            Log.e("", "--------onCallApiSuccess----发送好友请求失败---------");
        }
    }

    @Override
    public void onClick(View v) {
        String targetid = getIntent().getStringExtra("SEARCH_USERID");

        if (DemoContext.getInstance() != null && !"".equals(targetid)) {
            if (DemoContext.getInstance() != null) {
                String targetname = DemoContext.getInstance().getUserNameByUserId(targetid);
                mUserHttpRequest = DemoContext.getInstance().getDemoApi().sendFriendInvite(targetid,"请添加我为好友，I'm "+targetname, this);

                if (mDialog != null && !mDialog.isShowing()) {
                    mDialog.show();
                }
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
