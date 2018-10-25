package com.nana.client.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toolbar;

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


    @Override
    public void setActionBar(@Nullable Toolbar toolbar) {
        super.setActionBar(toolbar);
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.module_client_activity_home);
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
