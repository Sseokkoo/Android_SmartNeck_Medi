package com.smartneck.twofive.Member

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.smartneck.twofive.GlobalApplication
import com.smartneck.twofive.GlobalApplication.userPreference
import com.smartneck.twofive.Member.MemberSelectActivity.admin
import com.smartneck.twofive.R.*
import com.smartneck.twofive.util.Address
import com.smartneck.twofive.util.Constants
import com.smartneck.twofive.util.Constants.TAG
import com.smartneck.twofive.util.Constants.language
import com.smartneck.twofive.util.HttpConnect
import com.smartneck.twofive.util.Param
import com.smartneck.twofive.util.User.Member
import kotlinx.android.synthetic.main.admin_member_add_activity.*
import java.util.regex.Matcher
import java.util.regex.Pattern

//todo num = /^[0-9]*$/; 숫자만
//todo
class MemberAddActivity : AppCompatActivity() {

    private var users: ArrayList<Member>? = null
    private var isEdit = false
    private var editName: String? = null
    private var editGender: String? = null
    private var editPhone: String? = null
    private var editBirth: String? = null
    private var editMemberNo = 0
    internal var countryNo = 0
    private var position = 0
    internal var date = "1980-01-01"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.admin_member_add_activity)
//        Log.d(TAG, "onCreate: " + getCountryCode(admin.getCountry()));


        init()
        setListener()
    }

    private fun init() {
        if (language != "en") {
            edt_member_add_name2!!.visibility = View.GONE
            edt_member_add_name3!!.visibility = View.GONE
        }
        editTextSetting()
        radio_member_add_male!!.isChecked = true
        val intent: Intent = intent
        isEdit = intent.getBooleanExtra("edit", false)
        var type = ""
        var regist = ""
        if (isEdit) {
            regist = getString(string.word_edit)
            editName = intent.getStringExtra("name")
            editBirth = intent.getStringExtra("birth")
            editPhone = intent.getStringExtra("phone")
            editGender = intent.getStringExtra("gender")
            editMemberNo = intent.getIntExtra("memberNo", -1)
            position = intent.getIntExtra("position" , 0)
            Log.e(TAG , "position => $position")
            Log.e(TAG , "editMemberNo => $editMemberNo")
            users = intent.getSerializableExtra("users") as ArrayList<Member>
            if (editMemberNo == -1) {
                Toast.makeText(applicationContext, "회원정보 오류", Toast.LENGTH_SHORT).show()
                fileList()
            }

            //tmp data


            if (language == "en") {
                val name = editName?.split(" ")
                edt_member_add_name1!!.setText(name?.get(0))
                edt_member_add_name2!!.setText(name?.get(1))
                edt_member_add_name3!!.setText(name?.get(2))
            } else {
                edt_member_add_name1!!.setText(editName)
            }
            if (edt_member_add_phone.text.toString().contains("-")) {
                val phone = edt_member_add_phone.text.toString().split("-")
                edt_member_add_phone!!.setText(phone[1])
            } else {
                edt_member_add_phone!!.setText(editPhone)
            }
            val birthArray = editBirth?.split("-")
            date = editBirth!!
            date_picker.init(birthArray!![0].toInt() , birthArray!![1].toInt()-1 , birthArray!![2].toInt() ,
                    DatePicker.OnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
                var month = ""
                var day = ""
                if (monthOfYear < 9) {
                    month += "0"
                }
                month += (monthOfYear + 1).toString()
                if (dayOfMonth < 10) {
                    day += "0"
                }
                day += dayOfMonth.toString()
                date = "$year-$month-$day"
            })
            edt_member_add_birth_1!!.setText(birthArray?.get(0))
            edt_member_add_birth_2!!.setText(birthArray?.get(1))
            edt_member_add_birth_3!!.setText(birthArray?.get(2))
            if (editGender == "남자" || editGender == "male") {
                radio_member_add_male!!.isChecked = true
                radio_member_add_female!!.isChecked = false
            } else {
                radio_member_add_male!!.isChecked = false
                radio_member_add_female!!.isChecked = true
            }
            btn_member_add_resigt!!.text = getString(string.user_info_edit_title)
        } else {
            regist = getString(string.word_regist)
            date_picker!!.init(1980, 0, 1) { view, year, monthOfYear, dayOfMonth ->
                var month = ""
                var day = ""
                if (monthOfYear < 9) {
                    month += "0"
                }
                month += (monthOfYear + 1).toString()
                if (dayOfMonth < 10) {
                    day += "0"
                }
                day += dayOfMonth.toString()
                date = "$year-$month-$day"
            }
        }
            type = getString(string.word_patient)

        tv_member_add_title!!.text = "$type $regist"
    }

    private fun setListener() {
        img_member_add_back_btn!!.setOnClickListener { finish() }
        btn_member_add_resigt!!.setOnClickListener(OnClickListener {
            if (!inputCheck()) return@OnClickListener
            if (isEdit) {



                    memberEdit();


            } else {
                    memberAdd();

            }
        })
        spn_member_add_country!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                countryNo = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                countryNo = 0
            }
        }

    }

    private fun inputCheck(): Boolean {
        if (edt_member_add_name1!!.length() == 0) {
            Toast.makeText(this, getString(string.toast_insert_name), Toast.LENGTH_SHORT).show()
            return false
        }

//        if (edt_birth_1.length() == 0 || edt_birth_2.length() == 0 || edt_birth_3.length() == 0){
//            Toast.makeText(this, getString(R.string.toast_insert_birth), Toast.LENGTH_SHORT).show();
//            return false;
//        }


        if (edt_member_add_phone!!.length() == 0) {
            Toast.makeText(this, getString(string.toast_insert_phone_number), Toast.LENGTH_SHORT).show()
            return false
        }
        if (!validate(edt_member_add_phone!!.text.toString())) {
            Toast.makeText(this, getString(string.toast_insert_phone_number), Toast.LENGTH_SHORT).show()
            return false
        }
//        if (!validate(edt_birth_1.getText().toString())||!validate(edt_birth_2.getText().toString())||!validate(edt_birth_3.getText().toString())){
//            Toast.makeText(this, getString(R.string.toast_insert_birth), Toast.LENGTH_SHORT).show();
//            return false;
//        }


        return true
    }


