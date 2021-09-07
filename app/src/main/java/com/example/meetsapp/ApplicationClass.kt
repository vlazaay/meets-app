package com.example.meetsapp


import android.app.Application
import android.util.Log
import com.appsflyer.AppsFlyerLib
import com.example.meetsapp.model.User
import com.onesignal.OneSignal


class ApplicationClass : Application() {
    companion object {
        private const val ONESIGNAL_APP_ID = "ba6c621d-eff8-4107-84c8-5838cd29056a"
        private const val APPFLYER_APP_ID = "R62LEuE7pioe7KeMAUsHY7"
        var userData : User = User("", "", "" , 0, "", "", "", "")
    }
    override fun onCreate() {
        super.onCreate()
        //AppFlyer Initialization
        AppsFlyerLib.getInstance().init(APPFLYER_APP_ID, null, this)
        AppsFlyerLib.getInstance().start(this)
        AppsFlyerLib.getInstance().setDebugLog(true)

        // OneSignal Initialization
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
        val device = OneSignal.getDeviceState()
        var userid = device!!.userId
        userData.deviceID = userid
        Log.println(Log.DEBUG, "MYLOG", "id - ${userData.deviceID}")
    }
}