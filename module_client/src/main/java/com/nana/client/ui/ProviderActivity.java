package com.nana.client.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.nana.client.R;
import com.nana.server.aidl.Book;
import com.nana.server.provider.User;
import com.nana.devkit.BaseActivity;

/**
 * Description: PIC（进程间通信）:ContentProvider实例
 *
 * @author yangnana
 * @version 1
 * @since 1
 */
public class ProviderActivity extends BaseActivity {

    private static final String TAG = "ProviderActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);



        Uri bookUri = Uri.parse("content://com.nana.server.provider.BookProvider/book");
        ContentValues values = new ContentValues();
        values.put("_id", 6);
        values.put("name", "Android编程艺术");
        getContentResolver().insert(bookUri, values);

        Cursor bookCursor = getContentResolver().query(bookUri, new String[]{"_id", "name"}, null, null, null);
        while (bookCursor.moveToNext()) {
            Book book = new Book();
            book.id = bookCursor.getInt(0);
            book.name = bookCursor.getString(1);
            Log.i(TAG, "query book:" + book.toString());

        }
        bookCursor.close();

        Uri userUri = Uri.parse("content://com.nana.server.provider.BookProvider/user");

        Cursor userCursor = getContentResolver().query(userUri, new String[]{"_id", "name", "sex"}, null, null, null);
        while (userCursor.moveToNext()) {
            User user = new User();
            user.userId = userCursor.getInt(0);
            user.userName = userCursor.getString(1);
            user.isMale = userCursor.getInt(2) == 1;

            Log.i(TAG, "query user:" + user.toString());

        }
        userCursor.close();

    }
}
