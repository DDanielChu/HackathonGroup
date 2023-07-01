package com.example.apptester;


import org.apache.commons.text.similarity.JaccardSimilarity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.apptester.databinding.FragmentFirstBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        readFromFile();

        binding.checkButton.setOnClickListener(this::checkFact);
    }

    private void readFromFile() {
        try {
            AssetManager assetManager = requireActivity().getAssets();
            InputStream inputStream = assetManager.open("LiarDataSet.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by the "?!" delimiter
                String[] parts = line.split("\\?!,");
                if (parts.length == 2) {
                    // Extract the statement and authenticity rating
                    String statement = parts[1].trim();
                    int authenticityRating = Integer.parseInt(parts[0]);

                    // Create a Statement object and add it to the list
                    Data.statements.add(new Statement(statement, authenticityRating));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void checkFact(View view) {
        String enteredFact = binding.editTextText.getText().toString();

        if (enteredFact.equals("")) {
            Snackbar.make(view, "Please enter a fact before checking.", Snackbar.LENGTH_SHORT)
                    .setAnchorView(R.id.bottomNavigationView)
                    .setAction("Action", null).show();
            return;
        }

        int truthID = checkTruth(enteredFact);
        binding.truthText.setText(Data.interpretTruthRating(truthID));
        if (truthID == -1) {
            Data.pollStatements.add(new PollStatement(enteredFact, false));
        }
    }

    private int checkTruth(String fact) {

        // Perform text similarity comparison with the user's input
        JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
        Statement maxStatement = null;
        double maxSimilarity = 0;
        for (Statement statement : Data.statements) {
            double similarity = jaccardSimilarity.apply(statement.getStatement(), fact);

            if (similarity > maxSimilarity) {
                maxSimilarity = similarity;
                maxStatement = statement;
            }

            // // Print the statement and authenticity rating
            // System.out.println("Statement: " + statement.getStatement());
            // System.out.println("Authenticity Rating: " + statement.getAuthenticityRating());
            // System.out.println("Similarity with User's Question: " + similarity);
            // System.out.println();
        }

        if (maxSimilarity < 0.80)
            return -1;
        else
            return maxStatement.getAuthenticityRating();
    }

}