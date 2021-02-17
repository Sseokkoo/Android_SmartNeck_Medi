package com.ko.smartneck.medical.twofive.Chart;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.model.GradientColor;
import com.ko.smartneck.medical.twofive.Chart.ChartItem.ExerciseChartItem;
import com.ko.smartneck.medical.twofive.Chart.custom.HeightAxisValueFormatter;
import com.ko.smartneck.medical.twofive.Chart.custom.MyValueFormatter;
import com.ko.smartneck.medical.twofive.Chart.custom.XYMarkerView;
import com.ko.smartneck.medical.twofive.Chart.notimportant.DemoBase;
import com.ko.smartneck.medical.twofive.R;
import com.ko.smartneck.medical.twofive.util.Address;
import com.ko.smartneck.medical.twofive.util.Constants;
import com.ko.smartneck.medical.twofive.util.HttpConnect;
import com.ko.smartneck.medical.twofive.util.Param;
import com.ko.smartneck.medical.twofive.util.User.Exercise;
import com.ko.smartneck.medical.twofive.util.User.Height;
import com.ko.smartneck.medical.twofive.util.User.Weight;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ko.smartneck.medical.twofive.GlobalApplication.userPreference;
import static com.ko.smartneck.medical.twofive.Main.MainActivity.member;
import static com.ko.smartneck.medical.twofive.Main.MainActivity.preset;
import static com.ko.smartneck.medical.twofive.util.Constants.TAG;
import static com.ko.smartneck.medical.twofive.util.Constants.ageArrayListFemale;
import static com.ko.smartneck.medical.twofive.util.Constants.ageArrayListMale;

public class ChartActivity extends DemoBase {
    BarChart chart;
    TextView title;//title

    TextView tv_age, tv_height, tv_weight, tv_exercise;

    TextView tv_header_date, tv_header_count, tv_header_set, tv_header_stop;
    TextView tv_axis_x, tv_axis_y;
    FrameLayout chart_container;
    Handler handler;
    ArrayList dateArray;
    ArrayList<BarEntry> values;

    ListView exerciseList;
    ArrayList<Exercise> exerciseItems;
    ExerciseAdapter exerciseAdapter;

    LinearLayout listview_container;

    ImageView btn_dismiss;
    HorizontalScrollView hsview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_activity_main);

        init();
    }


    void init() {
        handler = new Handler();
        values = new ArrayList<>();
        dateArray = new ArrayList();
        chart = findViewById(R.id.chart1);
        title = findViewById(R.id.chart_title);

        tv_age = findViewById(R.id.tv_chart_age);
        tv_height = findViewById(R.id.tv_chart_height);
        tv_weight = findViewById(R.id.tv_chart_weight);
        tv_exercise = findViewById(R.id.tv_chart_exercise);

        hsview = findViewById(R.id.hsview);
        tv_header_date = findViewById(R.id.tv_header_date);
        tv_header_count = findViewById(R.id.tv_header_count);
        tv_header_set = findViewById(R.id.tv_header_set);
        tv_header_stop = findViewById(R.id.tv_header_stop);

        listview_container = findViewById(R.id.listview_container);

        btn_dismiss = findViewById(R.id.chart_dismiss_btn);

        chart_container = findViewById(R.id.chart_container);


        tv_axis_x = findViewById(R.id.tv_legend_x);
        tv_axis_y = findViewById(R.id.tv_legend_y);

        exerciseItems = userPreference.getExercise(member);
        Collections.reverse(exerciseItems);
        Log.d(TAG, "init: " + exerciseItems.toString());
        exerciseList = findViewById(R.id.listview_exercise);
        exerciseAdapter = new ExerciseAdapter(exerciseItems, getLayoutInflater());
        exerciseList.setAdapter(exerciseAdapter);
        exerciseAdapter.notifyDataSetChanged();


        //초기 실행 시 목근육 나이
        setTabButtonColor(1);
        setChart();

        onClick();
        setAge();
    }

    private void onClick() {
        tv_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setAge();
            }
        });
        tv_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listview_container.setVisibility(View.GONE);
                chart_container.setVisibility(View.VISIBLE);
                setTabButtonColor(2);
                setHeight();
                setTitleSize();
            }
        });
        tv_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listview_container.setVisibility(View.GONE);
                chart_container.setVisibility(View.VISIBLE);
                setTabButtonColor(3);
                setWeight();
                setTitleSize();
            }
        });
        tv_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTabButtonColor(4);
                setTitleSize();
                listview_container.setVisibility(View.VISIBLE);
                chart_container.setVisibility(View.GONE);

            }
        });
        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void setChart() {

        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);

        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
