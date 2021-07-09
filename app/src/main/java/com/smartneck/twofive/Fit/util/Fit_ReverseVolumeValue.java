package com.smartneck.twofive.Fit.util;

public class Fit_ReverseVolumeValue {

    /*
    Home 볼륨 방향이 반대이므로 볼륨값을 반대로 적용해야함
    ex90일때 -> 0, 89일때 ->1
    */

    public static int getReverseVolumeValue(int volume){

        int reverseVolumeValue = (volume * -1) + 90;

        return reverseVolumeValue;
    }
}
