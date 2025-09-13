package com.websarva.wings.android.hellosample

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
        // 表示ボタンであるButtonオブジェクトを取得
        val btClick = findViewById<Button>(R.id.btClick)
        // リスナクラスのインスタンスを作成
        val listener = HelloListener()
        // 表示ボタンにリスナを設定
        btClick.setOnClickListener(listener)

        val btClear = findViewById<Button>(R.id.btClear)
        btClear.setOnClickListener(listener)
    }
    private inner class HelloListener : View.OnClickListener {
        override fun onClick(view: View) {
            //名前入力欄であるEditTextオブジェクトを取得
            val input = findViewById<EditText>(R.id.etName)
            //メッセージを表示するTextViewオブジェクトを取得
            val output = findViewById<TextView>(R.id.tvOutput)

            // idのR値に応じて処理を分岐
            when (view.id) {
                R.id.btClick -> {
                    val inputStr = input.text.toString()
                    output.text = getString(R.string.greeting_message, inputStr)
                }

                R.id.btClear -> {
                    input.setText("")
                    output.text = ""
                }
            }
        }
    }
}