//        chart.setMaxVisibleValueCount(7);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);


        //범례 Enableed => false == Visible.Gone
        chart.getLegend().setEnabled(false);


        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);


        // setting data


        // chart.setDrawLegend(false);
        chart.zoom(1, 1, 0, 0);

    }

    private boolean setData(int labelCount, String type) {
        Log.d(TAG, " - - - - - - - LabelCount: " + labelCount);
        chart.animateY(500);
        Log.d(TAG, "setData: " + chart.getMaxVisibleCount());
        BarDataSet set1;
        if (labelCount > 5) {
            chart.getLayoutParams().width = 250 * labelCount;
            Log.d(TAG, "setData: size" + 250 * labelCount);

        } else {
            chart.getLayoutParams().width = 1000;
            Log.d(TAG, "setData: size" + 1000);

        }
        chart.setLayoutParams(chart.getLayoutParams());

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(tfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(labelCount);
        xAxis.setTextSize(14);
        chart.setExtraBottomOffset(20);


        ValueFormatter xAxisFormatter = new HeightAxisValueFormatter(chart, dateArray);


        xAxis.setValueFormatter(xAxisFormatter);

        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart
        chart.setTouchEnabled(false);

        ValueFormatter custom = new MyValueFormatter("");

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        if (Constants.DEVICE_TYPE.equals("MED")) {

        } else {

        }

//        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setDrawGridLines(true);
        leftAxis.setTextSize(14);

        if (type.equals("height")) {
            if (Constants.DEVICE_TYPE.equals("MED")) {
                leftAxis.setLabelCount(12, true);


                leftAxis.setAxisMaximum(330);
//                leftAxis.mCenteredEntries = y;
//                leftAxis.setValueFormatter();
//                leftAxis.set
//
//                leftAxis.
            } else {
                leftAxis.setAxisMaximum(270);
                leftAxis.setLabelCount(8, false);

            }
        } else {
            leftAxis.setAxisMaximum(11);
            leftAxis.setLabelCount(8, false);

        }

//        if (chart.getData() != null &&
//                chart.getData().getDataSetCount() > 0) {
//
//
//            int color1 = Color.parseColor("#BDBDBD");
//            int color2 = Color.parseColor("#cc1b17");
//
//
//            List<GradientColor> gradientColors = new ArrayList<>();
//            for (int i = 0; i < values.size(); i++) {
//                if (i != values.size() - 1) {
//                    gradientColors.add(new GradientColor(color1, color1));
//
//                } else if (i == values.size() - 1) {
//                    gradientColors.add(new GradientColor(color2, color2));
//
//                }
//
//            }
//
//            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
//            set1.setGradientColors(gradientColors);
//            set1.setValues(values);
//            if (labelCount > 5){
//                chart.getLayoutParams().width = 250 * labelCount;
//                hsview.getLayoutParams().width = 250 * labelCount;
//            }else{
//                chart.getLayoutParams().width = 1000;
//                hsview.getLayoutParams().width = 1000;
//
//            }
//            chart.getData().notifyDataChanged();
//            chart.notifyDataSetChanged();
//
//
//
//        } else {
        set1 = new BarDataSet(values, "");

        set1.setDrawIcons(false);
        set1.setDrawValues(false);


        int color1 = Color.parseColor("#BDBDBD");
        int color2 = Color.parseColor("#cc1b17");


        List<GradientColor> gradientColors = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            if (i != values.size() - 1) {
                gradientColors.add(new GradientColor(color1, color1));

            } else if (i == values.size() - 1) {
                gradientColors.add(new GradientColor(color2, color2));

            }

        }
        Log.d(TAG, "setData: " + values.size());
        set1.setGradientColors(gradientColors);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setValueTypeface(tfLight);
        data.setBarWidth(0.3f);
        data.setDrawValues(true);
        data.setValueTextColor(Color.parseColor("#000000"));
        data.setValueTextSize(15);


        chart.setData(data);


//        }

        return true;
    }

    private void setTabButtonColor(int select) {


        tv_exercise.setBackgroundColor(Color.parseColor("#ffffff"));
        tv_height.setBackgroundColor(Color.parseColor("#ffffff"));
        tv_weight.setBackgroundColor(Color.parseColor("#ffffff"));
        tv_age.setBackgroundColor(Color.parseColor("#ffffff"));
        tv_exercise.setTextColor(Color.parseColor("#000000"));
        tv_height.setTextColor(Color.parseColor("#000000"));
        tv_weight.setTextColor(Color.parseColor("#000000"));
        tv_age.setTextColor(Color.parseColor("#000000"));

        if (select == 1) {
            tv_age.setBackgroundColor(Color.parseColor("#cc1b17"));
            tv_age.setTextColor(Color.parseColor("#ffffff"));
            title.setText("\n");
            tv_axis_x.setText(getString(R.string.chart_weight));
        } else if (select == 2) {
            tv_height.setBackgroundColor(Color.parseColor("#cc1b17"));
            tv_height.setTextColor(Color.parseColor("#ffffff"));
            title.setText("\n" + getString(R.string.statistics_menu_height));
            tv_axis_x.setText(getString(R.string.chart_length));
        } else if (select == 3) {
            tv_weight.setBackgroundColor(Color.parseColor("#cc1b17"));
            tv_weight.setTextColor(Color.parseColor("#ffffff"));
            title.setText("\n" + getString(R.string.statistics_menu_weight));
            tv_axis_x.setText(getString(R.string.chart_weight));

        } else if (select == 4) {
            tv_exercise.setBackgroundColor(Color.parseColor("#cc1b17"));
            tv_exercise.setTextColor(Color.parseColor("#ffffff"));
            title.setText("\n" + getString(R.string.statistics_menu_exercise));

        }

    }

