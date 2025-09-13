package com.websarva.wings.android.mediasample

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.CompoundButton

class MainActivity : AppCompatActivity() {
    private var _player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        _player = MediaPlayer()
        val mediaFileUriStr = "android.resource://${packageName}/${R.raw.mountain_stream}"
        val mediaFileUri = Uri.parse(mediaFileUriStr)
        _player?.let {
            it.setDataSource(this, mediaFileUri)
            //非同期でのメディア再生準備が完了した際のリスナを設定
            it.setOnPreparedListener(PlayerPreparedListener())
            //メディア再生が終了した際のリスナを設定
            it.setOnCompletionListener(PlayerCompletionListener())
            //非同期でメディア再生を準備
            it.prepareAsync()
        }

        //スイッチを取得
        val loopSwitch = findViewById<CompoundButton>(R.id.swLoop)
        //スイッチにリスナを設定
        loopSwitch.setOnCheckedChangeListener(LoopSwitchChangedListener())
    }

    private inner class PlayerPreparedListener: MediaPlayer.OnPreparedListener {
        override fun onPrepared(mp: MediaPlayer) {
            val btPlay = findViewById<Button>(R.id.btPlay)
            btPlay.isEnabled = true
            val btBack = findViewById<Button>(R.id.btBack)
            btBack.isEnabled = true
            val btForward = findViewById<Button>(R.id.btForward)
            btForward.isEnabled = true
        }
    }

    private inner class PlayerCompletionListener: MediaPlayer.OnCompletionListener {
        override fun onCompletion(mp: MediaPlayer) {
            _player?.let {
                if(it.isLooping) {
                    val btPlay = findViewById<Button>(R.id.btPlay)
                    btPlay.setText(R.string.bt_play_play)
                }
            }
        }
    }

    fun onPlayButtonClick(view: View) {
        _player?.let {
            val btPlay = findViewById<Button>(R.id.btPlay)
            if(it.isPlaying) {
                //プレーヤーが再生中なら
                //プレーヤーを一時停止
                it.pause()
                //再生ボタンのラベルを「再生」に設定
                btPlay.setText(R.string.bt_play_play)
            } else {
                //プレーヤーが再生中ではなかったら
                //プレーヤーを再生
                it.start()
                //再生ボタンのラベルを「一時停止」に設定
                btPlay.setText(R.string.bt_play_pause)

            }
        }
    }

    override fun onStop() {
        _player?.let {
            //プレーヤーが再生中なら
            if(it.isPlaying) {
                //プレーヤーを停止
                it.stop()
            }
            //プレーヤーを解放
            it.release()
        }
        super.onStop()
    }

    fun onBackButtonClick(view: View) {
        //再生いちを先頭に変更
        _player?.seekTo(0)
    }

    fun onForwardButtonClick(view: View) {
        _player?.let {
            //メディアファイルの長さを取得
            val duration = it.duration
            //再生位置を終端まで進める
            it.seekTo(duration)
            //再生中でなければ
            if(!it.isPlaying) {
                //再生ボタンのラベルを「一時停止」に設定
                val btPlay = findViewById<Button>(R.id.btPlay)
                btPlay.setText(R.string.bt_play_pause)
                //再生開始
                it.start()
            }
        }
    }

    private inner class LoopSwitchChangedListener : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
            //ループするかどうかを設定
            _player?.isLooping = isChecked
        }
    }
}