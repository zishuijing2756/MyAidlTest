// IBookManager.aidl
package com.nana.bookserver;

// Declare any non-default types here with import statements
import com.nana.bookserver.Book;

interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

            List<Book> getBookList();
            boolean addBook(in Book book);
}