//    private int getCountryCode(int countryNo){
//
//        if (countryNo == 0){
//            //KR
//            return 82;
//        }else if (countryNo == 1){
//            //US
//            return 1;
//        }else if (countryNo == 2){
//            //CN
//            return 86;
//        }else{
//            //Default KR
//            return 82;
//        }
//    }

//    private int getCountryCode(String adminCountry){
//        if (adminCountry.contains("/ +82")){
//            return 82;
//        }else if (adminCountry.equals("/ +1")){
//            return 1;
//        }else if (adminCountry.equals("/ +86")){
//            return 86;
//        }else{
//            return 82;
//        }
//    }


    private fun memberAdd() {
            val receive = "yes"
            var gender = ""
            if (radio_member_add_male!!.isChecked) {
                gender = "남자"
            } else if (radio_member_add_female!!.isChecked) {
                gender = "여자"
            }
            var name = ""
            name = if (Constants.language == "en") {
                edt_member_add_name1!!.text.toString() + " " + edt_member_add_name2!!.text.toString() + " " + edt_member_add_name3!!.text.toString()
            } else {
                edt_member_add_name1!!.text.toString()
            }

        GlobalApplication.userPreference.addMember(admin.account.toString() , name , edt_member_add_phone.text.toString() , date , gender , "ko" , "")
        val nextIntent = Intent(this, MemberSelectActivity::class.java)
        startActivity(nextIntent)
        finish()
    }

    private fun memberEdit() {

        //                users.get(editMemberNo)


        val receive = "yes"
        var gender = ""
        if (radio_member_add_male!!.isChecked) {
            gender = "남자"
        } else if (radio_member_add_female!!.isChecked) {
            gender = "여자"
        }
        var name = ""
        name = if (Constants.language == "en") {
            edt_member_add_name1!!.text.toString() + " " + edt_member_add_name2!!.text.toString() + " " + edt_member_add_name3!!.text.toString()
        } else {
            edt_member_add_name1!!.text.toString()
        }

//        GlobalApplication.userPreference.addMember(admin.getAdmin().toString() , name , edt_member_add_phone.text.toString() , date , gender , "ko" , "")
        users?.get(position)?.name = name
        users?.get(position)?.gender = name
        users?.get(position)?.phone = edt_member_add_phone.text.toString()
        users?.get(position)?.birth = date
        users?.get(position)?.gender = gender

        if (MemberSelectActivity.admin.isBackup){
            Thread{
                val httpConnect = HttpConnect()
                val param = Param()
                param.add("type" , "update")
                param.add("admin" , admin.account)
                param.add("member_no" , editMemberNo)
                param.add("name" , users?.get(position)?.name)
                param.add("phone" , users?.get(position)?.phone)
                param.add("birth" , users?.get(position)?.birth)
                param.add("gender" , users?.get(position)?.gender)
                param.add("country" , users?.get(position)?.country)
                param.add("lately" , users?.get(position)?.lately)
                param.add("uid" , users?.get(position)?.uid)

                httpConnect.httpConnect(param.value , Address().member , true)

            }.start()
        }

        userPreference.editMember(users!!)


        finish()
    }

