package com.smartneck.twofive.Fit.Chart.custom;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

/**
 * Created by philipp on 02/06/16.
 */
public class Fit_HeightAxisValueFormatter extends ValueFormatter {


    private final BarLineChartBase<?> chart;
    private ArrayList dateArray;
    public Fit_HeightAxisValueFormatter(BarLineChartBase<?> chart , ArrayList dateArray) {
        this.chart = chart;
        this.dateArray = dateArray;
    }

    @Override
    public String getFormattedValue(float value) {


//        if (chart.getVisibleXRange() > 30 * 6) {
//
//        } else {

            int i = (int) value;

            if (i >= dateArray.size()) i = 0;
            if (dateArray.size() == 0){
                return "";
            }
            return String.valueOf(dateArray.get(i));
//        }
//        return "";
    }
}
