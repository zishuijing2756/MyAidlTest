package com.nana.bookserver.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.nana.bookserver.Book;
import com.nana.bookserver.IBookManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author yangnana
 * @version 1.3.2
 * @since 1.3.2
 */
public class BookService extends Service {

    private ArrayList<Book> bookList = new ArrayList<Book>();

    @Override
    public void onCreate() {
        super.onCreate();
        Book book = new Book();
        book.setId(12345);
        book.setName("Book 1");
        bookList.add(book);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private IBookManager.Stub stub = new IBookManager.Stub() {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public List<Book> getBookList() {
            return bookList;
        }

        @Override
        public boolean addBook(Book book)  {
            return bookList.add(book);
        }
    };

}
