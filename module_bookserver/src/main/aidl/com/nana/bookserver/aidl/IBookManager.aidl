// IBookManager.aidl
package com.nana.bookserver.aidl;

// Declare any non-default types here with import statements
import com.nana.bookserver.aidl.Book;
import com.nana.bookserver.aidl.IOnNewBookArrivedListener;

interface IBookManager {

        /**
         * 注：在aidl中能够使用的List只有ArrayList，但是我们可以使用CopyOnWriteArrayList，因为AIDL所支持的是抽象的List，而List
         * 只是一个接口，因此虽然服务端返回的是CopyOnWriteArrayList，但是在Binder中会按照List的规范去访问数据并最终形成一个新的ArrayList传给客户端。
         **/
        List<Book> getBookList();

        boolean addBook(in Book book);

        void registerListener(IOnNewBookArrivedListener listener);

        void unRegisterListener(IOnNewBookArrivedListener listener);

}
