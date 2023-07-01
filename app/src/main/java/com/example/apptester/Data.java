package com.example.apptester;

import java.util.ArrayList;

public class Data {

    public static String interpretTruthRating(int truthRating) {
        switch (truthRating) {
            case -1: return "There is not enough data to verify this fact. Your fact will be sent " +
                    "\nto the community polls instead.";
            case 0: return "Likely false";  //false
            case 1: return "Half true";     //half-true
            case 2: return "Likely true";   //mostly true
            case 3: return "Likely true";   //true
            case 4: return "Likely false";  //barely true
            case 5: return "Likely false";  //pants on fire
        }
        return "ERROR";
    }

    public static String name = "No Name";
    public static ArrayList<String> factList = new ArrayList<>();
    public static int tokenCount = 0;
    public static int votingRankNum = 1;

    public static String getVotingRank() {
        switch (votingRankNum) {
            case 0: return "Banned from voting";
            case 1: return "Curious Voter";
            case 2: return "Seasoned Voter";
            case 3: return "Expert Voter";
        }
        return "ERROR";
    }

}
