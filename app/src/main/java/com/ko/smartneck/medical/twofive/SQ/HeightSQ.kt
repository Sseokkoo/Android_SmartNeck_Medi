package com.ko.smartneck.medical.twofive.SQ

class HeightSQ(//유연성 측정 데이터 모델
        //환자 번호
        var memberNo: Int, //측정 인덱스
        var idx: Int, //측정 최대 유연성
        var maxHeight: Int, //측정 날짜
        var date: String) {

    override fun toString(): String {
        return "HeightSQ{" +
                "memberNo=" + memberNo.toString() +
                ", idx=" + idx.toString() +
                ", maxHeight=" + maxHeight.toString() +
                ", date='" + date + '\'' +
                '}'
    }

}