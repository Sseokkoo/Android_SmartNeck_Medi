package com.ko.smartneck.medical.twofive.Management

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.ko.smartneck.medical.twofive.Member.MemberSelectActivity.admin
import com.ko.smartneck.medical.twofive.R
import com.ko.smartneck.medical.twofive.util.Address
import com.ko.smartneck.medical.twofive.util.Constants
import com.ko.smartneck.medical.twofive.util.Constants.TAG
import com.ko.smartneck.medical.twofive.util.HttpConnect
import com.ko.smartneck.medical.twofive.util.Param
import kotlinx.android.synthetic.main.activity_statistics_management.*
import kotlinx.android.synthetic.main.admin_member_add_activity.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class MeasureManagementActivity : AppCompatActivity() {

    var heights: MutableList<Measurement> = mutableListOf()
    var weights: MutableList<Measurement> = mutableListOf()
    var measurements: MutableList<MeasurementFull> = mutableListOf()
    var searchResults: MutableList<MeasurementFull> = mutableListOf()
    val mHandler = Handler()
    var anglePerMillimeter: Float = 0f
    var isNameSort = false
    var isDateSort = false

    var isStrSort = false //수정12
    var isAngSort = false

    var selected = "height"
    lateinit var measureManagementAdapter: MeasureManagementAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics_management)
        init()
        initListener()


    }


    private fun init() {
        getMeasureMent()
        val now: Long = System.currentTimeMillis()
        val mDate = Date(now)
        val simpleDate = SimpleDateFormat("yyyy-MM-dd")
        val getTime: String = simpleDate.format(mDate)
        Log.e(TAG, "###init: $getTime")
        val split = getTime.split("-")
        edt_year_end.setText(split[0])
        edt_month_end.setText(split[1])
        edt_day_end.setText(split[2])

       anglePerMillimeter = 1f
//               if (Constants.DEVICE_TYPE.equals("MED")){ 수정8
//           3.6666667f
//       }else{
//            3f
//       }
    }

    private fun initListener() {
        editTextFocusChange()
        btn_back.setOnClickListener {
            finish()
        }
//태그1
        check_all.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                lay_date_box.visibility = View.GONE
            }else{
                lay_date_box.visibility = View.VISIBLE
            }
        }

        btn_download.setOnClickListener {
            val ja = JSONArray()
            if (check_all.isChecked) {
                for (i in 0 until measurements.size) {
                    val jo = JSONObject()
                    jo.put("name", measurements[i].name)
                    jo.put("height", measurements[i].height)
                    jo.put("weight", (measurements[i].weight.toFloat()) / 10)
                    jo.put("device", measurements[i].device)
                    jo.put("gender", measurements[i].gender)
                    jo.put("date", measurements[i].date)
                    jo.put("uid" , measurements[i].uid)
                    ja.put(jo)
                }
            }else{
                for (i in 0 until searchResults.size) {
                    val jo = JSONObject()
                    jo.put("name", searchResults[i].name)
                    jo.put("height", searchResults[i].height)
                    jo.put("weight", (searchResults[i].weight).toFloat() / 10)
                    jo.put("device", searchResults[i].device)
                    jo.put("gender", searchResults[i].gender)
                    jo.put("date", searchResults[i].date)
                    jo.put("uid" , searchResults[i].uid)
                    ja.put(jo)
                }
            }

            val dialogSendMail = DialogSendMail(context = this, json = ja.toString())
            dialogSendMail.show()
        }

        btn_search.setOnClickListener {
            hideKeyboard()

            if (check_all.isChecked){
                searchResults.clear()
                searchResults.addAll(measurements)
                measureManagementAdapter = MeasureManagementAdapter(searchResults, layoutInflater)
                lv_measure_management.adapter = measureManagementAdapter
                searchCount(searchResults.size)
                return@setOnClickListener
            }

            if (edt_year_start.text.isEmpty() || edt_month_start.text.isEmpty() || edt_day_start.text.isEmpty()
                    || edt_year_end.text.isEmpty() || edt_month_end.text.isEmpty() || edt_day_end.text.isEmpty()) {

                Toast.makeText(this, "날짜를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val startYear = edt_year_start.text.toString().toInt()
            val startMonth = edt_month_start.text.toString().toInt()
            val startDay = edt_day_start.text.toString().toInt()
            val endYear = edt_year_end.text.toString().toInt()
            val endMonth = edt_month_end.text.toString().toInt()
            val endDay = edt_day_end.text.toString().toInt()
            searchResults.clear()

            val before = LocalDate.of(startYear, startMonth, startDay)
            val after = LocalDate.of(endYear, endMonth, endDay)
            Log.e(TAG, "initListener: for before ${measurements.size}" )
                for (i in 0 until measurements.size) {
                    Log.e(TAG, "initListener: count $i ${measurements[i]}")
                    val split = measurements[i].date.split("-")
                    val year = split[0].toInt()
                    var month = split[1].toInt()
                    var day = split[2].toInt()

                    val now = LocalDate.of(year, month, day)
                    if (now in before..after) {
                        Log.e(TAG, "INSERT: count $i ${measurements[i]}")
                        searchResults.add(measurements[i])
                    }
                }



            measureManagementAdapter = MeasureManagementAdapter(searchResults, layoutInflater)
            lv_measure_management.adapter = measureManagementAdapter
            searchCount(searchResults.size)

        }

        tv_name.setOnClickListener {
//            searchResults = ArrayList(searchResults.sortedBy { it.name })
            if (isNameSort){
                isNameSort = false
                searchResults = ArrayList(searchResults.sortedBy { it.name })
            }else{
                isNameSort = true
                searchResults = ArrayList(searchResults.sortedByDescending { it.name })
            }
            measureManagementAdapter = MeasureManagementAdapter(searchResults, layoutInflater)
            lv_measure_management.adapter = measureManagementAdapter
        }
        tv_date.setOnClickListener {
            if (isDateSort){
                isDateSort = false
                searchResults = ArrayList(searchResults.sortedByDescending { it.date })
            }else{
                isDateSort = true
                searchResults = ArrayList(searchResults.sortedBy { it.date })
            }
            measureManagementAdapter = MeasureManagementAdapter(searchResults, layoutInflater)
            lv_measure_management.adapter = measureManagementAdapter
        }
        //수정12

        tv_max_str.setOnClickListener {
            if (isStrSort){
                isStrSort = false
                searchResults = ArrayList(searchResults.sortedByDescending { it.weight })
            }else{
                isStrSort = true
                searchResults = ArrayList(searchResults.sortedBy { it.weight })
            }
            measureManagementAdapter = MeasureManagementAdapter(searchResults, layoutInflater)
            lv_measure_management.adapter = measureManagementAdapter
        }
        tv_max_ang.setOnClickListener {
            if (isAngSort){
                isAngSort = false
                searchResults = ArrayList(searchResults.sortedByDescending { it.height })
            }else{
                isAngSort = true
                searchResults = ArrayList(searchResults.sortedBy { it.height })
            }
            measureManagementAdapter = MeasureManagementAdapter(searchResults, layoutInflater)
            lv_measure_management.adapter = measureManagementAdapter
        }

    }

    private fun editTextFocusChange(){
        edt_year_start.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edt_year_start!!.length() == 4) {
                    edt_month_start!!.requestFocus()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        edt_month_start.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edt_month_start!!.length() == 2) {
                    edt_day_start!!.requestFocus()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        edt_day_start.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edt_day_start!!.length() == 2) {
//                    if (edt_year_end.length() == 4 && edt_month_end.length() == 2 && edt_day_end.length() == 2){
//                        hideKeyboard()
//                    }else{
                        edt_year_end!!.requestFocus()
//                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })


        edt_year_end.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edt_year_end!!.length() == 4) {
                    edt_month_end!!.requestFocus()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        edt_month_end.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edt_month_end!!.length() == 2) {
                    edt_day_end!!.requestFocus()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        edt_day_end.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edt_day_end!!.length() == 2) {
                    hideKeyboard()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
    fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(edt_day_start.windowToken, 0)
    }
    private fun getMeasureMent() {
        Thread {
            val httpConnect = HttpConnect()
            val param = Param()
            param.add("account", admin.account)
            if (httpConnect.httpConnect(param.value, Address().getMeasureManagement, true) == 200) {
                if (!httpConnect.receiveMessage.contains("fail")) {
                    jsonParse(httpConnect.receiveMessage)
                }
            }
        }.start()

    }

    private fun jsonParse(json: String) {
        val jsonArray = JSONArray(json)
        val jsonObject: JSONObject = jsonArray.getJSONObject(0)
        val height = jsonObject.optJSONArray("height")
        val weight = jsonObject.optJSONArray("weight")
//        Log.e(TAG, "###jsonParse: ${height.toString()}" )

        jsonParseForHeight(height)
        jsonParseForWeight(weight)


        for (i in 0 until heights.size){
                measurements.add(MeasurementFull(i , heights[i].name , 0 , heights[i].value , heights[i].device , heights[i].date , heights[i].gender , heights[i].uid))
        }
        Log.e(TAG, "###jsonParse: height.size ${heights.size} , weight.size ${weights.size} measurements size = ${measurements.size}")
        for (i in 0 until weights.size){
            for (j in 0 until measurements.size){
                if (weights[i].name == measurements[j].name){
                    if (weights[i].date == measurements[j].date){
                        measurements[j].weight = weights[i].value
                        Log.e(TAG, "jsonParse: idx = $i , weight = ${weights[i].value} ", )
//                        break
                    }else{
//                        if (j == measurements.size-1){
//                            Log.e(TAG, "###jsonParse: add weight")
//                        mHandler.post {
//
//                            measurements.add(j , MeasurementFull(i , weights[i].name , weights[i].value , 0 , weights[i].device , weights[i].date , weights[i].gender))
//                        }
//                        break
//
//                        }

                    }
//                    break
                }
            }
        }
        mHandler.post {
            searchResults.addAll(measurements)
            measureManagementAdapter = MeasureManagementAdapter(searchResults, layoutInflater)
            lv_measure_management.adapter = measureManagementAdapter
             searchCount(searchResults.size)
        }
//        Log.e(TAG, "###jsonParse: ${height.toString()}" )
    }

    private fun searchCount(count: Int){
        tv_search_count.text =" - 총 ${count}명이 검색되었습니다."

    }
    private fun jsonParseForHeight(ja: JSONArray) {

        var idx = 0;
        for (i in 0 until ja.length()) {


            val jsonObject: JSONObject = ja.getJSONObject(i)
            val name = jsonObject.optString("name")
            val height = jsonObject.optInt("max_height") * anglePerMillimeter
            val device = jsonObject.optString("device")
            val date = jsonObject.optString("date")
            val gender = jsonObject.optString("gender")
            val uid = jsonObject.optString("uid")



            if (heights.size == 0) {
                heights.add(Measurement(i + 1, name, height.toInt(), device, date, gender , uid));
                idx++;
            } else {
                var isEdit = false
                for (j in 0 until heights.size) {
                    if (name == heights[j].name && date == heights[j].date) {
                        isEdit = true
                        if (height > heights[j].value) {
                            heights[j].value = height.toInt()
                        }
                        break
                    }
                }

                if (!isEdit) {
                    heights.add(Measurement(i + 1, name, height.toInt(), device, date, gender , uid))
                }


            }
        }
    }

    private fun jsonParseForWeight(ja: JSONArray) {

        for (i in 0 until ja.length()) {
            val jsonObject: JSONObject = ja.getJSONObject(i)
            val name = jsonObject.optString("name")
            var weight = jsonObject.optInt("max_weight")
            val device = jsonObject.optString("device")
            val date = jsonObject.optString("date")
            val gender = jsonObject.optString("gender")
            val uid = jsonObject.optString("uid")
// FIXME: 2021/01/06 weight 관련
//            if (weight >= 70) {
//                weight += 5
//            } else if (weight >= 45 && weight <= 65) {
//                weight += 10
//            } else if (weight >= 5 && weight <= 40) {
//                weight += 15
//            }



            if (weights.size == 0) {
                weights.add(Measurement(i + 1, name, weight, device, date, gender , uid));

            } else {
                var isEdit = false
                for (j in 0 until weights.size) {
                    if (name == weights[j].name && date == weights[j].date) {
                        isEdit = true
                        if (weight > weights[j].value) {
                            weights[j].value = weight
                        }
                        break
                    }
                }

                if (!isEdit) {
                    weights.add(Measurement(i + 1, name, weight, device, date, gender , uid))
                }


            }
        }
    }
}