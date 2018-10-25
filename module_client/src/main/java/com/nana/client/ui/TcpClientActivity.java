package com.nana.client.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nana.client.R;
import com.nana.server.socket.TCPServerService;
import com.nana.devkit.BaseActivity;
import com.nana.devkit.optimize.TaskPool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * Description:IPC :socket实例客户端
 *
 * @author yangnana
 * @version 1
 * @since 1
 */
public class TcpClientActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "TcpClientActivity";

    private static final int MESSAGE_RECEIVE_NEW_MSG = 1;
    private static final int MESSAGE_SOCKET_CONNECTED = 2;

    private PrintWriter mPrintWrite;
    private Socket mClientSocket;

    private static TextView sMessageTV;
    private static EditText sMessageET;
    private static TextView sSendBtn;

    private Handler mHandler = new MyHandler();


    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_tcp_client);
        initView();
        Intent intent = new Intent(this, TCPServerService.class);
        startService(intent);


        TaskPool.runTask(new Runnable() {
            @Override
            public void run() {
                connectTCPServer();
            }
        });
    }


    private void initView() {
        sMessageTV = findViewById(R.id.mtc_tcp_client_msg_tv);
        sMessageET = findViewById(R.id.mtc_tcp_client_send_msg_et);
        sSendBtn = findViewById(R.id.mtc_tcp_client_send_btn);
        sSendBtn.setOnClickListener(this);
    }


    private void connectTCPServer() {

        while (mClientSocket == null) {
            try {
                mClientSocket = new Socket("localhost", 8688);
                mPrintWrite = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mClientSocket.getOutputStream())), true);
                mHandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED);
                Log.i(TAG, "connect server success");
            } catch (IOException e) {
                SystemClock.sleep(1000);
                Log.e(TAG, e.getMessage());
                Log.e(TAG, "connect tcp server failed,retry...");
            }
        }

        try {
            /*读取服务端发送来的消息*/
            BufferedReader br = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));

            while (!TcpClientActivity.this.isFinishing()) {
                String msg = br.readLine();
                Log.i(TAG, "receive: " + msg);
                if (!TextUtils.isEmpty(msg)) {

                    Message obj = new Message();
                    obj.what = MESSAGE_RECEIVE_NEW_MSG;
                    obj.obj = "receive from server :" + msg + "\n";

                    mHandler.sendMessage(obj);
                }

            }

            Log.i(TAG, "disconnect server");
            mPrintWrite.close();
            br.close();
            mClientSocket.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.mtc_tcp_client_send_btn:
                final String msg = sMessageET.getText().toString();
                if (!TextUtils.isEmpty(msg) && mPrintWrite != null) {
                    sMessageET.setText("");
                    sMessageTV.append("self " + formatDateTime(System.currentTimeMillis()) + ":" + msg + "\n");

                    TaskPool.runTask(new Runnable() {
                        @Override
                        public void run() {
                            mPrintWrite.println(msg);

                        }
                    });
                }
                break;
            default:
                break;
        }

    }


    @Override
    protected void onDestroy() {
        if (mClientSocket != null) {
            try {
                mClientSocket.shutdownInput();
                mClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    @SuppressLint("NewApi")
    private String formatDateTime(long time) {
        return new SimpleDateFormat("HH:mm:ss").format(new Date(time));
    }


    private static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_RECEIVE_NEW_MSG:
                    sMessageTV.append(msg.obj.toString());
                    break;
                case MESSAGE_SOCKET_CONNECTED:
                    sSendBtn.setEnabled(true);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }


}
