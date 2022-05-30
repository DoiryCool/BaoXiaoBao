package com.doiry.baoxiaobao.ui.team;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.doiry.baoxiaobao.R;
import com.doiry.baoxiaobao.adapter.BindedListviewAdapter;
import com.doiry.baoxiaobao.beans.BindedListviewBeans;
import com.doiry.baoxiaobao.databinding.FragmentTeamBinding;
import com.doiry.baoxiaobao.interact.InfoInteract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TeamFragment extends Fragment {

    private FragmentTeamBinding binding;
    private String token;
    private int type;
    private int seletedNum = 0;

    public static final String PREFERENCE_NAME = "SaveSetting";
    public static int MODE = Context.MODE_ENABLE_WRITE_AHEAD_LOGGING;

    final Handler handler = new Handler();
    List<String> teachers = new ArrayList<>();
    List<String> teacher_uid = new ArrayList<>();

    @SuppressLint("WrongConstant")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences sp = requireActivity().getSharedPreferences(PREFERENCE_NAME,MODE);
        token = sp.getString("TOKEN", "");
        type = sp.getInt("USER_TYPE", 1);

        binding = FragmentTeamBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init();
        widgetListener();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void widgetListener() {
        binding.btnBindRElation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == 2){
                    InfoInteract.bindRelation(token, teacher_uid.get(seletedNum), new InfoInteract.getCallback() {
                        @SuppressLint("ResourceType")
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
                            if(code == 0) {
                                Looper.prepare();
                                Toast.makeText(getActivity(), "Bind Success!", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }else{
                                Looper.prepare();
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(getActivity(), "Sorry , Teacher can't bind students now.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        binding.spinBindTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seletedNum = i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    public void init(){
        teachers.clear();
        teacher_uid.clear();

        InfoInteract.getInfo(token, new InfoInteract.getCallback() {
            @SuppressLint("ResourceType")
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject partDaily = jsonArray.getJSONObject(i);
                        String getName= partDaily.getString("name");
                        teachers.add(getName);
                        String getUid= partDaily.getString("uid");
                        teacher_uid.add(getUid);
                    }
                    @SuppressLint("ResourceType") ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                            R.layout.item_spinner_select_teacher, teachers);
                    adapter.setDropDownViewResource(R.layout.item_dialogspinselect);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            binding.spinBindTeacher.setAdapter(adapter);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        InfoInteract.getbindedInfo(token, type, new InfoInteract.getCallback() {
            @SuppressLint("ResourceType")
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    List<BindedListviewBeans> bindedListviewBeansList = new ArrayList<BindedListviewBeans>();

                    for (int i= 0 ; i < jsonArray.length(); i++){
                        JSONObject info = jsonArray.getJSONObject(i);
                        bindedListviewBeansList.add(new BindedListviewBeans(info.getString("profile"), info.getString("name"), info.getString("t_id")));
                    }
                    BindedListviewAdapter adapter = new BindedListviewAdapter(getActivity(), bindedListviewBeansList);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            binding.bindedInfoListview.setAdapter(adapter);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}