package com.ko.smartneck.medical.twofive.SQ

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.ko.smartneck.medical.twofive.util.Constants
import com.ko.smartneck.medical.twofive.util.Constants.TAG

class DatabaseHelper(private val context: Context)
    : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {


    companion object {
        private val DB_NAME = "SmartNeck"
        private val DB_VERSION = 1
        private var instance: DatabaseHelper? = null
    }




    private val TABLE_HEIGHT = "height"
    private val TABLE_EXERCISE = "exercise"
    private val TABLE_ADMIN = "admin"
    private val TABLE_WEIGHT = "weight"
    private val TABLE_PRESET = "preset"
    private val TABLE_USER = "member"

    private val COLUMN_IDX = "idx"
    private val COlUMN_MEMBER_NO = "member_no"
    private val COlUMN_MAX_HEIGHT = "max_height"
    private val COlUMN_DATE = "date"
    private val COlUMN_STOP = "stop"
    private val COlUMN_SET = "set_"
    private val COlUMN_TOTAL_SET = "total_set"
    private val COlUMN_COUNT = "count"
    private val COlUMN_TOTAL_COUNT = "total_count"
    private val COlUMN_ACCOUNT = "account"
    private val COlUMN_PASSWORD = "password"
    private val COlUMN_COM_NAME = "com_name"
    private val COlUMN_COUNTRY = "country"
    private val COlUMN_MAX_WEIGHT = "max_weight"
    private val COlUMN_SEAT = "seat"
    private val COlUMN_SETUP = "setup"
    private val COlUMN_MEASURE_SETUP = "measure_setup"
    private val COlUMN_STRENGTH = "strength"
    private val COlUMN_BREAK_TIME = "break_time"
    private val COlUMN_NAME = "name"
    private val COlUMN_BIRTH = "birth"
    private val COlUMN_GENDER = "gender"
    private val COlUMN_PHONE = "phone"
    private val COlUMN_CREATE_DATE = "create_date"
    private val COlUMN_LATELY_DATE = "lately_date"

    private val TYPE_TEXT = "TEXT"
    private val TYPE_INTEGER = "INTEGER"
    private val TYPE_DOUBLE = "REAL"


    private var createSql = ""


    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(createSQLAdmin())
//        db?.execSQL(createSQLExercise())
//        db?.execSQL(createSQLHeight())
//        db?.execSQL(createSQLWeight())
//        db?.execSQL(createSQLUser())
//        db?.execSQL(createSQLPreset())

        Log.d(TAG , " - - - - - - - - - - DBHelper onCreate - - - - - - - - - - ")

    }
    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TABLE_ADMIN}"

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(SQL_DELETE_ENTRIES)

        onCreate(db)
    }

    @Synchronized
    fun getInstance(context: Context): DatabaseHelper {
        if (instance == null) {
            instance = DatabaseHelper(context.applicationContext)
        }
        return instance as DatabaseHelper
    }


    /* Create */

    fun createTable(tableName: String) {
        createSql = ""
        createSql += "CREATE TABLE $tableName(idx Integer PRIMARY KEY AUTOINCREMENT,"
    }

    fun createTableAddColumn(type: String, value: String) {
        createSql += "$value $type,"
    }

    fun createTableEnd(): String {
        return "${createSql.substring(0, createSql.length - 1)})"
    }

    fun createSQLAdmin(): String {
        createTable(TABLE_ADMIN)
        createTableAddColumn(TYPE_TEXT, COlUMN_ACCOUNT)
        createTableAddColumn(TYPE_TEXT, COlUMN_PASSWORD)
        createTableAddColumn(TYPE_TEXT, COlUMN_COM_NAME)
        createTableAddColumn(TYPE_TEXT, COlUMN_COUNTRY)
        return createTableEnd()
    }

    fun insertAdmin(admin: AdminSQ): Boolean {

        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COlUMN_ACCOUNT, admin.account)
            put(COlUMN_PASSWORD, admin.password)
            put(COlUMN_COM_NAME, admin.com_name)
            put(COlUMN_COUNTRY, admin.country)
        }

        val _success = db.insert(TABLE_ADMIN, null, values)
        db.close()
        Log.d(TAG , "insert result = $_success")
        return (Integer.parseInt("$_success") != -1)
    }

    fun selectAllAdmin(): String {

        var allUser: String = "";
        val db = this.writableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_ADMIN"
        val cursor = db.rawQuery(selectALLQuery, null)
        var id: String = ""
        var account = ""
        var password = ""
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    id = cursor.getString(cursor.getColumnIndex(COLUMN_IDX))
                    account = cursor.getString(cursor.getColumnIndex(COlUMN_ACCOUNT))
                    password = cursor.getString(cursor.getColumnIndex(COlUMN_PASSWORD))

                    allUser = "$allUser \n $id $account $password"

                } while (cursor.moveToNext())
            }
        }
        cursor.close()
