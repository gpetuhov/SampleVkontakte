package com.gpetuhov.android.samplevkontakte

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// Don't forget to initialize VK SDK in Application.onCreate()

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
