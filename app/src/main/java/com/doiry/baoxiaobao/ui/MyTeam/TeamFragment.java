package com.doiry.baoxiaobao.ui.MyTeam;

import static com.doiry.baoxiaobao.utils.configs.BASE_URL;
import static com.doiry.baoxiaobao.utils.configs.PORT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.doiry.baoxiaobao.R;
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
    private int token;

    private SharedPreferences sp;
    public static final String PREFERENCE_NAME = "SaveSetting";
    public static int MODE = Context.MODE_ENABLE_WRITE_AHEAD_LOGGING;
    final Handler handler = new Handler();

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
        token = sp.getInt("token", 1);
        getInfoUtil.getInfo(token, new getInfoUtil.getInfoCallback() {
            @SuppressLint("ResourceType")
            @Override
            public void onSuccess(String result) {
                try {
                    int code = -1;
                    JSONArray jsonArray = new JSONArray(result);

                    List<String> teachers = new ArrayList<>();

                    int j=1;
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject partDaily = jsonArray.getJSONObject(i);
                        String getName= partDaily.getString("name");
                        teachers.add(getName);
                    }

                    @SuppressLint("ResourceType") ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),
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
    }
}


class getInfoUtil {
    public static void getInfo(int token, final com.doiry.baoxiaobao.ui.MyTeam.getInfoUtil.getInfoCallback callback) {
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