package io.rong.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.rong.app.activity.SOSOLocationActivity;
import io.rong.app.common.DemoApi;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Bob on 2015/1/30.
 */
public class DemoContext {

    private static DemoContext mDemoContext;
    public Context mContext;
    private DemoApi mDemoApi;
    private HashMap<String, Group> groupMap;
    private ArrayList<UserInfo> mUserInfos;
    private ArrayList<UserInfo> mFriendInfos;
    private SharedPreferences mPreferences;
    private RongIM.LocationProvider.LocationCallback mLastLocationCallback;


    public static DemoContext getInstance() {

        if (mDemoContext == null) {
            mDemoContext = new DemoContext();
        }
        return mDemoContext;
    }

    private DemoContext() {
    }

    private DemoContext(Context context) {
        mContext = context;
        mDemoContext = this;
        //http初始化 用于登录、注册使用
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        RongIM.setLocationProvider(new LocationProvider());

        mDemoApi = new DemoApi(context);
    }

    public static void init(Context context) {
        mDemoContext = new DemoContext(context);
    }

    public SharedPreferences getSharedPreferences() {
        return mPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.mPreferences = sharedPreferences;
    }

    public void setGroupMap(HashMap<String, Group> groupMap) {
        this.groupMap = groupMap;
    }

    public HashMap<String, Group> getGroupMap() {
        return groupMap;
    }


    public ArrayList<UserInfo> getUserInfos() {
        return mUserInfos;
    }

    public void setUserInfos(ArrayList<UserInfo> userInfos) {
        mUserInfos = userInfos;
    }

    /**
     * 临时存放用户数据
     *
     * @param userInfos
     */
    public void setFriends(ArrayList<UserInfo> userInfos) {

        this.mFriendInfos = userInfos;
    }

    public ArrayList<UserInfo> getFriends() {
        return mFriendInfos;
    }

    public DemoApi getDemoApi() {
        return mDemoApi;
    }


    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    public UserInfo getUserInfoById(String userId) {

        UserInfo userInfoReturn = null;

        if (!TextUtils.isEmpty(userId) && mUserInfos != null) {
            for (UserInfo userInfo : mUserInfos) {

                if (userId.equals(userInfo.getUserId())) {
                    userInfoReturn = userInfo;
                    break;
                }

            }
        }
        return userInfoReturn;
    }

    /**
     * 通过userid 获得username
     *
     * @param userId
     * @return
     */
    public String getUserNameByUserId(String userId) {
        UserInfo userInfoReturn = null;
        if (!TextUtils.isEmpty(userId) && mUserInfos != null) {
            for (UserInfo userInfo : mUserInfos) {

                if (userId.equals(userInfo.getUserId())) {
                    userInfoReturn = userInfo;
                    break;
                }
            }
        }
        return userInfoReturn.getName();
    }

    /**
     * 获取用户信息列表
     *
     * @param userIds
     * @return
     */
    public List<UserInfo> getUserInfoByIds(String[] userIds) {

        List<UserInfo> userInfoList = new ArrayList<UserInfo>();

        if (userIds != null && userIds.length > 0) {
            for (String userId : userIds) {
                for (UserInfo userInfo : mUserInfos) {
                    Log.e("", "0409-------getUserInfoByIds-" + userInfo.getUserId() + "---userid;" + userId);
                    if (userId.equals(userInfo.getUserId())) {
                        Log.e("", "0409-------getUserInfoByIds-" + userInfo.getName());
                        userInfoList.add(userInfo);
                    }
                }
            }
        }
        return userInfoList;
    }

    /**
     * 通过groupid 获得groupname
     *
     * @param groupid
     * @return
     */
    public String getGroupNameById(String groupid) {
        Group groupReturn = null;
        if (!TextUtils.isEmpty(groupid) && groupMap != null) {

            if (groupMap.containsKey(groupid)) {
                groupReturn = groupMap.get(groupid);
            }else
                return null;

        }
        return groupReturn.getName();
    }


    public RongIM.LocationProvider.LocationCallback getLastLocationCallback() {
        return mLastLocationCallback;
    }

    public void setLastLocationCallback(RongIM.LocationProvider.LocationCallback lastLocationCallback) {
        this.mLastLocationCallback = lastLocationCallback;
    }

    class LocationProvider implements RongIM.LocationProvider {

        /**
         * 位置信息提供者:LocationProvider 的回调方法，打开第三方地图页面。
         *
         * @param context  上下文
         * @param callback 回调
         */
        @Override
        public void onStartLocation(Context context, RongIM.LocationProvider.LocationCallback callback) {
            /**
             * demo 代码  开发者需替换成自己的代码。
             */
            DemoContext.getInstance().setLastLocationCallback(callback);
            Intent intent = new Intent(context, SOSOLocationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);//SOSO地图
        }
    }

}
