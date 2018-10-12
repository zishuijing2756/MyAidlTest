package com.nana.aidlservice.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.nana.aidlservice.IServerAidlInterface;

/**
 * Description: 服务端提供给客户端的服务
 *
 * @author yangnana
 * @version 1.3.2
 * @since 1.3.2
 */
public class AidlService extends Service {
    private static final String TAG="AidlService";

    private IServerAidlInterface.Stub stub=new IServerAidlInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public int getInitNum() throws RemoteException {
            return 0;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if(stub!=null){
           return stub;
        }
        return null;
    }

    public int getInitNum(){
        if(stub!=null){
            try {
                return stub.getInitNum();
            } catch (RemoteException e) {
                Log.i(TAG,e.getMessage());
            }
        }
        return 0;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
