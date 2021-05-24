package com.smartneck.twofive.Fit.Chart.ChartItem;

public class Fit_ExerciseChartItem {
    int count;
    int totalCount;
    int set;
    int totalSet;
    int stop;
    String date;


    public Fit_ExerciseChartItem(int count, int totalCount, int set, int totalSet, int stop, String date) {
        this.count = count;
        this.totalCount = totalCount;
        this.set = set;
        this.totalSet = totalSet;
        this.stop = stop;
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getSet() {
        return set;
    }

    public int getTotalSet() {
        return totalSet;
    }

    public int getStop() {
        return stop;
    }

    public String getDate() {
        return date;
    }
}

