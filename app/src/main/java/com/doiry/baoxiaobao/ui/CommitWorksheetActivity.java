package com.doiry.baoxiaobao.ui;

import static android.content.ContentValues.TAG;

import static com.doiry.baoxiaobao.utils.configs.BASE_URL;
import static com.doiry.baoxiaobao.utils.configs.PORT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.doiry.baoxiaobao.MainActivity;
import com.doiry.baoxiaobao.R;
import com.doiry.baoxiaobao.adapter.BindedListviewAdapter;
import com.doiry.baoxiaobao.beans.BindedListviewBeans;
import com.doiry.baoxiaobao.ui.loginUi.RegisterActivity;

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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommitWorksheetActivity extends AppCompatActivity {

    ImageView btn_image = null;
    TextView showFilePwd = null;
    Button chooseFile = null;
    Spinner spinner_commit_binded = null;
    private String token;
    private int type;

    private SharedPreferences sp;
    public static final String PREFERENCE_NAME = "SaveSetting";
    public static int MODE = Context.MODE_ENABLE_WRITE_AHEAD_LOGGING;
    final Handler handler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.commit_worksheet);
        btn_image = (ImageView) findViewById(R.id.commmitBack);
        spinner_commit_binded = (Spinner) findViewById(R.id.commmitBindedSpinner);
        chooseFile = (Button) findViewById(R.id.btn_choose_file);
        showFilePwd = (TextView) findViewById(R.id.tv_show_choose_file);

        init();
    }

    @SuppressLint("WrongConstant")
    public void init(){
        sp = this.getSharedPreferences(PREFERENCE_NAME,MODE);
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
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,1);
            }
        });

        getBindedInfoUtil.getInfo(token, type, new getBindedInfoUtil.getBindedInfoCallback() {
            @SuppressLint("ResourceType")
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    List<BindedListviewBeans> bindedListviewBeansList = new ArrayList<BindedListviewBeans>();

                    for (int i= 0 ; i < jsonArray.length(); i++){
                        JSONObject info = jsonArray.getJSONObject(i);
                        bindedListviewBeansList.add(new BindedListviewBeans(
                                info.getString("profile"),
                                info.getString("name"),
                                info.getString("t_id"
                                )
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor.getString(actual_image_column_index);
            File file = new File(img_path);
            showFilePwd.setText(file.getName());
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

class getCommitInfoUtil {
    public static void getProfile(String phs, final com.doiry.baoxiaobao.ui.getCommitInfoUtil.getCommitInfoCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Map m = new HashMap();
        m.put("phone", phs);
        JSONObject jsonObject = new JSONObject(m);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);
        Request request = new Request.Builder()
                .url(BASE_URL + ":" + PORT + "/commitInfo")
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

    public interface getCommitInfoCallback {
        void onSuccess(String result);
    }
}