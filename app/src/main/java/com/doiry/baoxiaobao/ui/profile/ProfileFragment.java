package com.doiry.baoxiaobao.ui.profile;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.doiry.baoxiaobao.databinding.FragmentProfileBinding;
import com.doiry.baoxiaobao.interact.InfoInteract;
import com.doiry.baoxiaobao.ui.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private SharedPreferences sp;
    public static final String PREFERENCE_NAME = "SaveSetting";
    public static int MODE = Context.MODE_ENABLE_WRITE_AHEAD_LOGGING;

    @SuppressLint("WrongConstant")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

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
        binding.telephoneShow.setText(sp.getString("USER_NAME", ""));
        InfoInteract.getProfile(sp.getString("USER_NAME", ""), new InfoInteract.getCallback() {
            @Override
            public void onSuccess(String result) {
                String name = "Default Name";
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    name = jsonObject.optString("name");
                    if(jsonObject.optInt("user_type") == 1) {
                        binding.usertypeShow.setText("Teacher");
                    }else {
                        binding.usertypeShow.setText("Student");
                    }
                    binding.usernameShow.setText(name);
                    binding.refisterTimeShow.setText(jsonObject.optString("created_at").split("T")[0]);
                    Log.d(TAG, "onSuccess: " + jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}