package com.ko.smartneck.medical.twofive.util

class Address {
    val adminRegists = DOMAIN + "backup/adminRegist.php"
    val dataBackup = DOMAIN + "backup/dataBackup.php"
    val getData = DOMAIN + "backup/getData.php"
    val member = DOMAIN + "backup/member.php"
    val preset = DOMAIN + "backup/preset.php"
    val measure = DOMAIN + "backup/measure.php"
    val exercise = DOMAIN + "backup/exercise.php"
    val latelyUpdate = DOMAIN + "backup/latelyUpdate.php"
    val login = DOMAIN + "backup/login.php"
    val errorReceipt = DOMAIN + "backup/errorReceipt.php"
    private val _1to1Receipt = DOMAIN + "backup/1to1Receipt.php"
    private val _1to1load = DOMAIN + "backup/1to1load.php"
    val adminPasswordChange = DOMAIN + "backup/adminPasswordChange.php"
    val findAccount = DOMAIN + "backup/findAccount.php"
    val findPassword = DOMAIN + "backup/findPassword.php"
    val sendMailForPassword = DOMAIN + "backup/mail/sendMailForPassword.php"

    val getMeasureManagement = DOMAIN + "backup/getMeasureManagement.php"
    val loadFAQList = DOMAIN + "backup/loadFAQList.php"
    val loadNotice = DOMAIN + "backup/loadNotice.php"

    val sendMailExcel = DOMAIN + "backup/sendMailExcel.php"
    val memberDelete = DOMAIN + "backup/memberDelete.php"

    //    final private String login = DOMAIN + "backup/login.php";
    //    final private String Login = DOMAIN + "MedSignIn.php";
    val join = DOMAIN + "MedSignUp.php"

    //    public String getLogin() {
    //        return Login;
    //    }
    val autoLogin = DOMAIN + "AutoLogin.php"
    val memberAdd = DOMAIN + "MedMemberAdd.php"
    val mmberList = DOMAIN + "MedGetMemberList.php"
    val loadUserPreset = DOMAIN + "MedLoadUserPreset.php"
    val updatePreset = DOMAIN + "MedUpdatePreset_ver_2.php"
    val measureUpdate = DOMAIN + "MedMeasureUpdate.php"
    val insertExercise = DOMAIN + "MedInsertExercise.php"
    val loadUserStatistics = DOMAIN + "MedLoadUserStatistics.php"
    val mememberEdit = DOMAIN + "MedMemberEdit.php"
//    val memberDelete = DOMAIN + "MedMemberDelete.php"
    val checkToID = DOMAIN + "CheckToID.php"
    val updateUserInfo = DOMAIN + "UpdateUserInfo.php"
    val insertReview = DOMAIN + "InsertReview.php"
    val loadReview = DOMAIN + "LoadReview.php"
    val insertError = DOMAIN + "InsertError.php"
    val insert1To1 = DOMAIN + "Insert1To1.php"
    val load1To1List = DOMAIN + "Load1To1List.php"
    val youTubeUrl = DOMAIN + "YouTubeUrl.php"
    val termsOfUse = DOMAIN + "TermsOfUse.php"
    val knhimJoin = ""
    val knhimPasswordChange = ""
    val knhimPhoneChange = ""

    fun get_1to1Receipt(): String {
        return _1to1Receipt
    }

    fun get_1to1load(): String {
        return _1to1load
    }

    companion object {
        var YOUTUBE_SUB_EXERCISE: String? = null
        var YOUTUBE_HOW_TO_EXERCISE: String? = null
        var DOMAIN = "http://110.10.147.212/medical/"
    }
}