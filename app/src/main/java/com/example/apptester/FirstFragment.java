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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

        binding.checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int truthID = checkTruth();
                binding.truthText.setText(Data.interpretTruthRating(truthID));
                if (truthID == -1) {
                    Data.factList.add(binding.editTextText.getText().toString());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private int checkTruth() {
//        String filePath = "LiarDataSet.txt";
        String userQuestion = "The economic turnaround started at the end of my term."; // Replace with the user's input

                List<Statement> statements = new ArrayList<>();

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
                            statements.add(new Statement(statement, authenticityRating));
                        }
                    }

                    // Perform text similarity comparison with the user's input
                    JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
                    Statement maxStatement = null;
                    double maxSimilarity = 0;
                    for (Statement statement : statements) {
                        double similarity = jaccardSimilarity.apply(statement.getStatement(), userQuestion);

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

                    if (maxSimilarity < 0.80){
                        System.out.print ("Sorry, that isn't in our data base");
                        return -1;
                    }
                    else{
                        System.out.println("Most similar: " + maxSimilarity);
                        System.out.println("Statement: " + maxStatement.getStatement() );
                        System.out.println("Authenticity Rating: " + maxStatement.getAuthenticityRating());
                        return maxStatement.getAuthenticityRating();

                    }
                } catch (IOException e) {
                    System.err.println("Error reading file: " + e.getMessage());
                }

                return -3;

    }

    static class Statement {
        private String statement;
        private int authenticityRating;

        public Statement(String statement, int authenticityRating) {
            this.statement = statement;
            this.authenticityRating = authenticityRating;
        }

        public String getStatement() {
            return statement;
        }

        public int getAuthenticityRating() {
            return authenticityRating;
        }
    }

}