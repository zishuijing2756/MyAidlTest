package com.nana.bookclient.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nana.bookclient.R;
import com.nana.devkit.BaseActivity;

/**
 * Description: PIC（进程间通信）:ContentProvider实例
 *
 * @author yangnana
 * @version 1
 * @since 1
 */
public class ProviderActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);


        Uri uri = Uri.parse("content://com.nana.bookserver.provider.BookProvider");
        getContentResolver().query(uri, null, null, null, null);
        getContentResolver().query(uri, null, null, null, null);
        getContentResolver().query(uri, null, null, null, null);
    }
}
