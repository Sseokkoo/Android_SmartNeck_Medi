package com.smartneck.twofive.Review;

public class ReviewItem {
    String name;
    int no;
    String contents;
    String date;


    public ReviewItem(String id, int no, String contents, String date) {
        this.name = id;
        this.no = no;
        this.contents = contents;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public int getNo() {
        return no;
    }



    public String getContents() {
        return contents;
    }

    public String getDate() {
        return date;
    }
}
