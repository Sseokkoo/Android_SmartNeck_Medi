package com.smartneck.fit.util.User

class Membe {
    var name: String? = null
    var phone: String? = null
    var birth: String? = null
    var gender: String? = null
    var country: String? = null
    var lately: String? = null

    constructor() {}
    constructor(name: String?, phone: String?, birth: String?, gender: String?, country: String?, lately: String?) {
        this.name = name
        this.phone = phone
        this.birth = birth
        this.gender = gender
        this.country = country
        this.lately = lately
    }

}