package com.smartneck.twofive.Review;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.smartneck.twofive.R;

public class ReviewAdapter extends BaseAdapter {
    ArrayList<ReviewItem> reviewItems;
    LayoutInflater inflater;
    Holder holder;
    public ReviewAdapter(ArrayList<ReviewItem> reviewItems, LayoutInflater inflater) {
        this.reviewItems = reviewItems;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return reviewItems.size();
    }

    @Override
    public Object getItem(int i) {
        return reviewItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.item_review, null);
            holder = new Holder();
        }


        holder.tv_date = view.findViewById(R.id.review_date);
        holder.tv_contents = view.findViewById(R.id.review_contents);
        holder.tv_id = view.findViewById(R.id.review_id);

        holder.tv_date.setText(reviewItems.get(i).getDate());
        holder.tv_contents.setText(reviewItems.get(i).getContents());
        holder.tv_id.setText(reviewItems.get(i).getName());
        return view;
    }

    class Holder{
        TextView tv_date , tv_contents , tv_id;
    }
}
