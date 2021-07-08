package com.smartneck.fit.Management

class Measurement(var idx: Int, var name: String, var value: Int, var device: String, var date: String , var gender: String , var uid: String) {

    override fun toString(): String {
        return "Measurement(idx=$idx, name='$name', value=$value, device='$device', date='$date', gender='$gender')"
    }
}