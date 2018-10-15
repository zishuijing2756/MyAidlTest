package com.nana.bookclient;

import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nana.appbase.BaseActivity;

public class BookClientActivity extends BaseActivity implements View.OnClickListener {

    private BookServiceManager mManager;
    private Button bindServiceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_client);

        initServiceManager();
        initView();
    }

    private void initServiceManager() {
        mManager = BookServiceManager.getmInstance(this);
    }

    private void initView() {
        bindServiceBtn = findViewById(R.id.mtc_book_client_bind_service_btn);
        bindServiceBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.mtc_book_client_bind_service_btn:
                /**绑定服务*/
                Toast.makeText(BookClientActivity.this,"绑定成功",Toast.LENGTH_LONG).show();
                try {
                    if (mManager != null) {
                        mManager.getBookService().getBookList();
                    }
                } catch (RemoteException e) {
                }

                break;
            default:
                break;
        }
    }
}
