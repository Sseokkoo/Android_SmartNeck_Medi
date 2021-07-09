package com.smartneck.twofive.util.User

class Admin {

    var account: String = ""
    var isBackup: Boolean = false
    var password: String = ""
    var comName: String = ""
    var phone: String = ""
    var name: String = ""
    var adminTel: String = ""
    var adminTitle: String = ""
    var adminEmail: String = ""
    var ceoName: String = ""
    var ceoPhone: String = ""
    var ceoTel: String = ""
    var ceoEmail: String = ""
    var address: String = ""
    var country: String = ""
    var createDate: String = ""



    constructor(account: String, isBackup: Boolean) {
        this.account = account
        this.isBackup = isBackup

    }

    constructor(account: String, isBackup: Boolean , createDate: String) {
        this.account = account
        this.isBackup = isBackup
        this.createDate = createDate

    }

    constructor()


}