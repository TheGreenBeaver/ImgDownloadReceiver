package com.example.imgdownloadreceiver

import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var imageReceiver: ImageReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageReceiver = ImageReceiver(findViewById(R.id.path_to_img))
        val filter = IntentFilter(Constants.LOADING_SUCCESS_ACTION.name)
        registerReceiver(imageReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(imageReceiver)
    }
}