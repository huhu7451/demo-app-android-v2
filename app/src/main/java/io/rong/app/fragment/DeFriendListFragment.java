package io.rong.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.rong.app.DemoContext;
import io.rong.app.R;
import io.rong.app.adapter.DeFriendListAdapter;
import io.rong.app.adapter.DeFriendMultiChoiceAdapter;
import io.rong.app.model.Friend;
import io.rong.app.ui.DePinnedHeaderListView;
import io.rong.app.ui.DeSwitchGroup;
import io.rong.app.ui.DeSwitchItemView;
import io.rong.imlib.model.UserInfo;

public class DeFriendListFragment extends Fragment implements DeSwitchGroup.ItemHander, OnClickListener, TextWatcher, DeFriendListAdapter.OnFilterFinished, OnItemClickListener {

    private static final String TAG = DeFriendListFragment.class.getSimpleName();
    protected DeFriendListAdapter mAdapter;
    private DePinnedHeaderListView mListView;
    private DeSwitchGroup mSwitchGroup;
    private EditText mEditText;

    protected List<Friend> mFriendsList;

    private boolean isMultiChoice = false;

    private ArrayList<String> mSelectedItemIds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.de_list_friend, null);

        mListView = (DePinnedHeaderListView) view.findViewById(R.id.de_ui_friend_list);
        mSwitchGroup = (DeSwitchGroup) view.findViewById(R.id.de_ui_friend_message);
        mEditText = (EditText) view.findViewById(R.id.de_ui_search);

        mListView.setPinnedHeaderView(LayoutInflater.from(this.getActivity()).inflate(R.layout.de_item_friend_index,
                mListView, false));

        mListView.setFastScrollEnabled(false);

        mListView.setOnItemClickListener(this);
        mSwitchGroup.setItemHander(this);
        mEditText.addTextChangedListener(this);

        mListView.setHeaderDividersEnabled(false);
        mListView.setFooterDividersEnabled(false);

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        ArrayList<UserInfo> userInfos = null;

        if (DemoContext.getInstance() != null) {
            userInfos = DemoContext.getInstance().getFriends();
        }

        mFriendsList = new ArrayList<Friend>();

        if (userInfos != null) {
            for (UserInfo userInfo : userInfos) {
                Friend friend = new Friend();
                friend.setNickname(userInfo.getName());
                friend.setPortrait(userInfo.getPortraitUri() + "");
                friend.setUserId(userInfo.getUserId());
                mFriendsList.add(friend);

            }
        }
        mFriendsList = sortFriends(mFriendsList);

        if (mSelectedItemIds != null && isMultiChoice) {

            for (String id : mSelectedItemIds) {
                for (Friend friend : mFriendsList) {
                    if (id.equals(friend.getUserId())) {
                        friend.setSelected(true);
                        break;
                    }
                }
            }
        }

        mAdapter = isMultiChoice ? new DeFriendMultiChoiceAdapter(this.getActivity(), mFriendsList, mSelectedItemIds) : new DeFriendListAdapter(this.getActivity(), mFriendsList);
        mListView.setAdapter(mAdapter);
        // mListView.addScrollStateListener(mAdapter);
        fillData();

        super.onViewCreated(view, savedInstanceState);
    }

    private final void fillData() {

//        mAdapter.removeAll();
        mAdapter.setAdapterData(mFriendsList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFilterFinished() {
        if (mFriendsList != null && mFriendsList.size() == 0) {
            return;
        }

        if (mAdapter == null || mAdapter.isEmpty()) {
        } else {
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mAdapter != null) {
            mAdapter.getFilter().filter(s);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {

        if (v instanceof DeSwitchItemView) {
            CharSequence tag = ((DeSwitchItemView) v).getText();

            if (mAdapter != null && mAdapter.getSectionIndexer() != null) {
                Object[] sections = mAdapter.getSectionIndexer().getSections();
                int size = sections.length;

                for (int i = 0; i < size; i++) {
                    if (tag.equals(sections[i])) {
                        int index = mAdapter.getPositionForSection(i);
                        mListView.setSelection(index + mListView.getHeaderViewsCount());
                        break;
                    }
                }
            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object tagObj = view.getTag();

        if (tagObj != null && tagObj instanceof DeFriendListAdapter.ViewHolder) {
            DeFriendListAdapter.ViewHolder viewHolder = (DeFriendListAdapter.ViewHolder) tagObj;
            mAdapter.onItemClick(viewHolder.friend.getUserId(), viewHolder.choice);
            return;
        }
    }

    @Override
    public void onDestroyView() {
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        super.onDestroyView();
    }

    public boolean isMultiChoice() {
        return isMultiChoice;
    }

    public void setMultiChoice(boolean isMultiChoice, ArrayList<String> selectedItemIds) {
        this.isMultiChoice = isMultiChoice;
        this.mSelectedItemIds = selectedItemIds;
    }


    private ArrayList<Friend> sortFriends(List<Friend> friends) {

        String[] searchLetters = getResources().getStringArray(R.array.de_search_letters);

        HashMap<String, ArrayList<Friend>> userMap = new HashMap<String, ArrayList<Friend>>();

        ArrayList<Friend> friendsArrayList = new ArrayList<Friend>();

        for (Friend friend : friends) {
            String letter = new String(new char[]{friend.getSearchKey()});

            if (userMap.containsKey(letter)) {
                ArrayList<Friend> friendList = userMap.get(letter);
                friendList.add(friend);
            } else {
                ArrayList<Friend> friendList = new ArrayList<Friend>();
                friendList.add(friend);
                userMap.put(letter, friendList);
            }
        }

        for (int i = 0; i < searchLetters.length; i++) {
            String letter = searchLetters[i];
            ArrayList<Friend> fArrayList = userMap.get(letter);
            if (fArrayList != null) {
                friendsArrayList.addAll(fArrayList);
            }
        }

        return friendsArrayList;
    }

}
