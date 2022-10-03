package com.span.devtest.services.data;

import java.util.ArrayList;
import java.util.List;

public class InputDataCapturer implements DataCapturer {

    private List<String> leagueData = new ArrayList<>();

    @Override
    public List<String> captureData(final String data) {

        leagueData.add(data);
        return leagueData;
    }
}
