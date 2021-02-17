package com.ko.smartneck.medical.twofive.SQ

class UserSQ(//회원 등록시 사용할 환자 데이터 모델
        //환자 인덱스
        var idx: Int, //환자 이름
        var name: String, //환자 생년월일
        var birth: String, //환자 성별
        var gender: String, //환자 휴대폰번호
        var phone: String, //환자 국가 -> 앱 시작 시 현재 위치 기준으로 국가 가져오기
        var country: String, //등록 일자
        var createDate: String, //최근 방문 일자
        var latelyDate: String) {

    override fun toString(): String {
        return "UserSQ{" +
                "idx=" + idx.toString() +
                ", name='" + name + '\''.toString() +
                ", birth='" + birth + '\''.toString() +
                ", gender='" + gender + '\''.toString() +
                ", phone='" + phone + '\''.toString() +
                ", country='" + country + '\''.toString() +
                ", createDate='" + createDate + '\''.toString() +
                ", latelyDate='" + latelyDate + '\'' +
                '}'
    }

}