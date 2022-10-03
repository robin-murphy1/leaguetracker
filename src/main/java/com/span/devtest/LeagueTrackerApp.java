package com.span.devtest;

import com.span.devtest.services.TrackerProcessing;
import com.span.devtest.services.data.DataCapturer;
import com.span.devtest.services.data.FileDataCapturer;
import com.span.devtest.services.data.InputDataCapturer;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeagueTrackerApp {


    private static final String INPUT_DATA = "I";
    private static final String UPLOAD_FILE = "F";

    public static void main(String[] args) {
        DataCapturer leagueData;
        Console console = System.console();

        if (console == null) {
            System.out.printf("No console available %n");
            return;
        }


        console.printf("League Tracker App - Enter match datas and display ranking of the teams %n " +
                "Enter the match data via console or upload a file: %n " +
                "To enter match data via console, type 'I' or 'F' to provide a data file:  ");

        String inputType = console.readLine().toUpperCase();
        List<String> inputList = new ArrayList<>();

        switch (inputType) {
            case INPUT_DATA:
                leagueData = new InputDataCapturer();
                console.printf("Enter Team scores in the following format: Team A <score>, Team B <score> - eg Fish Eagles 2, Hurricanes 0 %n " +
                        "Hit Return key or type 'exit' when finished entering team scores %n");
                while (true) {
                    String input = console.readLine();
                    if (StringUtils.isEmpty(input) || input.equalsIgnoreCase("exit")) {
                        break;
                    }
                    inputList = leagueData.captureData(input);
                }
                break;
            case UPLOAD_FILE:

                String dataDir = System.getenv("SCAN_DATA_DIR");

                if (StringUtils.isNotEmpty(dataDir)) {
                    console.printf("Data DIR has been set as a ENV var (%s) - enter filename only:  ", dataDir);
                } else {
                    console.printf("Enter full file path:  ");
                }

                String consoleInput;

                while (true) {
                    consoleInput = console.readLine();
                    if (StringUtils.isNotEmpty(consoleInput)) {
                        break;
                    }
                    console.printf("A value must be entered to continue:  ");
                }

                //concat the Data Dir set in ENV if provided else read the full path
                String input = StringUtils.isNotEmpty(dataDir)
                        ? String.format("%s/%s", dataDir, consoleInput)
                        : consoleInput;

                if (StringUtils.isNotEmpty(input)) {
                    leagueData = new FileDataCapturer();
                    inputList = leagueData.captureData(input);
                }
                break;
            default:
                console.printf("Incorrect value entered: %s. Valid input is %s or %s", inputType, INPUT_DATA, UPLOAD_FILE);
        }

        if (ObjectUtils.isEmpty(inputList)) {
            console.printf("No entries to process - exiting %n");
            return;
        }

        console.printf("Thanks, the team scores loaded for processing are: %n");
        inputList.forEach(i -> console.printf(i + "%n"));

        TrackerProcessing trackerProcessing = new TrackerProcessing();

        Map<String, Integer> teamsAllocatedPoints = trackerProcessing.allocateMatchPoints(inputList, new HashMap<>());
        Map<String, Integer> sortedLeagueRankings = trackerProcessing.getSortedTeamPoints(teamsAllocatedPoints);

        console.printf("%n ***********************");
        console.printf("%n *** League Rankings ***");
        console.printf("%n ***********************%n");
        List<String> leagueRankingResults = trackerProcessing.displayTeamsByRankingsOrder(sortedLeagueRankings);

        leagueRankingResults.stream()
                .forEach(t -> console.printf(t + "%n"));
    }


}
