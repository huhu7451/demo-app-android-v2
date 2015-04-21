package io.rong.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.rong.app.R;
import io.rong.app.activity.DeFriendListActivity;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.DispatchResultFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by Bob on 2015/3/27.
 */
public class DeSettingFragment extends DispatchResultFragment  {
    private static final String TAG = DeSettingFragment.class.getSimpleName();
    private String targetId;
    private String targetIds;
    private Conversation.ConversationType mConversationType;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.de_ac_friend_setting, null);

        init();
        return view;
    }

    private void init() {
        Intent intent = getActivity().getIntent();
        if (intent.getData() != null) {
            targetId = intent.getData().getQueryParameter("targetId");
            targetIds = intent.getData().getQueryParameter("targetIds");
            final String delimiter = intent.getData().getQueryParameter("delimiter");

            if (targetId != null) {
                mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase());
            } else if (targetIds != null)
                mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase());
//                mConversationType = Conversation.ConversationType.valueOf(intent.getData().getQueryParameter("type").toUpperCase());
            Log.e(TAG, "----  targetId----:" +targetId+ ",targetIds----" + targetIds + ",mConversationType--" + mConversationType );

            RongContext.getInstance().setOnMemberSelectListener(new RongIM.OnMemberSelectListener() {
                @Override
                public void startMemberSelect(Context context, Conversation.ConversationType conversationType, String targetId) {

                    if (targetId != null)
                        mConversationType = Conversation.ConversationType.valueOf(getActivity().getIntent().getData().getLastPathSegment().toUpperCase());

                    startActivity(new Intent(getActivity(), DeFriendListActivity.class));


                }
            });
        }
    }


    @Override
    protected void initFragment(Uri uri) {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
