package com.ko.smartneck.medical.twofive.Management

class MeasurementFull (var idx: Int, var name: String, var weight: Int , var height: Int, var device: String, var date: String, var gender: String , var uid: String) {



    override fun toString(): String {
        return "MeasurementFull(idx=$idx, name='$name', weight=$weight, height=$height, device='$device', date='$date', gender='$gender')"
    }
}