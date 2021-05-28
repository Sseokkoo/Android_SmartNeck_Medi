package com.smartneck.twofive.Member

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View.OnClickListener
import android.widget.Toast
import com.smartneck.twofive.GlobalApplication
import com.smartneck.twofive.GlobalApplication.helper
import com.smartneck.twofive.GlobalApplication.userPreference
import com.smartneck.twofive.R
import com.smartneck.twofive.util.Address
import com.smartneck.twofive.util.Constants.TAG
import com.smartneck.twofive.util.Constants.context
import com.smartneck.twofive.util.HttpConnect
import com.smartneck.twofive.util.Param
import kotlinx.android.synthetic.main.admin_join_activity.*
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

/**
 * 입력값 유효성검사 추가
 */
class JoinActivity : AppCompatActivity() {


    val handler: Handler = Handler()
    var logoImg: File? = null
    lateinit var type: String
    private var selectFileName: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_join_activity)
        init()
    }


    private fun init() {
        clearApplicationData(this)
        type = intent.getStringExtra("type") ?: ""

// TODO: 2020-01-13 테스트 후 삭제
//        edt_com_name!!.setText("tmp")
//        edt_ceo_name!!.setText("tmp")
//        edt_ceo_phone!!.setText("01000000000")
//        edt_ceo_telephone!!.setText("01000000000")
//        edt_ceo_email!!.setText("tmp")
//        edt_admin_name!!.setText("tmp")
//        edt_admin_title!!.setText("tmp")
//        edt_admin_email!!.setText("tmp")
//        edt_admin_telephone!!.setText("01000000000")
//        edt_admin_phone!!.setText("01000000000")
//        edt_address!!.setText("tmp")
//        edt_url!!.setText("tmp")
//        edt_password!!.setText("temp")
        setListener()
    }

    private fun setListener() {
        img_join_back_btn!!.setOnClickListener { finish() }
        btn_sign_up!!.setOnClickListener {
            signUp()
        }
        img_logo_change_btn!!.setOnClickListener(OnClickListener {
            if (edt_com_name!!.length() == 0 || edt_account!!.length() == 0) {
                Toast.makeText(this@JoinActivity, "please enter all the information", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PICK_FROM_ALBUM)
        })
    }

    private fun signUp() {

        val account = edt_account!!.text.toString()
        val password = edt_password!!.text.toString()
        val comName = edt_com_name!!.text.toString()

        val adminPhone = edt_admin_phone.text.toString()
        val adminName = edt_admin_name.text.toString()
        val adminTelephone = edt_admin_telephone.text.toString()
        val adminTitle = edt_admin_title.text.toString()
        val adminEmail = edt_admin_email.text.toString()
        val ceoName = edt_ceo_name.text.toString()
        val ceoPhone = edt_ceo_phone.text.toString()
        val ceoTelephone = edt_ceo_telephone.text.toString()
        val ceoEmail = edt_ceo_email.text.toString()
        val address = edt_address.text.toString()

//        val country = spn_country!!.selectedItem.toString()

        if (account.length == 0){
            Toast.makeText(this , getString(R.string.toast_insert_email) , Toast.LENGTH_SHORT).show()
            return
        }
        if (password.length == 0){
            Toast.makeText(this , getString(R.string.toast_insert_all_password) , Toast.LENGTH_SHORT).show()
            return
        }

        if (comName.length == 0){
            Toast.makeText(this , getString(R.string.join_mutual_hint) , Toast.LENGTH_SHORT).show()
            return
        }
        if (ceoName.length == 0){
            Toast.makeText(this , getString(R.string.join_name_1_hint) , Toast.LENGTH_SHORT).show()
            return
        }

        if (ceoPhone.length == 0){
            Toast.makeText(this , getString(R.string.join_phone_1_hint) , Toast.LENGTH_SHORT).show()
            return
        }
        if (ceoTelephone.length == 0){
            Toast.makeText(this , getString(R.string.join_telephone_1_hint) , Toast.LENGTH_SHORT).show()
            return
        }
        if (ceoEmail.length == 0){
            Toast.makeText(this , getString(R.string.join_email_1_hint) , Toast.LENGTH_SHORT).show()
            return
        }
        if (adminName.length == 0){
            Toast.makeText(this , getString(R.string.join_name_2_hint) , Toast.LENGTH_SHORT).show()
            return
        }
        if (adminTitle.length == 0){
            Toast.makeText(this , getString(R.string.join_title_hint) , Toast.LENGTH_SHORT).show()
            return
        }
        if (adminPhone.length == 0){
            Toast.makeText(this , getString(R.string.join_phone_2_hint) , Toast.LENGTH_SHORT).show()
            return
        }

        if (adminTelephone.length == 0){
            Toast.makeText(this , getString(R.string.join_telephone_2_hint) , Toast.LENGTH_SHORT).show()
            return
        }

        if (adminEmail.length == 0) {
            Toast.makeText(this, getString(R.string.join_email_2_hint), Toast.LENGTH_SHORT).show()
            return
        }

        if (address.length == 0){
            Toast.makeText(this , getString(R.string.join_address_hint) , Toast.LENGTH_SHORT).show()
            return
        }

//        helper.getInstance(context)
//        helper.insertAdmin(account , password , comName , country)
        val currentDateTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(currentDateTime)
        val isBackup = ck_data_backup.isChecked
        if (ck_data_backup.isChecked) {

            Thread {
                val param = Param()
                val country: String = if (spn_country.selectedItemPosition == 0){
                    "KR"
                }else if (spn_country.selectedItemPosition == 1){
                    "US"
                }else if (spn_country.selectedItemPosition == 2){
                    "CN"
                }else{
                    "KR"
                }

                param.add("account", edt_account!!.text.toString())
                param.add("password", edt_password!!.text.toString())
                param.add("com_name", edt_com_name!!.text.toString())//
                param.add("phone", edt_admin_phone!!.text.toString())//admin phone
                param.add("name" , edt_admin_name!!.text.toString())//edt_admin_name
                param.add("admin_telephone" , edt_admin_telephone.text.toString())
                param.add("admin_title" , edt_admin_title.text.toString())//직함
                param.add("admin_email" , edt_admin_email.text.toString())
                param.add("ceo_name" , edt_ceo_name.text.toString())
                param.add("ceo_phone" , edt_ceo_phone.text.toString())
                param.add("ceo_telephone" , edt_ceo_telephone.text.toString())
                param.add("ceo_email" , edt_ceo_email.text.toString())
                param.add("address" , edt_address.text.toString())
                param.add("country" , country)//스피너 값 edt_ceo_name

                Log.e("확인1", account + edt_password + edt_admin_phone + edt_admin_email)
                val httpConnect = HttpConnect()
                if (httpConnect.httpConnect(param.value, Address().adminRegists, true) == 200) {

                    if (httpConnect.getReceiveMessage() == "success") {
                        handler.post {
                            finish()
                            Toast.makeText(this@JoinActivity, getString(R.string.toast_sign_up_complete), Toast.LENGTH_SHORT).show()
                            userPreference.join(account, password, isBackup , dateFormat);

                        }
                    } else {
                        handler.post {

                            Toast.makeText(this@JoinActivity, getString(R.string.toast_exist_account), Toast.LENGTH_SHORT).show()
                        }

                    }

                    //이미지를 설정한 경우만 이미지 업로드
                } else {
                    Log.e(TAG, "response code : ${httpConnect.responseCode} \naddress : ${Address().adminRegists}")
                }
            }.start()

        } else {
            if (userPreference.join(account, password, isBackup , dateFormat)) {

                Toast.makeText(this@JoinActivity, getString(R.string.toast_sign_up_complete), Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@JoinActivity, getString(R.string.toast_exist_account), Toast.LENGTH_SHORT).show()

            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_FROM_ALBUM) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    val `in` = contentResolver.openInputStream(data!!.data!!)
                    val img: Bitmap = BitmapFactory.decodeStream(`in`)
                    `in`!!.close()
                    img_logo_output!!.setImageBitmap(img)
                    selectFileName = edt_account!!.text.toString() + "_" + edt_com_name!!.text.toString()
                    if (saveBitmapToJpeg(img, selectFileName)) {
                        val file = File(cacheDir.toString())
                        val files: Array<File> = file.listFiles()!!
                        for (tempFile in files) {
                            Log.d(TAG, tempFile.name)
                            Log.d(TAG, tempFile.path)
                            logoImg = tempFile
                        }
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "onActivityResult: 사진등록 실패\n$e")
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveBitmapToJpeg(bitmap: Bitmap, name: String): Boolean {
        // TODO: 2020-01-13
        //내부저장소 캐시 경로를 받아옵니다.


        val storage: File? = cacheDir

        //저장할 파일 이름


        val fileName = "$name.jpg"

        //storage 에 파일 인스턴스를 생성합니다.


        val tempFile = File(storage, fileName)
        try {
            // 자동으로 빈 파일을 생성합니다.

            tempFile.createNewFile()

            // 파일을 쓸 수 있는 스트림을 준비합니다.


            val out = FileOutputStream(tempFile)

            // compress 함수를 사용해 스트림에 비트맵을 저장합니다.


            bitmap.compress(CompressFormat.JPEG, 100, out)

            // 스트림 사용후 닫아줍니다.


            out.close()
        } catch (e: FileNotFoundException) {
            Log.e("MyTag", "FileNotFoundException : " + e.message)
            return false
        } catch (e: IOException) {
            Log.e("MyTag", "IOException : " + e.message)
            return false
        }
        return true
    }

    private fun fileUpload(uri: String, sourceFileUri: String, selectFileName: String) {
        try {
            val lineEnd = "\r\n"
            val twoHyphens = "--"
            val boundary = "*****"

//            String fileName = "aa";


            val fileName = selectFileName
            Log.d(TAG, "fileUpload: $sourceFileUri\n$fileName")
            var bytesRead: Int
            var bytesAvailable: Int
            var bufferSize: Int
            val buffer: ByteArray
            val maxBufferSize = 1 * 1024 * 1024
            val sourceFile = File(sourceFileUri)
            val url = URL(uri)
            val conn = url.openConnection() as HttpURLConnection

            // open a URL connection to the Servlet


            val fileInputStream = FileInputStream(sourceFile)


            // Open a HTTP  connection to  the URL


            conn.doInput = true // Allow Inputs

            conn.doOutput = true // Allow Outputs

            conn.useCaches = false // Don't use a Cached Copy

            conn.requestMethod = "POST"
            conn.setRequestProperty("Connection", "Keep-Alive")
            conn.setRequestProperty("ENCTYPE", "multipart/form-data")
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=$boundary")
            conn.setRequestProperty("uploaded_file", fileName)
            val dos = DataOutputStream(conn.outputStream)
            dos.writeBytes(twoHyphens + boundary + lineEnd)
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""

                    + fileName + "\"" + lineEnd)
            dos.writeBytes(lineEnd)


            // create a buffer of  maximum size

            bytesAvailable = fileInputStream.available()
            bufferSize = Math.min(bytesAvailable, maxBufferSize)
            buffer = ByteArray(bufferSize)
            bytesRead = fileInputStream.read(buffer, 0, bufferSize)
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize)
                bytesAvailable = fileInputStream.available()
                bufferSize = Math.min(bytesAvailable, maxBufferSize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize)
            }


            // send multipart form data necesssary after file data...


            dos.writeBytes(lineEnd)
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)


            // Responses from the server (code and message)

            val serverResponseCode = conn.responseCode
            val serverResponseMessage: String = conn.responseMessage
            Log.i(TAG, "HTTP Response is : "

                    + serverResponseMessage + ": " + serverResponseCode)
            if (serverResponseCode == 200) {
                runOnUiThread {
                    val msg = "File Upload Completed.\n See uploaded file here"
                    Toast.makeText(this@JoinActivity, msg, Toast.LENGTH_SHORT).show()
                    Toast.makeText(this@JoinActivity, "File Upload Complete.",

                            Toast.LENGTH_SHORT).show()
                }
            }


            //close the streams //


            fileInputStream.close()
            dos.flush()
            dos.close()
        } catch (ex: MalformedURLException) {
            ex.printStackTrace()
            Log.e("Upload file to server", "error: " + ex.message, ex)


        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Exception : " + e.message, e)
        }

    }

    companion object {
        private const val PICK_FROM_ALBUM = 1
        fun clearApplicationData(context: Context) {
            val cache: File = context.cacheDir
            val appDir = File(cache.parent!!)
            if (appDir.exists()) {
                val children: Array<String> = appDir.list()!!
                for (s in children) {

                    deleteDir(File(appDir, s))
                    Log.d("test", "File /data/data/" + context.packageName + "/" + s + " DELETED")
                }
            }
        }

        private fun deleteDir(dir: File?): Boolean {
            if (dir != null && dir.isDirectory) {
                val children: Array<String> = dir.list()!!
                for (i in children.indices) {
                    val success = deleteDir(File(dir, children[i]))
                    if (!success) {
                        return false
                    }
                }
            }
            return dir!!.delete()
        }
    }
}

