package com.nana.bookclient;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.nana.bookserver.IBookManager;

/**
 * Description:
 *
 * @author yangnana
 * @version 1.3.2
 * @since 1.3.2
 */
public class BookServiceManager {

    private Context mContext;
    private IBookManager mService;
    private static BookServiceManager mInstance;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IBookManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    private BookServiceManager(Context mContext) {
        this.mContext = mContext;
    }

    public static BookServiceManager getmInstance(Context context) {

        if (mInstance == null) {
            mInstance = new BookServiceManager(context);
        }
        return mInstance;
    }

    public IBookManager getBookService() {
        while (mService == null) {
            connectService();
        }
        return mService;
    }


    public boolean connectService() {
        if (mService == null) {
            Intent intent = new Intent();
            intent.setAction("com.nana.bookserver.service.BookService");
            intent.setPackage("com.nana.bookserver");
            mContext.bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
        }
        return true;
    }

}

