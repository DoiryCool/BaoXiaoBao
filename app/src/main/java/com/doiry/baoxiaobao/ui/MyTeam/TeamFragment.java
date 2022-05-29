package com.doiry.baoxiaobao.ui.MyTeam;

import static android.content.ContentValues.TAG;
import static com.doiry.baoxiaobao.utils.configs.BASE_URL;
import static com.doiry.baoxiaobao.utils.configs.PORT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.doiry.baoxiaobao.R;
import com.doiry.baoxiaobao.adapter.BindedListviewAdapter;
import com.doiry.baoxiaobao.beans.BindedListviewBeans;
import com.doiry.baoxiaobao.databinding.FragmentTeamBinding;

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
    private int type;
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
        token = sp.getString("TOKEN", "");
        type = sp.getInt("USER_TYPE", 1);

        binding = FragmentTeamBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init();
        setListener();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setListener() {
        binding.btnBindRElation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == 2){
                    BindUtil.bindRelation(token, teacher_uid.get(seletedNum), new BindUtil.BindCallback() {
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

        getBindedInfoUtil.getInfo(token, type, new getBindedInfoUtil.getBindedInfoCallback() {
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
                        /*
                        iconArray[i] = info.getString("profile");
                        nameArray[i] = info.getString("name");
                        numberArray[i] = info.getString("t_id");
                        */
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
    public static void getInfo(String token, int type, final com.doiry.baoxiaobao.ui.MyTeam.getBindedInfoUtil.getBindedInfoCallback callback) {
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