package com.nana.bookserver.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Description:IPC（进程间通信）：ContentProvider-》ContentProvider底层实现Binder，只不过系统已经封装好，使用起来比较方便；
 * 注：1、除了onCreate由系统回调并运行在UI线程中，其它方法均由外界回调并运行在Binder线程中；
 * 2、ContentProvider支持表格数据和文件数据；
 * 3、处理文件数据，可以在ContentProvider中返回文件的句柄给外界，从而让外界来访问ContentProvider中的文件信息；
 * 4、Android系统提供的MediaStore，就是文件类型的ContentProvider；
 * 5、ContentProvider对底层数据的存储方式没有任何要求，可以是SQLite数据库、文件存储、或者是内存中的一个对象来进行数据存储；
 * 6、注册BookProvider
 *
 * @author yangnana
 * @version 1.3.2
 * @since 1.3.2
 */
public class BookProvider extends ContentProvider {

    private static final String TAG = "BookProvider";

    /**
     * ContentProvider的创建，一般做一些初始化操作
     * 运行在UI线程中
     *
     * @return
     */
    @Override
    public boolean onCreate() {
        Log.i(TAG, "onCreate ,current thread:" + Thread.currentThread().getName());
        return false;
    }

    /**
     * 运行在Binder线程池中
     *
     * @param uri
     * @return 返回一个Uri请求所对应的MIME类型
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.i(TAG, "getType ,current thread:"+ Thread.currentThread().getName());

        return null;
    }

    /**
     * 运行在Binder线程池中
     *
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.i(TAG, "query ,current thread:"+ Thread.currentThread().getName());

        return null;
    }


    /**
     * 运行在Binder线程池中
     *
     * @param uri
     * @param values
     * @return
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.i(TAG, "insert ,current thread:"+ Thread.currentThread().getName());

        return null;
    }

    /**
     * 运行在Binder线程池中
     *
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.i(TAG, "delete ,current thread:"+ Thread.currentThread().getName());

        return 0;
    }

    /**
     * 运行在Binder线程池中
     *
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.i(TAG, "");

        return 0;
    }
}
