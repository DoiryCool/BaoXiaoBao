package com.doiry.baoxiaobao.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.doiry.baoxiaobao.databinding.FragmentTeamBinding;

public class ProfileFragment extends Fragment {

    private FragmentTeamBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel teamViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentTeamBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}