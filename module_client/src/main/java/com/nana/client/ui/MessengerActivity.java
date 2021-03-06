package com.nana.client.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import com.nana.client.R;
import com.nana.devkit.BaseActivity;
import com.nana.server.messenger.MessengerService;

/**
 * Description:PIC（进程间通信）：Messenger实例
 *
 * @author yangnana
 * @version 1
 * @since 1
 */
public class MessengerActivity extends BaseActivity {
    private static final String TAG = "MessengerActivity";
    private static final int MSG_FROM_CLIENT = 0;
    private static final int MSG_FROM_SERVER = 1;
    private static TextView mContent;


    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        connectService();
    }

    /**
     * 连接服务
     */
    private void connectService() {
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void injectContentView() {
        setContentView(R.layout.module_client_activity_messenger);
    }

    @Override
    protected void injectViews() {
        initToolbar();
        mContent = findViewById(R.id.mtc_client_messenger_content_tv);
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.toobar);
        mToolbar.setTitle("进程间通信->Messenger");
        mToolbar.setNavigationIcon(R.drawable.ic_search);
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onDestroy() {
        /*取消绑定*/
        unbindService(mConnection);
        super.onDestroy();
    }

    /**
     * 运行在UI线程中
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Messenger mService = new Messenger(service);
            Message msg = Message.obtain(null, MSG_FROM_CLIENT);
            Bundle data = new Bundle();
            data.putString("msg", "hello ,this is client");
            msg.setData(data);
            msg.replyTo = mGetReplyMessenger;

            try {
                /*发送消息到服务端*/
                mService.send(msg);
            } catch (RemoteException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    /**
     * 接收服务端回复的内容
     */
    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_FROM_SERVER:

                    mContent.append("receive message from server-->" + msg.getData().getString("reply"));
                    break;
                case MSG_FROM_CLIENT:

                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }


    /**
     * 客户端用户接收服务端消息的Messenger
     */
    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());


}
