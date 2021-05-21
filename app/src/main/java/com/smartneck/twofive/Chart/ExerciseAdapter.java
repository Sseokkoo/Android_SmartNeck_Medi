package com.smartneck.twofive.Chart;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.smartneck.twofive.Chart.ChartItem.ExerciseChartItem;
import com.smartneck.twofive.R;
import com.smartneck.twofive.util.User.Exercise;

import static android.support.constraint.Constraints.TAG;

public class ExerciseAdapter extends BaseAdapter {
    ArrayList<Exercise> exerciseItems;
    LayoutInflater inflater;
    Holder holder;
    public ExerciseAdapter(ArrayList<Exercise> exerciseItems, LayoutInflater inflater) {
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

        holder.tv_idx = view.findViewById(R.id.item_exercise_idx);
        holder.tv_date = view.findViewById(R.id.item_exercise_date);
        holder.tv_count = view.findViewById(R.id.item_exercise_count);
        holder.tv_set = view.findViewById(R.id.item_exercise_set);
        holder.tv_stop = view.findViewById(R.id.item_exercise_stop);

        holder.tv_date.setText(exerciseItems.get(i).getDate().split(" ")[0]);
//        holder.tv_date.setText(exerciseItems.get(i).getDate());

        holder.tv_idx.setText(String.valueOf(exerciseItems.get(i).getIdx()));
        holder.tv_count.setText(exerciseItems.get(i).getCount() + "/" + exerciseItems.get(i).getTotalCount());
        holder.tv_set.setText(exerciseItems.get(i).getSet() + "/" + exerciseItems.get(i).getTotalSet());
        holder.tv_stop.setText(String.valueOf(exerciseItems.get(i).getStop()));
        Log.e(TAG, "getView: " + i );
        return view;
    }

    class Holder{
        TextView tv_date , tv_count , tv_set , tv_stop , tv_idx;
    }
}
