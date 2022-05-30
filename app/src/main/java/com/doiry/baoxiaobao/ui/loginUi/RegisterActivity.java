package com.doiry.baoxiaobao.ui.loginUi;

import static android.view.ViewGroup.*;
import static com.doiry.baoxiaobao.utils.configs.BASE_URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.doiry.baoxiaobao.R;
import com.doiry.baoxiaobao.utils.RegisterUtil;

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

public class RegisterActivity extends AppCompatActivity {
    private Button mRegisterButton = null;
    private Button mBackButton = null;

    private EditText mPhoneEditText = null;
    private EditText mNameEditText = null;
    private EditText mPasswordEditText = null;
    private EditText mEmailEditText = null;
    private EditText mInviteEditText = null;
    private TextView mTeacherIdView = null;
    private View mLineView = null;
    private View mParamsView = null;
    private EditText mIdEditText = null;
    private TableLayout mTableLayout = null;
    private TableRow mTableRow = null;
    private Spinner mIdentitySpinner = null;

    private String mPhoneString = null;
    private String mNameString = null;
    private String mPasswordString = null;
    private String mEmailString = null;
    private String mIdentityString = null;
    private String mInviteString = null;
    private String mIdString = null;

    public static final String PREFERENCE_NAME = "SaveSetting";
    public static int MODE = Context.MODE_ENABLE_WRITE_AHEAD_LOGGING;

    private static final String TAG = "RegisterActivity";
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.login_register_page);

        mRegisterButton = (Button) findViewById(R.id.bt_comfirm);
        mBackButton = (Button) findViewById(R.id.bt_back);
        mTableLayout = (TableLayout) findViewById(R.id.tb_layout_register);
        mTableRow = new TableRow(this);
        mTeacherIdView = new TextView(this);
        mIdEditText = new EditText(this);
        mLineView = new View(this);
        mTeacherIdView.setLayoutParams(
                findViewById(R.id.getParamsTextView).getLayoutParams()
        );
        mTeacherIdView.setText("Employee Number:");
        mIdEditText.setLayoutParams(
                findViewById(R.id.tv_phone).getLayoutParams()
        );
        mIdEditText.setHint("Less than 11 bit.");
        mLineView.setLayoutParams(
                findViewById(R.id.getParamsView).getLayoutParams()
        );
        mTableRow.addView(mTeacherIdView);
        mTableRow.addView(mLineView);
        mTableRow.addView(mIdEditText);
        mTableRow.setBackgroundResource(R.drawable.edittext_style_1);

        mPhoneEditText = findViewById(R.id.tv_phone);
        mNameEditText = findViewById(R.id.tv_name);
        mPasswordEditText = findViewById(R.id.tv_reg_passwd);
        mEmailEditText = findViewById(R.id.tv_email);
        mInviteEditText = findViewById(R.id.tv_inviteCode);
        mIdentitySpinner = findViewById(R.id.spi_identity);

        listenButton();
    }

    public void listenButton(){
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                register(v);
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mIdentitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getItemAtPosition(i).toString().equals("Teacher")){
                    mTableLayout.addView(mTableRow);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void register(View v){
        mPhoneString = mPhoneEditText.getText().toString();
        mNameString = mNameEditText.getText().toString();
        mPasswordString = mPasswordEditText.getText().toString();
        mEmailString = mEmailEditText.getText().toString();
        mIdentityString = mIdentitySpinner.getSelectedItem().toString();
        mInviteString = mInviteEditText.getText().toString();
        mIdString = mIdEditText.getText().toString();

        if(mNameString.length() == 0  ) {
            Toast.makeText(getApplicationContext(),"用户名不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(mNameString.length() > 16  ) {
            Toast.makeText(getApplicationContext(),"用户名必须小于16位",Toast.LENGTH_SHORT).show();
            return;
        }
        if(mPasswordString.length() == 0 ) {
            Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(mPasswordString.length() == 0 ) {
            Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        RegisterUtil registerUtil = new RegisterUtil();
        registerUtil.regToWeb(mPhoneString, mNameString, mPasswordString, mEmailString, mIdentityString, mInviteString, mIdString, new RegisterUtil.registerCallback() {
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

                    editor.putString("Name", mNameEditText.getText().toString());
                    editor.putString("Password", mPasswordEditText.getText().toString());
                    editor.commit();//提交
                    Intent intent = new Intent(RegisterActivity.this, com.doiry.baoxiaobao.ui.loginUi.LoginActivity.class);
                    startActivity(intent);
                    finish();
                    Looper.loop();
                }
            }
        });
    }
}