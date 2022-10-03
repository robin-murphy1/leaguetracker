package com.span.devtest.services;

import com.span.devtest.services.data.FileDataCapturer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TrackerProcessingTest {

    List<String> providedLeagueData;
    List<String> providedLeagueOutputData;

    @BeforeAll
    public void loadTestData() {

        FileDataCapturer fileDataCapturer = new FileDataCapturer();

        Path resource = Paths.get("src", "test", "resources", "data", "inputdata-1.txt");
        providedLeagueData = fileDataCapturer.captureData(resource.toAbsolutePath().toString());

        resource = Paths.get("src", "test", "resources", "data", "outputdata.txt");
        providedLeagueOutputData = fileDataCapturer.captureData(resource.toAbsolutePath().toString());

    }

    @Test
    public void givenProvidedInputData_whenAllResultsAreProcessed_thenOutputMatchesProvidedOutput() {
        TrackerProcessing trackerProcessing = new TrackerProcessing();
        Map<String, Integer> allocatedPoints = trackerProcessing.allocateMatchPoints(providedLeagueData, new HashMap<>());
        List<String> outputResults = trackerProcessing.displayTeamsByRankingsOrder(trackerProcessing.getSortedTeamPoints(allocatedPoints));
        assertEquals(providedLeagueOutputData, outputResults);
    }

    @Test
    public void givenMatchResults_whenTeamAIsWinner_thenAllocateCorrectPoints() {
        TrackerProcessing trackerProcessing = new TrackerProcessing();
        List<String> matchList = new ArrayList<String>(Arrays.asList("Tarantulas 1, FC Awesome 0"));
        Map<String, Integer> allocatedPoints = trackerProcessing.allocateMatchPoints(matchList, new HashMap<>());
        assertEquals(3, allocatedPoints.get("Tarantulas"));
        assertEquals(0, allocatedPoints.get("FC Awesome"));
    }

    @Test
    public void givenMatchResults_whenTeamBIsWinner_thenAllocateCorrectPoints() {
        TrackerProcessing trackerProcessing = new TrackerProcessing();
        List<String> matchList = new ArrayList<String>(Arrays.asList("Tarantulas 0, FC Awesome 1"));
        Map<String, Integer> allocatedPoints = trackerProcessing.allocateMatchPoints(matchList, new HashMap<>());
        assertEquals(3, allocatedPoints.get("FC Awesome"));
        assertEquals(0, allocatedPoints.get("Tarantulas"));
    }

    @Test
    public void givenMatchResults_whenMatchIsDraw_thenAllocateCorrectPoints() {
        TrackerProcessing trackerProcessing = new TrackerProcessing();
        List<String> matchList = new ArrayList<String>(Arrays.asList("Tarantulas 1, FC Awesome 1"));
        Map<String, Integer> allocatedPoints = trackerProcessing.allocateMatchPoints(matchList, new HashMap<>());
        assertEquals(1, allocatedPoints.get("FC Awesome"));
        assertEquals(1, allocatedPoints.get("Tarantulas"));

    }

    @Test
    public void givenMultipleMatchResults_whenTeamHasPlayedMultipleGames_thenAggregateTeamsPoints() {
        HashMap<String, Integer> leagueTracker = new HashMap<>();
        TrackerProcessing trackerProcessing = new TrackerProcessing();

        trackerProcessing.allocateMatchPoints(providedLeagueData, leagueTracker);

        assertEquals(6, leagueTracker.get("Tarantulas"));
        assertEquals(1, leagueTracker.get("Snakes"));
    }

    @Test
    public void givenMapWithLeaguePoints_MapIsUnsorted_thenAssertPointsAreSortedDesc() {

        Map<String, Integer> leaguePoints = new HashMap<>();
        leaguePoints.put("Lions", 2);
        leaguePoints.put("Snakes", 2);
        leaguePoints.put("Campers", 2);
        leaguePoints.put("Mambas", 2);
        leaguePoints.put("Xanadu", 2);
        leaguePoints.put("Tarantulas", 5);
        leaguePoints.put("FC Awesome", 8);
        leaguePoints.put("Grouches", 7);
        leaguePoints.put("Hurricanes", 1);
        leaguePoints.put("Fish Eagles", 0);
        leaguePoints.put("woodpeckers", 0);

        TrackerProcessing trackerProcessing = new TrackerProcessing();
        Map<String, Integer> sortedRankings = trackerProcessing.getSortedTeamPoints(leaguePoints);

        //sorted input values to compare to returned values
        List<Integer> sortedList = leaguePoints.values()
                .stream()
                .sorted((i1, i2) -> i2.compareTo(i1))
                .collect(toList());

        List<Integer> returnedSortedRankings = sortedRankings.values()
                .stream()
                .collect(toList());

        assertEquals(sortedList, returnedSortedRankings);
    }

    @Test
    public void givenMapWithLeaguePoints_TeamPointsAreEqual_thenMapSortedInAlphabeticalOrder() {

        Map<String, Integer> leaguePoints = new HashMap<>();
        leaguePoints.put("Lions", 2);
        leaguePoints.put("Snakes", 2);
        leaguePoints.put("Campers", 2);

        TrackerProcessing trackerProcessing = new TrackerProcessing();
        Map<String, Integer> sortedRankings = trackerProcessing.getSortedTeamPoints(leaguePoints);

        //sorted input values to compare to returned values
        List<String> keyList = leaguePoints.keySet()
                .stream()
                .sorted(Comparator.comparing(n -> n))
                .collect(toList());

        List<String> returnedSortedRankings = sortedRankings.keySet()
                .stream()
                .collect(toList());

        assertEquals(keyList, returnedSortedRankings);
    }

    @Test
    public void givenSortedMapWithLeaguePoints_whenTeamsWithDifferrentRankingScore_thenMsgRankFieldsAreCorreectlyPopulated() {

        Map<String, Integer> leagueRankings = new HashMap<>();
        leagueRankings.put("Snakes", 4);
        leagueRankings.put("Lions", 3);
        leagueRankings.put("Campers", 2);

        TrackerProcessing trackerProcessing = new TrackerProcessing();
        Map<String, Integer> sortedRankings = trackerProcessing.getSortedTeamPoints(leagueRankings);

        List<String> rankingsMessageList = trackerProcessing.displayTeamsByRankingsOrder(sortedRankings);
        assertTrue(rankingsMessageList.get(0).contains("Snakes") && rankingsMessageList.get(0).startsWith("1"));

    }

    @Test
    public void givenSortedMapWithLeaguePoints_whenTeamsPointsIsOne_thenMsgPTFieldIsSingular() {

        Map<String, Integer> leagueRankings = new HashMap<>();
        leagueRankings.put("Snakes", 4);
        leagueRankings.put("Lions", 1);

        TrackerProcessing trackerProcessing = new TrackerProcessing();
        Map<String, Integer> sortedRankings = trackerProcessing.getSortedTeamPoints(leagueRankings);

        List<String> rankingsMessageList = trackerProcessing.displayTeamsByRankingsOrder(sortedRankings);
        String[] splitMessage = rankingsMessageList.get(1).split(" ");
        assertTrue(splitMessage[3].equals("pt"));

    }

    @Test
    public void givenSortedMapWithLeaguePoints_whenTeamsPointsIsNotOne_thenMsgPTFieldIsPlural() {

        Map<String, Integer> leagueRankings = new HashMap<>();
        leagueRankings.put("Snakes", 4);
        leagueRankings.put("Lions", 1);

        TrackerProcessing trackerProcessing = new TrackerProcessing();
        Map<String, Integer> sortedRankings = trackerProcessing.getSortedTeamPoints(leagueRankings);

        List<String> rankingsMessageList = trackerProcessing.displayTeamsByRankingsOrder(sortedRankings);
        String[] splitMessage = rankingsMessageList.get(0).split(" ");
        assertTrue(splitMessage[3].equals("pts"));

    }

    @Test
    public void givenSortedMapWithLeaguePoints_whenTeamsPointsAreEqual_thenMsgRankingsAreSame() {

        Map<String, Integer> leagueRankings = new HashMap<>();
        leagueRankings.put("Snakes", 2);
        leagueRankings.put("Lions", 2);
        leagueRankings.put("Campers", 2);
        leagueRankings.put("Tarantulas", 1);

        TrackerProcessing trackerProcessing = new TrackerProcessing();
        Map<String, Integer> sortedRankings = trackerProcessing.getSortedTeamPoints(leagueRankings);

        List<String> rankingsMessageList = trackerProcessing.displayTeamsByRankingsOrder(sortedRankings);
        assertTrue(rankingsMessageList.get(0).startsWith("1")
                && rankingsMessageList.get(1).startsWith("1")
                && rankingsMessageList.get(2).startsWith("1")
                && rankingsMessageList.get(3).startsWith("2")
        );

    }

}