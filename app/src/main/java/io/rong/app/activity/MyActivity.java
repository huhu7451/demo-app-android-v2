package io.rong.app.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.rong.app.R;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;
import io.rong.imkit.widget.AsyncImageView ;

//import io.rong.imkit.logic.MessageLogic;


public class MyActivity extends FragmentActivity implements View.OnClickListener {


    Button mButton1, mButton2, mButton3, mButton4;
    TextView mTextView;
    AsyncImageView mIcon;

    Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);
        RongContext.getInstance().getEventBus().register(this);
        mHandler = new Handler();
        mTextView = (TextView) findViewById(android.R.id.text1);

        mButton1 = (Button) findViewById(android.R.id.button1);
        mButton2 = (Button) findViewById(android.R.id.button2);
        mButton3 = (Button) findViewById(android.R.id.button3);
        mButton4 = (Button) findViewById(android.R.id.copy);

        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);

        mIcon = (AsyncImageView) findViewById(android.R.id.icon);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    String mCurrentUserId;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            try {

                RongIM.connect("l6PSMOyNyK9mPLvZk1KJs82yq+hfEluLjZ78E1qo4hGoJtlXfHETWEcbNgxVEM9EUDxvbABgjKKNvcEjDhAfBQ==", new RongIMClient.ConnectCallback() {
                    @Override
                    public void onSuccess(String userId) {
                        Toast.makeText(MyActivity.this, "onSuccess", Toast.LENGTH_SHORT).show();
                        Log.d("Rong", "success:" + userId);
                        mCurrentUserId = userId;
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Toast.makeText(MyActivity.this, "onError", Toast.LENGTH_SHORT).show();

                        Log.e("Rong", "fail:" + errorCode.getValue());
                    }
                });

            } catch (Exception e) {

            }


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mButton1)) {
            Message message = Message.obtain(mCurrentUserId, io.rong.imlib.model.Conversation.ConversationType.PRIVATE, new TextMessage("test"));

            RongIM.getInstance().getRongClient().sendMessage(Conversation.ConversationType.PRIVATE, mCurrentUserId, new TextMessage("test"), null, new RongIMClient.SendMessageCallback() {
                @Override
                public void onSuccess(Integer messageId) {
                    Toast.makeText(MyActivity.this, "onSuccess", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Integer messageId, RongIMClient.ErrorCode errorCode) {
                    Toast.makeText(MyActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
                }

            });


        } else if (v.equals(mButton2)) {
            List<String> ids = new ArrayList<>();
            ids.add("6801");
            ids.add("15");
            ids.add("12306");
            RongIM.getInstance().createDiscussionChat(this, ids, "讨论组");

        } else if (v.equals(mButton3)) {
            RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE, mCurrentUserId, "Self");

        } else if (v.equals(mButton4)) {
//            RongIM.getInstance().disconnect();/

            RongIM.getInstance().startConversationList(this);

        }
    }

    public void onEvent(Message message) {
        Log.d("onEvent", message.toString());
    }

    public void onEvent(UserInfo userInfo) {
        Log.d("onEvent", userInfo.toString());
    }
}
