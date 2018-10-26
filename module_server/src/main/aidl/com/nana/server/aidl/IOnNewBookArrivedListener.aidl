// IOnNewBookArrivedListener.aidl
package com.nana.server.aidl;

import com.nana.server.aidl.Book;

// Declare any non-default types here with import statements
//客户端-接收有新书通知的接口，实现类应该写在客户端
interface IOnNewBookArrivedListener {

    void onNewBookArrived(in Book newBook);
}
