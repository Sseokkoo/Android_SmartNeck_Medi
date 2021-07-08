package com.smartneck.fit.Fit.Chart.notimportant;

/**
 * Created by Philipp Jahoda on 07/12/15.
 */
class Fit_ContentItem {

    final String name;
    final String desc;
    boolean isSection = false;

    Fit_ContentItem(String n) {
        name = n;
        desc = "";
        isSection = true;
    }

    Fit_ContentItem(String n, String d) {
        name = n;
        desc = d;
    }
}
