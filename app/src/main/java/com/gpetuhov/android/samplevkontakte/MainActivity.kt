package com.gpetuhov.android.samplevkontakte

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pawegio.kandroid.defaultSharedPreferences
import com.pawegio.kandroid.toast
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import kotlinx.android.synthetic.main.activity_main.*

// Notice that in VK SDK version 2 we don't have to initialize VK in Application.onCreate()
// (but we need to listen to token expiration).

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initName()

        loginButton.setOnClickListener { login() }
        logoutButton.setOnClickListener { logout() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object: VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                // User passed authorization
                toast("Login success")

                // Save current token
                token.save(defaultSharedPreferences)

                initName()
            }

            override fun onLoginFailed(errorCode: Int) {
                // User didn't pass authorization
                toast("Login failed")
            }
        }

        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    // Login and request access to wall posts and photos.
    // Result is passed into onActivityResult()
    private fun login() = VK.login(this, arrayListOf(VKScope.WALL, VKScope.PHOTOS))

    private fun logout() {
        VK.logout()
        initName()
    }

    private fun initName() {
        // If user is logged in, show user name, otherwise clear user name
        if (VK.isLoggedIn()) {
            // Restore current token
            val token = VKAccessToken.restore(defaultSharedPreferences)

            // Get user ID from the token (if exists)
            userName.text = token?.userId?.toString() ?: ""

        } else {
            userName.text = ""
        }
    }
}
