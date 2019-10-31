package com.example.nikumeshi_azddi9.hmc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private var ringerMode: Int by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_WIFI_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_WIFI_STATE
                ), 1
            )
        }

        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        ringerMode = audioManager.ringerMode
        val wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager

        val ssid = wifiManager.connectionInfo.ssid

        button.setOnClickListener {
            textView.text = wifiManager.connectionInfo.ipAddress.toString()
            Toast.makeText(this, ssid, Toast.LENGTH_LONG).show()
            when (wifiManager.connectionInfo.ssid) {
                "\"egg-g-734088\"" -> audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
                "\"egg-a-734088\"" -> audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
            }
            ringerMode = audioManager.ringerMode
        }

        val mcRequest = OneTimeWorkRequestBuilder<MannerCanceller>().build()
        WorkManager.getInstance().enqueue(mcRequest)
    }

    class MannerCanceller(context: Context, params: WorkerParameters) : Worker(context, params){
        override fun doWork(): Result {

            val audioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            when (wifiManager.connectionInfo.ssid) {
                    "\"egg-g-734088\"" -> audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
                    "\"egg-a-734088\"" -> audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
            }

            return Result.success()
        }

    }
}