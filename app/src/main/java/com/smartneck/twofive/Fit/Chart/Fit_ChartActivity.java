package com.smartneck.twofive.Fit.Chart;

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
import com.smartneck.twofive.Chart.ChartItem.ExerciseChartItem;
import com.smartneck.twofive.Chart.custom.HeightAxisValueFormatter;
import com.smartneck.twofive.Chart.custom.MyValueFormatter;
import com.smartneck.twofive.Chart.custom.XYMarkerView;
import com.smartneck.twofive.Chart.notimportant.DemoBase;
import com.smartneck.twofive.Fit.util.Fit_Address;
import com.smartneck.twofive.Fit.util.Fit_Constants;
import com.smartneck.twofive.Fit.util.Fit_HttpConnect;
import com.smartneck.twofive.Fit.util.Fit_Param;
import com.smartneck.twofive.Fit.util.User.Fit_Preset;
import com.smartneck.twofive.Fit.util.User.Fit_User;
import com.smartneck.twofive.R;
import com.smartneck.twofive.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.smartneck.twofive.Fit.util.User.Fit_User.MemberNo;
import static com.smartneck.twofive.Fit.util.User.Fit_User.token;
import static com.smartneck.twofive.util.Constants.TAG;
import static com.smartneck.twofive.util.Constants.ageArrayListFemale;
import static com.smartneck.twofive.util.Constants.ageArrayListMale;

public class Fit_ChartActivity extends DemoBase {
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
    ArrayList<ExerciseChartItem> exerciseItems;
    Fit_ExerciseAdapter exerciseAdapter;

    LinearLayout listview_container;

