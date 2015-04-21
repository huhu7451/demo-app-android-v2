package io.rong.app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import io.rong.app.activity.DePersonalDetailActivity;
import io.rong.app.activity.MainActivity;
import io.rong.app.activity.PhotoActivity;
import io.rong.app.activity.SOSOLocationActivity;
import io.rong.app.message.DeAgreedFriendRequestMessage;
import io.rong.imkit.RongIM;
import io.rong.imkit.UiConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.LocationMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * Created by zhjchen on 1/29/15.
 */

/**
 * 融云SDK事件监听处理。
 * 把事件统一处理，开发者可直接复制到自己的项目中去使用。
 * <p/>
 * 该类包含的监听事件有：
 * 1、消息接收器：OnReceiveMessageListener。
 * 2、发出消息接收器：OnSendMessageListener。
 * 3、用户信息提供者：GetUserInfoProvider。
 * 4、好友信息提供者：GetFriendsProvider。
 * 5、群组信息提供者：GetGroupInfoProvider。
 * 6、会话界面操作的监听器：ConversationBehaviorListener。
 * 7、连接状态监听器，以获取连接相关状态：ConnectionStatusListener。
 * 8、地理位置提供者：LocationProvider。
 */
public final class RongCloudEvent implements RongIMClient.OnReceiveMessageListener, RongIM.OnSendMessageListener, RongIM.GetUserInfoProvider, RongIM.GetGroupInfoProvider, RongIM.ConversationBehaviorListener, RongIMClient.ConnectionStatusListener, RongIM.LocationProvider {

    private static final String TAG = RongCloudEvent.class.getSimpleName();

    private static RongCloudEvent mRongCloudInstance;

    private Context mContext;

    /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {

        if (mRongCloudInstance == null) {

            synchronized (RongCloudEvent.class) {

                if (mRongCloudInstance == null) {
                    mRongCloudInstance = new RongCloudEvent(context);
                }
            }
        }
    }

    /**
     * 构造方法。
     *
     * @param context 上下文。
     */
    private RongCloudEvent(Context context) {
        mContext = context;
        initDefaultListener();
    }

    /**
     * RongIM.init(this) 后直接可注册的Listener。
     */
    private void initDefaultListener() {
        RongIM.setGetUserInfoProvider(this, true);//设置用户信息提供者。
        RongIM.setGetGroupInfoProvider(this);//设置群组信息提供者。
        RongIM.setConversationBehaviorListener(this);//设置会话界面操作的监听器。
        RongIM.setLocationProvider(this);//设置地理位置提供者,不用位置的同学可以注掉此行代码
    }

    /*
     * 连接成功注册。
     * <p/>
     * 在RongIM-connect-onSuccess后调用。
     */
    public void setOtherListener() {
        RongIM.getInstance().getRongClient().setOnReceiveMessageListener(this);//设置消息接收监听器。
        RongIM.getInstance().setSendMessageListener(this);//设置发出消息接收监听器.
        RongIM.getInstance().getRongClient().setConnectionStatusListener(this);//设置连接状态监听器。
    }


    /**
     * 获取RongCloud 实例。
     *
     * @return RongCloud。
     */
    public static RongCloudEvent getInstance() {
        return mRongCloudInstance;
    }

