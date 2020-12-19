package com.example.imgdownloadreceiver

import android.content.*
import android.os.*
import android.util.Log
import android.view.View
import android.webkit.URLUtil
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var startDownloadBtn: Button
    private lateinit var spinner: ProgressBar
    private lateinit var urlInput: EditText
    private lateinit var pathOutput: TextView

    private var serviceMessengerInstance: Messenger? = null
    private val clientMessengerInstance = Messenger(ResponseHandler())

    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            serviceMessengerInstance = null
            isBound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i("qwer", "bound")
            serviceMessengerInstance = Messenger(service)
            isBound = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startDownloadBtn = findViewById(R.id.load_btn)
        urlInput = findViewById(R.id.url_input)
        pathOutput = findViewById(R.id.path_output)
        spinner = findViewById(R.id.progress_bar)

        startDownloadBtn.setOnClickListener {
            val urlToLoad = urlInput.text.toString()

            if (!isBound || !URLUtil.isValidUrl(urlToLoad)) {
                return@setOnClickListener
            }

            val msg: Message = Message.obtain(null, Constants.DOWNLOAD_REQUEST.ordinal, urlToLoad)
            msg.replyTo = clientMessengerInstance
            try {
                serviceMessengerInstance?.send(msg)

                startDownloadBtn.visibility = View.INVISIBLE
                urlInput.visibility = View.INVISIBLE
                spinner.visibility = View.VISIBLE
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent()
        intent.component = ComponentName("com.example.imgdownload", "com.example.imgdownload.LoaderService")
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
    }

    inner class ResponseHandler: Handler() {

        override fun handleMessage(msg: Message) {

            when (msg.what) {
                Constants.DOWNLOAD_RESPONSE.ordinal -> {
                    spinner.visibility = View.GONE
                    urlInput.visibility = View.VISIBLE
                    startDownloadBtn.visibility = View.VISIBLE

                    pathOutput.text = msg.obj.toString()
                }
                else ->
                    super.handleMessage(msg)
            }
        }
    }
}