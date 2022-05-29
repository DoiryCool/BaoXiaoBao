package com.doiry.baoxiaobao.ui;

import static android.content.ContentValues.TAG;

import static com.doiry.baoxiaobao.utils.configs.BASE_URL;
import static com.doiry.baoxiaobao.utils.configs.PORT;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.MediaStore;
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

import com.doiry.baoxiaobao.MainActivity;
import com.doiry.baoxiaobao.R;
import com.doiry.baoxiaobao.adapter.BindedListviewAdapter;
import com.doiry.baoxiaobao.beans.BindedListviewBeans;
import com.doiry.baoxiaobao.utils.DBUtil;
import com.doiry.baoxiaobao.utils.HttpAssist;
import com.doiry.baoxiaobao.utils.RealPathFromUriUtils;
import com.mysql.jdbc.Connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommitWorksheetActivity extends AppCompatActivity {

    ImageView btn_image = null;
    ImageView show_image = null;
    TextView showFilePwd = null;
    EditText des = null;
    Button chooseFile = null;
    Button uploadFile = null;
    Spinner spinner_commit_binded = null;
    File file;
    String filePath;
    public int seletedNum = 0;
    private String token;
    private int type;

    private SharedPreferences sp;
    public static final String PREFERENCE_NAME = "SaveSetting";
    public static int MODE = Context.MODE_ENABLE_WRITE_AHEAD_LOGGING;
    List<String> teacher_uid = new ArrayList<>();
    final Handler handler = new Handler();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.commit_worksheet);

        ActivityCompat.requestPermissions(CommitWorksheetActivity.this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        btn_image = (ImageView) findViewById(R.id.commmitBack);
        show_image = (ImageView) findViewById(R.id.iv_show_image);
        spinner_commit_binded = (Spinner) findViewById(R.id.commmitBindedSpinner);
        chooseFile = (Button) findViewById(R.id.btn_choose_file);
        uploadFile = (Button) findViewById(R.id.btn_upload_commit);
        showFilePwd = (TextView) findViewById(R.id.tv_show_choose_file);
        des = (EditText) findViewById(R.id.et_description);
        des.setFocusedByDefault(false);
        init();
    }

    @SuppressLint("WrongConstant")
    public void init() {
        sp = this.getSharedPreferences(PREFERENCE_NAME, MODE);
        token = sp.getString("TOKEN", "");
        type = sp.getInt("USER_TYPE", 1);

        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommitWorksheetActivity.this, MainActivity.class);
                CommitWorksheetActivity.this.startActivity(intent);
                finish();
            }
        });

        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 0);
            }
        });

        spinner_commit_binded.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seletedNum = i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFileUtil.sendFile(token, file, des.getText().toString(), teacher_uid.get(seletedNum), new sendFileUtil.sendFileCallback() {
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
                            spinner_commit_binded.setAdapter(adapter);
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
            filePath = realPathFromUri;
            file = new File(filePath);
            showFilePwd.setText(file.getName());
            show_image.setImageURI(uri);
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
    public static void sendFile(String token, File file, String desc, String t_id, final sendFileUtil.sendFileCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        RequestBody fileBody = RequestBody.create(MEDIA_TYPE_PNG, file);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "testImage.jpg", fileBody)
                .addFormDataPart("token", token)
                .addFormDataPart("remark", desc)
                .addFormDataPart("t_id", t_id)
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