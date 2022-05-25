package com.doiry.baoxiaobao.ui.loginUi;

import static com.doiry.baoxiaobao.utils.configs.BASE_URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.NavigationUI;

import com.doiry.baoxiaobao.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    Button bt_register_comfirm;
    Button bt_register_back;


    EditText phone = null;
    EditText name = null;
    EditText password = null;
    EditText email = null;
    EditText ivtCode = null;
    Spinner identity = null;

    String phonestring = null;
    String namestring = null;
    String passwordstring = null;
    String emailstring = null;
    String identitystring = null;
    String ivtCodeString = null;

    public static final String PREFERENCE_NAME = "SaveSetting";
    public static int MODE = Context.MODE_ENABLE_WRITE_AHEAD_LOGGING;

    private static final String TAG = "RegisterActivity";
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.register_page);
        bt_register_comfirm = (Button) findViewById(R.id.bt_comfirm);
        bt_register_back = (Button) findViewById(R.id.bt_back);

        phone = findViewById(R.id.tv_phone);
        name = findViewById(R.id.tv_name);
        password = findViewById(R.id.tv_reg_passwd);
        email = findViewById(R.id.tv_email);
        ivtCode = findViewById(R.id.tv_inviteCode);
        identity = findViewById(R.id.spi_identity);

        listenButton();
    }

    public void listenButton(){
        bt_register_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                register(v);
            }
        });

        bt_register_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void register(View v){
        phonestring = phone.getText().toString();
        namestring = name.getText().toString();
        passwordstring = password.getText().toString();
        emailstring = email.getText().toString();
        identitystring = identity.getSelectedItem().toString();
        ivtCodeString = ivtCode.getText().toString();

        //判断用户名
        if(namestring.length() == 0  ) {
            Toast.makeText(getApplicationContext(),"用户名不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(namestring.length() > 16  ) {
            Toast.makeText(getApplicationContext(),"用户名必须小于16位",Toast.LENGTH_SHORT).show();
            return;
        }
        //判断密码
        if(passwordstring.length() == 0 ) {
            Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        registerUtil registerUtil = new registerUtil();
        registerUtil.regToWeb(phonestring, namestring, passwordstring, emailstring, identitystring, ivtCodeString, new registerUtil.registerCallback() {
            @Override
            public void onSuccess(String result) {
                String msg = "";
                int code = -1;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.optInt("code");
                    msg = jsonObject.optString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code != 0){
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else if (code == 0){
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                    @SuppressLint("WrongConstant")
                    SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("Name",name.getText().toString());
                    editor.putString("Password",password.getText().toString());
                    editor.commit();//提交
                    Intent intent = new Intent(RegisterActivity.this, com.doiry.baoxiaobao.ui.loginUi.LoginActivity.class);
                    startActivity(intent);
                    Looper.loop();
                }
            }
        });

    }

}

class registerUtil {
    public void regToWeb(String phs,
                        String ns,
                        String ps,
                        String es,
                        String is,
                        String cs,
                        final registerCallback callback){

        OkHttpClient client = new OkHttpClient.Builder().build();
        Map post = new HashMap();
        post.put("phone", phs);
        post.put("name", ns);
        post.put("password", ps);
        post.put("email", es);
        post.put("identity", is);
        post.put("inviteCode", cs);
        JSONObject jsonObject = new JSONObject(post);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);
        Request request = new Request.Builder()
                .url(BASE_URL + ":8000/userRegister")
                .addHeader("contentType", "application/json;charset=utf-8")
                .post(requestBodyJson)
                .build();
        final Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("onFilure", e.getMessage());
            }

            String msg = "UNKNOW";

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String result = response.body().string();
                callback.onSuccess(result);
            }

        });
    }
    public interface registerCallback {
        void onSuccess(String result);
    }

}

