package com.doiry.baoxiaobao.ui.home;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.doiry.baoxiaobao.R;
import com.doiry.baoxiaobao.adapter.BindedListviewAdapter;
import com.doiry.baoxiaobao.adapter.ShowBillListviewAdapter;
import com.doiry.baoxiaobao.beans.BindedListviewBeans;
import com.doiry.baoxiaobao.beans.ShowBillListviewBeans;
import com.doiry.baoxiaobao.databinding.FragmentHomeBinding;
import com.doiry.baoxiaobao.interact.InfoInteract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ListView listSheetView;

    String token = null;

    final Handler handler = new Handler();
    public static final String PREFERENCE_NAME = "SaveSetting";
    public static int MODE = Context.MODE_ENABLE_WRITE_AHEAD_LOGGING;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        @SuppressLint("WrongConstant") SharedPreferences sp = requireActivity().getSharedPreferences(PREFERENCE_NAME,MODE);
        token = sp.getString("TOKEN", "");

        initView();

        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public int initView(){
        listSheetView = binding.lvSheetlist;
        listSheetView.setFocusable(false);

        InfoInteract.getBills(token, new InfoInteract.getCallback() {
            @SuppressLint("ResourceType")
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    List<ShowBillListviewBeans> showBillListviewBeans = new ArrayList<ShowBillListviewBeans>();
                    for (int i= 0 ; i < jsonArray.length(); i++){
                        JSONObject info = jsonArray.getJSONObject(i);
                        showBillListviewBeans.add(new ShowBillListviewBeans(
                                info.getString("profile"),
                                info.getString("name"),
                                new Float(info.getDouble("amount")),
                                info.getString("remark")));
                    }
                    ShowBillListviewAdapter adapter = new ShowBillListviewAdapter(getActivity(), showBillListviewBeans);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listSheetView.setAdapter(adapter);

                            }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return 0;
    }
}