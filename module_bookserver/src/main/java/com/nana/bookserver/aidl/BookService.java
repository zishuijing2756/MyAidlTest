package com.nana.bookserver.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.nana.bookserver.aidl.Book;
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

    private static ArrayList<Book> bookList = new ArrayList<Book>();

    @Override
    public void onCreate() {
        super.onCreate();
        Book book = new Book(12345, "Book 1");
        bookList.add(book);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    private IBookManager.Stub stub = new IBookManager.Stub() {

        @Override
        public List<Book> getBookList() {
            return bookList;
        }

        @Override
        public boolean addBook(Book book) {
            return bookList.add(book);
        }
    };

}
