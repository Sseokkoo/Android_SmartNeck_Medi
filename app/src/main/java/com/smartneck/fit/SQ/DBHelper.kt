package com.smartneck.fit.SQ


import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.smartneck.fit.util.Constants.TAG


/**
 * Created by muhammed on 11/11/17.
 */
class DBHelper(context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {




    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TABLE_ADMIN}"



    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createSQLAdmin())
//        db?.execSQL(createSQLExercise())
//        db?.execSQL(createSQLHeight())
//        db?.execSQL(createSQLWeight())
//        db?.execSQL(createSQLUser())
//        db?.execSQL(createSQLPreset())
    }
    open fun getInstance(context: Context ): SQLiteOpenHelper { // 싱글턴 패턴으로 구현하였다.
        if(sqLiteHelper == null){
            sqLiteHelper = DBHelper(context)
        }

        return sqLiteHelper as SQLiteOpenHelper;
    }



    fun insertAdmin(account: String , password: String , com_name: String , country: String) {

        val db = this.writableDatabase
//        val contentValues = ContentValues()
//        contentValues.put(COlUMN_ACCOUNT, account)
//        contentValues.put(COlUMN_PASSWORD, password)
//        contentValues.put(COlUMN_COM_NAME, com_name)
//        contentValues.put(COlUMN_COUNTRY, country)
        db?.execSQL("insert into $TABLE_ADMIN (account , password , com_name , country) values ('$account' , '$password' , '$com_name' , '$country')")


//        val _success = db.insert(TABLE_ADMIN, null, contentValues)
//        Log.d(Constants.TAG, "insert result = $_success")
//        return (Integer.parseInt("$_success") != -1)
//        db.setTransactionSuccessful()
        selectAllAdmin()
    }


//    fun insertData(firstname:String , lastname:String , age:String, address:String,department:String){
//        val db = this.writableDatabase
//        val contentValues = ContentValues()
//        contentValues.put(COL_2 , firstname)
//        contentValues.put(COL_3 , lastname)
//        contentValues.put(COL_4 , age)
//        contentValues.put(COL_5 , address)
//        contentValues.put(COL_6 , department)
//        db.insert(TABLE_NAME,null,contentValues)
//    }

    fun selectAdmin(account: String, password: String): Cursor {

        val db = this.readableDatabase
        val res = db.rawQuery("select * from $TABLE_ADMIN where account = '$account' and password = '$password'" , null)


        return res
    }

    fun selectAllAdmin() {

        val db = this.readableDatabase
        val res = db.rawQuery("select * from $TABLE_ADMIN" , null)

        if(res.moveToFirst()) {
            do {
                Log.d(TAG , "${res.getInt(0)} , ${res.getString(1)} , ${res.getString(2)} , ${res.getString(3)} , ${res.getString(4)}" )
            } while(res.moveToNext())
        }
        res.close()



    }


//    fun updateData(id:String ,firstname:String , lastname:String ,
//                   age:String, address:String,department:String):Boolean{
//
//        val db = this.writableDatabase
//        val contentValues = ContentValues()
//        contentValues.put(COL_1 , id)
//        contentValues.put(COL_2 , firstname)
//        contentValues.put(COL_3 , lastname)
//        contentValues.put(COL_4 , age)
//        contentValues.put(COL_5 , address)
//        contentValues.put(COL_6 , department)
//        db.update(TABLE_NAME,contentValues,"ID = ?", arrayOf(id))
//        return true
//    }
//
//    fun deleteData(id:String):Int{
//        val db = this.writableDatabase
//        return db.delete(TABLE_NAME, "ID = ?", arrayOf(id))

//    }
/* Create */

    fun createTable(tableName: String) {
        createSql = ""
        createSql += "CREATE TABLE IF NOT EXISTS $tableName (idx Integer PRIMARY KEY AUTOINCREMENT,"
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

    companion object {

        var sqLiteHelper: SQLiteOpenHelper? = null

        val DATABASE_NAME = "SmartNeck.db"
        val TABLE_HEIGHT = "height"
        val TABLE_EXERCISE = "exercise"
        val TABLE_ADMIN = "admin"
        val TABLE_WEIGHT = "weight"
        val TABLE_PRESET = "preset"
        val TABLE_USER = "member"

        val COLUMN_IDX = "idx"
        val COlUMN_MEMBER_NO = "member_no"
        val COlUMN_MAX_HEIGHT = "max_height"
        val COlUMN_DATE = "date"
        val COlUMN_STOP = "stop"
        val COlUMN_SET = "set_"
        val COlUMN_TOTAL_SET = "total_set"
        val COlUMN_COUNT = "count"
        val COlUMN_TOTAL_COUNT = "total_count"
        val COlUMN_ACCOUNT = "account"
        val COlUMN_PASSWORD = "password"
        val COlUMN_COM_NAME = "com_name"
        val COlUMN_COUNTRY = "country"
        val COlUMN_MAX_WEIGHT = "max_weight"
        val COlUMN_SEAT = "seat"
        val COlUMN_SETUP = "setup"
        val COlUMN_MEASURE_SETUP = "measure_setup"
        val COlUMN_STRENGTH = "strength"
        val COlUMN_BREAK_TIME = "break_time"
        val COlUMN_NAME = "name"
        val COlUMN_BIRTH = "birth"
        val COlUMN_GENDER = "gender"
        val COlUMN_PHONE = "phone"
        val COlUMN_CREATE_DATE = "create_date"
        val COlUMN_LATELY_DATE = "lately_date"

        val TYPE_TEXT = "TEXT"
        val TYPE_INTEGER = "INTEGER"
        val TYPE_DOUBLE = "REAL"


        var createSql = ""
    }

}