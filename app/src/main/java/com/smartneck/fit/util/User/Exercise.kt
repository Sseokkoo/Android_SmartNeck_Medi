package com.smartneck.fit.util.User

class Exercise(var idx: Int , var date: String, var count: Int, var totalCount: Int, var set: Int, var totalSet: Int, var stop: Int , var device: String){


    override fun toString(): String {
        return "Exercise(date='$date', count=$count, totalCount=$totalCount, set=$set, totalSet=$totalSet, stop=$stop)"
    }
}