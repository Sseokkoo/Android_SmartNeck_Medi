package com.smartneck.fit.Fit.CustomerSupport.Item;

public class Fit_Customer1To1Item {

    String state;
    String contents;
    String date;

    public Fit_Customer1To1Item(String state, String contents, String date) {
        this.state = state;
        this.contents = contents;
        this.date = date;
    }


    public String getState() {
        return state;
    }

    public String getContents() {
        return contents;
    }

    public String getDate() {
        return date;
    }
}
