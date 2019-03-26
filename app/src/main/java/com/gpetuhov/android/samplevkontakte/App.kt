package com.gpetuhov.android.samplevkontakte

import android.app.Application
import com.pawegio.kandroid.toast
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Notice that in VK SDK version 2 initialization is done automatically

        // But we need to listen to token expiration
        VK.addTokenExpiredHandler(object: VKTokenExpiredHandler {
            override fun onTokenExpired() {
                // Token expired, do something
                toast("Token expired")
            }
        })
    }
}