    /**
     * 接收消息的监听器：OnReceiveMessageListener 的回调方法，接收到消息后执行。
     *
     * @param message 接收到的消息的实体信息。
     * @param left    剩余未拉取消息数目。
     */
    @Override
    public boolean onReceived(Message message, int left) {

        Message.MessageContent messageContent = message.getContent();

        if (messageContent instanceof TextMessage) {//文本消息
            TextMessage textMessage = (TextMessage) messageContent;
            Log.d(TAG, "onReceived-TextMessage:" + textMessage.getContent());
        } else if (messageContent instanceof ImageMessage) {//图片消息
            ImageMessage imageMessage = (ImageMessage) messageContent;
            Log.d(TAG, "onReceived-ImageMessage:" + imageMessage.getRemoteUri());
        } else if (messageContent instanceof VoiceMessage) {//语音消息
            VoiceMessage voiceMessage = (VoiceMessage) messageContent;
            Log.d(TAG, "onReceived-voiceMessage:" + voiceMessage.getUri().toString());
        } else if (messageContent instanceof RichContentMessage) {//图文消息
            RichContentMessage richContentMessage = (RichContentMessage) messageContent;
            Log.d(TAG, "onReceived-RichContentMessage:" + richContentMessage.getContent());
        } else if (messageContent instanceof InformationNotificationMessage) {//小灰条消息
            InformationNotificationMessage informationNotificationMessage = (InformationNotificationMessage) messageContent;
            Log.d(TAG, "onReceived-informationNotificationMessage:" + informationNotificationMessage.getMessage());
        } else if (messageContent instanceof DeAgreedFriendRequestMessage) {//好友添加成功消息
            DeAgreedFriendRequestMessage deAgreedFriendRequestMessage = (DeAgreedFriendRequestMessage) messageContent;
            Log.d(TAG, "onReceived-deAgreedFriendRequestMessage:" + deAgreedFriendRequestMessage.getMessage());
            reciverAgreeSuccess(deAgreedFriendRequestMessage);
        } else if (messageContent instanceof ContactNotificationMessage) {//好友添加消息
            ContactNotificationMessage contactContentMessage = (ContactNotificationMessage) messageContent;
            Log.d(TAG, "onReceived-ContactNotificationMessage:getExtra;" + contactContentMessage.getExtra());
            Log.d(TAG, "onReceived-ContactNotificationMessage:+getmessage:" + contactContentMessage.getMessage().toString());
//            RongIM.getInstance().getRongClient().deleteMessages(new int[]{message.getMessageId()});
//            if(DemoContext.getInstance()!=null) {
//                RongIM.getInstance().getRongClient().removeConversation(Conversation.ConversationType.SYSTEM, "10000");
//                String targetname = DemoContext.getInstance().getUserNameByUserId(contactContentMessage.getSourceUserId());
//                RongIM.getInstance().getRongClient().insertMessage(Conversation.ConversationType.SYSTEM, "10000", contactContentMessage.getSourceUserId(), contactContentMessage, null);
//
//            }
            Intent in = new Intent();
            in.setAction(MainActivity.ACTION_DMEO_RECEIVE_MESSAGE);
            in.putExtra("rongCloud", contactContentMessage);
            in.putExtra("has_message", true);
            mContext.sendBroadcast(in);
        } else {
            Log.d(TAG, "onReceived-其他消息，自己来判断处理");
        }

        return false;

    }

    /**
     *
     * @param deAgreedFriendRequestMessage
     */
    private void reciverAgreeSuccess(DeAgreedFriendRequestMessage deAgreedFriendRequestMessage) {
        ArrayList<UserInfo> friendreslist = new ArrayList<UserInfo>();
        if (DemoContext.getInstance() != null) {
            friendreslist = DemoContext.getInstance().getFriends();
            friendreslist.add(deAgreedFriendRequestMessage.getUserInfo());
            DemoContext.getInstance().setFriends(friendreslist);
        }
        Intent in = new Intent();
        in.setAction(MainActivity.ACTION_DMEO_AGREE_REQUEST);
        in.putExtra("AGREE_REQUEST", true);
        mContext.sendBroadcast(in);

    }


    /**
     * 消息在UI展示后执行/自己的消息发出后执行,无论成功或失败。
     *
     * @param message 消息。
     */
    @Override
    public Message onSent(Message message) {

        Message.MessageContent messageContent = message.getContent();

        if (messageContent instanceof TextMessage) {//文本消息
            TextMessage textMessage = (TextMessage) messageContent;
            Log.d(TAG, "onSent-TextMessage:" + textMessage.getContent());
        } else if (messageContent instanceof ImageMessage) {//图片消息
            ImageMessage imageMessage = (ImageMessage) messageContent;
            Log.d(TAG, "onSent-ImageMessage:" + imageMessage.getRemoteUri());
        } else if (messageContent instanceof VoiceMessage) {//语音消息
            VoiceMessage voiceMessage = (VoiceMessage) messageContent;
            Log.d(TAG, "onSent-voiceMessage:" + voiceMessage.getUri().toString());
        } else if (messageContent instanceof RichContentMessage) {//图文消息
            RichContentMessage richContentMessage = (RichContentMessage) messageContent;
            Log.d(TAG, "onSent-RichContentMessage:" + richContentMessage.getContent());
        } else {
            Log.d(TAG, "onSent-其他消息，自己来判断处理");
        }
        return message;
    }

