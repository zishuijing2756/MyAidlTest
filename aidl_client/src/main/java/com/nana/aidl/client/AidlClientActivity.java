package com.nana.aidl.client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.os.IResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class AidlClientActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG="AidlClientActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl_client);

        initView();

    }

    private void initView() {
       findViewById(R.id.bind_service_btn).setOnClickListener(this);
        findViewById(R.id.get_data_tv).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bind_service_btn:

                Intent intent=new Intent();
                intent.setAction("com.nana.aidlservice.service.AidlService");
                bindService(intent, new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        Log.i(TAG,"******服务已连接");

                        IResultReceiver.Stub.asInterface(service);
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        Log.i(TAG,"*******服务已断开");

                    }
                },Activity.BIND_AUTO_CREATE);
                break;
            case R.id.get_data_tv:
                break;
            default:
                break;
        }
    }
}
