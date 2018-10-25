package com.nana.server.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.nana.devkit.optimize.TaskPool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Description:IPC:socket 实例
 *
 * @author yangnana
 * @version 1
 * @since 1
 */
public class TCPServerService extends Service {

    private static final String TAG = "TCPServerService";

    private boolean mIsServiceDestroyed = false;
    private String[] mDefinedMessages = new String[]{
            "哈哈！你好啊！",
            "你叫什么名字",
            "今天天气怎么样？",
            "给你讲个笑话吧！"
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        TaskPool.runTask(new TcpServer());
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        mIsServiceDestroyed = true;
        super.onDestroy();
    }

    private class TcpServer implements Runnable {

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(8688);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                Log.e(TAG, "establish tcp server failed,port 8688");
                return;
            }

            while (!mIsServiceDestroyed) {
                try {
                    final Socket client = serverSocket.accept();
                    Log.i(TAG, "accept");

                    TaskPool.runTask(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    });

                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }


    private void responseClient(Socket client) throws IOException {

        /*用于接收客户端消息*/
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        /*用于向客户端发送消息*/
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);

        out.println("👏来到聊天室");

        while (!mIsServiceDestroyed) {
            String str = in.readLine();
            Log.i(TAG, "message from client:" + str);
            if (str == null) {
                break;
            }

            int i = new Random().nextInt(mDefinedMessages.length);
            String message = mDefinedMessages[i];
            out.println(message);

            Log.i(TAG, "server send to client:" + message);

        }

        Log.i(TAG, "client quit.");

        /*关闭流*/
        out.flush();
        out.close();
        in.close();
        client.close();

    }
}
