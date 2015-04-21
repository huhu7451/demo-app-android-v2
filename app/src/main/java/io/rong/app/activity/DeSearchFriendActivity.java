package io.rong.app.activity;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import io.rong.app.DemoContext;
import io.rong.app.R;
import io.rong.app.adapter.DeSearchFriendAdapter;
import io.rong.app.model.ApiResult;
import io.rong.app.model.Friends;
import io.rong.app.ui.LoadingDialog;
import io.rong.app.utils.DeConstants;
import me.add1.exception.BaseException;
import me.add1.network.AbstractHttpRequest;

/**
 * Created by Bob on 2015/3/26.
 */
public class DeSearchFriendActivity extends BaseApiActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText mEtSearch;
    private Button mBtSearch;
    private ListView mListSearch;
    private AbstractHttpRequest<Friends> searchHttpRequest;
    private List<ApiResult> mResultList;
    private DeSearchFriendAdapter adapter;
    private LoadingDialog mDialog;

    @Override
    protected int setContentViewResId() {
        return R.layout.de_ac_search;
    }

    @Override
    protected void initView() {
        getSupportActionBar().setTitle(R.string.public_account_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        mEtSearch = (EditText) findViewById(R.id.de_ui_search);
        mBtSearch = (Button) findViewById(R.id.de_search);
        mListSearch = (ListView) findViewById(R.id.de_search_list);
        mResultList = new ArrayList<>();
        mDialog = new LoadingDialog(this);

    }

    @Override
    protected void initData() {
        mBtSearch.setOnClickListener(this);
        mListSearch.setOnItemClickListener(this);
    }

    @Override
    public void onCallApiSuccess(AbstractHttpRequest request, Object obj) {
        if (searchHttpRequest == request) {
            if (mDialog != null)
                mDialog.dismiss();
            if (obj instanceof Friends) {
                final Friends friends = (Friends) obj;

                if (friends.getCode() == 200) {
                    if (friends.getResult().size() > 0) {
                        for (int i = 0; i < friends.getResult().size(); i++) {
                            mResultList.add(friends.getResult().get(i));
                            Log.e("", "------onCallApiSuccess-user.getCode() == 200)-----" + friends.getResult().get(0).getId().toString());

                        }
                        adapter = new DeSearchFriendAdapter(mResultList, DeSearchFriendActivity.this);
                        mListSearch.setAdapter(adapter);
                    }

                }
            }
        }

    }

    @Override
    public void onCallApiFailure(AbstractHttpRequest request, BaseException e) {
        if (searchHttpRequest == request) {
            if (mDialog != null)
                mDialog.dismiss();
            Log.e("", "------onCallApiSuccess-user.============onCallApiFailure()--");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mBtSearch)) {
            String userName = mEtSearch.getText().toString();
            if (DemoContext.getInstance() != null) {
                searchHttpRequest = DemoContext.getInstance().getDemoApi().searchUserByUserName(userName, this);

            }

            if (mDialog != null && !mDialog.isShowing()) {
                mDialog.show();
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode ==  DeConstants.PERSONAL_REQUESTCODE){
            Intent intent = new Intent();
            this.setResult( DeConstants.SEARCH_REQUESTCODE, intent);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent in = new Intent(this, DePersonalDetailActivity.class);
        in.putExtra("SEARCH_USERID", mResultList.get(position).getId());
        in.putExtra("SEARCH_USERNAME", mResultList.get(position).getUsername());
        in.putExtra("SEARCH_PORTRAIT", mResultList.get(position).getPortrait());
        startActivityForResult(in,  DeConstants.SEARCH_REQUESTCODE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
