package com.example.imgdownloadreceiver

import android.content.*
import android.os.*
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var imageReceiver: ImageReceiver

    private var mService: Messenger? = null
    private var bound = false

    private val mConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            mService = Messenger(service)
            bound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            mService = null
            bound = false
        }
    }

    fun sayHello(v: View) {
        if (!bound) return
        val msg = Message.obtain(null, 17, 0, 0)
        msg.obj = "wooo"
        try {
            mService?.send(msg)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bindAndLoadBtn = findViewById<Button>(R.id.bind_and_load_btn)
        bindAndLoadBtn.setOnClickListener { view ->
            sayHello(view)
        }
    }

    override fun onStart() {
        super.onStart()
        // Bind to the service
        val connectionIntent = Intent()
        connectionIntent.component = ComponentName("com.example.imgdownload", "com.example.imgdownload.LoaderService")
        bindService(connectionIntent, mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        // Unbind from the service
        if (bound) {
            unbindService(mConnection)
            bound = false
        }
    }




//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        imageReceiver = ImageReceiver(findViewById(R.id.path_to_img))
//        val filter = IntentFilter(Constants.LOADING_SUCCESS_ACTION.name)
//        registerReceiver(imageReceiver, filter)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        unregisterReceiver(imageReceiver)
//    }
}