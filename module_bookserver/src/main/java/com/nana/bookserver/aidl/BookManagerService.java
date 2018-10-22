package com.nana.bookserver.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Description:服务端BookService
 *
 * @author yangnana
 * @version 1.3.2
 * @since 1.3.2
 */
public class BookManagerService extends Service {

    private static final String TAG = "BMS";

    private static CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<Book>();

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "Book 1"));
        mBookList.add(new Book(2, "Book 2"));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private Binder mBinder = new IBookManager.Stub() {

        @Override
        public List<Book> getBookList() {
            return mBookList;
        }

        @Override
        public boolean addBook(Book book) {
            return mBookList.add(book);
        }
    };

}
