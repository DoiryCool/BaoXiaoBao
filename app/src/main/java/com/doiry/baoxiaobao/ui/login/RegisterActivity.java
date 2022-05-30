package com.doiry.baoxiaobao.ui.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.doiry.baoxiaobao.R;
import com.doiry.baoxiaobao.interact.RegisterInteract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The type Register activity.
 */
public class RegisterActivity extends AppCompatActivity {

    private Button mRegisterButton = null;
    private Button mBackButton = null;
    private EditText mPhoneEditText = null;
    private EditText mNameEditText = null;
    private EditText mPasswordEditText = null;
    private EditText mEmailEditText = null;
    private EditText mInviteEditText = null;
    private EditText mIdEditText = null;
    private TableLayout mTableLayout = null;
    private TableRow mTableRow = null;
    private Spinner mIdentitySpinner = null;

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
        TextView mTeacherIdView = new TextView(this);
        mIdEditText = new EditText(this);
        View mLineView = new View(this);
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

        widgetListener();
    }

    /**
     * Widget listener.
     */
    public void widgetListener() {
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                register();
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

    /**
     * Register.
     */
    public void register(){
        String mPhoneString = mPhoneEditText.getText().toString();
        String mNameString = mNameEditText.getText().toString();
        String mPasswordString = mPasswordEditText.getText().toString();
        String mEmailString = mEmailEditText.getText().toString();
        String mIdentityString = mIdentitySpinner.getSelectedItem().toString();
        String mInviteString = mInviteEditText.getText().toString();
        String mIdString = mIdEditText.getText().toString();

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
        if(mPasswordString.length() < 8 ) {
            Toast.makeText(getApplicationContext(),"密码不能小于8位",Toast.LENGTH_SHORT).show();
            return;
        }
        interactWithJS(mPhoneString,
                mNameString,
                mPasswordString,
                mEmailString,
                mIdentityString,
                mInviteString,
                mIdString);
    }

    /**
     * Interact with js.
     *
     * @param phone    the phone
     * @param name     the name
     * @param password the password
     * @param email    the email
     * @param identity the identity
     * @param invite   the invite
     * @param id       the id
     */
    public void interactWithJS(String phone, String name, String password, String email, String identity, String invite, String id) {
        RegisterInteract registerInteract = new RegisterInteract();
        registerInteract.regToWeb(phone, name, password, email, identity, invite, id, new RegisterInteract.registerCallback() {
            @Override
            public void onSuccess(String result) {
                String msg = null;
                int code = -1;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.optInt("code");
                    msg = jsonObject.optString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code == 0){
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                    @SuppressLint("WrongConstant")
                    SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("Name", mNameEditText.getText().toString());
                    editor.putString("Password", mPasswordEditText.getText().toString());
                    editor.commit();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    Looper.loop();
                } else {
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        });
    }
}