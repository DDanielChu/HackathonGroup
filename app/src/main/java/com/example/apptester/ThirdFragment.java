package com.example.apptester;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apptester.databinding.FragmentThirdBinding;

import java.util.ArrayList;

public class ThirdFragment extends Fragment {

    private FragmentThirdBinding binding;
    private ArrayList<View[]> pollList = new ArrayList<>();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentThirdBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @SuppressLint("DefaultLocale")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.nameText.setText(Data.name);
        binding.tokenText.setText(String.format("%d", Data.tokenCount));
        binding.rankText.setText(Data.getVotingRank());
    }


}