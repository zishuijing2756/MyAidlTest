package com.nana.client.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.nana.client.R;
import com.nana.devkit.BaseActivity;
import com.nana.server.aidl.Book;
import com.nana.server.provider.User;

/**
 * Description: PIC（进程间通信）:ContentProvider实例
 *
 * @author yangnana
 * @version 1
 * @since 1
 */
public class ProviderActivity extends BaseActivity {

    private static final String TAG = "ProviderActivity";
    private TextView mContent;


    @Override
    protected void create(Bundle savedInstanceState) {
        Uri bookUri = Uri.parse("content://com.nana.server.provider.BookProvider/book");
        ContentValues values = new ContentValues();
        values.put("_id", 6);
        values.put("name", "Android编程艺术");
        /*插入数据*/
        getContentResolver().insert(bookUri, values);

        /*查询数据*/
        Cursor bookCursor = getContentResolver().query(bookUri, new String[]{"_id", "name"}, null, null, null);
        while (bookCursor.moveToNext()) {
            Book book = new Book();
            book.id = bookCursor.getInt(0);
            book.name = bookCursor.getString(1);
            Log.i(TAG, "query book:" + book.toString());
            mContent.append("query book:" + book.toString() + "\n");
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
            mContent.append("query user:" + user.toString() + "\n");


        }
        userCursor.close();
    }

    @Override
    protected void injectContentView() {
        setContentView(R.layout.module_client_activity_provider);
    }

    @Override
    protected void injectViews() {
        initToolbar();
        mContent = findViewById(R.id.mtc_client_provider_content_tv);
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.toobar);
        mToolbar.setTitle("进程间通信->AIDL");
        mToolbar.setNavigationIcon(R.drawable.ic_search);
        setSupportActionBar(mToolbar);
    }
}
