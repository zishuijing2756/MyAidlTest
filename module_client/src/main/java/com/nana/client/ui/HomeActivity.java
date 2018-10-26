package com.nana.client.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nana.client.R;
import com.nana.devkit.BaseActivity;

/**
 * Description:
 *
 * @author yangnana
 * @version 1
 * @since 1
 */
public class HomeActivity extends BaseActivity {

    private static final String TAG = "HomeActivity";
    private Toolbar mToolbar;


    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.module_client_activity_home);
    }

    @Override
    protected void injectViews() {
        initToolbar();

    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.toobar);
        mToolbar.setTitle("进程间通信");
        // mToolbar.setNavigationIcon();
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*设置返回键可用，如果某个页面想隐藏掉返回键比如首页，可以调用*/
        mToolbar.setNavigationIcon(null);
    }

    public void jumpToAidl(View view) {
        Intent intent = new Intent(this, BookManagerActivity.class);
        startActivity(intent);
    }

    public void jumpToMessenger(View view) {
        Intent intent = new Intent(this, MessengerActivity.class);
        startActivity(intent);
    }

    public void jumpToProvider(View view) {
        Intent intent = new Intent(this, ProviderActivity.class);
        startActivity(intent);
    }

    public void jumpToSocket(View view) {
        Intent intent = new Intent(this, TcpClientActivity.class);
        startActivity(intent);
    }
}
