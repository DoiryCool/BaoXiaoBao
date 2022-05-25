package com.doiry.baoxiaobao.utils;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.doiry.baoxiaobao.R;

public class baseUtils extends AppCompatActivity {
    public Context mContext;
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    public void showToast(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

}

