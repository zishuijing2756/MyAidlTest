package com.nana.client.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nana.client.R;
import com.nana.devkit.optimize.TaskPool;
import com.nana.server.aidl.Book;
import com.nana.server.aidl.BookManagerService;
import com.nana.server.aidl.IBookManager;
import com.nana.server.aidl.IOnNewBookArrivedListener;
import com.nana.devkit.BaseActivity;

import java.util.List;

/**
 * Description: PIC（进程间通信）：AIDL -》客户端调用服务实例
 *
 * @author yangnana
 * @version 1
 * @since 1
 */
public class BookManagerActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "BookManagerActivity";

    private static final int LOG_INFO = 0;
    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;
    private IBookManager mRemoteBookManager;

    private static TextView sContentTv;

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
    }

    @Override
    protected void injectContentView() {
        setContentView(R.layout.module_client_activity_bookmanager);
    }

    @Override
    protected void injectViews() {
        sContentTv = findViewById(R.id.mtc_client_book_manager_content_tv);
        findViewById(R.id.mtc_client_book_manager_bind_service_btn).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.mtc_client_book_manager_bind_service_btn:
                Log.i(TAG, "click on bind service Button");
                sContentTv.append("->click on bind service Button \n");
                /*连接服务*/
                connectBinderService();
                break;
            default:
                break;
        }
    }

    /**
     * 第二步：绑定服务；
     */
    private void connectBinderService() {
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        /*
         * 解除已经注册到服务端的listener
         */
        if (mRemoteBookManager != null && mRemoteBookManager.asBinder().isBinderAlive()) {
            try {
                mRemoteBookManager.unRegisterListener(mOnNewBookArrivedListener);
                Log.i(TAG, "unRegister listerner:" + mOnNewBookArrivedListener);
                sContentTv.append("->" + "unRegister listerner:" + mOnNewBookArrivedListener + "\n");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        unbindService(mConnection);
        super.onDestroy();
    }

    private Handler sHandler = new MyHandler();

    private static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.i(TAG, "receive new book:" + msg.obj);
                    sContentTv.append("receive new book->" + msg.obj + "\n");
                    break;
                case LOG_INFO:
                    sContentTv.append("log info->" + msg.obj + "\n");
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * 第一步：新建ServiceConnection对象；
     * 注：onServiceConnected和onServiceDisconnected方法运行在UI线程中，
     * 不可以在这两个方法中执行耗*时操作；
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            sContentTv.append("log info-> connect service success"  + "\n");


            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            try {
                /**
                 * 第三步：连接服务成功，通过IBinder对象获取aidl接口对应的Binder类，然后根据方法，获取对应的数据；
                 * ，这里的数据处理工作如果比较耗时，要在子线程中执行
                 */
                mRemoteBookManager = bookManager;

                TaskPool.runTask(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            List<Book> list = mRemoteBookManager.getBookList();
                            if (list == null || list.isEmpty()) {
                                sHandler.obtainMessage(LOG_INFO, "query book list is empty!").sendToTarget();
                            } else {
                                sHandler.obtainMessage(LOG_INFO, "query book list:" + list.toString()).sendToTarget();
                            }
                            Log.i(TAG, "query book list:" + list.toString());


                            Book newBook = new Book(3, "Android 开发艺术探索");
                            mRemoteBookManager.addBook(newBook);
                            Log.i(TAG, "add Book:" + newBook.toString());
                            sHandler.obtainMessage(LOG_INFO, "add Book:" + newBook.toString()).sendToTarget();


                            List<Book> newList = mRemoteBookManager.getBookList();
                            Log.i(TAG, "query book newList:" + newList.toString());
                            sHandler.obtainMessage(LOG_INFO, "query book newList:" + newList.toString()).sendToTarget();

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }


                    }
                });

                /*第四步：注册监听到服务端，当服务端有新书时，通知客户端；*/
                /*第五步：设置DeathRecipient监听*/
                bookManager.registerListener(mOnNewBookArrivedListener);
                bookManager.asBinder().linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                Log.e(TAG, "log info-> exception after connected service:  " + e.getMessage());
                sContentTv.append("log info-> exception after connected service:" + e.getMessage() + "\n");

            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteBookManager = null;
            Log.i(TAG, "binder disconnected");
            sContentTv.append("log info-> binder disconnected" + "\n");
        }
    };


    /**
     * IOnNewBookArrivedListener服务端提供给客户端，用于接收添加新书的提醒信息；首先客户端注册监听到服务端，当服务端*
     * 新书时，回调onNewBookArrived(Book newBook)
     * 方法，通知客户端有新书啦！
     * <p>
     * 注：IOnNewBookArrivedListener.Stub继承自android.os.Binder，
     * 所有的aidl接口类的方法都运行在客户端的Binder的线程池中，故不能在方法中执行UI操作，需要借助Handler切换到UI线程；
     */
    private IOnNewBookArrivedListener mOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {

        /**
         * @param newBook 服务端添加的新书信息
         */
        @Override
        public void onNewBookArrived(Book newBook) {
            sHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook).sendToTarget();
        }

    };

    /**
     * 运行在Binder的线程池中
     * DeathRecipient监听，当Binder死亡时，会收到binderDied方法的回调，然后在binderDied方法中重新连接服务。
     */
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            /*移除客户端之前注册的监听，否则可能会一直收到Binder死亡的通知*/
            mRemoteBookManager.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mRemoteBookManager = null;
            sHandler.obtainMessage(LOG_INFO, "reconnect service").sendToTarget();
            connectBinderService();
        }
    };


}