//    private void setHeight() {
//
//        tv_axis_x.setText(getString(R.string.chart_length));
//        tv_axis_y.setText("(" + getString(R.string.statistics_menu_exercise_colume_date) + ")");
//        tv_axis_y.setVisibility(View.VISIBLE);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Param param = new Param();
////                param.add("token", User.token);
////                param.add("memberNo", user.getMemberNo());
//                param.add("type", "height");
//
//                Address address = new Address();
//
//                final HttpConnect httpConnect = new HttpConnect();
//
//                if (httpConnect.httpConnect(param.getValue(), address.getLoadUserStatistics(), true) == 200) {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            getHeightJson(httpConnect.getReceiveMessage());
//
//                        }
//                    });
//
//
//                }
//            }
//        }).start();
//
//    }

//    private void getHeightJson(String JsonString) {
//        dateArray.clear();
//        values.clear();
//        try {
//            JSONArray jsonArray = new JSONArray(JsonString);
//            int count = 0;
//            if (jsonArray.length() < 5) {
//                count = jsonArray.length();
//            } else {
//                count = 5;
//            }
//            int idx = 0;
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                String dateJson = jsonObject.getString("Date");
//                String[] datesplit = dateJson.split("-");
//                String month = datesplit[1];
//                String day = datesplit[2].substring(0, 2);
//                String date = Integer.parseInt(month) + "/" + Integer.parseInt(day);
//
//                int valueJson = jsonObject.getInt("MaxHeight");
//                float height = ((float) valueJson) * 3.6666667f;
//                if (Constants.language.equals("en")) {
//                    height /= Constants.INCHES;
//                }
//                if (values.size() == 0) {
//                    values.add(new BarEntry(idx, height));
//                    dateArray.add(date);
//                    idx++;
//                } else {
//                    if (!dateArray.get(idx - 1).equals(date)) {
//                        values.add(new BarEntry(idx, height));
//                        dateArray.add(date);
//                        idx++;
//                    } else {
//                        if (values.get(idx - 1).getY() < height) {
//                            Log.d(TAG, "getHeightJson: setsetsetsetsetsetset");
//                            values.set(idx - 1, new BarEntry(idx - 1, height));
//
//                        }
//
//                    }
//                }
//            }
//            for (int i = values.size(); i > 5; i--) {
//                values.remove(0);
////                dateArray.remove(0);
//            }
//            setData(dateArray.size());
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

