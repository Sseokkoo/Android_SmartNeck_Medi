package com.ko.smartneck.medical.twofive.BleDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.ko.smartneck.medical.twofive.R;

public class BleAdapter extends BaseAdapter {

    ArrayList<BleItem> bleItems;
    LayoutInflater inflater;
    Holder holder;
    public BleAdapter(ArrayList<BleItem> bleItems, LayoutInflater inflater) {
        this.bleItems = bleItems;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return bleItems.size();
    }

    @Override
    public Object getItem(int position) {
        return bleItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_ble_dialog , parent , false);
            holder = new Holder();
        }
        holder.tv_name = convertView.findViewById(R.id.item_ble_dialog_tv);
        holder.tv_name.setText(bleItems.get(position).getName().substring(13,18));
//        holder.tv_name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((ChartActivity)ChartActivity.mContext).setConnect(bleItems.get(position).getBluetoothDevice());
//            }
//        });

        return convertView;
    }

    class Holder {
        TextView tv_name;
    }
}
