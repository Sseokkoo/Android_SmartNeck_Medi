package com.smartneck.twofive.BleDialog;

import android.bluetooth.BluetoothDevice;

public class BleItem {

    BluetoothDevice bluetoothDevice;
    String address;
    String name;

    public BleItem(BluetoothDevice bluetoothDevice, String address, String name) {
        this.bluetoothDevice = bluetoothDevice;
        this.address = address;
        this.name = name;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
