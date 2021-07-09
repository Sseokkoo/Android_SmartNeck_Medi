package com.smartneck.twofive.util.User

import java.io.Serializable

class Preset : Serializable {
    var admin: String = ""
    var memberNo = 0
    var count = 5
    var set = 2
    var stop = 3
    var seat = 0
    var setup = 0
    var breakTime = 15
    var maxWeight = 0
    var maxHeight = 0
    var strength = 0
    var soundType = ""
    var heightSelected = 0
    var isBreakTime = false
    var uid = ""
    var device: String = ""

    constructor() {}
    constructor(admin: String, memberNo: Int,uid: String, count: Int, set: Int, stop: Int, seat: Int, setup: Int, breakTime: Int, maxWeight: Int, maxHeight: Int, strength: Int, soundType: String, heightSelected: Int , device: String) {
        this.admin = admin
        this.memberNo = memberNo
        this.count = count
        this.set = set
        this.stop = stop
        this.seat = seat
        this.setup = setup
        this.breakTime = breakTime
        this.maxWeight = maxWeight
        this.maxHeight = maxHeight
        this.strength = strength
        this.soundType = soundType
        this.heightSelected = heightSelected
        this.uid = uid
        this.device = device
    }




    fun getHeightSetting(): Float {
        when (heightSelected) {
            0 -> return 0.4f
            1 -> return 0.45f
            2 ->  return 0.5f
            3 ->  return 0.55f
            4 ->  return 0.6f
            5 ->  return 0.65f
            6 ->  return 0.7f
            7 ->  return 0.75f
            8 ->  return 0.8f
            9 ->  return 0.85f
            10 -> return 0.9f
            11 -> return 0.95f
            12 -> return 1f
        }
        return 0.7f
    }

    override fun toString(): String {
        return "Preset(admin='$admin', memberNo=$memberNo, count=$count, set=$set, stop=$stop, seat=$seat, setup=$setup, breakTime=$breakTime, maxWeight=$maxWeight, maxHeight=$maxHeight, strength=$strength, soundType='$soundType', heightSelected=$heightSelected, isBreakTime=$isBreakTime, uid='$uid')"
    }


}