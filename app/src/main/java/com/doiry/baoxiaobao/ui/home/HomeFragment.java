package com.doiry.baoxiaobao.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.doiry.baoxiaobao.adapter.BindedListviewAdapter;
import com.doiry.baoxiaobao.adapter.ShowBillListviewAdapter;
import com.doiry.baoxiaobao.beans.BindedListviewBeans;
import com.doiry.baoxiaobao.beans.ShowBillListviewBeans;
import com.doiry.baoxiaobao.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ListView listSheetView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
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

        List<ShowBillListviewBeans> showBillListviewBeans = new ArrayList<ShowBillListviewBeans>();
        showBillListviewBeans = ShowBillListviewBeans.getDefaultList();
        ShowBillListviewAdapter adapter = new ShowBillListviewAdapter(getActivity(), showBillListviewBeans);

        listSheetView.setAdapter(adapter);
        return 0;
    }
}