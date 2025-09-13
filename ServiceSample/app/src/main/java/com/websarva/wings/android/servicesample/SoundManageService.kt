package com.websarva.wings.android.servicesample

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class SoundManageService : Service() {
    companion object {
        //通知チャネルID文字列定数
        private const val CHANNEL_ID = "soundmanagerservice_notification_channel"// MainActivity に通知するためのアクション文字列
    }

    private var _player: MediaPlayer? = null

    // テキストにない追加
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate() {

        // テキストにない追加箇所
        val attributionContext = createAttributionContext("mediaPlayBack")

        _player = MediaPlayer()
        //通知チャネル名をstring.xmlから取得
        val name = getString(R.string.notification_channel_name)
        //通知チャネルの重要度を標準に設定
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        //通知チャネルを生成
        val channel = NotificationChannel(CHANNEL_ID, name, importance)

        //NotificationManagerオブジェクトを取得（テキストにないatributionContextの追加）
        val manager = attributionContext.getSystemService(NotificationManager::class.java) as NotificationManager
        //通知チャネルを設定
        manager.createNotificationChannel(channel)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val mediaFileUriStr = "android.resource://${packageName}/${R.raw.test}"
        val mediaFileUri = Uri.parse(mediaFileUriStr)
        _player?.let {
            it.setDataSource(this@SoundManageService, mediaFileUri)
            //非同期でのメディア再生準備が完了した際のリスナを設定
            it.setOnPreparedListener(PlayerPreparedListener())
            //メディア再生が終了した際のリスナを設定
            it.setOnCompletionListener(PlayerCompletionListener())
            //非同期でメディア再生を準備
            it.prepareAsync()
        }

        //定数を返す
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        _player?.let {
            if (it.isPlaying) {
                it.stop()
            }
            //プレイヤーを解放
            it.release()
        }
    }

    //メティアが再生準備完了した時のリスナクラス
    private inner class PlayerPreparedListener: MediaPlayer.OnPreparedListener {
        override fun onPrepared(mp: MediaPlayer) {
            //メディアを再生
            mp.start()
            //Notificationを作成するBuilderクラスを生成
            val builder = NotificationCompat.Builder(this@SoundManageService, CHANNEL_ID)
            builder.setSmallIcon(android.R.drawable.ic_dialog_info)
            builder.setContentTitle(getString(R.string.msg_notification_title_start))
            builder.setContentText(getString(R.string.msg_notification_text_start))
            //起動先Activityクラスを指定したIntentオブジェクトの生成
            val intent = Intent(this@SoundManageService, MainActivity::class.java)
            //起動先アクティビティに引き継ぎデータを作能
            intent.putExtra("fromNotification", true)
            //PendingIntentオブジェクトを取得
            val stopServiceIntent = PendingIntent.getActivity(
                this@SoundManageService, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            //PendingIntentオブジェクトをビルダーに設定
            builder.setContentIntent(stopServiceIntent)
            //タップされた通知メッセージを自動的に消去するように設定
            builder.setAutoCancel(true)
            //BuilderからNotificationオブジェクトを生成
            val notification = builder.build()
            //Notificationオブジェクトをもとにサービスをフォアグラウンド化
            startForeground(200, notification)
        }
    }

    //メディアが再生終了した時のリスナクラス
    private inner class PlayerCompletionListener: MediaPlayer.OnCompletionListener {
        @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
        override fun onCompletion(mp: MediaPlayer) {
            //Notificationを作成するBuilderクラス生成
            val builder = NotificationCompat.Builder(this@SoundManageService, CHANNEL_ID)
            //通知エリアに表示されるアイコンを設定
            builder.setSmallIcon(android.R.drawable.ic_dialog_info)
            //通知ドロワーでの表示タイトルを設定
            builder.setContentTitle(getString(R.string.msg_notification_title_finish))
            //通知ドロワーでの表示メッセージを設定
            builder.setContentText(getString(R.string.msg_notification_text_finish))
            //BuilderからNotificationオブジェクトを生成
            val notification = builder.build()
            //NotificationManagerオブジェクトを取得
            val manager = NotificationManagerCompat.from(this@SoundManageService)
            //通知
            manager.notify(100, notification)
            stopSelf()
        }
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}