//    private fun memberEdit() {
//        Thread(Runnable {
//            var receive = ""
//            var gender = ""
//            receive = if (cb_receive!!.isChecked) {
//                "yes"
//            } else {
//                "no"
//            }
//            if (radio_male!!.isChecked) {
//                gender = "남자"
//            } else if (radio_female!!.isChecked) {
//                gender = "여자"
//            }
//            var name = ""
//            name = if (admin.language == "en") {
//                edt_name1!!.text.toString() + " " + edt_name2!!.text.toString() + " " + edt_name3!!.text.toString()
//            } else {
//                edt_name1!!.text.toString()
//            }
//            val param = Param()
//            param.add("gender", gender)
//            param.add("name", name)
////                param.add("birth" , edt_birth_1.getText().toString() + "-" + edt_birth_2.getText().toString() + "-" +  edt_birth_3.getText().toString());
//
//
//            param.add("birth", date)
//            param.add("phone", edt_phone!!.text.toString())
//            param.add("receive", receive)
//            param.add("memberNo", editMemberNo)
//            Log.d(TAG, "param: \n" + param.value)
//            val httpConnect = HttpConnect()
//            if (httpConnect.httpConnect(param.value, Address().mememberEdit, true) == 200) {
//                finish()
//            }
//        }).start()
//    }

    private fun editTextSetting() {
        if (language == "en") {
            edt_member_add_birth_1!!.hint = "DD"
            edt_member_add_birth_2!!.hint = "MM"
            edt_member_add_birth_3!!.hint = "YYYY"
            edt_member_add_birth_1!!.filters = arrayOf<InputFilter>(LengthFilter(2))
            edt_member_add_birth_2!!.filters = arrayOf<InputFilter>(LengthFilter(2))
            edt_member_add_birth_3!!.filters = arrayOf<InputFilter>(LengthFilter(4))
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            edt_member_add_birth_1!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (edt_member_add_birth_1!!.length() == 2) {
                        edt_member_add_birth_2!!.requestFocus()
                        edt_member_add_birth_2!!.isCursorVisible = true
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
            edt_member_add_birth_2!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (edt_member_add_birth_2!!.length() == 2) {
                        edt_member_add_birth_3!!.requestFocus()
                        edt_member_add_birth_3!!.isCursorVisible = true
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
            edt_member_add_birth_3!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (edt_member_add_birth_3!!.length() == 4) {
                        imm.hideSoftInputFromWindow(edt_member_add_birth_3!!.windowToken, 0)
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        } else {
            edt_member_add_birth_1!!.hint = "YYYY"
            edt_member_add_birth_2!!.hint = "MM"
            edt_member_add_birth_3!!.hint = "DD"
            edt_member_add_birth_1!!.filters = arrayOf<InputFilter>(LengthFilter(4))
            edt_member_add_birth_2!!.filters = arrayOf<InputFilter>(LengthFilter(2))
            edt_member_add_birth_3!!.filters = arrayOf<InputFilter>(LengthFilter(2))
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            edt_member_add_birth_1!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (edt_member_add_birth_1!!.length() == 4) {
                        edt_member_add_birth_2!!.requestFocus()
                        edt_member_add_birth_2!!.isCursorVisible = true
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
            edt_member_add_birth_2!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (edt_member_add_birth_2!!.length() == 2) {
                        edt_member_add_birth_3!!.requestFocus()
                        edt_member_add_birth_3!!.isCursorVisible = true
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
            edt_member_add_birth_3!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (edt_member_add_birth_3!!.length() == 2) {
                        imm.hideSoftInputFromWindow(edt_member_add_birth_3!!.windowToken, 0)
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    companion object {
        val VALID_EMAIL_ADDRESS_REGEX: Pattern = //            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
                Pattern.compile("^[0-9]*$", Pattern.CASE_INSENSITIVE)

        fun validate(numString: String): Boolean {
            val matcher: Matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(numString)
            return matcher.find()
        }
    }
}