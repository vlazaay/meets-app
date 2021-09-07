package com.example.meetsapp

//import com.google.gson.JsonObject

import android.app.Application
import android.util.Log
import com.appsflyer.AppsFlyerConversionListener
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
//        val conversionListener: AppsFlyerConversionListener = object : AppsFlyerConversionListener {
//            /* Returns the attribution data. Note - the same conversion data is returned every time per install */
//            override fun onConversionDataSuccess(conversionData: Map<String, Any>) {
//                for (attrName in conversionData.keys) {
//                    Log.d(
//                        AppsFlyerLibCore.LOG_TAG,
//                        "attribute: " + attrName + " = " + conversionData[attrName]
//                    )
//                }
//                setInstallData(conversionData)
//            }
//
//            override fun onConversionDataFail(errorMessage: String) {
//                Log.d(AppsFlyerLibCore.LOG_TAG, "error getting conversion data: $errorMessage")
//            }
//
//            /* Called only when a Deep Link is opened */
//            override fun onAppOpenAttribution(conversionData: Map<String, String>) {
//                for (attrName in conversionData.keys) {
//                    Log.d(
//                        AppsFlyerLibCore.LOG_TAG,
//                        "attribute: " + attrName + " = " + conversionData[attrName]
//                    )
//                }
//            }
//
//            override fun onAttributionFailure(errorMessage: String) {
//                Log.d(AppsFlyerLibCore.LOG_TAG, "error onAttributionFailure : $errorMessage")
//            }
//        }
//
//
//        /* This API enables AppsFlyer to detect installations, sessions, and updates. */
//
//
//        /* This API enables AppsFlyer to detect installations, sessions, and updates. */AppsFlyerLib.getInstance()
//            .init(
//                AF_DEV_KEY, conversionListener,
//                applicationContext
//            )
//        AppsFlyerLib.getInstance().startTracking(this)
//
//
//        /* Set to true to see the debug logs. Comment out or set to false to stop the function */
//
//
//        /* Set to true to see the debug logs. Comment out or set to false to stop the function */AppsFlyerLib.getInstance()
//            .setDebugLog(true)
        // Logging set to help debug issues, remove before releasing your app.


        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
        val device = OneSignal.getDeviceState()
        var userid = device!!.userId
        userData.deviceID = userid
        Log.println(Log.DEBUG, "MYLOG", "id - ${userData.deviceID}")
//        makeApiRequestPushUser()




    }
}