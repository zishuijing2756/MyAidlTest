package com.nana.aidlservice.service;

import android.os.RemoteException;

import com.nana.aidlservice.IServerAidlInterface;

/**
 * Description:IServerAidlInterface的实现类，实现进程间通信接口
 *
 * @author yangnana
 * @version 1.3.2
 * @since 1.3.2
 */
public class ServerAidlInterfaceImpl extends IServerAidlInterface.Stub {
    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

    }

    @Override
    public int getInitNum() throws RemoteException {
        return 0;
    }
}