//        db.close()
        return allUser
    }

    fun createSQLExercise(): String {
        createTable(TABLE_EXERCISE)
        createTableAddColumn(TYPE_INTEGER, COlUMN_MEMBER_NO)
        createTableAddColumn(TYPE_INTEGER, COlUMN_COUNT)
        createTableAddColumn(TYPE_INTEGER, COlUMN_TOTAL_COUNT)
        createTableAddColumn(TYPE_INTEGER, COlUMN_SET)
        createTableAddColumn(TYPE_INTEGER, COlUMN_TOTAL_SET)
        createTableAddColumn(TYPE_INTEGER, COlUMN_STOP)
        createTableAddColumn(TYPE_TEXT, COlUMN_DATE)

        return createTableEnd()
    }

    fun createSQLHeight(): String {

        createTable(TABLE_HEIGHT)
        createTableAddColumn(TYPE_INTEGER, COlUMN_MEMBER_NO)
        createTableAddColumn(TYPE_INTEGER, COlUMN_MAX_HEIGHT)
        createTableAddColumn(TYPE_TEXT, COlUMN_DATE)

        return createTableEnd()

    }

    fun createSQLWeight(): String {

        createTable(TABLE_WEIGHT)
        createTableAddColumn(TYPE_INTEGER, COlUMN_MEMBER_NO)
        createTableAddColumn(TYPE_INTEGER, COlUMN_MAX_WEIGHT)
        createTableAddColumn(TYPE_TEXT, COlUMN_DATE)

        return createTableEnd()

    }


    fun createSQLPreset(): String {
        createTable(TABLE_PRESET)
        createTableAddColumn(TYPE_INTEGER, COlUMN_MEMBER_NO)
        createTableAddColumn(TYPE_INTEGER, COlUMN_COUNT)
        createTableAddColumn(TYPE_INTEGER, COlUMN_SET)
        createTableAddColumn(TYPE_INTEGER, COlUMN_STOP)
        createTableAddColumn(TYPE_INTEGER, COlUMN_SEAT)
        createTableAddColumn(TYPE_INTEGER, COlUMN_SETUP)
        createTableAddColumn(TYPE_INTEGER, COlUMN_MEASURE_SETUP)
        createTableAddColumn(TYPE_INTEGER, COlUMN_MAX_HEIGHT)
        createTableAddColumn(TYPE_INTEGER, COlUMN_MAX_WEIGHT)
        createTableAddColumn(TYPE_INTEGER, COlUMN_STRENGTH)
        createTableAddColumn(TYPE_INTEGER, COlUMN_BREAK_TIME)

        return createTableEnd()

    }

    fun createSQLUser(): String {

        createTable(TABLE_USER)
        createTableAddColumn(TYPE_TEXT, COlUMN_NAME)
        createTableAddColumn(TYPE_TEXT, COlUMN_BIRTH)
        createTableAddColumn(TYPE_TEXT, COlUMN_GENDER)
        createTableAddColumn(TYPE_TEXT, COlUMN_PHONE)
        createTableAddColumn(TYPE_TEXT, COlUMN_CREATE_DATE)
        createTableAddColumn(TYPE_TEXT, COlUMN_LATELY_DATE)

        return createTableEnd()
    }


}