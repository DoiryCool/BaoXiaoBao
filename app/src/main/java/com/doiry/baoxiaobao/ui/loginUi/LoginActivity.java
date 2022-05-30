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

import com.doiry.baoxiaobao.ui.MainActivity;
import com.doiry.baoxiaobao.R;
import com.doiry.baoxiaobao.utils.LoginUtil;

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
    private String mPhoneString;
    private String mPasswordString;

    private EditText mUserName = null;
    private EditText mPassword = null;
    private CheckBox mRememberPassword = null;
    private CheckBox mAutoLogin = null;
    private Button mLoginButton = null;
    private Button mRegisterButton = null;

    private SharedPreferences sp;
    public static final String PREFERENCE_NAME = "SaveSetting";
    public static int MODE = Context.MODE_ENABLE_WRITE_AHEAD_LOGGING;

    @SuppressLint("WrongConstant")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_page);
        sp = getSharedPreferences(PREFERENCE_NAME,MODE);

        mUserName = (EditText) findViewById(R.id.tv_login_phone);
        mPassword = (EditText) findViewById(R.id.tv_login_passwd);
        mRememberPassword = (CheckBox) findViewById(R.id.cb_remberPasswd);
        mAutoLogin = (CheckBox) findViewById(R.id.cb_autoLogin);
        mLoginButton = (Button) findViewById(R.id.bt_login);
        mRegisterButton = (Button) findViewById(R.id.bt_register);

        if(sp.getBoolean("ISCHECK", true)) {
            mRememberPassword.setChecked(true);
            mUserName.setText(sp.getString("USER_NAME", ""));
            mPassword.setText(sp.getString("PASSWORD", ""));

            if(sp.getBoolean("AUTO_ISCHECK", true)) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(intent);
                finish();
            }
        }

        mLoginButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mPhoneString = mUserName.getText().toString();
                mPasswordString = mPassword.getText().toString();
                LoginUtil LoginUtil=new LoginUtil();
                LoginUtil.checkAccount(mPhoneString, mPasswordString, new LoginUtil.loginCallback() {
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

                        if(code == 0)//success
                        {
                            Looper.prepare();
                            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                            if(mRememberPassword.isChecked()) {
                                Editor editor = sp.edit();
                                editor.putString("USER_NAME", mPhoneString);
                                editor.putString("PASSWORD", mPasswordString);
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

        mRememberPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (mRememberPassword.isChecked()) {
                    System.out.println("记住密码已选中");
                    sp.edit().putBoolean("ISCHECK", true).commit();
                }else {
                    System.out.println("记住密码没有选中");
                    sp.edit().putBoolean("ISCHECK", false).commit();
                }

            }
        });

        mAutoLogin.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (mAutoLogin.isChecked()) {
                    System.out.println("自动登录已选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();

                } else {
                    System.out.println("自动登录没有选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
                }
            }
        });

        mRegisterButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
