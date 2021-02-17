package com.ko.smartneck.medical.twofive.util.User

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class Member (var memberNo: Int ,var admin: String , var uid: String, var name: String , var phone: String , var birth: String , var gender: String , var country: String , var lately: String , var deleted: Boolean) : Serializable {



    fun getAge(): Int {
        val now = System.currentTimeMillis()
        val date = Date(now)
        val sdf = SimpleDateFormat("yyyy")
        val getYear: String = sdf.format(date)
        val currentYear = Integer.parseInt(getYear)
        val split = birth.split("-").toTypedArray()

        var age = currentYear - Integer.parseInt(split[0]) + 1
        return age
    }

    override fun toString(): String {
        return "Member(memberNo=$memberNo, admin='$admin', uid='$uid', name='$name', phone='$phone', birth='$birth', gender='$gender', country='$country', lately='$lately', deleted=$deleted)"
    }


}

