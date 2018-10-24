package com.nana.bookclient.ui;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nana.bookclient.R;
import com.nana.bookserver.socket.TCPServerService;
import com.nana.devkit.BaseActivity;

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

    private TextView mMessageTV;
    private EditText mMessageET;
    private Button mSendBtn;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_RECEIVE_NEW_MSG:
                    mMessageTV.append(msg.obj.toString());
                    break;
                case MESSAGE_SOCKET_CONNECTED:
                    mSendBtn.setEnabled(true);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);


    }

    @Override
    protected void injectContentView() {
        setContentView(R.layout.activity_tcp_client);
    }

    @Override
    protected void injectViews() {
        mMessageTV = findViewById(R.id.mtc_tcp_client_msg_tv);
        mMessageET = findViewById(R.id.mtc_tcp_client_send_msg_et);
        mSendBtn = findViewById(R.id.mtc_tcp_client_send_btn);
        mSendBtn.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, TCPServerService.class);
        startService(intent);
        new Thread() {
            @Override
            public void run() {
                connectTCPServer();
            }
        }.start();
    }

    private void connectTCPServer() {

        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket("localhost", 8688);
                mClientSocket = socket;
                mPrintWrite = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                mHandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED);
                Log.i(TAG, "connect server success");
            } catch (IOException e) {
                SystemClock.sleep(1000);
                Log.e(TAG, e.getMessage());
                Log.e(TAG, "connect tcp server failed,retry...");
            }
        }

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!TcpClientActivity.this.isFinishing()) {
                String msg = br.readLine();
                Log.i(TAG, "receive: " + msg);
                if (msg != null) {
                    mHandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG, "server " + formatDateTime(System.currentTimeMillis
                            ()) + ": " + msg + "\n");
                }
            }

            Log.i(TAG, "quit...");
            mPrintWrite.close();
            br.close();
            socket.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.mtc_book_client_bind_service_btn:
                final String msg = mMessageET.getText().toString();
                if (!TextUtils.isEmpty(msg) && mPrintWrite != null) {
                    mPrintWrite.println(msg);
                    mMessageET.setText("");
                    mMessageTV.append("self " + formatDateTime(System.currentTimeMillis()) + ":" + msg + "\n");
                }
                break;
            default:
                break;
        }

    }


    @Override
    protected void onPause() {
        if (mClientSocket != null) {
            try {
                mClientSocket.shutdownInput();
                mClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("NewApi")
    private String formatDateTime(long time) {
        return new SimpleDateFormat("HH:mm:ss").format(new Date(time));
    }
}
