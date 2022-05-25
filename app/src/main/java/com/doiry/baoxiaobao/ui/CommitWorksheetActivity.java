package com.doiry.baoxiaobao.ui;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.doiry.baoxiaobao.R;

public class CommitWorksheetActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.commit_worksheet);

    }
}
