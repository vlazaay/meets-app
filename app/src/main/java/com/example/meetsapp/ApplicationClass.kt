package com.example.meetsapp


import android.app.Application
import android.os.SystemClock.sleep
import android.util.Log
import com.android.installreferrer.api.InstallReferrerClient
import com.appsflyer.AppsFlyerLib
import com.example.meetsapp.model.User
import com.onesignal.OneSignal


class ApplicationClass : Application() {
    private lateinit var referrerClient: InstallReferrerClient

    companion object {
        private const val ONESIGNAL_APP_ID = "ba6c621d-eff8-4107-84c8-5838cd29056a"
        private const val APPFLYER_APP_ID = "nUgqyC72Y9uKoWYEGKdU9g"
        private val appMessageID = "1935958b3ec1a856"
        private val regionMessage = "eu"
        var userData : User = User("", "", "" , 0, "", "", "", "","")
    }
    override fun onCreate() {
        super.onCreate()

        // OneSignal Initialization
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
        val device = OneSignal.getDeviceState()
//        Log.println(Log.DEBUG, "MYLOG", device!!.userId)
            var userid= device?.userId
        if (userid != null) {
            userData.deviceID = userid
        }else{
            sleep(3000)
            userData.deviceID = userid.toString()
        }

        Log.println(Log.DEBUG, "MYLOG", "id - ${userData.deviceID}")
        //AppFlyer Initialization
        AppsFlyerLib.getInstance().init(APPFLYER_APP_ID, null, this)
        AppsFlyerLib.getInstance().start(this)
        AppsFlyerLib.getInstance().setDebugLog(true)

        //Install Referrer
//        referrerClient = InstallReferrerClient.newBuilder(this).build()
//        referrerClient.startConnection(object : InstallReferrerStateListener {
//
//            override fun onInstallReferrerSetupFinished(responseCode: Int) {
//                when (responseCode) {
//                    InstallReferrerClient.InstallReferrerResponse.OK -> {
//                        // Connection established.
//                        val response: ReferrerDetails = referrerClient.installReferrer
//                        val referrerUrl: String = response.installReferrer
//                        val referrerClickTime: Long = response.referrerClickTimestampSeconds
//                        val appInstallTime: Long = response.installBeginTimestampSeconds
//                        val instantExperienceLaunched: Boolean = response.googlePlayInstantParam
////                        Log.println(Log.DEBUG, "MYLOG", response.toString())
////                        Log.println(Log.DEBUG, "MYLOG", referrerUrl)
////                        Log.println(Log.DEBUG, "MYLOG", referrerClickTime.toString())
////                        Log.println(Log.DEBUG, "MYLOG", appInstallTime.toString())
////                        Log.println(Log.DEBUG, "MYLOG", instantExperienceLaunched.toString())
//                        referrerClient.endConnection()
//                    }
//                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
//                        // API not available on the current Play Store app.
//                    }
//                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
//                        // Connection couldn't be established.
//                    }
//                }
//            }

//            override fun onInstallReferrerServiceDisconnected() {
//                // Try to restart the connection on the next request to
//                // Google Play by calling the startConnection() method.
//            }
//        })




    }
}