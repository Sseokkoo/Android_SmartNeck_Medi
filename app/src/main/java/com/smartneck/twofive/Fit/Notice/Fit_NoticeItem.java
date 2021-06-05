package com.smartneck.twofive.Fit.Notice;

public class Fit_NoticeItem {

    String title;
    String contents;
    int no;
    String date;

    public Fit_NoticeItem(String title, String contents, int no, String date) {
        this.title = title;
        this.contents = contents;
        this.no = no;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public int getNo() {
        return no;
    }

    public String getDate() {
        return date;
    }

    public void setNo(int no) {
        this.no = no;
    }
}
