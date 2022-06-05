package com.doiry.baoxiaobao.ui;

import static android.content.ContentValues.TAG;

import static com.doiry.baoxiaobao.utils.configs.BASE_URL;
import static com.doiry.baoxiaobao.utils.configs.PORT;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.doiry.baoxiaobao.R;
import com.doiry.baoxiaobao.adapter.BindedListviewAdapter;
import com.doiry.baoxiaobao.beans.BindedListviewBeans;
import com.doiry.baoxiaobao.interact.InfoInteract;
import com.doiry.baoxiaobao.interact.SendFileInteract;
import com.doiry.baoxiaobao.ui.login.LoginActivity;
import com.doiry.baoxiaobao.utils.RealPathFromUriUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommitWorksheetActivity extends AppCompatActivity {

    private ImageView mBackImage = null;
    private ImageView mshowImage = null;
    private TextView mFilePwdShow = null;
    private EditText mRemarkEdit = null;
    private EditText mAmountEdit = null;
    private Button mChooseFileButton = null;
    private Button mUploadFileButton = null;
    private Spinner mBindedSpinner = null;
    private File mFile = null;
    private String mFilePath = null;
    public int seletedNum = 0;
    private int type;
    private String token;
    private String uid;

    public static final String PREFERENCE_NAME = "SaveSetting";
    public static int MODE = Context.MODE_ENABLE_WRITE_AHEAD_LOGGING;
    private List<String> teacher_uid = new ArrayList<>();
    final Handler handler = new Handler();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.commit_worksheet);

        ActivityCompat.requestPermissions(CommitWorksheetActivity.this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        mBackImage = findViewById(R.id.commmitBack);
        mshowImage = findViewById(R.id.iv_show_image);
        mBindedSpinner = findViewById(R.id.commmitBindedSpinner);
        mChooseFileButton = findViewById(R.id.btn_choose_file);
        mUploadFileButton = findViewById(R.id.btn_upload_commit);
        mFilePwdShow = findViewById(R.id.tv_show_choose_file);
        mRemarkEdit = findViewById(R.id.et_description);
        mAmountEdit = findViewById(R.id.ev_amount);
        mRemarkEdit.setFocusedByDefault(false);
        init();
    }

    @SuppressLint("WrongConstant")
    public void init() {
        SharedPreferences sp = this.getSharedPreferences(PREFERENCE_NAME, MODE);
        token = sp.getString("TOKEN", "");
        type = sp.getInt("USER_TYPE", 1);
        uid = sp.getString("uid", "");

        mBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommitWorksheetActivity.this, MainActivity.class);
                CommitWorksheetActivity.this.startActivity(intent);
                finish();
            }
        });

        mChooseFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 0);
            }
        });

        mBindedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seletedNum = i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mUploadFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendFileInteract.sendFile(uid, mFile, mRemarkEdit.getText().toString(), teacher_uid.get(seletedNum), mAmountEdit.getText().toString(), new SendFileInteract.sendFileCallback() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onSuccess(String result) {
                        Looper.prepare();
                        Toast.makeText(CommitWorksheetActivity.this, "Upload Successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CommitWorksheetActivity.this,MainActivity.class);
                        CommitWorksheetActivity.this.startActivity(intent);
                        finish();
                        Looper.loop();
                    }
                });
            }
        });

        InfoInteract.getbindedInfo(token, type, new InfoInteract.getCallback() {
            @SuppressLint("ResourceType")
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    List<BindedListviewBeans> bindedListviewBeansList = new ArrayList<BindedListviewBeans>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject partDaily = jsonArray.getJSONObject(i);
                        String getUid= partDaily.getString("uid");
                        teacher_uid.add(getUid);

                        JSONObject info = jsonArray.getJSONObject(i);
                        bindedListviewBeansList.add(new BindedListviewBeans(
                                info.getString("profile"),
                                info.getString("name"),
                                info.getString("t_id")
                        ));
                    }
                    BindedListviewAdapter adapter = new BindedListviewAdapter(CommitWorksheetActivity.this, bindedListviewBeansList);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mBindedSpinner.setAdapter(adapter);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }
        if (requestCode == 0 && resultCode == -1) {
            Uri uri = data.getData();
            mFilePath = RealPathFromUriUtils.getRealPathFromUri(this, data.getData());
            mFile = new File(mFilePath);
            mFilePwdShow.setText(mFile.getName());
            if(mFile.getName().endsWith(".jpg")){
                mshowImage.setImageURI(uri);
            }
        }
    }
}