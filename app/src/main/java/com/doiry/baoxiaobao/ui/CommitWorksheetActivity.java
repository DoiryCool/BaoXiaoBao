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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.doiry.baoxiaobao.R;
import com.doiry.baoxiaobao.adapter.BindedListviewAdapter;
import com.doiry.baoxiaobao.beans.BindedListviewBeans;
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
    private File mFile;
    private String mFilePath;
    public int seletedNum = 0;
    private String token;
    private int type;

    private SharedPreferences sp;
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

        mBackImage = (ImageView) findViewById(R.id.commmitBack);
        mshowImage = (ImageView) findViewById(R.id.iv_show_image);
        mBindedSpinner = (Spinner) findViewById(R.id.commmitBindedSpinner);
        mChooseFileButton = (Button) findViewById(R.id.btn_choose_file);
        mUploadFileButton = (Button) findViewById(R.id.btn_upload_commit);
        mFilePwdShow = (TextView) findViewById(R.id.tv_show_choose_file);
        mRemarkEdit = (EditText) findViewById(R.id.et_description);
        mAmountEdit = (EditText) findViewById(R.id.ev_amount);
        mRemarkEdit.setFocusedByDefault(false);
        init();
    }

    @SuppressLint("WrongConstant")
    public void init() {
        sp = this.getSharedPreferences(PREFERENCE_NAME, MODE);
        token = sp.getString("TOKEN", "");
        type = sp.getInt("USER_TYPE", 1);

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
                sendFileUtil.sendFile(token, mFile, mRemarkEdit.getText().toString(), mAmountEdit.getText().toString(),teacher_uid.get(seletedNum), new sendFileUtil.sendFileCallback() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        getBindedInfoUtil.getInfo(token, type, new getBindedInfoUtil.getBindedInfoCallback() {
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
            String realPathFromUri = RealPathFromUriUtils.getRealPathFromUri(this, data.getData());
            mFilePath = realPathFromUri;
            mFile = new File(mFilePath);
            mFilePwdShow.setText(mFile.getName());
            if(mFile.getName().endsWith(".jpg")){
                mshowImage.setImageURI(uri);
            }
        }
    }
}

class getBindedInfoUtil {
    public static void getInfo(String token, int type, final getBindedInfoUtil.getBindedInfoCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Map m = new HashMap();
        m.put("token", token);
        JSONObject jsonObject = new JSONObject(m);
        String jsonStr = jsonObject.toString();
        String sendPost = "/bindedInfo";
        if(type == 1){
            sendPost = "/bindedInfoT";
        }
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);
        Request request = new Request.Builder()
                .url(BASE_URL + ":" + PORT + sendPost)
                .addHeader("contentType", "application/json;charset=utf-8")
                .post(requestBodyJson)
                .build();
        final Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("onFilure", e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String result = response.body().string();
                callback.onSuccess(result);
            }
        });
    }

    public interface getBindedInfoCallback {
        void onSuccess(String result);
    }
}

class sendFileUtil {
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
    public static void sendFile(String token, File file, String desc, String t_id, String amount, final sendFileUtil.sendFileCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        RequestBody fileBody = RequestBody.create(MEDIA_TYPE_PNG, file);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", null, fileBody)
                .addFormDataPart("token", token)
                .addFormDataPart("remark", desc)
                .addFormDataPart("t_id", t_id)
                .addFormDataPart("amount", amount)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + ":" + PORT + "/image")
                .addHeader("contentType", "application/json;charset=utf-8")
                .post(requestBody)
                .build();
        final Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("onFilure", e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String result = response.body().string();
                callback.onSuccess(result);
            }
        });
    }

    public interface sendFileCallback {
        void onSuccess(String result);
    }
}