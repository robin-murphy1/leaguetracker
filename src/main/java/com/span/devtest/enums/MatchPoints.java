package com.span.devtest.enums;

public enum MatchPoints {

    MATCH_WIN(3),
    MATCH_DRAW(1),
    MATCH_LOSE(0);

    private final int value;

    MatchPoints(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
