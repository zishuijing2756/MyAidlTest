package com.nana.devkit;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.noober.background.BackgroundLibrary;

/**
 * Description:Activity 基类
 *
 * @author yangnana
 * @version 1.3.2
 * @since 1.3.2
 */
public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BackgroundLibrary.inject(this);
        super.onCreate(savedInstanceState);
        hideBottomUIMenu();
        injectMembers();
        injectContentView();
        create(savedInstanceState);
    }

    protected void create(Bundle savedInstanceState) {

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        injectViews();
    }

    private void hideBottomUIMenu() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 隐藏虚拟按键，并且全屏，隐藏system bars
        try {
            // lower api
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB &&
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                View v = getWindow().getDecorView();
                v.setSystemUiVisibility(View.GONE);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //for new api versions.
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        // 隐藏status bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        /*
                         * 隐藏system bars，提供沉浸体验
                         * 确保SYSTEM_UI_FLAG_HIDE_NAVIGATION不会在用户交互的时候被强制清除，
                         * 确保SYSTEM_UI_FLAG_FULLSCREEN不会在用户从屏幕上方往下滑动的时候被强制清除
                         */
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                decorView.setSystemUiVisibility(uiOptions);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    protected void injectMembers() {
        // do nothing
    }

    protected void injectContentView() {
        // do nothing
    }

    protected void injectViews() {
        // do nothing
    }

}
