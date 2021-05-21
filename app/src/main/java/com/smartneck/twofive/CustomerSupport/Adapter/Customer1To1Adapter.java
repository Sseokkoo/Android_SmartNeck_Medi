package com.smartneck.twofive.CustomerSupport.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.smartneck.twofive.CustomerSupport.Item.Customer1To1Item;
import com.smartneck.twofive.R;

public class Customer1To1Adapter extends BaseAdapter {
    ArrayList<Customer1To1Item> customer1To1Items;
    LayoutInflater inflater;
    Holder holder;
    public Customer1To1Adapter(ArrayList<Customer1To1Item> customer1To1Items, LayoutInflater inflater) {
        this.customer1To1Items = customer1To1Items;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return customer1To1Items.size();
    }

    @Override
    public Object getItem(int i) {
        return customer1To1Items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.item_1to1, null);
            holder = new Holder();
        }


        holder.tv_date = view.findViewById(R.id.customer_1to1_list_date);
        holder.tv_title = view.findViewById(R.id.customer_1to1_list_title);
        holder.tv_no = view.findViewById(R.id.customer_1to1_list_no);
        holder.tv_state = view.findViewById(R.id.customer_1to1_list_state);

        holder.tv_state.setText(customer1To1Items.get(i).getState());
        holder.tv_date.setText(customer1To1Items.get(i).getDate());
        holder.tv_title.setText(customer1To1Items.get(i).getContents());
        holder.tv_no.setText(String.valueOf(i + 1));
        holder.tv_date.setText(customer1To1Items.get(i).getDate());
//        holder.tv_no.setText(String.valueOf(customer1To1Items.get(i).getNo()));
//        holder.tv_title.setText(customer1To1Items.get(i).getTitle());
        return view;
    }

    class Holder{
        TextView tv_date , tv_title , tv_no , tv_state;
    }
}
