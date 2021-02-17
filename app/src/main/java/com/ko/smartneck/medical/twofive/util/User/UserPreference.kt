package com.ko.smartneck.medical.twofive.util.User

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.ko.smartneck.medical.twofive.Member.MemberSelectActivity
import com.ko.smartneck.medical.twofive.Member.MemberSelectActivity.admin
import com.ko.smartneck.medical.twofive.util.Address
import com.ko.smartneck.medical.twofive.util.Constants
import com.ko.smartneck.medical.twofive.util.Constants.TAG
import com.ko.smartneck.medical.twofive.util.HttpConnect
import com.ko.smartneck.medical.twofive.util.Param
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserPreference(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("medical", Context.MODE_PRIVATE)
    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return prefs.getBoolean(key, defValue)
    }

    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }

    fun setBoolean(key: String, bool: Boolean){
        prefs.edit().putBoolean(key, bool).apply()

    }


    fun join(account: String, password: String , isBackup: Boolean , createDate: String): Boolean {

        val adminJson = getString("admin", "")
        Log.d(TAG, "admin Json " + adminJson)
        if (adminJson == "") {
            Log.d(TAG, "first insert")
            val json = JSONObject()
            json.put("account", account)
            json.put("password", password)
            json.put("backup" , isBackup)
            json.put("create_date" ,createDate )
            val jsonArray = JSONArray()
            jsonArray.put(json)
            val outer = JSONObject()
            outer.put("admin", jsonArray)
            Log.d(TAG, outer.toString())
            setString("admin", outer.toString())
        } else {
            Log.d(TAG, "insert")

            val jo = JSONObject(adminJson)
            val jsonArray = jo.getJSONArray("admin")

            val jsonArray2 = JSONArray()
            val outer = JSONObject()
            Log.d(TAG, "json Length " + jsonArray.length())

            for (i in 0 until jsonArray.length()) {
                val adminOb = jsonArray.getJSONObject(i)
                if (account == adminOb.optString("account")){
                    return true
                }
                val json = JSONObject()
                json.put("account", adminOb.getString("account"))
                json.put("password", adminOb.getString("password"))
                json.put("backup" , adminOb.getBoolean("backup"))
                json.put("create_date" , adminOb.getString("create_date"))

                jsonArray2.put(json)
//                outer.put("admin" , jsonArray)

            }
            val json = JSONObject()
            json.put("account", account)
            json.put("password", password)
            json.put("backup" , isBackup)
            json.put("create_date" , createDate)

            jsonArray2.put(json)
            outer.put("admin", jsonArray2)
//            prefs.edit().remove("admin")
            Log.d(TAG, "outerString " + outer.toString())

            setString("admin", outer.toString())

        }

        return true
    }

    fun login(account: String, password: String): Admin {
        val adminJson = getString("admin", "")
        if (adminJson == "") return Admin()

        val jo = JSONObject(adminJson)
        val jsonArray = jo.getJSONArray("admin")

        for (i in 0 until jsonArray.length()) {
            val adminOb = jsonArray.getJSONObject(i)
            Log.d(TAG, "idx -> $i  content ->${adminOb.toString()}")
            if (account == adminOb.optString("account", "") && password == adminOb.optString("password", "")) {
                return Admin(account , adminOb.optBoolean("backup", false) , adminOb.optString("create_date", ""))
            }
        }

        return Admin()
    }

    val PRE_KEY_MEMBER = "member"
    fun addMember(admin: String, name: String, phone: String, birth: String, gender: String, country: String, lately: String) {



        val random = java.util.Random().nextInt(89999999) + 10000000
        val uid = "$random$admin$name".md5()
        Log.e(TAG , "###$uid")
        var memberNo = 0
        val outer = JSONObject()
        val memberJson = getString("member", "")
        Log.d(TAG, "member Json " + memberJson)
        if (memberJson == "") {
            Log.d(TAG, "first insert")
            val json = JSONObject()
            json.put("memberNo", 0)
            json.put("admin", admin)
            json.put("name", name)
            json.put("phone", phone)
            json.put("birth", birth)
            json.put("gender", gender)
            json.put("country", "ko")
            json.put("lately", "")
            json.put("uid" , uid)
            json.put("deleted" , false)

            val jsonArray = JSONArray()
            jsonArray.put(json)
            outer.put("member", jsonArray)
            Log.d(TAG, outer.toString())
            setString("member", outer.toString())
            addPreset(admin, 0 , uid)

        } else {
            Log.d(TAG, "insert")

            val jo = JSONObject(memberJson)
            val jsonArray = jo.getJSONArray("member")

            val jsonArray2 = JSONArray()
            Log.d(TAG, "json Length " + jsonArray.length())

            for (i in 0 until jsonArray.length()) {
                val adminOb = jsonArray.getJSONObject(i)

                val _admin = adminOb.optString("admin")
                val _name = adminOb.optString("name")
                val _phone = adminOb.optString("phone")
                val _birth = adminOb.optString("birth")
                val _gender = adminOb.optString("gender")
                val _country = adminOb.optString("country")
                val _lately = adminOb.optString("lately")
                val _uid = adminOb.optString("uid")
                val _deleted = adminOb.optBoolean("deleted")

//                tempArrayList.add(Member( i ,admin ,  name, phone, birth, gender, country, lately))
                val json = JSONObject()
                json.put("memberNo", i)
                json.put("admin", _admin)
                json.put("name", _name)
                json.put("phone", _phone)
                json.put("birth", _birth)
                json.put("gender", _gender)
                json.put("country", _country)
                json.put("lately", _lately)
                json.put("uid" , _uid)
                json.put("deleted" , _deleted)

                jsonArray2.put(json)
                outer.put("admin", jsonArray)
                memberNo = i
            }
            memberNo++
            val json = JSONObject()
            json.put("memberNo", memberNo)
            json.put("admin", admin)
            json.put("name", name)
            json.put("phone", phone)
            json.put("birth", birth)
            json.put("gender", gender)
            json.put("country", "ko")
            json.put("uid" , uid)
            json.put("deleted" , false)
            jsonArray2.put(json)
            outer.put("member", jsonArray2)
//            prefs.edit().remove("admin")
            Log.d(TAG, "outerString " + outer.toString())

            setString("member", outer.toString())
            addPreset(admin, memberNo , uid)
        }
//        backupData(PRE_KEY_MEMBER , outer.toString())


        if (MemberSelectActivity.admin.isBackup){
            Thread{
                val httpConnect = HttpConnect()
                val param = Param()
                param.add("type" , "insert")
                param.add("admin" , admin)
                param.add("member_no" , memberNo)
                param.add("name" , name)
                param.add("phone" , phone)
                param.add("birth" , birth)
                param.add("gender" , gender)
                param.add("country" , country)
                param.add("lately" , lately)
                param.add("uid" , uid)

                httpConnect.httpConnect(param.value , Address().member , true)

            }.start()
        }

    }



    val PRE_KEY_PRESET = "preset"
    fun addPreset(admin: String, memberNo: Int , uid: String) {
        val outer = JSONObject()

        val presetJson = getString("preset", "")
        if (presetJson.length == 0) {
            val json = JSONObject()
            val array = JSONArray()
            json.put("admin", admin)
            json.put("memberNo", memberNo)
            json.put("uid" , uid)
            json.put("count", 5)
            json.put("set", 2)
            json.put("stop", 3)
            json.put("seat", 0)
            json.put("setup", 0)
            json.put("breakTime", 15)
            json.put("maxWeight", 0)
            json.put("maxHeight", 0)
            json.put("strength", 3)
            json.put("soundType", "female")
            json.put("heightSelected", 8)
            json.put("device" , Constants.DEVICE_NAME)

            array.put(json)
            outer.put("preset", array)

            setString("preset", outer.toString())
            Log.d(TAG, "insert Preset ${outer.toString()} ")

        } else {
            Log.d(TAG, "# # # # # addPreset: $presetJson")
            val jo = JSONObject(presetJson)
            val jsonArray = jo.getJSONArray("preset")

            val array = JSONArray()

            for (i in 0 until jsonArray.length()) {
                val presetOb = jsonArray.getJSONObject(i)

                val _admin = presetOb.optString("admin")
                val _memberNo = presetOb.optInt("memberNo")
                val _count = presetOb.optInt("count")
                val _set = presetOb.optInt("set")
                val _stop = presetOb.optInt("stop")
                val _seat = presetOb.optInt("seat")
                val _setup = presetOb.optInt("setup")
                val _breakTime = presetOb.optInt("breakTime")
                val _maxWeight = presetOb.optInt("maxWeight")
                val _maxHeight = presetOb.optInt("maxHeight")
                val _strength = presetOb.optInt("strength")
                val _soundType = presetOb.optString("soundType")
                val _heightSelected = presetOb.optInt("heightSelected", 8)
                val _uid = presetOb.optString("uid")
                val _device = presetOb.optString("device")

                val json = JSONObject()
                json.put("admin", _admin)
                json.put("memberNo", _memberNo)
                json.put("count", _count)
                json.put("set", _set)
                json.put("stop", _stop)
                json.put("seat", _seat)
                json.put("setup", _setup)
                json.put("breakTime", _breakTime)
                json.put("maxWeight", _maxWeight)
                json.put("maxHeight", _maxHeight)
                json.put("strength", _strength)
                json.put("soundType", _soundType)
                json.put("heightSelected", _heightSelected)
                json.put("uid" , _uid)
                json.put("device" , _device)

                array.put(json)
                outer.put("preset", array)
            }
            val json = JSONObject()

            json.put("admin", admin)
            json.put("memberNo", memberNo)
            json.put("count", 5)
            json.put("set", 2)
            json.put("stop", 3)
            json.put("seat", 0)
            json.put("setup", 0)
            json.put("breakTime", 15)
            json.put("maxWeight", 0)
            json.put("maxHeight", 0)
            json.put("strength", 3)
            json.put("soundType", "female")
            json.put("heightSelected", 8)
            json.put("uid" , uid)
            json.put("device" , Constants.DEVICE_NAME)

            array.put(json)
            outer.put("preset", array)

            setString("preset", outer.toString())
            Log.d(TAG, "insert Preset ${outer.toString()} ")


        }

//        backupData(PRE_KEY_PRESET , outer.toString())



    }

    fun getPreset(admin: String, uid: String): Preset {
        val presetJson = getString("preset", "")
        Log.e(TAG, "getPreset: presetJson ${presetJson} ")
        if (presetJson == "" || presetJson == "{}") return Preset()

        val jo = JSONObject(presetJson)
        val jsonArray = jo.getJSONArray("preset")
        var preset: Preset = Preset()
        for (i in 0 until jsonArray.length()) {
            val presetOb = jsonArray.getJSONObject(i)
            Log.d(TAG, "idx -> $i  content ->${presetOb.toString()}")
            if (admin == presetOb.optString("admin", "") &&
                    uid == presetOb.optString("uid")) {
                val _admin = presetOb.optString("admin")
                val _memberNo = presetOb.optInt("memberNo")
                val _count = presetOb.optInt("count")
                val _set = presetOb.optInt("set")
                val _stop = presetOb.optInt("stop")
                val _seat = presetOb.optInt("seat")
                val _setup = presetOb.optInt("setup")
                val _breakTime = presetOb.optInt("breakTime")
                val _maxWeight = presetOb.optInt("maxWeight")
                val _maxHeight = presetOb.optInt("maxHeight")
                val _strength = presetOb.optInt("strength")
                val _soundType = presetOb.optString("soundType")
                val _heightSelected = presetOb.optInt("heightSelected", 8)
                val _uid = presetOb.optString("uid")
                val _device = presetOb.optString("device")

                preset = Preset(_admin, _memberNo,_uid, _count, _set, _stop, _seat, _setup, _breakTime, _maxWeight,
                        _maxHeight, _strength, _soundType, _heightSelected , _device)

                Log.d(TAG, "getPreset$preset")
                break
            }
        }

        return preset
    }


    fun editPreset(preset: Preset) {
        Log.d(TAG, "editPreset: ${preset.toString()}")
        val presets = ArrayList<Preset>()
        val presetJson = getString("preset", "")
        val jo = JSONObject(presetJson)
        val jsonArray = jo.getJSONArray("preset")


        for (i in 0 until jsonArray.length()) {
            val presetOb = jsonArray.getJSONObject(i)
            val _admin = presetOb.optString("admin")
            val _memberNo = presetOb.optInt("memberNo")
            val _count = presetOb.optInt("count")
            val _set = presetOb.optInt("set")
            val _stop = presetOb.optInt("stop")
            val _seat = presetOb.optInt("seat")
            val _setup = presetOb.optInt("setup")
            val _breakTime = presetOb.optInt("breakTime")
            val _maxWeight = presetOb.optInt("maxWeight")
            val _maxHeight = presetOb.optInt("maxHeight")
            val _strength = presetOb.optInt("strength")
            val _soundType = presetOb.optString("soundType")
            val _heightSelected = presetOb.optInt("heightSelected", 8)
            val _uid = presetOb.optString("uid")
            val _device = presetOb.optString("device")
            presets.add(Preset(_admin, _memberNo,_uid ,_count, _set, _stop, _seat, _setup, _breakTime, _maxWeight,
                    _maxHeight, _strength, _soundType, _heightSelected , _device))

        }

        for (i in 0 until presets.size){
            if (presets[i].uid == preset.uid && presets[i].admin == preset.admin){

                presets[i] = preset
                break
            }
        }

        val outer = JSONObject()
        val array = JSONArray()

        for (i in 0 until presets.size){
            val _admin = presets[i].admin
            val _memberNo = presets[i].memberNo
            val _count = presets[i].count
            val _set = presets[i].set
            val _stop = presets[i].stop
            val _seat = presets[i].seat
            val _setup = presets[i].setup
            val _breakTime = presets[i].breakTime
            val _maxWeight = presets[i].maxWeight
            val _maxHeight = presets[i].maxHeight
            val _strength = presets[i].strength
            val _soundType = presets[i].soundType
            val _heightSelected = presets[i].heightSelected
            val _uid = presets[i].uid
            val _device = presets[i].device

            val json = JSONObject()
            json.put("admin", _admin)
            json.put("memberNo", _memberNo)
            json.put("count", _count)
            json.put("set", _set)
            json.put("stop", _stop)
            json.put("seat", _seat)
            json.put("setup", _setup)
            json.put("breakTime", _breakTime)
            json.put("maxWeight", _maxWeight)
            json.put("maxHeight", _maxHeight)
            json.put("strength", _strength)
            json.put("soundType", _soundType)
            json.put("heightSelected", _heightSelected)
            json.put("uid", _uid)
            json.put("device" , _device)

            array.put(json)
            outer.put("preset", array)

        }

        setString("preset" , outer.toString())
//        backupData(PRE_KEY_PRESET , outer.toString())
        if(MemberSelectActivity.admin.isBackup){
            Thread{
                val httpConnect = HttpConnect()
                val param = Param()
                param.add("admin" , preset.admin)
                param.add("member_no" , preset.memberNo)
                param.add("count" , preset.count)
                param.add("set" , preset.set)
                param.add("stop" , preset.stop)
                param.add("setup" , preset.setup)
                param.add("break_time" , preset.breakTime)
                param.add("max_weight" , preset.maxWeight)
                param.add("max_height" , preset.maxHeight)
                param.add("strength" , preset.strength)
                param.add("sound_type" , preset.soundType)
                param.add("height_selected" , preset.heightSelected)
                param.add("uid" , preset.uid)
                param.add("device" , Constants.DEVICE_NAME)
                httpConnect.httpConnect(param.value , Address().preset , true)
            }.start()

        }
        Log.d(TAG , "edit Preset ${outer.toString()}")


    }



    fun getMember(admin: String): ArrayList<Member> {
        val memberList = ArrayList<Member>()
        val memberJson = getString("member", "")
        if (memberJson == "") return memberList

        val jo = JSONObject(memberJson)
        val jsonArray = jo.getJSONArray("member")

        for (i in 0 until jsonArray.length()) {
            val memberOb = jsonArray.getJSONObject(i)
            Log.d(TAG, "idx -> $i  content ->${memberOb.toString()}")
            if (admin == memberOb.optString("admin", "")) {

                val memberNo = memberOb.optInt("memberNo")
                val name = memberOb.optString("name")
                val phone = memberOb.optString("phone")
                val birth = memberOb.optString("birth")
                val gender = memberOb.optString("gender")
                val country = memberOb.optString("country")
                val lately = memberOb.optString("lately")
                val uid = memberOb.optString("uid")
                val deleted = memberOb.optBoolean("deleted" , false)
                Log.d(TAG, "select member Name -> $i  ->${name}")
                Log.d(TAG, "select member no -> $i  ->${memberNo}")

                if (!deleted){
                    memberList.add(Member(memberNo, admin, uid ,  name, phone, birth, gender, country, lately , deleted))
                }
            }
        }

        return memberList
    }

    fun editMember(users: ArrayList<Member>) {

        if (users.size == 0){
            setString("member", "")
            return
        }
        val outer = JSONObject()
        val array = JSONArray()

        for (i in 0 until users.size) {

            val json = JSONObject()
            val _memberNo = users[i].memberNo
            val _admin = users[i].admin
            val _name = users[i].name
            val _phone = users[i].phone
            val _birth = users[i].birth
            val _gender = users[i].gender
            val _country = users[i].country
            val _lately = users[i].lately
            val _uid = users[i].uid
            val _deleted = users[i].deleted
            json.put("memberNo", _memberNo)
            json.put("admin", _admin)
            json.put("name", _name)
            json.put("phone", _phone)
            json.put("birth", _birth)
            json.put("gender", _gender)
            json.put("country", _country)
            json.put("lately", _lately)
            json.put("uid", _uid)
            json.put("deleted" , _deleted)
            array.put(json)
            outer.put("member", array)
        }

        setString("member", outer.toString())


    }

    fun editMember(member: Member): ArrayList<Member> {
        val members = getMember(admin.account)
        for (i in 0 until members.size){
            if (members[i].uid == member.uid){
                members[i] = member
                break
            }
        }
        return members
    }

    val PRE_KEY_EXERCISE = "exercise"
    fun addExericse(member: Member , preset: Preset , count: Int, set: Int) {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val currentDate= current.format(formatter)
        val admin = member.admin
        val memberNo = member.memberNo
        val outer = JSONObject()

        val presetJson = getString(PRE_KEY_EXERCISE, "")
        if (presetJson.length == 0) {
            val json = JSONObject()
            val array = JSONArray()
            json.put("admin", admin)
            json.put("memberNo", memberNo)
            json.put("count", count)
            json.put("totalCount", preset.count)
            json.put("set", set)
            json.put("totalSet", preset.set)
            json.put("stop", preset.stop)
            json.put("date", currentDate)
            json.put("uid" , member.uid)
            json.put("device" , Constants.DEVICE_NAME)

            array.put(json)
            outer.put(PRE_KEY_EXERCISE, array)

            setString(PRE_KEY_EXERCISE, outer.toString())
            Log.d(TAG, "insert exercise ${outer.toString()} ")

        } else {

            val jo = JSONObject(presetJson)
            val jsonArray = jo.getJSONArray(PRE_KEY_EXERCISE)

            val array = JSONArray()

            for (i in 0 until jsonArray.length()) {
                val exersiceOb = jsonArray.getJSONObject(i)
                val _admin = exersiceOb.optString("admin")
                val _memberNo = exersiceOb.optInt("memberNo")
                val _count = exersiceOb.optInt("count")
                val _totalCount = exersiceOb.optInt("totalCount")
                val _set = exersiceOb.optInt("set")
                val _totalSet = exersiceOb.optInt("totalSet")
                val _stop = exersiceOb.optInt("stop")
                val _date = exersiceOb.optString("date")
                val _uid = exersiceOb.optString("uid")
                val _device = exersiceOb.optString("device")

                val json = JSONObject()
                json.put("admin", _admin)
                json.put("memberNo", _memberNo)
                json.put("count", _count)
                json.put("totalCount", _totalCount)
                json.put("set", _set)
                json.put("totalSet", _totalSet)
                json.put("stop", _stop)
                json.put("date", _date)
                json.put("uid", _uid)
                json.put("device", _device)


                array.put(json)
                outer.put(PRE_KEY_EXERCISE, array)
            }
            val json = JSONObject()

            json.put("admin", admin)
            json.put("memberNo", memberNo)
            json.put("count", count)
            json.put("totalCount", preset.count)
            json.put("set", set)
            json.put("totalSet", preset.set)
            json.put("stop", preset.stop)
            json.put("date", currentDate)
            json.put("uid", member.uid)
            json.put("device" , Constants.DEVICE_NAME)

            array.put(json)
            outer.put(PRE_KEY_EXERCISE, array)

            setString(PRE_KEY_EXERCISE, outer.toString())
            Log.d(TAG, "insert exercise ${outer.toString()} ")


        }


        if (MemberSelectActivity.admin.isBackup){
            Thread{
                val httpConnect = HttpConnect()
                val param = Param()
                param.add("admin", admin)
                param.add("member_no", memberNo)
                param.add("count", count)
                param.add("total_count", preset.count)
                param.add("set", set)
                param.add("total_set", preset.set)
                param.add("stop", preset.stop)
                param.add("date", currentDate)
                param.add("uid", member.uid)
                param.add("device" , Constants.DEVICE_NAME)
                httpConnect.httpConnect(param.value , Address().exercise , true)
            }.start()
        }


//        backupData(PRE_KEY_EXERCISE , outer.toString())

    }

    fun getExercise(member: Member): ArrayList<Exercise> {
        val exerciseList = ArrayList<Exercise>()
        val memberJson = getString(PRE_KEY_EXERCISE, "")
        if (memberJson == "") return exerciseList

        val jo = JSONObject(memberJson)
        val jsonArray = jo.getJSONArray(PRE_KEY_EXERCISE)

        var count = 0
        for (i in 0 until jsonArray.length()) {
            val exerciseOb = jsonArray.getJSONObject(i)
            Log.d(TAG, "idx -> $i  content ->${exerciseOb.toString()}")
            if (member.uid == exerciseOb.optString("uid")) {
                count++
                val _date = exerciseOb.optString("date")
                val _count = exerciseOb.optInt("count")
                val _totalCount = exerciseOb.optInt("totalCount")
                val _set = exerciseOb.optInt("set")
                val _totalSet = exerciseOb.optInt("totalSet")
                val _stop = exerciseOb.optInt("stop")
                val _device = exerciseOb.optString("device")

                exerciseList.add(Exercise(count , _date, _count, _totalCount, _set, _totalSet, _stop , _device))
            }
        }

        return exerciseList
    }

    val PRE_KEY_HEIGHT = "height"
    fun addHeight(member: Member , maxHeight: Int) {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val currentDate= current.format(formatter)
        val admin = member.admin
        val memberNo = member.memberNo
        val outer = JSONObject()

        val presetJson = getString(PRE_KEY_HEIGHT, "")
        if (presetJson.length == 0) {
            val json = JSONObject()
            val array = JSONArray()
            json.put("admin", admin)
            json.put("memberNo", memberNo)
            json.put("maxHeight", maxHeight)
            json.put("date", currentDate)
            json.put("uid", member.uid)
            json.put("device" , Constants.DEVICE_NAME)

            array.put(json)
            outer.put(PRE_KEY_HEIGHT, array)

            setString(PRE_KEY_HEIGHT, outer.toString())
            Log.d(TAG, "insert height ${outer.toString()} ")

        } else {

            val jo = JSONObject(presetJson)
            val jsonArray = jo.getJSONArray(PRE_KEY_HEIGHT)

            val array = JSONArray()

            for (i in 0 until jsonArray.length()) {
                val exersiceOb = jsonArray.getJSONObject(i)
                val _admin = exersiceOb.optString("admin")
                val _memberNo = exersiceOb.optInt("memberNo")
                val _maxHeight = exersiceOb.optInt("maxHeight")
                val _uid = exersiceOb.optString("uid")
                val _date = exersiceOb.optString("date")
                val _device = exersiceOb.optString("device")

                val json = JSONObject()
                json.put("admin", _admin)
                json.put("memberNo", _memberNo)
                json.put("maxHeight", _maxHeight)
                json.put("date", _date)
                json.put("uid", _uid)
                json.put("device", _device)

                array.put(json)
                outer.put(PRE_KEY_HEIGHT, array)
            }
            val json = JSONObject()

            json.put("admin", admin)
            json.put("memberNo", memberNo)
            json.put("maxHeight", maxHeight)
            json.put("date", currentDate)
            json.put("uid", member.uid)
            json.put("device" , Constants.DEVICE_NAME)


            array.put(json)
            outer.put(PRE_KEY_HEIGHT, array)

            setString(PRE_KEY_HEIGHT, outer.toString())
            Log.d(TAG, "insert height ${outer.toString()} ")


        }

        if (MemberSelectActivity.admin.isBackup){
            Thread{
                val httpConnect = HttpConnect()
                val param = Param()
                param.add("type" , "height")
                param.add("admin" , member.admin)
                param.add("member_no" , member.memberNo)
                param.add("max_value" , maxHeight)
                param.add("date" , currentDate)
                param.add("uid" , member.uid)
                param.add("device" , Constants.DEVICE_NAME)
                httpConnect.httpConnect(param.value , Address().measure , true)
            }.start()
        }

    }

    val PRE_KEY_WEIGHT = "weight"
    fun addWeight(member: Member , maxWeight: Int) {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val currentDate= current.format(formatter)
        val admin = member.admin
        val memberNo = member.memberNo
        val outer = JSONObject()

        val presetJson = getString(PRE_KEY_WEIGHT, "")
        if (presetJson.length == 0) {
            val json = JSONObject()
            val array = JSONArray()
            json.put("admin", admin)
            json.put("memberNo", memberNo)
            json.put("maxWeight", maxWeight)
            json.put("date", currentDate)
            json.put("uid", member.uid)
            json.put("device" , Constants.DEVICE_NAME)

            array.put(json)
            outer.put(PRE_KEY_WEIGHT, array)

            setString(PRE_KEY_WEIGHT, outer.toString())
            Log.d(TAG, "insert weight ${outer.toString()} ")

        } else {

            val jo = JSONObject(presetJson)
            val jsonArray = jo.getJSONArray(PRE_KEY_WEIGHT)

            val array = JSONArray()

            for (i in 0 until jsonArray.length()) {
                val exersiceOb = jsonArray.getJSONObject(i)
                val _admin = exersiceOb.optString("admin")
                val _memberNo = exersiceOb.optInt("memberNo")
                val _maxHeight = exersiceOb.optInt("maxWeight")
                val _date = exersiceOb.optString("date")
                val _uid = exersiceOb.optString("uid")
                val _device = exersiceOb.optString("device")
                Log.e(TAG, "addWeight: uid $_uid" )

                val json = JSONObject()
                json.put("admin", _admin)
                json.put("memberNo", _memberNo)
                json.put("maxWeight", _maxHeight)
                json.put("date", _date)
                json.put("uid", _uid)
                json.put("device", _device)

                array.put(json)
                outer.put(PRE_KEY_WEIGHT, array)
            }
            val json = JSONObject()

            json.put("admin", admin)
            json.put("memberNo", memberNo)
            json.put("maxWeight", maxWeight)
            json.put("date", currentDate)
            json.put("uid", member.uid)
            Log.e(TAG, "addWeight: ", )
            json.put("device" , Constants.DEVICE_NAME)

            array.put(json)
            outer.put(PRE_KEY_WEIGHT, array)

            setString(PRE_KEY_WEIGHT, outer.toString())
            Log.d(TAG, "insert weight ${outer.toString()} ")


        }


        if (MemberSelectActivity.admin.isBackup){
            Thread{
                val httpConnect = HttpConnect()
                val param = Param()
                param.add("type" , "weight")
                param.add("admin" , member.admin)
                param.add("member_no" , member.memberNo)
                param.add("max_value" , maxWeight)
                param.add("date" , currentDate)
                param.add("uid" , member.uid)
                param.add("device" , Constants.DEVICE_NAME)
                httpConnect.httpConnect(param.value , Address().measure , true)
            }.start()
        }


    }

    fun getWeight(member: Member): ArrayList<Weight> {
        val weights = ArrayList<Weight>()
        val weightJson = getString(PRE_KEY_WEIGHT, "")
        if (weightJson == "") return weights

        Log.e(TAG, "getWeight: $weightJson" )
        val jo = JSONObject(weightJson)
        val jsonArray = jo.getJSONArray(PRE_KEY_WEIGHT)

        for (i in 0 until jsonArray.length()) {
            val weightOb = jsonArray.getJSONObject(i)
            Log.d(TAG, "idx -> $i  content ->${weightOb.toString()}")
            if (member.uid == weightOb.optString("uid")) {

                val _maxWeight = weightOb.optInt("maxWeight")
                val _date = weightOb.optString("date")
                val _device = weightOb.optString("device")

                weights.add(Weight(_maxWeight , _date , _device))
            }
        }

        return weights
    }

    fun getHeight(member: Member): ArrayList<Height> {
        val heights = ArrayList<Height>()
        val heightJson = getString(PRE_KEY_HEIGHT, "")
        if (heightJson == "") return heights

        val jo = JSONObject(heightJson)
        val jsonArray = jo.getJSONArray(PRE_KEY_HEIGHT)

        for (i in 0 until jsonArray.length()) {
            val heightOb = jsonArray.getJSONObject(i)
            Log.d(TAG, "idx -> $i  content ->${heightOb.toString()}")
            if (member.uid == heightOb.optString("uid")) {

                val _maxHeight = heightOb.optInt("maxHeight")
                val _date = heightOb.optString("date")
                val _device = heightOb.optString("device")

                heights.add(Height(_maxHeight , _date ,_device))
            }
        }

        return heights
    }

    fun getBackData(json: String){

        val jo = JSONObject(json)

        val member = jo.optString("member")
        val preset = jo.optString("preset")
        val height = jo.optString("height")
        val weight = jo.optString("weight")
        val exercise = jo.optString("exercise")

//        val m = JSONObject()
//        val p = JSONObject()
//        val h = JSONObject()
//        val w = JSONObject()
//        val e = JSONObject()
//
//        m.put("member" , member)
//        p.put("preset" , preset)
//        h.put("height" , height)
//        w.put("weight" , weight)
//        e.put("exercise" , exercise)
//
//        Log.e(TAG , "member=>>   ${m.toString()}")
//        Log.e(TAG , "preset=>>   ${p.toString()}")
//        Log.e(TAG , "height=>>   ${h.toString()}")
//        Log.e(TAG , "weight=>>   ${w.toString()}")
//        Log.e(TAG , "exercise=>>   ${e.toString()}")


        setString("member" , member)
        setString("preset" , preset)
        setString("height" , height)
        setString("weight" , weight)
        setString("exercise" , exercise)

        Log.d(TAG , "member -> ${getString("member" , "")}")
        Log.d(TAG , "member -> ${getString("preset" , "")}")
        Log.d(TAG , "member -> ${getString("height" , "")}")
        Log.d(TAG , "member -> ${getString("weight" , "")}")
        Log.d(TAG , "member -> ${getString("exercise" , "")}")


    }
//fun backupData(type: String , json: String){
//    if (!admin.isBackup) return
//    Thread{
//        val httpConnect = HttpConnect()
//        val param = Param()
//        param.add("type" , type)
//        param.add("json" , json)
//        param.add("admin" , admin.account)
//
//        Log.d(TAG , "param => ${param.value}")
//        if (httpConnect.httpConnect(param.value , Address().dataBackup , true) == 200){
//
//        }else{
//
//        }
//
//    }.start()
//}

    fun String.md5(): String {
        val md = java.security.MessageDigest.getInstance("MD5")
        return java.math.BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }

}