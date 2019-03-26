package com.gpetuhov.android.samplevkontakte

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gpetuhov.android.samplevkontakte.models.VKUser
import com.gpetuhov.android.samplevkontakte.requests.VKUsersRequest
import com.pawegio.kandroid.defaultSharedPreferences
import com.pawegio.kandroid.toast
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import com.vk.api.sdk.exceptions.VKApiExecutionException
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

            if (token != null) {
                // Get user UID from the token
                getUserName(token.userId)
            } else {
                userName.text = ""
            }

        } else {
            userName.text = ""
        }
    }

    private fun getUserName(userUid: Int) {
        VK.execute(VKUsersRequest(intArrayOf(userUid)), object: VKApiCallback<List<VKUser>> {
            override fun success(result: List<VKUser>) {
                userName.text = if (!result.isEmpty()) result[0].firstName else "Unknown"
            }

            override fun fail(error: VKApiExecutionException) {
                toast("Error getting user name")
                userName.text = ""
            }
        })
    }
}