    ImageView btn_dismiss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_fit_main);

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


        tv_header_date = findViewById(R.id.tv_header_date);
        tv_header_count = findViewById(R.id.tv_header_count);
        tv_header_set = findViewById(R.id.tv_header_set);
        tv_header_stop = findViewById(R.id.tv_header_stop);

        listview_container = findViewById(R.id.listview_container);

        btn_dismiss = findViewById(R.id.chart_dismiss_btn);

        chart_container = findViewById(R.id.chart_container);

        tv_axis_x = findViewById(R.id.tv_legend_x);
        tv_axis_y = findViewById(R.id.tv_legend_y);

        exerciseItems = new ArrayList<>();
        exerciseList = findViewById(R.id.listview_exercise);
        exerciseAdapter = new Fit_ExerciseAdapter(exerciseItems, getLayoutInflater());
        exerciseList.setAdapter(exerciseAdapter);


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
                setTabButtonColor(1);
            }
        });

        // 차트유연성 부분분
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
                setExercise();
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
        chart.setMaxVisibleValueCount(7);

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

    private void setData(int labelCount) {
        Log.d(TAG, " - - - - - - - LabelCount: " + labelCount);
        chart.animateY(500);

        BarDataSet set1;


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
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setDrawGridLines(true);
        leftAxis.setTextSize(14);

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {


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

            Log.d(TAG, "setData: " + gradientColors.size());
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setGradientColors(gradientColors);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
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


        }
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
        } else if (select == 2) {
            tv_height.setBackgroundColor(Color.parseColor("#cc1b17"));
            tv_height.setTextColor(Color.parseColor("#ffffff"));
            title.setText("\n" + getString(R.string.statistics_menu_height));
        } else if (select == 3) {
            tv_weight.setBackgroundColor(Color.parseColor("#cc1b17"));
            tv_weight.setTextColor(Color.parseColor("#ffffff"));
            title.setText("\n" + getString(R.string.statistics_menu_weight));
        } else if (select == 4) {
            tv_exercise.setBackgroundColor(Color.parseColor("#cc1b17"));
            tv_exercise.setTextColor(Color.parseColor("#ffffff"));
            title.setText("\n" + getString(R.string.statistics_menu_exercise));
        }

    }

    private void setHeight() {

        tv_axis_x.setText(getString(R.string.chart_length));
        tv_axis_y.setText("(" + getString(R.string.statistics_menu_exercise_colume_date) + ")");
        tv_axis_y.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Fit_Param param = new Fit_Param();
                param.add("token", Fit_User.token);
                param.add("memberNo", Fit_User.MemberNo);
                param.add("type", "height");

                Fit_Address address = new Fit_Address();

                final Fit_HttpConnect httpConnect = new Fit_HttpConnect();

                if (httpConnect.httpConnect(param.getParam(), address.getLoadUserStatistics()) == 200) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            getHeightJson(httpConnect.getReceiveMessage());

                        }
                    });


                } else {
                    Log.d(Constants.TAG, "HttpConnect Error: " + httpConnect.getResponseCode());
                }
            }
        }).start();

    }

    private void getHeightJson(String JsonString) {
        dateArray.clear();
        values.clear();
        try {
            JSONArray jsonArray = new JSONArray(JsonString);
            int count = 0;
            if (jsonArray.length() < 5) {
                count = jsonArray.length();
            } else {
                count = 5;
            }
            int idx = 0;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String dateJson = jsonObject.getString("Date");
                String[] datesplit = dateJson.split("-");
                String month = datesplit[1];
                String day = datesplit[2].substring(0, 2);
                String date = Integer.parseInt(month) + "/" + Integer.parseInt(day);

                int valueJson = jsonObject.getInt("MaxHeight");
                float height = ((float) valueJson) * 3;
                if (Fit_User.language.equals("en")) {
                    height /= Fit_Constants.INCHES;
                }
                if (values.size() == 0) {
                    values.add(new BarEntry(idx, height));
                    dateArray.add(date);
                    idx++;
                } else {
                    if (!dateArray.get(idx - 1).equals(date)) {
                        values.add(new BarEntry(idx, height));
                        dateArray.add(date);
                        idx++;
                    } else {
                        if (values.get(idx - 1).getY() < height) {
                            Log.d(TAG, "getHeightJson: setsetsetsetsetsetset");
                            values.set(idx - 1, new BarEntry(idx - 1, height));

                        }

                    }
                }
            }
            for (int i = values.size(); i > 5; i--) {
                values.remove(0);
//                dateArray.remove(0);
            }
            setData(dateArray.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setWeight() {
        tv_axis_x.setText(getString(R.string.chart_weight));
        tv_axis_y.setText("(" + getString(R.string.statistics_menu_exercise_colume_date) + ")");
        tv_axis_y.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Fit_Param param = new Fit_Param();
                param.add("token", Fit_User.token);
                param.add("memberNo", Fit_User.MemberNo);
                param.add("type", "weight");

                Fit_Address address = new Fit_Address();

                final Fit_HttpConnect httpConnect = new Fit_HttpConnect();

                if (httpConnect.httpConnect(param.getParam(), address.getLoadUserStatistics()) == 200) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            getWeightJson(httpConnect.getReceiveMessage());

                        }
                    });


                } else {
                    Log.d(Constants.TAG, "HttpConnect Error: " + httpConnect.getResponseCode());
                }
            }
        }).start();

    }

    private void getWeightJson(String JsonString) {
        dateArray.clear();
        values.clear();
        try {
            JSONArray jsonArray = new JSONArray(JsonString);
            int count = 0;
            if (jsonArray.length() < 5) {
                count = jsonArray.length();
            } else {
                count = 5;
            }
            int idx = 0;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String dateJson = jsonObject.getString("Date");
                String[] datesplit = dateJson.split("-");
                String month = datesplit[1];
                String day = datesplit[2].substring(0, 2);
                String date = Integer.parseInt(month) + "/" + Integer.parseInt(day);

                int valueJson = jsonObject.getInt("MaxWeight");
                float weight = ((float) valueJson) * 0.1f;
                if (Fit_User.language.equals("en")) {
                    weight *= Constants.POUND;
                }
                if (values.size() == 0) {
                    values.add(new BarEntry(idx, weight));
                    dateArray.add(date);
                    idx++;
                } else {
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

            for (int i = values.size(); i > 5; i--) {
                values.remove(0);
//                dateArray.remove(0);
            }

            setData(dateArray.size());


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setAge() {
        tv_axis_x.setText(getString(R.string.chart_weight));
        tv_axis_y.setText("");
        tv_axis_y.setVisibility(View.GONE);
        listview_container.setVisibility(View.GONE);
        chart_container.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Fit_Param param = new Fit_Param();
                param.add("token", token);
                param.add("memberNo", MemberNo);
                param.add("type", "age");

                Fit_Address address = new Fit_Address();

                final Fit_HttpConnect httpConnect = new Fit_HttpConnect();

                if (httpConnect.httpConnect(param.getParam(), address.getLoadUserStatistics()) == 200) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            getAgeJson(httpConnect.getReceiveMessage());

                        }
                    });


                } else {
                    Log.d(Constants.TAG, "HttpConnect Error: " + httpConnect.getResponseCode());
                }
            }
        }).start();

    }

    private void getAgeJson(String JsonString) {
        dateArray.clear();
        values.clear();
        try {
            JSONArray jsonArray = new JSONArray(JsonString);
            float max = 0;
            float min = 0;
            float avg = 0;
            float mine = Fit_Preset.MaxWeight * 0.1f;

            int neckage = 0;

            List<Integer> tmpAgeArrayList = new ArrayList<>();

            if (Fit_User.gender.equals("남자")) {
                if (Fit_User.age < 17) {
                    neckage = 0;
                } else if (Fit_User.age >= 17) {
                    float tmpMine = mine;
                    if (tmpMine > 10){
                        tmpMine = 10;
                    }
                    if (ageArrayListMale.size() > 0)
                    for (int i = 0; i < ageArrayListMale.size(); i++) {
                        if (tmpMine * 10 == ageArrayListMale.get(i).getAgeWeight()) {
                            tmpAgeArrayList.add(ageArrayListMale.get(i).getAge());
                        }
                    }
                    int tmpMin = 0;
                    int tmpNum = 0;
                    if (tmpAgeArrayList.size() > 0)
                    for (int i = 0; i < tmpAgeArrayList.size(); i++) {
                        if (i == 0){
                            tmpNum = tmpAgeArrayList.get(i);
                            tmpMin = tmpNum;
                        }else{
                            tmpNum = tmpAgeArrayList.get(i);

                            if (tmpNum == Fit_User.age){
                                tmpMin = Fit_User.age;
                                break;
                            }

                            if (tmpMin > tmpNum){
                                tmpMin = tmpNum;
                            }

                        }


                    }

                    neckage = tmpMin;
                    Log.d(TAG, " * * * * * * * * * * resultAge: " + neckage);
                }
            }else if (Fit_User.gender.equals("여자")) {
                if (Fit_User.age < 17) {
                    neckage = 0;
                } else if (Fit_User.age >= 17) {
                    float tmpMine = mine;
                    if (tmpMine > 8.5){
                        tmpMine = 8.5f;
                    }
                    if (ageArrayListFemale.size() >0)
                    for (int i = 0; i < ageArrayListFemale.size(); i++) {
                        if (tmpMine * 10 == ageArrayListFemale.get(i).getAgeWeight()) {
                            tmpAgeArrayList.add(ageArrayListFemale.get(i).getAge());
                            Log.d(TAG, " * * * * * * * * * * ArrayAdd " + ageArrayListFemale.get(i).getAge());
                        }
                    }
                    int tmpMin = 0;
                    int tmpNum = 0;
                    if (tmpAgeArrayList.size() > 0)
                    for (int i = 0; i < tmpAgeArrayList.size(); i++) {
                        if (i == 0){
                            tmpNum = tmpAgeArrayList.get(i);
                            tmpMin = tmpNum;
                        }else{

                            tmpNum = tmpAgeArrayList.get(i);

                            if (tmpNum == Fit_User.age){
                                tmpMin = Fit_User.age;
                                break;
                            }

                            if (tmpMin > tmpNum){
                                tmpMin = tmpNum;
                            }


                        }


                    }

                    neckage = tmpMin;
                    Log.d(TAG, " * * * * * * * * * * resultAge: " + neckage);
                }
            }


            if (Fit_User.gender.equals("남자")) {
                if (ageArrayListMale.size() > 0)
                for (int i = 0; i < Constants.ageArrayListMale.size(); i++) {

                    if (Fit_User.age == Constants.ageArrayListMale.get(i).getAge()) {
                        avg = Constants.ageArrayListMale.get(i).getAgeWeight();
                        break;
                    }
                }
            } else if (Fit_User.gender.equals("여자")) {
                if (ageArrayListFemale.size() >0)
                for (int i = 0; i < Constants.ageArrayListFemale.size(); i++) {
                    if (Fit_User.age == Constants.ageArrayListFemale.get(i).getAge()) {
                        avg = Constants.ageArrayListFemale.get(i).getAgeWeight();
                        break;
                    }
                }
            }
            Log.d(TAG, "getAgeJson: Age = " + Fit_User.age);
            Log.d(TAG, "getAgeJson: Gender = " + Fit_User.gender);
            Log.d(TAG, "getAgeJson: AVG = " + avg);
            Log.d(TAG, "getAgeJson: Size = " + ageArrayListMale.size());
            max = avg + 15;
            if (Fit_Preset.MaxWeight > max) {
                max = Fit_Preset.MaxWeight;
            }
            min = mine / 0.2f;

            if (min == 0) {
                min = 15;
            }
            if (neckage == 0){
                max = 0;
                min = 0;
                avg = 0;
            }

            if (Fit_User.language.equals("ko")) {
                values.add(new BarEntry(0, max * 0.1f));
                values.add(new BarEntry(1, min * 0.1f));
                values.add(new BarEntry(2, avg * 0.1f));
                values.add(new BarEntry(3, mine));
            } else if (Fit_User.language.equals("en")) {
                values.add(new BarEntry(0, max * 0.1f * Constants.POUND));
                values.add(new BarEntry(1, min * 0.1f * Constants.POUND));
                values.add(new BarEntry(2, avg * 0.1f * Constants.POUND));
                values.add(new BarEntry(3, mine * Constants.POUND));
            }else if (Fit_User.language.equals("zh")){
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

            setData(4);
            title.setText("");
            title.setTextSize(20);




            if (Fit_Preset.MaxWeight == 0 || Fit_Preset.MaxHeight == 0) {
                if (Fit_User.language.equals("ko")) {
                    title.setText("측정 후 이용해주세요.");
                } else if (Fit_User.language.equals("en")) {
                    title.setText("Please use after measurement.");
                }else if (Fit_User.language.equals("zh")){
                    title.setText("请测量后使用。");
                }
                return;
            }
            if (neckage == 0){
                if (Fit_User.language.equals("ko")){
                    title.setText("해당 데이터가 없습니다.");

                }else if (Fit_User.language.equals("en")){
                    title.setText("There is no such data.");

                }else if (Fit_User.language.equals("zh")){
                    title.setText("没有相应数据。");

                }
                return;
            }
            if (Fit_User.language.equals("ko")) {
                final SpannableStringBuilder sps = new SpannableStringBuilder("목 근력 나이는\n" + neckage + "세입니다.");
                sps.setSpan(new AbsoluteSizeSpan(80), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sps.setSpan(new AbsoluteSizeSpan(200), 9, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sps.setSpan(new ForegroundColorSpan(Color.parseColor("#cc1b17")), 9, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sps.setSpan(new StyleSpan(Typeface.BOLD), 9, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sps.setSpan(new StyleSpan(Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                final SpannableStringBuilder sps2 = new SpannableStringBuilder(Fit_User.name + "님의");
                sps2.setSpan(new StyleSpan(Typeface.BOLD), 0, sps2.length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                title.setText(sps2);
                title.append(sps);
            } else if (Fit_User.language.equals("en")) {

                final SpannableStringBuilder sps = new SpannableStringBuilder("neck strength is\n" + neckage + " years old");
                sps.setSpan(new AbsoluteSizeSpan(80), 0, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sps.setSpan(new AbsoluteSizeSpan(200), 17, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sps.setSpan(new ForegroundColorSpan(Color.parseColor("#cc1b17")), 17, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sps.setSpan(new StyleSpan(Typeface.BOLD), 17, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sps.setSpan(new StyleSpan(Typeface.BOLD), 0, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                final SpannableStringBuilder sps2 = new SpannableStringBuilder(Fit_User.name + "'s ");
                sps2.setSpan(new StyleSpan(Typeface.BOLD), 0, sps2.length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                title.setText(sps2);
                title.append(sps);
            } else if (Fit_User.language.equals("zh")){
                // TODO: 2020-07-20 목근육나이 중국
                final SpannableStringBuilder sps = new SpannableStringBuilder("我的颈部肌肉年龄是" + neckage + "岁。");
                sps.setSpan(new AbsoluteSizeSpan(80), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sps.setSpan(new AbsoluteSizeSpan(80), 9, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sps.setSpan(new ForegroundColorSpan(Color.parseColor("#cc1b17")), 9, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sps.setSpan(new StyleSpan(Typeface.BOLD), 9 , 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//                sps.setSpan(new StyleSpan(Typeface.BOLD), 10, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                sps.setSpan(new StyleSpan(Typeface.BOLD), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                final SpannableStringBuilder sps2 = new SpannableStringBuilder(Fit_User.name);
                sps2.setSpan(new StyleSpan(Typeface.BOLD), 0, sps2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sps2.setSpan(new AbsoluteSizeSpan(60), 0, sps2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                title.setText(sps2);
                title.append(sps);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setExercise() {

        listview_container.setVisibility(View.VISIBLE);
        chart_container.setVisibility(View.GONE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Fit_Param param = new Fit_Param();
                param.add("token", Fit_User.token);
                param.add("memberNo", Fit_User.MemberNo);
                param.add("type", "exercise");

                Fit_Address address = new Fit_Address();

                final Fit_HttpConnect httpConnect = new Fit_HttpConnect();

                if (httpConnect.httpConnect(param.getParam(), address.getLoadUserStatistics()) == 200) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            getExericseJson(httpConnect.getReceiveMessage());

                        }
                    });


                } else {
                    Log.d(Constants.TAG, "HttpConnect Error: " + httpConnect.getResponseCode());
                }
            }
        }).start();

    }

    private void getExericseJson(String JsonString) {
        exerciseItems.clear();
        try {
            JSONArray jsonArray = new JSONArray(JsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String dateJson = jsonObject.getString("Date");
                String[] datesplit = dateJson.split("-");
                String month = datesplit[1];
                String day = datesplit[2].substring(0, 2);
                String date = Integer.parseInt(month) + "/" + Integer.parseInt(day);

                int count = jsonObject.getInt("Count");
                int totalCount = jsonObject.getInt("TotalCount");
                int set = jsonObject.getInt("Sett");
                int totalSet = jsonObject.getInt("TotalSet");

                int stop = jsonObject.getInt("Stop");

                exerciseItems.add(new ExerciseChartItem(count, totalCount, set, totalSet, stop, date));

                Log.d(TAG, " - - - - - getExerciseData: Date->" + date);
                Log.d(TAG, " - - - - - getExerciseData: Date->" + count);
                Log.d(TAG, " - - - - - getExerciseData: Date->" + totalCount);
                Log.d(TAG, " - - - - - getExerciseData: Date->" + set);
                Log.d(TAG, " - - - - - getExerciseData: Date->" + totalSet);
                Log.d(TAG, " - - - - - getExerciseData: Date->" + stop);

            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            exerciseAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }).start();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int makeNeckAge(int neckage) {
        String sub = String.valueOf(neckage).substring(0, 1);
        String sub2 = String.valueOf(Fit_User.age).substring(0, 1);
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
