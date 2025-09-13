package com.websarva.wings.android.servicesample

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        makePermissionRequest()

        val btPlay = findViewById<View>(R.id.btPlay)
        val btStop = findViewById<View>(R.id.btStop)

        val fromNotification = intent.getBooleanExtra("fromNotification", false)
        //引き継ぎデータが存在、つまり通知のタップからならば・・・
        if (fromNotification) {
            //再生ボタンをタップ不可に、停止ボタンをタップ可に変更
            btPlay.isEnabled = false
            btStop.isEnabled = true
        }
    }

    // 通知の権限のリクエスト。
    // テキストに載っていないので、自分で作成
    fun makePermissionRequest() {
        // 通知の権限のリクエスト
        val permissions = arrayOf(Manifest.permission.POST_NOTIFICATIONS)
        val requestCode = 100
        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "通知が許可されていません", Toast.LENGTH_SHORT).show()
            // リクエストを送信する処理
            requestPermissions(permissions,requestCode)
        } else {
            Toast.makeText(this, "通知が許可されています", Toast.LENGTH_SHORT).show()
        }
    }

    fun onPlayButtonClick(view: View) {
        val intent = Intent(this@MainActivity, SoundManageService::class.java)
        // サービスを起動
        startService(intent)

        //再生ボタンをタップ不可に、停止ボタンをタップ可に変更
        val btPlay = findViewById<View>(R.id.btPlay)
        val btStop = findViewById<View>(R.id.btStop)
        btPlay.isEnabled = false
        btStop.isEnabled = true
    }

    fun onStopButtonClick(view: View) {
        val intent = Intent(this@MainActivity, SoundManageService::class.java)
        //サービスを停止
        stopService(intent)
        //再生ボタンをタップ可に、停止ボタンをタップ不可に変更
        val btPlay = findViewById<View>(R.id.btPlay)
        val btStop = findViewById<View>(R.id.btStop)
        btPlay.isEnabled = true
        btStop.isEnabled = false
    }

}