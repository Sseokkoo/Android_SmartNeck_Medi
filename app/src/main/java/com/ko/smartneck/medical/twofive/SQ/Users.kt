package com.ko.smartneck.medical.twofive.SQ



class Users {
    var id : Int = 0
    var firstName : String = ""
    var lastName : String = ""

    constructor(){}

    constructor(id:Int, firstName:String, lastName:String){
        this.id=id
        this.firstName=firstName
        this.lastName=lastName
    }

}