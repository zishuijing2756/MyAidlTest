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

import com.nana.client.R;
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
    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;
    private IBookManager mRemoteBookManager;

    private Handler sHandler = new Handler(
    ) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.i(TAG, "receive new book:" + msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    /**
     * 注：onServiceConnected和onServiceDisconnected方法运行在UI线程中，不可以在这两个方法中执行耗时操作；
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            try {

                mRemoteBookManager = bookManager;

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            List<Book> list = mRemoteBookManager.getBookList();

                            Log.i(TAG, "*******query book list:" + list.toString());
                            Log.i(TAG, "*******query book list:" + list.toString());

                            Book newBook = new Book(3, "Android 开发艺术探索");

                            mRemoteBookManager.addBook(newBook);

                            Log.i(TAG, "*******add Book:" + newBook.toString());

                            List<Book> newList = mRemoteBookManager.getBookList();
                            Log.i(TAG, "*******query book newList:" + newList.toString());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                    }
                }.start();

                bookManager.registerListener(mOnNewBookArrivedListener);

            } catch (RemoteException e) {
                Log.e(TAG, "*******after connected service--->" + e.getMessage());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            mRemoteBookManager = null;
            Log.i(TAG, "binder died");
        }
    };


    private IOnNewBookArrivedListener mOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {
        /**
         * 运行在客户端的Binder的线程池中，故不能在里面执行UI操作，需要借助Handler切换到UI线程；
         * @param newBook
         * @throws RemoteException
         */
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {

            sHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook).sendToTarget();
        }

    };

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.module_client_activity_bookmanager);
        initView();
    }

    private void initView() {
        findViewById(R.id.mtc_book_client_bind_service_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.mtc_book_client_bind_service_btn:
                /**绑定服务*/
                Log.i(TAG, "*******click on bind service Button");

                bindService();
                break;
            default:
                break;
        }
    }

    private void bindService() {
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        /**
         * 解除已经注册到服务端的listener
         */
        if (mRemoteBookManager != null && mRemoteBookManager.asBinder().isBinderAlive()) {
            try {
                mRemoteBookManager.unRegisterListener(mOnNewBookArrivedListener);
                Log.i(TAG, "unRegister listerner:" + mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        unbindService(mConnection);
        super.onDestroy();
    }

}
