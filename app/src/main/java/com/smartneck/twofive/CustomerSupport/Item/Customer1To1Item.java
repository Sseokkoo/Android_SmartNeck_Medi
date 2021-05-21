package com.smartneck.twofive.CustomerSupport.Item;

public class Customer1To1Item {

    String state;
    String contents;
    String date;

    public Customer1To1Item(String state, String contents, String date) {
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
