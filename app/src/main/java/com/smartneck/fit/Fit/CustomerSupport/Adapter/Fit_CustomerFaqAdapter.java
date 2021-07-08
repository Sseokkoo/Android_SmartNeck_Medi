package com.smartneck.fit.Fit.CustomerSupport.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartneck.fit.Fit.CustomerSupport.Item.Fit_CustomerFaqItem;
import com.smartneck.fit.R;

import java.util.ArrayList;

public class Fit_CustomerFaqAdapter extends BaseAdapter {
    ArrayList<Fit_CustomerFaqItem> customerFaqItems;
    LayoutInflater inflater;
    Holder holder;
    public Fit_CustomerFaqAdapter(ArrayList<Fit_CustomerFaqItem> customerFaqItems, LayoutInflater inflater) {
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
