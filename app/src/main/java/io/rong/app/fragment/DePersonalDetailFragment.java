package io.rong.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import io.rong.app.DemoContext;
import io.rong.app.R;
import io.rong.app.model.Friend;
import io.rong.imlib.model.UserInfo;
import io.rong.imkit.widget.AsyncImageView ;

/**
 * Created by Bob on 2015/4/7.
 */
public class DePersonalDetailFragment extends Fragment implements View.OnClickListener {
    private AsyncImageView mPersonalImg;
    private TextView mPersonalName;
    private TextView mPersonalId;
    private TextView mPersonalArea;
    private TextView mPersonalsignature;
    private Button mSendMessage;
    private Button mSendVoip;
    protected List<Friend> mFriendsList;
    private Friend mFriend;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.de_fr_personal_intro, null);
        mPersonalImg = (AsyncImageView) view.findViewById(R.id.personal_portrait);
        mPersonalName = (TextView) view.findViewById(R.id.personal_name);
        mPersonalId = (TextView) view.findViewById(R.id.personal_id);
        mPersonalArea = (TextView) view.findViewById(R.id.personal_area);
        mPersonalsignature = (TextView) view.findViewById(R.id.personal_signature);
        mSendMessage = (Button) view.findViewById(R.id.send_message);
        mSendVoip = (Button) view.findViewById(R.id.send_voip);
        initDate();
        return view;
    }

    private void initDate() {

        mSendMessage.setOnClickListener(this);
        mSendVoip.setOnClickListener(this);
        if (getActivity().getIntent().hasExtra("PERSONAL") && DemoContext.getInstance() != null) {
            String friendid = getActivity().getIntent().getStringExtra("PERSONAL");
            UserInfo userInfo = DemoContext.getInstance().getUserInfoById(friendid);
            mPersonalName.setText(userInfo.getName().toString());
            mPersonalId.setText("Id:"+userInfo.getUserId().toString());

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_message:

                break;
            case R.id.send_voip:

                break;
        }

    }
}
