package com.span.devtest.services;

import com.span.devtest.domain.TeamData;
import com.span.devtest.enums.MatchPoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class TrackerProcessing {

    private volatile int rank = 0;
    private volatile int previousValue = -1;

    /**
     * Data is passed in as Match line items and points are awarded for winning(3pts) teams and teams that draw(1pt)
     * Team points are aggregated for each match played
     *
     * @param dataList
     * @param pointsTracker
     * @return
     */
    public Map<String, Integer> allocateMatchPoints(final List<String> dataList, final Map<String, Integer> pointsTracker) {

        dataList.forEach(data -> {

            String[] teamLineItem = data.split(",");
            TeamData teamA = new TeamData(
                    teamLineItem[0].strip().substring(0, teamLineItem[0].lastIndexOf(" ")).strip(),
                    Integer.parseInt(teamLineItem[0].strip().substring(teamLineItem[0].lastIndexOf(" ")).strip())

            );
            TeamData teamB = new TeamData(
                    teamLineItem[1].strip().substring(0, teamLineItem[1].lastIndexOf(" ")).strip(),
                    Integer.parseInt(teamLineItem[1].strip().substring(teamLineItem[1].lastIndexOf(" ")).strip())

            );

            if (teamA.compareTo(teamB) == 0) { //match draw
                if (pointsTracker.containsKey(teamA.getTeamName())) {
                    pointsTracker.compute(teamA.getTeamName(), (key, value) -> Integer.sum(value, MatchPoints.MATCH_DRAW.getValue()));
                } else {
                    pointsTracker.put(teamA.getTeamName(), MatchPoints.MATCH_DRAW.getValue());
                }

                if (pointsTracker.containsKey(teamB.getTeamName())) {
                    pointsTracker.compute(teamB.getTeamName(), (key, value) -> Integer.sum(value, MatchPoints.MATCH_DRAW.getValue()));
                } else {
                    pointsTracker.put(teamB.getTeamName(), MatchPoints.MATCH_DRAW.getValue());
                }
            } else if (teamA.compareTo(teamB) > 0) { //match Win
                if (pointsTracker.containsKey(teamA.getTeamName())) {
                    pointsTracker.compute(teamA.getTeamName(), (key, value) -> Integer.sum(value, MatchPoints.MATCH_WIN.getValue()));
                } else {
                    pointsTracker.put(teamA.getTeamName(), MatchPoints.MATCH_WIN.getValue());
                }

                if (pointsTracker.containsKey(teamB.getTeamName())) {
                    pointsTracker.compute(teamB.getTeamName(), (key, value) -> Integer.sum(value, MatchPoints.MATCH_LOSE.getValue()));
                } else {
                    pointsTracker.put(teamB.getTeamName(), MatchPoints.MATCH_LOSE.getValue());
                }
            } else { //match Lose
                if (pointsTracker.containsKey(teamA.getTeamName())) {
                    pointsTracker.compute(teamA.getTeamName(), (key, value) -> Integer.sum(value, MatchPoints.MATCH_LOSE.getValue()));
                } else {
                    pointsTracker.put(teamA.getTeamName(), MatchPoints.MATCH_LOSE.getValue());
                }

                if (pointsTracker.containsKey(teamB.getTeamName())) {
                    pointsTracker.compute(teamB.getTeamName(), (key, value) -> Integer.sum(value, MatchPoints.MATCH_WIN.getValue()));
                } else {
                    pointsTracker.put(teamB.getTeamName(), MatchPoints.MATCH_WIN.getValue());
                }
            }
        });
        return pointsTracker;
    }

    /**
     * Map is sorted, firstly by number of points desc, and then if team points are equal, alphabetically
     *
     * @param leagueMatchPoints
     * @return
     */
    public HashMap<String, Integer> getSortedTeamPoints(final Map<String, Integer> leagueMatchPoints) {

        return leagueMatchPoints.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

    }

    /**
     * Rankings need to be ordered by highest ranking and teams with same points must have same ranking.
     * The format of the displayed message is : eg - "1. Tarantulas, 6 pts"
     * If team has a single point(1) then the "pts" in the message must reflect as "pt"
     *
     * @param sortedLeaguePoints
     * @return
     */
    public List<String> displayTeamsByRankingsOrder(final Map<String, Integer> sortedLeaguePoints) {

        List<String> rankings = new ArrayList<>();
        String rankingsMesage = "%d. %s, %d pt%s";

        sortedLeaguePoints.forEach((key, value) -> {
                    if (value != previousValue || previousValue == -1) {
                        rank++;
                    }
                    rankings.add(String.format(rankingsMesage, rank, key, value, value == 1 ? "" : "s"));
                    previousValue = value;
                }
        );

        return rankings;
    }
}
