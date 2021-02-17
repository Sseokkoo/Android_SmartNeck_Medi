package com.ko.smartneck.medical.twofive.CustomerSupport.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.ko.smartneck.medical.twofive.CustomerSupport.Item.CustomerFaqItem;
import com.ko.smartneck.medical.twofive.R;

public class CustomerFaqAdapter extends BaseAdapter {
    ArrayList<CustomerFaqItem> customerFaqItems;
    LayoutInflater inflater;
    Holder holder;
    public CustomerFaqAdapter(ArrayList<CustomerFaqItem> customerFaqItems, LayoutInflater inflater) {
        this.customerFaqItems = customerFaqItems;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return customerFaqItems.size();
    }

    @Override
    public Object getItem(int i) {
        return customerFaqItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.item_faq, null);
            holder = new Holder();
            holder.tv_questions = view.findViewById(R.id.faq_questions);
            holder.tv_answer = view.findViewById(R.id.faq_answer);

            holder.tv_questions.setText("Q. " + customerFaqItems.get(i).getQuestions());
            holder.tv_answer.setText("A. " + customerFaqItems.get(i).getAnswer());
            holder.tv_answer.setVisibility(View.GONE);
        }

        return view;
    }

    class Holder{
        TextView tv_questions , tv_answer;
    }
}
