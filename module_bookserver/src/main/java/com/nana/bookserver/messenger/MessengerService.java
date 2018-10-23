package com.nana.bookserver.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Description:Messenger实现进程间通信，服务端代码
 *
 * @author yangnana
 * @version 1.3.2
 * @since 1.3.2
 */
public class MessengerService extends Service {

    private static final String TAG = "MessengerService";
    private static final int MSG_FROM_CLIENT = 0;
    private static final int MSG_FROM_SERVER = 1;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    /**
     * 接收客户端发送的消息
     */
    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //TODO 收到客户端发来的数据，进行处理
                case MSG_FROM_CLIENT:

                    /**输出客户端发送来的消息*/
                    Log.i(TAG,"******receive message from client:"+msg.getData().getString("msg"));


                    /**服务端收到客户端消息之后，回复客户端*/
                    Messenger client=msg.replyTo;
                    Message replyMessage=Message.obtain(null,MSG_FROM_SERVER);
                    Bundle bundle=new Bundle();
                    bundle.putString("reply","ok ,我已经收到你的消息了，稍后会回复你");
                    replyMessage.setData(bundle);
                    try {
                        client.send(replyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    //服务端用于接收客户端消息的Messenger
    private final Messenger mMessenger = new Messenger(new MessengerHandler());

}
