package com.example.imgdownloadreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.TextView


class ImageReceiver(private val pathDisplay: TextView) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Constants.LOADING_SUCCESS_ACTION.name == intent.action) {
            val pathToFile = intent.getStringExtra(Constants.RES_BITMAP_KEY.name)
            pathDisplay.text = pathToFile
        }
    }
}