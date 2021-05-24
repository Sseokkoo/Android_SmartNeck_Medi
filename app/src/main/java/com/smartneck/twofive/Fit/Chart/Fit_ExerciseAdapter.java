package com.smartneck.twofive.Fit.Chart;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.smartneck.twofive.Chart.ChartItem.ExerciseChartItem;
import com.smartneck.twofive.R;

import static android.support.constraint.Constraints.TAG;

public class Fit_ExerciseAdapter extends BaseAdapter {
    ArrayList<ExerciseChartItem> exerciseItems;
    LayoutInflater inflater;
    Holder holder;
    public Fit_ExerciseAdapter(ArrayList<ExerciseChartItem> exerciseItems, LayoutInflater inflater) {
        this.exerciseItems = exerciseItems;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return exerciseItems.size();
    }

    @Override
    public Object getItem(int i) {
        return exerciseItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.item_exercise, null);
            holder = new Holder();
        }

        holder.tv_date = view.findViewById(R.id.item_exercise_date);
        holder.tv_count = view.findViewById(R.id.item_exercise_count);
        holder.tv_set = view.findViewById(R.id.item_exercise_set);
        holder.tv_stop = view.findViewById(R.id.item_exercise_stop);

        holder.tv_date.setText(exerciseItems.get(i).getDate());
        holder.tv_count.setText(exerciseItems.get(i).getCount() + "/" + exerciseItems.get(i).getTotalCount());
        holder.tv_set.setText(exerciseItems.get(i).getSet() + "/" + exerciseItems.get(i).getTotalSet());
        holder.tv_stop.setText(String.valueOf(exerciseItems.get(i).getStop()));
        Log.e(TAG, "getView: " + i );
        return view;
    }

    class Holder{
        TextView tv_date , tv_count , tv_set , tv_stop;
    }
}
