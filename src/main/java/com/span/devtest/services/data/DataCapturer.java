package com.span.devtest.services.data;

import java.util.List;

public interface DataCapturer {

    /***
     * Method allows for different sources of data to be captured (ie. file, console, etc)
     * and generate a list of team scores to be processed
     * @param data - depending on input it may be a file path or input captured from screen
     *             The data in a file or however captured must be in the format: Team a <score>, Team B <score>
     * @return - List<String> of entered data
     */
    List<String> captureData(String data);
}
