package com.example.snapets.view.menu.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.snapets.R;
import com.example.snapets.view.ExpoList;

import java.util.List;

public class HomeFragmenty extends Fragment implements ExpoList {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        @SuppressLint("ResourceType")
        View root = inflater.inflate(R.menu.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        return root;
    }


    @Override
    public void showList(List list, boolean success) {

    }
}