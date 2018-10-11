package com.nana.aidlservice.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Description: 服务端提供给客户端的服务
 *
 * @author yangnana
 * @version 1.3.2
 * @since 1.3.2
 */
public class AidlService extends Service {

    private ServerAidlInterfaceImpl process;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
