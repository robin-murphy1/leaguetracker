package com.span.devtest.domain;

public class TeamData implements Comparable<TeamData> {

    private String teamName;
    private Integer matchScore;

    public TeamData(String teamName, Integer matchScore) {
        this.teamName = teamName;
        this.matchScore = matchScore;
    }

    @Override
    public int compareTo(TeamData o) {
        return this.matchScore.compareTo(o.matchScore);
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(Integer matchScore) {
        this.matchScore = matchScore;
    }
}
