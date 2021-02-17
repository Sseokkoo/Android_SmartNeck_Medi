package com.ko.smartneck.medical.twofive.Notice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.ko.smartneck.medical.twofive.R;

public class NoticeAdapter extends BaseAdapter {
    ArrayList<NoticeItem> noticeItems;
    LayoutInflater inflater;
    Holder holder;
    public NoticeAdapter(ArrayList<NoticeItem> noticeItems, LayoutInflater inflater) {
        this.noticeItems = noticeItems;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return noticeItems.size();
    }

    @Override
    public Object getItem(int i) {
        return noticeItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.item_notice, null);
            holder = new Holder();
        }


        holder.tv_date = view.findViewById(R.id.notice_list_date);
        holder.tv_title = view.findViewById(R.id.notice_list_title);
        holder.tv_no = view.findViewById(R.id.notice_list_no);


        holder.tv_date.setText(noticeItems.get(i).getDate());
        holder.tv_no.setText(String.valueOf(noticeItems.get(i).getNo()));
        holder.tv_title.setText(noticeItems.get(i).getTitle());
        return view;
    }

    class Holder{
        TextView tv_date , tv_title , tv_no;
    }
}
