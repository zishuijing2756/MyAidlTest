package com.nana.bookserver.aidl;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Description:服务端BookService
 *
 * @author yangnana
 * @version 1.3.2
 * @since 1.3.2
 */
public class BookManagerService extends Service {

    private static final String TAG = "BMS";

    private AtomicBoolean mIsServiceDestory = new AtomicBoolean(false);

    private static CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();

    /**
     * 1、RemoteCallbackList系统提供专门用于删除跨进程listener的接口。
     * 2、客户端进程结束后，能够自动移除客户端所注册的listener；
     * 3、它内部实现了线程同步功能（在注册和解注册时，不需要做额外的线程同步工作）
     */
    private static RemoteCallbackList<IOnNewBookArrivedListener> mListernerList = new
            RemoteCallbackList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "Book 1"));
        mBookList.add(new Book(2, "Book 2"));

        new Thread(new ServiceWork()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int check = checkCallingOrSelfPermission("com.nana.bookserver.permission.ACCESS_BOOK_SERVER");
        if (check == PackageManager.PERMISSION_DENIED) {
            return null;
        }
        return mBinder;
    }

    /**
     * 注：由于服务端的方法本身就运行在服务端的Binder线程池中，服役服务端方法本身就可以执行大量的耗时操作，所以切记不要在服务端的方法中开线程执行异步任务；
     */
    private Binder mBinder = new IBookManager.Stub() {

        @Override
        public List<Book> getBookList() {
            return mBookList;
        }

        @Override
        public boolean addBook(Book book) {
            return mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {

            mListernerList.register(listener);
            Log.i(TAG, "register listener , current size->:" + mListernerList.getRegisteredCallbackCount());
        }

        @Override
        public void unRegisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListernerList.unregister(listener);
            Log.i(TAG, "unregister listener , current size->:" + mListernerList.getRegisteredCallbackCount());

        }

        /**
         * 服务端加入权限验证，只允许指定的包名的应用可以调用远程服务接口
         * @param code
         * @param data
         * @param reply
         * @param flags
         * @return
         * @throws RemoteException
         */
        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int check = checkCallingOrSelfPermission("com.nana.bookserver.permission.ACCESS_BOOK_SERVER");
            if (check == PackageManager.PERMISSION_DENIED) {
                return false;
            }
            String packageName = null;
            String[] packages = getPackageManager().getPackagesForUid(getCallingUid());
            if (packages != null && packages.length > 0) {
                packageName = packages[0];
            }
            if (!packageName.startsWith("com.nana")) {
                return false;
            }
            return super.onTransact(code, data, reply, flags);
        }
    };

    private void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
        /**
         * beginBroadcast和finishBroadcast必须配对使用，即使是获取元素个数，也要配对使用；
         */
        final int N = mListernerList.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener l = mListernerList.getBroadcastItem(i);
            if (l != null) {
                l.onNewBookArrived(book);
            }
        }
        mListernerList.finishBroadcast();
    }


    private class ServiceWork implements Runnable {

        @Override
        public void run() {

            while (!mIsServiceDestory.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId, "new Book#" + bookId);

                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
