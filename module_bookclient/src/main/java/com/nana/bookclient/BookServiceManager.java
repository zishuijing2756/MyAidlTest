package com.nana.bookclient;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.nana.bookserver.aidl.IBookManager;

/**
 * Description:
 *
 * @author yangnana
 * @version 1.3.2
 * @since 1.3.2
 */
public class BookServiceManager {

    private static final String TAG = "BookServiceManager";
    private Context mContext;
    private IBookManager mBookManager;
    private static BookServiceManager mInstance;

    private BookServiceManager(Context mContext) {
        this.mContext = mContext;
    }

    public static BookServiceManager getmInstance(Context context) {

        if (mInstance == null) {
            mInstance = new BookServiceManager(context);
        }
        return mInstance;
    }


    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mBookManager == null) {
                return;
            }
            mBookManager.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mBookManager = null;
            //TODO 重新绑定远程服务
            bindService();
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                mBookManager = IBookManager.Stub.asInterface(service);
                service.linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBookManager = null;
        }
    };




    /**
     * 绑定服务
     *
     * @return
     */
    private boolean bindService() {
        if (mBookManager == null) {
            Intent intent = new Intent();
            intent.setAction("com.nana.bookserver.aidl.BookManagerService");
            intent.setPackage("com.nana.bookserver");
            boolean mIsBound = mContext.bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
            if (!mIsBound) {
                Log.d(TAG, ":remote进程尚未启动，IPC服务绑定失败！");
                try {
                    // 依据bindService的文档介绍，绑定失败时依旧需要及时释放连接
                    mContext.unbindService(serviceConnection);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            } else {
                Log.d(TAG, ":remote进程IPC服务绑定成功！");
            }
        }
        return true;
    }

    public IBookManager getBookService() {
        while (mBookManager == null) {
            bindService();
        }
        return mBookManager;
    }

}

