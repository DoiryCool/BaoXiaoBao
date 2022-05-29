package com.doiry.baoxiaobao.ui.loginUi;

import static com.doiry.baoxiaobao.utils.configs.BASE_URL;
import static com.doiry.baoxiaobao.utils.configs.PORT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.doiry.baoxiaobao.MainActivity;
import com.doiry.baoxiaobao.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends Activity {

    private EditText userPhone, password;
    private CheckBox rem_pw, auto_login;
    private Button btn_login, btn_register;
    private String userPhoneValue,passwordValue;
    private SharedPreferences sp;
    public static final String PREFERENCE_NAME = "SaveSetting";
    public static int MODE = Context.MODE_ENABLE_WRITE_AHEAD_LOGGING;

    @SuppressLint("WrongConstant")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_page);

        sp = getSharedPreferences(PREFERENCE_NAME,MODE);

        userPhone = (EditText) findViewById(R.id.tv_login_phone);
        password = (EditText) findViewById(R.id.tv_login_passwd);
        rem_pw = (CheckBox) findViewById(R.id.cb_remberPasswd);
        auto_login = (CheckBox) findViewById(R.id.cb_autoLogin);
        btn_login = (Button) findViewById(R.id.bt_login);
        btn_register = (Button) findViewById(R.id.bt_register);

        if(sp.getBoolean("ISCHECK", true))
        {
            //设置默认是记录密码状态
            rem_pw.setChecked(true);
            userPhone.setText(sp.getString("USER_NAME", ""));
            password.setText(sp.getString("PASSWORD", ""));
            //判断自动登陆多选框状态
            if(sp.getBoolean("AUTO_ISCHECK", false))
            {
                //设置默认是自动登录状态
                auto_login.setChecked(true);
                //跳转界面
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(intent);
                finish();

            }
        }

        btn_login.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                userPhoneValue = userPhone.getText().toString();
                passwordValue = password.getText().toString();
                LoginUtil LoginUtil=new LoginUtil();
                LoginUtil.checkAccount(userPhoneValue, passwordValue, new LoginUtil.loginCallback() {
                    @Override
                    public void onSuccess(String result) {
                        String msg = "";
                        int code = -1;
                        String token = "";
                        int type = 1;
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            code = jsonObject.optInt("code");
                            msg = jsonObject.optString("msg");
                            token = jsonObject.optString("token");
                            type = jsonObject.optInt("user_type");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(code == 0) {
                            Looper.prepare();
                            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                            if(rem_pw.isChecked()) {
                                Editor editor = sp.edit();
                                editor.putString("USER_NAME", userPhoneValue);
                                editor.putString("PASSWORD",passwordValue);
                                editor.putString("TOKEN", token);
                                editor.putInt("USER_TYPE", type);
                                editor.commit();
                            }
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            LoginActivity.this.startActivity(intent);
                            finish();
                            Looper.loop();
                        }else{
                            Looper.prepare();
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                });
            }
        });

        //监听记住密码多选框按钮事件
        rem_pw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (rem_pw.isChecked()) {
                    System.out.println("记住密码已选中");
                    sp.edit().putBoolean("ISCHECK", true).commit();
                }else {
                    System.out.println("记住密码没有选中");
                    sp.edit().putBoolean("ISCHECK", false).commit();
                }

            }
        });

        //监听自动登录多选框事件
        auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (auto_login.isChecked()) {
                    System.out.println("自动登录已选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();

                } else {
                    System.out.println("自动登录没有选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
                }
            }
        });

        btn_register.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }



}

class LoginUtil {
    public void checkAccount(String phs,
                            String ps,
                            final loginCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .callTimeout(120, TimeUnit.SECONDS)
                .pingInterval(5, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS).build();
        Map m = new HashMap();
        m.put("phone", phs);
        m.put("password", ps);
        JSONObject jsonObject = new JSONObject(m);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);
        Request request = new Request.Builder()
                .url(BASE_URL + ":" + PORT + "/userLogin")
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
                call.cancel();
                callback.onSuccess(result);
            }
        });
    }

    public interface loginCallback {
        void onSuccess(String result);
    }
}
