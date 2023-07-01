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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import com.example.apptester.databinding.FragmentSecondBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private ArrayList<View[]> pollList = new ArrayList<>();

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

        int initialLength = 0;
        for (PollStatement statement : Data.pollStatements) {
            if (!statement.isTest())
                initialLength++;
        }

        for (int i = 0; i < initialLength; i++) {
            int rng = (int) (Math.random() * 2);

            if (rng == 0) {
                int statementIndex = (int) (Math.random() * Data.statements.size());
                Statement newStatement = Data.statements.get(statementIndex);
                Data.pollStatements.add(new PollStatement(newStatement.getStatement(), true,
                        newStatement.getAuthenticityRating()));
                addNewPoll(newStatement.getStatement());
            }

            addNewPoll(Data.pollStatements.get(i).getStatement());
        }

        binding.submitButton.setOnClickListener(this::submitVotes);
    }

    private void submitVotes(View view) {
        int addedCredits = 0;

        for (int i = 0; i < pollList.size(); i++) {
            PollStatement pollStatement = Data.pollStatements.get(i);
            View[] viewArr = pollList.get(i);
            String fact = ((TextView) viewArr[0]).getText().toString();

            //Real is selected: 3
            if (((RadioButton) viewArr[1]).isChecked()) {
                addedCredits += getPointsEarned(3, fact, pollStatement.getCorrectAns(), 1);
                pollStatement.addNumVotes(Data.votingRankNum, 0);
            }

            //Fake is selected: 0
            else if (((RadioButton) viewArr[2]).isChecked()) {
                addedCredits += getPointsEarned(0, fact, pollStatement.getCorrectAns(), 2);
                pollStatement.addNumVotes(Data.votingRankNum, 1);
            }

            //Half true is selected: 1
            else if (((RadioButton) viewArr[3]).isChecked()) {
                addedCredits += getPointsEarned(1, fact, pollStatement.getCorrectAns(), 3);
                pollStatement.addNumVotes(Data.votingRankNum, 2);
            }

            else continue;

            if (pollStatement.getNumVotes() >= 3) {
                int majorityIndex = 1;
                int currentMax = 0;
                for (int j = 0; j < pollStatement.votes.length; j++) {
                    if (pollStatement.votes[j] > currentMax) {
                        currentMax = pollStatement.votes[j];
                        majorityIndex = j;
                    }
                }

                switch (majorityIndex) {
                    case 0 -> writeToData(2, pollStatement.getStatement());
                    case 1 -> writeToData(0, pollStatement.getStatement());
                    case 2 -> writeToData(1, pollStatement.getStatement());
                }
            }

            //Refresh views and update credits
            updateCredits(view, addedCredits);
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void updateCredits(View view, int addedCredits) {
        Data.credits += addedCredits;

        int newRankNum;
        if (Data.credits >= 6)
            newRankNum = 3;
        else if (Data.credits >= 3)
            newRankNum = 2;
        else if (Data.credits >= 0)
            newRankNum = 1;
        else
            newRankNum = 0;

        if (newRankNum > Data.votingRankNum) {
            Data.votingRankNum = newRankNum;
            Snackbar.make(view, "Thanks for your votes! You earned " + addedCredits + " credits, and " +
                            "you ranked up to " + Data.getVotingRank() + "!", Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.bottomNavigationView)
                    .setAction("Votes1", null).show();
        }
        else if (newRankNum < Data.votingRankNum) {
            Data.votingRankNum = newRankNum;
            Snackbar.make(view, "Thanks for your votes! You lost " + addedCredits + " credits due " +
                            "to incorrect votes, and " +
                            "you ranked down to " + Data.getVotingRank() + ".", Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.bottomNavigationView)
                    .setAction("Votes2", null).show();
        }
        else if (addedCredits >= 0) {
            Snackbar.make(view, "Thanks for your votes! You earned " + addedCredits + " credits.",
                            Snackbar.LENGTH_SHORT)
                    .setAnchorView(R.id.bottomNavigationView)
                    .setAction("Votes3", null).show();
        }
        else {
            Snackbar.make(view, "Thanks for your votes! You lost " + addedCredits + " credits due " +
                            "to incorrect votes.", Snackbar.LENGTH_SHORT)
                    .setAnchorView(R.id.bottomNavigationView)
                    .setAction("Votes4", null).show();
        }
    }

    private void addNewPoll(String fact) {
        View[] views = new View[4];

        views[0] = addFactTextView(fact);
        addRadioButtons(views);

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

    private RadioGroup addRadioButtons(View[] views) {
        RadioButton realButton = new RadioButton(this.getContext());
        realButton.setText("Real");
        realButton.setGravity(Gravity.CENTER_HORIZONTAL);
        views[1] = realButton;

        RadioButton fakeButton = new RadioButton(this.getContext());
        fakeButton.setText("Fake");
        fakeButton.setGravity(Gravity.CENTER_HORIZONTAL);
        views[2] = fakeButton;

        RadioButton halfTrueButton = new RadioButton(this.getContext());
        halfTrueButton.setText("Half true");
        halfTrueButton.setGravity(Gravity.CENTER_HORIZONTAL);
        views[3] = halfTrueButton;

        RadioGroup group = new RadioGroup(this.getContext());
        group.addView(realButton);
        group.addView(fakeButton);
        group.addView(halfTrueButton);
        binding.linearLayout.addView(group);

        return group;
    }

    private int getPointsEarned(int truthRating, String fact, int correctAns, int selectedView) {
        //No correct ans; this is a normal poll question
        if (correctAns == -3) {
            return 1;
        }

        //There is a correct ans; this is a test question
        //True
        else if (correctAns == 2 || correctAns == 3) {
            if (selectedView == 1)
                return 1;
            return -1;
        }
        //False
        else if (correctAns == 0 || correctAns == 4 || correctAns == 5) {
            if (selectedView == 2)
                return 1;
            return -1;
        }
        //Half true
        else {
            if (selectedView == 3)
                return 1;
            return -1;
        }
    }

    private void writeToData(int truthRating, String fact) {
        //Lists
        Data.statements.add(new Statement(fact, truthRating));

        //File
        try {
            System.out.println("added to file");
            PrintWriter writer = new PrintWriter(new FileWriter("LiarDataSet.txt", true));
            writer.println(truthRating + "?!," + fact + "?!,");
            writer.close();
        } catch (IOException e) {
            System.err.println("FILE WRITE ERROR: " + e.getMessage());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}