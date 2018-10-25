package com.nana.server.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
 * 7、
 *
 * @author yangnana
 * @version 1
 * @since 1
 */
public class BookProvider extends ContentProvider {

    private static final String TAG = "BookProvider";

    private Context mContext;
    private SQLiteDatabase mDb;

    private static final String AUTHORITY = "com.nana.server.provider.BookProvider";

    /**
     * 指定book的Uri
     */
    private static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");

    /**
     * 指定User的Uri
     */
    private static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");

    /**
     * Uri_Code
     */
    private static final int BOOK_URI_CODE = 0;
    private static final int USER_URI_CODE = 1;

    private static UriMatcher sUriMatcher;

    /**
     * 通过UriMatcher关联Uri和Uri_Code
     */
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE);
        sUriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);
    }

    /**
     * ContentProvider的创建，一般做一些初始化操作
     * 运行在UI线程中
     *
     * @return
     */
    @Override
    public boolean onCreate() {
        Log.i(TAG, "onCreate ,current thread:" + Thread.currentThread().getName());
        mContext = getContext();

        /**ContentProvider创建时，初始化数据库。注：实际使用时，不建议在UI线程中进行耗时的数据库操作*/
        initProviderData();
        return false;
    }

    /**
     * 初始化数据库
     */
    private void initProviderData() {
        mDb = new DbOpenHelper(mContext).getWritableDatabase();
        mDb.execSQL("delete from " + DbOpenHelper.BOOK_TABLE_NAME);
        mDb.execSQL("delete from " + DbOpenHelper.USER_TABLE_NAME);
        mDb.execSQL("insert into book values(3,'Android')");
        mDb.execSQL("insert into book values(4,'IOS')");
        mDb.execSQL("insert into book values(5,'HTML')");

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
        Log.i(TAG, "getType ,current thread:" + Thread.currentThread().getName());

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
        Log.i(TAG, "query ,current thread:" + Thread.currentThread().getName());

        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return mDb.query(table, projection, selection, selectionArgs, null, sortOrder, null);
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
        Log.i(TAG, "insert ,current thread:" + Thread.currentThread().getName());

        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        mDb.insert(table, null, values);
        mContext.getContentResolver().notifyChange(uri, null);

        return uri;
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
        Log.i(TAG, "delete ,current thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int count = mDb.delete(table, selection, selectionArgs);
        if (count > 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }

        return count;
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
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int row = mDb.update(table, values, selection, selectionArgs);
        if (row > 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return row;
    }

    private String getTableName(Uri uri) {
        if (uri == null) {
            return null;
        }
        String tableName = null;
        switch (sUriMatcher.match(uri)) {
            case BOOK_URI_CODE:
                tableName = DbOpenHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
            default:
                break;
        }

        return tableName;

    }


}
