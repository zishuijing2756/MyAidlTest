// IBookManager.aidl
package com.nana.bookserver;

// Declare any non-default types here with import statements
import com.nana.bookserver.aidl.Book;

interface IBookManager {

        List<Book> getBookList();
        boolean addBook(in Book book);

}