//    private void setWeight() {
//        tv_axis_x.setText(getString(R.string.chart_weight));
//        tv_axis_y.setText("(" + getString(R.string.statistics_menu_exercise_colume_date) + ")");
//        tv_axis_y.setVisibility(View.VISIBLE);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Param param = new Param();
////                param.add("token", User.token);
////                param.add("memberNo", user.getMemberNo());
//                param.add("type", "weight");
//
//                Address address = new Address();
//
//                final HttpConnect httpConnect = new HttpConnect();
//
//                if (httpConnect.httpConnect(param.getValue(), address.getLoadUserStatistics(), true) == 200) {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
////                            getWeightJson(httpConnect.getReceiveMessage());
//
//                        }
//                    });
//
//
//                }
//            }
//        }).start();
//
//    }


    float anglePerMillimeter = 3.6666667f;

    private void setHeight() {

        if (Constants.DEVICE_TYPE.equals("FIT")) {
            anglePerMillimeter = 3f;
        } else if (Constants.DEVICE_TYPE.equals("MED")) {
            anglePerMillimeter = 3.6666667f;
        }

        dateArray.clear();
        values.clear();

        int count = 0;

        int idx = 0;

        ArrayList<Height> heights = userPreference.getHeight(member);

        for (int i = 0; i < heights.size(); i++) {
            String date = heights.get(i).getDate().split("-")[1] + "/" + heights.get(i).getDate().split("-")[2].split(" ")[0];
            Log.d(TAG, "setHeight: " + date);
            float height = ((float) heights.get(i).getMaxHeight()) * anglePerMillimeter;
            if (Constants.language.equals("en")) {
                height /= Constants.INCHES;
            }
            if (values.size() == 0) {
                values.add(new BarEntry(idx, height));
                dateArray.add(date);
                idx++;
            } else {
                //주석부분 -> 같은 날짜의 데이터는 제일 큰 값만 가져옴
                if (!dateArray.get(idx - 1).equals(date)) {
                    values.add(new BarEntry(idx, height));
                    dateArray.add(date);
                    idx++;
                } else {
                    if (values.get(idx - 1).getY() < height) {
                        values.set(idx - 1, new BarEntry(idx - 1, height));

                    }

                }
            }

        }

        if (setData(dateArray.size(), "height")) {
            hsview.fullScroll(View.FOCUS_RIGHT);

        }


    }


    private void setWeight() {
        dateArray.clear();
        values.clear();

        int count = 0;

        int idx = 0;

        ArrayList<Weight> weights = userPreference.getWeight(member);

        for (int i = 0; i < weights.size(); i++) {
            String date = weights.get(i).getDate().split("-")[1] + "/" + weights.get(i).getDate().split("-")[2].split(" ")[0];
            float weight = ((float) weights.get(i).getMaxWeight()) * 0.1f;

            if (values.size() == 0) {
                values.add(new BarEntry(idx, weight));
                dateArray.add(date);
                idx++;
            } else {
                //주석부분 -> 같은 날짜의 데이터는 제일 큰 값만 가져옴
                if (!dateArray.get(idx - 1).equals(date)) {
                    values.add(new BarEntry(idx, weight));
                    dateArray.add(date);
                    idx++;
                } else {
                    if (values.get(idx - 1).getY() < weight) {
                        values.set(idx - 1, new BarEntry(idx - 1, weight));

                    }

                }
            }

        }

        if (setData(dateArray.size(), "weight")) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            hsview.fullScroll(View.FOCUS_RIGHT);

                        }
                    });
                }
            }).start();

        }
    }

    private void setAge() {
        setTabButtonColor(1);
        tv_axis_x.setText(getString(R.string.chart_weight));
        tv_axis_y.setText("");
        tv_axis_y.setVisibility(View.GONE);
        listview_container.setVisibility(View.GONE);
        chart_container.setVisibility(View.VISIBLE);
        dateArray.clear();
        values.clear();

        float max = 0;
        float min = 0;
        float avg = 0;
        float mine = preset.getMaxWeight() * 0.1f;

        int neckage = 0;

        List<Integer> tmpAgeArrayList = new ArrayList<>();

        if (member.getGender().equals("남자")) {
            if (member.getAge() < 17) {
                neckage = 0;
            } else if (member.getAge() >= 17) {
                float tmpMine = mine;
                if (tmpMine > 10) {
                    tmpMine = 10;
                }
                for (int i = 0; i < ageArrayListMale.size(); i++) {
                    if (tmpMine * 10 == ageArrayListMale.get(i).getAgeWeight()) {
                        tmpAgeArrayList.add(ageArrayListMale.get(i).getAge());
                        Log.d(TAG, " * * * * * * * * * * ArrayAdd " + ageArrayListMale.get(i).getAge());
                    }
                }
                int tmpMin = 0;
                int tmpNum = 0;

                for (int i = 0; i < tmpAgeArrayList.size(); i++) {
                    if (i == 0) {
                        tmpNum = tmpAgeArrayList.get(i);
                        tmpMin = tmpNum;
                    } else {
                        tmpNum = tmpAgeArrayList.get(i);

                        if (tmpNum == member.getAge()) {
                            tmpMin = member.getAge();
                            break;
                        }

                        if (tmpMin > tmpNum) {
                            tmpMin = tmpNum;
                        }

                    }


                }

                neckage = tmpMin;
                Log.d(TAG, " * * * * * * * * * * resultAge: " + neckage);
            }
        } else if (member.getGender().equals("여자")) {
            if (member.getAge() < 17) {
                neckage = 0;
            } else if (member.getAge() >= 17) {
                float tmpMine = mine;
                if (tmpMine > 8.5) {
                    tmpMine = 8.5f;
                }
                for (int i = 0; i < ageArrayListFemale.size(); i++) {
                    if (tmpMine * 10 == ageArrayListFemale.get(i).getAgeWeight()) {
                        tmpAgeArrayList.add(ageArrayListFemale.get(i).getAge());
                        Log.d(TAG, " * * * * * * * * * * ArrayAdd " + ageArrayListFemale.get(i).getAge());
                    }
                }
                int tmpMin = 0;
                int tmpNum = 0;

                for (int i = 0; i < tmpAgeArrayList.size(); i++) {
                    if (i == 0) {
                        tmpNum = tmpAgeArrayList.get(i);
                        tmpMin = tmpNum;
                    } else {

                        tmpNum = tmpAgeArrayList.get(i);

                        if (tmpNum == member.getAge()) {
                            tmpMin = member.getAge();
                            break;
                        }

                        if (tmpMin > tmpNum) {
                            tmpMin = tmpNum;
                        }


                    }


                }

                neckage = tmpMin;
                Log.d(TAG, " * * * * * * * * * * resultAge: " + neckage);
            }
        }


        if (member.getGender().equals("남자")) {

            for (int i = 0; i < Constants.ageArrayListMale.size(); i++) {

                if (member.getAge() == Constants.ageArrayListMale.get(i).getAge()) {
                    avg = Constants.ageArrayListMale.get(i).getAgeWeight();
                    break;
                }
            }
        } else if (member.getGender().equals("여자")) {
            for (int i = 0; i < Constants.ageArrayListFemale.size(); i++) {
                if (member.getAge() == Constants.ageArrayListFemale.get(i).getAge()) {
                    avg = Constants.ageArrayListFemale.get(i).getAgeWeight();
                    break;
                }
            }
        }
        Log.d(TAG, "getAgeJson: Age = " + member.getAge());
        Log.d(TAG, "getAgeJson: Gender = " + member.getGender());
        Log.d(TAG, "getAgeJson: AVG = " + avg);
        Log.d(TAG, "getAgeJson: Size = " + ageArrayListMale.size());
        max = avg + 15;
        if (preset.getMaxWeight() > max) {
            max = preset.getMaxWeight();
        }
        min = mine / 0.2f;

        if (min == 0) {
            min = 15;
        }
        if (neckage == 0) {
            max = 0;
            min = 0;
            avg = 0;
        }

        if (Constants.language.equals("ko")) {
            values.add(new BarEntry(0, max * 0.1f));
            values.add(new BarEntry(1, min * 0.1f));
            values.add(new BarEntry(2, avg * 0.1f));
            values.add(new BarEntry(3, mine));
        } else if (Constants.language.equals("en")) {
            values.add(new BarEntry(0, max * 0.1f * Constants.POUND));
            values.add(new BarEntry(1, min * 0.1f * Constants.POUND));
            values.add(new BarEntry(2, avg * 0.1f * Constants.POUND));
            values.add(new BarEntry(3, mine * Constants.POUND));
        } else if (Constants.language.equals("zh")) {
            values.add(new BarEntry(0, max * 0.1f));
            values.add(new BarEntry(1, min * 0.1f));
            values.add(new BarEntry(2, avg * 0.1f));
            values.add(new BarEntry(3, mine));
        }

        Log.e(TAG, "avg: " + avg);

        dateArray.add(getString(R.string.statistics_menu_age_legend_max));
        dateArray.add(getString(R.string.statistics_menu_age_legend_min));
        dateArray.add(getString(R.string.statistics_menu_age_legend_avg));
        dateArray.add(getString(R.string.statistics_menu_age_legend_mine));

        setData(4, "age");
        title.setText("");
        title.setTextSize(20);


        if (preset.getMaxWeight() == 0 || preset.getMaxHeight() == 0) {
            if (Constants.language.equals("ko")) {
                title.setText("측정 후 이용해주세요.");
            } else if (Constants.language.equals("en")) {
                title.setText("Please use after measurement.");
            } else if (Constants.language.equals("zh")) {
                title.setText("请测量后使用。");
            }
            return;
        }
        if (neckage == 0) {
            if (Constants.language.equals("ko")) {
                title.setText("해당 데이터가 없습니다.");

            } else if (Constants.language.equals("en")) {
                title.setText("There is no such data.");

            } else if (Constants.language.equals("zh")) {
                title.setText("没有相应数据。");

            }
            return;
        }


        if (Constants.language.equals("ko")) {
            final SpannableStringBuilder sps = new SpannableStringBuilder("목 근력 나이는\n" + neckage + "세입니다.");
            sps.setSpan(new AbsoluteSizeSpan(60), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sps.setSpan(new AbsoluteSizeSpan(150), 9, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sps.setSpan(new ForegroundColorSpan(Color.parseColor("#cc1b17")), 9, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sps.setSpan(new StyleSpan(Typeface.BOLD), 9, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sps.setSpan(new StyleSpan(Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            final SpannableStringBuilder sps2 = new SpannableStringBuilder(member.getName() + "님의");
            sps2.setSpan(new StyleSpan(Typeface.BOLD), 0, sps2.length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            title.setText(sps2);
            title.append(sps);
        } else if (Constants.language.equals("en")) {

            final SpannableStringBuilder sps = new SpannableStringBuilder("neck strength is\n" + neckage + " years old");
            sps.setSpan(new AbsoluteSizeSpan(60), 0, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sps.setSpan(new AbsoluteSizeSpan(150), 17, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sps.setSpan(new ForegroundColorSpan(Color.parseColor("#cc1b17")), 17, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sps.setSpan(new StyleSpan(Typeface.BOLD), 17, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sps.setSpan(new StyleSpan(Typeface.BOLD), 0, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            final SpannableStringBuilder sps2 = new SpannableStringBuilder(member.getName() + "'s ");
            sps2.setSpan(new StyleSpan(Typeface.BOLD), 0, sps2.length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            title.setText(sps2);
            title.append(sps);
        } else if (Constants.language.equals("zh")) {
            // TODO: 2020-07-20 목근육나이 중국
            final SpannableStringBuilder sps = new SpannableStringBuilder("我的颈部肌肉年龄是" + neckage + "岁。");
            sps.setSpan(new AbsoluteSizeSpan(80), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sps.setSpan(new AbsoluteSizeSpan(80), 9, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sps.setSpan(new ForegroundColorSpan(Color.parseColor("#cc1b17")), 9, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sps.setSpan(new StyleSpan(Typeface.BOLD), 9, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//                sps.setSpan(new StyleSpan(Typeface.BOLD), 10, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                sps.setSpan(new StyleSpan(Typeface.BOLD), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            final SpannableStringBuilder sps2 = new SpannableStringBuilder(member.getName());
            sps2.setSpan(new StyleSpan(Typeface.BOLD), 0, sps2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sps2.setSpan(new AbsoluteSizeSpan(60), 0, sps2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            title.setText(sps2);
            title.append(sps);
        }
    }

//    private void setExercise() {
//
//        listview_container.setVisibility(View.VISIBLE);
//        chart_container.setVisibility(View.GONE);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Param param = new Param();
////                param.add("token", User.token);
//                param.add("memberNo", member.getMemberNo());
//                param.add("type", "exercise");
//
//                Address address = new Address();
//
//                final HttpConnect httpConnect = new HttpConnect();
//
//                if (httpConnect.httpConnect(param.getValue(), address.getLoadUserStatistics(), true) == 200) {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            getExericseJson(httpConnect.getReceiveMessage());
//
//                        }
//                    });
//
//
//                }
//            }
//        }).start();
//
//    }

//    private void getExericseJson(String JsonString) {
//        exerciseItems.clear();
//        try {
//            JSONArray jsonArray = new JSONArray(JsonString);
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                String dateJson = jsonObject.getString("Date");
//                String[] datesplit = dateJson.split("-");
//                String month = datesplit[1];
//                String day = datesplit[2].substring(0, 2);
//                String date = Integer.parseInt(month) + "/" + Integer.parseInt(day);
//
//                int count = jsonObject.getInt("Count");
//                int totalCount = jsonObject.getInt("TotalCount");
//                int set = jsonObject.getInt("Sett");
//                int totalSet = jsonObject.getInt("TotalSet");
//
//                int stop = jsonObject.getInt("Stop");
//
//                exerciseItems.add(new ExerciseChartItem(count, totalCount, set, totalSet, stop, date));
//
//
//                Log.d(TAG, " - - - - - getExerciseData: Date->" + date);
//                Log.d(TAG, " - - - - - getExerciseData: Date->" + count);
//                Log.d(TAG, " - - - - - getExerciseData: Date->" + totalCount);
//                Log.d(TAG, " - - - - - getExerciseData: Date->" + set);
//                Log.d(TAG, " - - - - - getExerciseData: Date->" + totalSet);
//                Log.d(TAG, " - - - - - getExerciseData: Date->" + stop);
//
//            }
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            exerciseAdapter.notifyDataSetChanged();
//
//                        }
//                    });
//                }
//            }).start();
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    private int makeNeckAge(int neckage) {
        String sub = String.valueOf(neckage).substring(0, 1);
        String sub2 = String.valueOf(member.getAge()).substring(0, 1);
        neckage = neckage + (Integer.parseInt(sub2) - Integer.parseInt(sub));
        Log.d(TAG, "getAgeJson: sub : " + sub);
        Log.d(TAG, "getAgeJson: sub2 : " + sub2);

        return neckage;
    }

    private void setTitleSize() {
        title.setTextSize(30);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void saveToGallery() {

    }
}