    /**
     * 用户信息的提供者：GetUserInfoProvider 的回调方法，获取用户信息。
     *
     * @param userId 用户 Id。
     * @return 用户信息，（注：由开发者提供用户信息）。
     */
    @Override
    public UserInfo getUserInfo(String userId) {
        /**
         * demo 代码  开发者需替换成自己的代码。
         */
        return DemoContext.getInstance().getUserInfoById(userId);
//        return new UserInfo("10000","新好友消息", Uri.parse("test"));
    }


    /**
     * 群组信息的提供者：GetGroupInfoProvider 的回调方法， 获取群组信息。
     *
     * @param groupId 群组 Id.
     * @return 群组信息，（注：由开发者提供群组信息）。
     */
    @Override
    public Group getGroupInfo(String groupId) {
        /**
         * demo 代码  开发者需替换成自己的代码。
         */
        if (DemoContext.getInstance().getGroupMap() == null)
            return null;

        return DemoContext.getInstance().getGroupMap().get(groupId);
//        return null;
    }

    /**
     * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击用户头像后执行。
     *
     * @param context          应用当前上下文。
     * @param conversationType 会话类型。
     * @param user             被点击的用户的信息。
     * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
     */
    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo user) {
        Log.d(TAG, "onUserPortraitClick");

        /**
         * demo 代码  开发者需替换成自己的代码。
         */
        Log.d("Begavior", conversationType.getName() + ":" + user.getName());
        Intent in = new Intent(context, DePersonalDetailActivity.class);
        in.putExtra("SEARCH_USERNAME", user.getName());
        in.putExtra("SEARCH_USERID", user.getUserId());
        context.startActivity(in);

        return false;
    }

    /**
     * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击消息时执行。
     *
     * @param context 应用当前上下文。
     * @param message 被点击的消息的实体信息。
     * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
     */
    @Override
    public boolean onMessageClick(Context context, Message message) {
        Log.d(TAG, "onMessageClick");

        /**
         * demo 代码  开发者需替换成自己的代码。
         */
        if (message.getContent() instanceof LocationMessage) {
            Intent intent = new Intent(context, SOSOLocationActivity.class);
            intent.putExtra("location", message.getContent());
            context.startActivity(intent);
        } else if (message.getContent() instanceof RichContentMessage) {
            RichContentMessage mRichContentMessage = (RichContentMessage) message.getContent();
            Log.d("Begavior", "extra:" + mRichContentMessage.getExtra());

        } else if (message.getContent() instanceof ImageMessage) {
            ImageMessage imageMessage = (ImageMessage) message.getContent();
            Intent intent = new Intent(context, PhotoActivity.class);

            intent.putExtra("photo", imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri() : imageMessage.getLocalUri());

            context.startActivity(intent);
        }

        Log.d("Begavior", message.getObjectName() + ":" + message.getMessageId());

        return false;
    }



    @Override
    public boolean onMessageLongClick(Context context, Message message){
        return false;
    }

    @Override
    public boolean onConversationLongClick(Context context,UiConversation conversation){
        return false;
    }

    /**
     * 连接状态监听器，以获取连接相关状态:ConnectionStatusListener 的回调方法，网络状态变化时执行。
     *
     * @param status 网络状态。
     */
    @Override
    public void onChanged(ConnectionStatus status) {
        Log.d(TAG, "onChanged:" + status);
    }


    /**
     * 位置信息提供者:LocationProvider 的回调方法，打开第三方地图页面。
     *
     * @param context  上下文
     * @param callback 回调
     */
    @Override
    public void onStartLocation(Context context, LocationCallback callback) {
        /**
         * demo 代码  开发者需替换成自己的代码。
         */
        DemoContext.getInstance().setLastLocationCallback(callback);
        context.startActivity(new Intent(context, SOSOLocationActivity.class));//SOSO地图
    }
}
