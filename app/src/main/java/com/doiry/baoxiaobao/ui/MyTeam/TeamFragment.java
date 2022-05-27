package com.doiry.baoxiaobao.ui.MyTeam;

import static android.content.ContentValues.TAG;
import static com.doiry.baoxiaobao.utils.configs.BASE_URL;
import static com.doiry.baoxiaobao.utils.configs.PORT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.doiry.baoxiaobao.R;
import com.doiry.baoxiaobao.adapter.BindedListviewAdapter;
import com.doiry.baoxiaobao.beans.BindedListviewBeans;
import com.doiry.baoxiaobao.databinding.FragmentTeamBinding;
import com.doiry.baoxiaobao.ui.loginUi.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class TeamFragment extends Fragment {

    private FragmentTeamBinding binding;
    private String token;
    public int seletedNum = 0;

    private SharedPreferences sp;
    public static final String PREFERENCE_NAME = "SaveSetting";
    public static int MODE = Context.MODE_ENABLE_WRITE_AHEAD_LOGGING;
    final Handler handler = new Handler();
    List<String> teachers = new ArrayList<>();
    List<String> teacher_uid = new ArrayList<>();

    @SuppressLint("WrongConstant")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TeamViewModel teamViewModel =
                new ViewModelProvider(this).get(TeamViewModel.class);

        sp = getActivity().getSharedPreferences(PREFERENCE_NAME,MODE);
        binding = FragmentTeamBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void init(){
        binding.spinBindTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seletedNum = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        token = sp.getString("token", "");
        getInfoUtil.getInfo(token, new getInfoUtil.getInfoCallback() {
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
                            R.layout.item_spinselect, teachers);
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

        getBindedInfoUtil.getInfo(token, new getBindedInfoUtil.getBindedInfoCallback() {
            @SuppressLint("ResourceType")
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    String[] iconArray = new String[jsonArray.length()];
                    String[] nameArray = new String[jsonArray.length()];
                    String[] numberArray = new String[jsonArray.length()];
                    List<BindedListviewBeans> bindedListviewBeansList = new ArrayList<BindedListviewBeans>();

                    for (int i= 0 ; i < jsonArray.length(); i++){
                        JSONObject info = jsonArray.getJSONObject(i);
                        iconArray[i] = info.getString("profile");
                        nameArray[i] = info.getString("name");
                        numberArray[i] = info.getString("t_id");
                        bindedListviewBeansList.add(new BindedListviewBeans(iconArray[i], nameArray[i], numberArray[i]));
                    }
                    Log.d(TAG, nameArray + "");

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

        binding.btnBindRElation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BindUtil.bindRelation(token, teacher_uid.get(seletedNum), new BindUtil.BindCallback() {
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
    }
}

/**
 * @Description: Send post to web.
 */


class getInfoUtil {
    public static void getInfo(String token, final com.doiry.baoxiaobao.ui.MyTeam.getInfoUtil.getInfoCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Map m = new HashMap();
        m.put("token", token);
        JSONObject jsonObject = new JSONObject(m);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);
        Request request = new Request.Builder()
                .url(BASE_URL + ":" + PORT + "/bindInfo")
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

    public interface getInfoCallback {
        void onSuccess(String result);
    }
}

class getBindedInfoUtil {
    public static void getInfo(String token, final com.doiry.baoxiaobao.ui.MyTeam.getBindedInfoUtil.getBindedInfoCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Map m = new HashMap();
        m.put("token", token);
        JSONObject jsonObject = new JSONObject(m);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);
        Request request = new Request.Builder()
                .url(BASE_URL + ":" + PORT + "/bindedInfo")
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

class BindUtil {
    public static void bindRelation(String token, String uid, final com.doiry.baoxiaobao.ui.MyTeam.BindUtil.BindCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Map m = new HashMap();
        m.put("token", token);
        m.put("t_id", uid);
        JSONObject jsonObject = new JSONObject(m);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);
        Request request = new Request.Builder()
                .url(BASE_URL + ":" + PORT + "/bind")
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

    public interface BindCallback {
        void onSuccess(String result);
    }
}