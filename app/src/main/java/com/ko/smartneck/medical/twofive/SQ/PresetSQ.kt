package com.ko.smartneck.medical.twofive.SQ

class PresetSQ(//환자 운동설정 데이터 모델
        var idx: Int, //환자 번호
        var memberNo: Int, //운동 횟수 설정
        var count: Int, //운동 세트 설정
        var set: Int, //운동 정지시간
        var stop: Int, //의자 높이 설정
        var seat: Int, //운동 레벨 설정
        var setup: Int, //측정 운동 레벨 설정
        var measureSetup: Int, //측정 최대 유연성
        var maxHeight: Int, //측정 최대 무게
        var maxWeight: Int, //운동강도 1,2,3
        var strength: Int, //쉬는시간
        var breakTime: Int) {

    override fun toString(): String {
        return "PresetSQ{" +
                "idx=" + idx.toString() +
                ", memberNo=" + memberNo.toString() +
                ", count=" + count.toString() +
                ", set=" + set.toString() +
                ", stop=" + stop.toString() +
                ", seat=" + seat.toString() +
                ", setup=" + setup.toString() +
                ", measureSetup=" + measureSetup.toString() +
                ", maxHeight=" + maxHeight.toString() +
                ", maxWeight=" + maxWeight.toString() +
                ", strength=" + strength.toString() +
                ", breakTime=" + breakTime +
                '}'.toInt()
    }

}