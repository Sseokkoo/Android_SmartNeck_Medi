package com.smartneck.twofive.SQ

class ExerciseSQ(var idx: Int,
                 var memberNo: Int,
                 var count: Int,
                 var totalCount: Int,
                 var set: Int,
                 var totalSet: Int,
                 var stop: Int,
                 var date: Int) {
    //운동 정보 데이터 모델


    override fun toString(): String {
        return "ExerciseSQ{" +
                "idx=" + idx.toString() +
                ", memberNo=" + memberNo.toString() +
                ", count=" + count.toString() +
                ", totalCount=" + totalCount.toString() +
                ", set=" + set.toString() +
                ", totalSet=" + totalSet.toString() +
                ", stop=" + stop.toString() +
                ", date=" + date +
                '}'
    }

}