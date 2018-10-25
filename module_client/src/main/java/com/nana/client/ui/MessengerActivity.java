package com.nana.client.ui;

import android.app.Activity;
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
import android.support.annotation.Nullable;
import android.util.Log;

import com.nana.client.R;
import com.nana.server.messenger.MessengerService;

/**
 * Description:PIC（进程间通信）：Messenger实例
 *
 * @author yangnana
 * @version 1
 * @since 1
 */
public class MessengerActivity extends Activity {
    private static final String TAG = "MessengerActivity";
    private static final int MSG_FROM_CLIENT = 0;
    private static final int MSG_FROM_SERVER = 1;


    private Messenger mService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        bindService();

    }

    /**
     * 绑定服务
     */
    private void bindService() {
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        //取消绑定
        unbindService(mConnection);
        super.onDestroy();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            Message msg = Message.obtain(null, MSG_FROM_CLIENT);
            Bundle data = new Bundle();
            data.putString("msg", "hello ,this is client");
            msg.setData(data);

            msg.replyTo = mGetReplyMessenger;
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
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
                    Log.i(TAG, "******receive message from server:" + msg.getData().getString("reply"));

                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }


    //客户端用户接收服务端消息的Messenger
    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());


}
