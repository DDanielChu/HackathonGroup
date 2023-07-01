package com.example.apptester;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.apptester.databinding.FragmentSecondBinding;

import java.util.ArrayList;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private ArrayList<View[]> pollList = new ArrayList<>();
    private int tokenCount = 0;

    public int getTokenCount() {
        return tokenCount;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        for (String statement : Data.factList) {
            addNewPoll(statement);
        }

        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewPoll("test");
            }
        });
    }

    private void addNewPoll(String fact) {
        View[] views = new View[3];

        views[0] = addFactTextView(fact);
        views[1] = addRadioButtons();

        pollList.add(views);
    }

    private TextView addFactTextView(String fact) {
        TextView factTextView = new TextView(this.getContext());
        factTextView.setText(fact);
        factTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        factTextView.setTextSize(20);
        binding.linearLayout.addView(factTextView);

        return factTextView;
    }

    private RadioGroup addRadioButtons() {
        RadioButton realButton = new RadioButton(this.getContext());
        realButton.setText("Real");
        realButton.setGravity(Gravity.CENTER_HORIZONTAL);

        RadioButton fakeButton = new RadioButton(this.getContext());
        fakeButton.setText("Fake");
        fakeButton.setGravity(Gravity.CENTER_HORIZONTAL);

        RadioGroup group = new RadioGroup(this.getContext());
        group.addView(realButton);
        group.addView(fakeButton);
        binding.linearLayout.addView(group);

        return group;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}