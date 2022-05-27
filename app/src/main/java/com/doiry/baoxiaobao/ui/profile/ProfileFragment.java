package com.doiry.baoxiaobao.ui.profile;

import static com.doiry.baoxiaobao.utils.configs.BASE_URL;
import static com.doiry.baoxiaobao.utils.configs.PORT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.doiry.baoxiaobao.MainActivity;
import com.doiry.baoxiaobao.databinding.FragmentProfileBinding;
import com.doiry.baoxiaobao.ui.loginUi.LoginActivity;

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

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private String userPhoneValue;

    private SharedPreferences sp;
    public static final String PREFERENCE_NAME = "SaveSetting";
    public static int MODE = Context.MODE_ENABLE_WRITE_AHEAD_LOGGING;

    @SuppressLint("WrongConstant")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel teamViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        sp = getActivity().getSharedPreferences(PREFERENCE_NAME,MODE);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void init(){
        userPhoneValue = sp.getString("USER_NAME", "");
        binding.telephoneShow.setText(userPhoneValue);
        getProfileUtil.getProfile(userPhoneValue, new getProfileUtil.getProfileCallback() {
            @Override
            public void onSuccess(String result) {
                String msg = "";
                String name = "Default Name";
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    name = jsonObject.optString("name");
                    if(jsonObject.optInt("type") == 1) {
                        binding.usertypeShow.setText("Teacher");
                    }else {
                        binding.usertypeShow.setText("Student");
                    }
                    binding.usernameShow.setText(name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }
}

class getProfileUtil {
    public static void getProfile(String phs, final getProfileUtil.getProfileCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Map m = new HashMap();
        m.put("phone", phs);
        JSONObject jsonObject = new JSONObject(m);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);
        Request request = new Request.Builder()
                .url(BASE_URL + ":" + PORT + "/userProfile")
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

    public interface getProfileCallback {
        void onSuccess(String result);
    }
}