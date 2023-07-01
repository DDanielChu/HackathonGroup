package com.example.apptester;

import android.view.View;

import java.util.ArrayList;

public class Data {

    public static String interpretTruthRating(int truthRating) {
        return switch (truthRating) {
            case -1 -> "There is not enough data to verify this fact. Your fact will be sent " +
                    "to the community polls instead.";
            case 0 -> "Likely false";  //false
            case 1 -> "Half true";     //half-true
            case 2 -> "Likely true";   //mostly true
            case 3 -> "Likely true";   //true
            case 4 -> "Likely false";  //barely true
            case 5 -> "Likely false";  //pants on fire
            default -> "ERROR";
        };
    }

    public static String name = "No Name";
    public static ArrayList<Statement> statements = new ArrayList<>();
    public static ArrayList<PollStatement> pollStatements = new ArrayList<>();
    public static int credits = 0;
    public static int votingRankNum = 1;

    public static String getVotingRank() {
        return switch (votingRankNum) {
            case 0 -> "Banned from voting";
            case 1 -> "Curious Voter";
            case 2 -> "Seasoned Voter";
            case 3 -> "Expert Voter";
            default -> "ERROR";
        };
    }

}
