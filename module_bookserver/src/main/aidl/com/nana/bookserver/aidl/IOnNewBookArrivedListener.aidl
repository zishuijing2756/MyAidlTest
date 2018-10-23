// IOnNewBookArrivedListener.aidl
package com.nana.bookserver.aidl;

import com.nana.bookserver.aidl.Book;

// Declare any non-default types here with import statements
//
interface IOnNewBookArrivedListener {

    void onNewBookArrived(in Book newBook);
}
